package com.romiis.core;

import com.romiis.util.Pair;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Main functionality of the library. Whole deepEquals implementation is in this class.
 *
 * @author Roman Pejs
 * @version 1.0
 * @see Pair
 */
public class EqualLib {

    /**
     * Cache for fields of classes
     */
    private static final Map<Class<?>, Field[]> FIELD_CACHE = new ConcurrentHashMap<>();


    /**
     * Method that takes two objects and deep compares them to determine if they are equal
     * <p>
     * This method uses a breadth-first search algorithm to compare the objects.
     * It compares the objects field by field and if the field is an object, it compares the fields of the object.
     * If the field is a primitive type, it compares the values of the fields.
     * Wrappers and String are compared by calling the equals' method.
     * <br>
     * Arrays are compared index by index.
     * Lists are compared index by index.
     * Maps are compared key by key.
     * Sets (UNORDERED) are compared element by element.
     * <br>
     * Collection comparison is done with recursion.
     * <br>
     * Object can be null. If two objects are null, they are considered equal.
     * </p>
     *
     * @param obj1 First object to compare (can be null)
     * @param obj2 Second object to compare (can be null)
     * @return True if the objects are deeply equal, false otherwise
     */
    public static boolean areEqual(Object obj1, Object obj2) {
        return EqualLib.areEqual(obj1, obj2, new EqualLibConfig());
    }

    /**
     * Method that takes two objects and deep compares them to determine if they are equal
     *
     * @param obj1   First object to compare (can be null)
     * @param obj2   Second object to compare (can be null)
     * @param config Configuration for the comparison
     * @return True if the objects are deeply equal, false otherwise
     */
    public static boolean areEqual(Object obj1, Object obj2, EqualLibConfig config) {
        if (config == null) {
            config = new EqualLibConfig();
        }
        return EqualLib.areEqual(new Pair(obj1, obj2, 0), new HashSet<>(), new LinkedList<>(), config);
    }


    /**
     * Internal method that takes two objects and deep compares them to determine if they are equal
     * First check if the objects are identical (reference check), the if one of them is null, return false
     * <p>
     * Then the BFS algorithm is used to compare the objects for
     *
     * @param visited List of visited pairs (when null, new list is created)
     * @param queue   Queue of objects to compare (when null, new queue is created)
     * @return - True if the objects are deeply equal, false otherwise
     */
    private static boolean areEqual(Pair pairToCompare, Set<Pair> visited, Queue<Pair> queue, EqualLibConfig config) {


        // Add the first pair to the queue
        queue.add(pairToCompare);

        // While the queue is not empty, compare the objects
        while (!queue.isEmpty()) {

            // Get the next pair from the queue
            Pair pair = queue.poll();

            // If the pair is identical, continue (same reference)
            if (Pair.isIdentical(pair)) {
                continue;
            }

            // If one of the objects is null, return false
            if (Pair.isNullPresent(pair)) {
                if (config.isDebugEnabled()) System.out.println("One of the objects is null");
                return false;
            }


            if (config.getMaxComparisonDepth() != -1 && pair.getDepth() >= config.getMaxComparisonDepth()) {
                if (config.isUseStandardEqualsAfterDepth()) {
                    if (!compareWrapperOrString(pair)) {
                        return false;
                    }
                }
                continue;
            }


            // If the pair is already visited, continue
            if (visited.contains(pair)) {
                continue;
            }


            // Compare all fields of the object
            if (!compareObject(pair, queue, visited, config)) {
                return false;
            }

            // Add the pair to the visited list
            visited.add(pair);
        }

        return true;
    }


    /**
     * Internal method to decide how to compare the objects
     * <p>
     * When deepEquals accept primitive type, JVM will autobox it to wrapper type.
     * For Primitive, Wrapper and String, we can use equals method.
     * For Array, List expanding the Object tree and compare each element.
     * For Map, Set, using recursion to compare each element.
     * For Object, compare each field.
     * </p>
     *
     * @param pairToCompare Pair of objects to compare
     * @param visited       List of visited pairs (when null, new list is created)
     * @param queue         Queue of objects to compare (when null, new queue is created)
     * @return - True if the objects are deeply equal, false otherwis
     */
    private static boolean compareObject(Pair pairToCompare, Queue<Pair> queue, Set<Pair> visited, EqualLibConfig config) {

        // Type check
        Class<?> type = Pair.getCommonType(pairToCompare, config.isCompareInheritedFields());

        // There is no class to compare not found
        if (type == null) {


            // If it is not the anonymous class, return false
            type = Pair.getAnonymousClassType(pairToCompare);

            // If it is not the anonymous class, return false
            if (type == null) {

                if (config.isDebugEnabled()) {
                    if (config.isCompareInheritedFields())
                        System.out.println("No common superclass found for pair: " + pairToCompare);

                    else System.out.println("No class found for pair: " + pairToCompare);
                }

                return false;
            }

        }


        boolean result;

        if (useCustomEquals(type, config)) {
            if (!pairToCompare.getFirst().equals(pairToCompare.getSecond())) {
                if (config.isDebugEnabled()) System.out.println("Custom equals method returned false");
                return false;
            }
            return true;
        }


        // Decide what to do based on the type
        if (type.isPrimitive() || isWrapperOrString(type)) {
            result = compareWrapperOrString(pairToCompare);
            // Wrappers and String have equals method implemented

        } else if (type.isArray()) {
            // Arrays => Compare arrays index by index
            result = compareArray(pairToCompare, queue);
        } else {

            if (isCollectionOrMap(type) && config.isCompareCollectionsByElements()) {
                // Collections => Compare collections element by element
                result = compareCollectionOrMap(pairToCompare, queue, visited, config);
            } else {
                // If the objects are not collections or maps, compare the fields of the objects
                result = compareFields(pairToCompare, queue, config, type);

            }

        }

        if (config.isDebugEnabled()) System.out.println("Pair: " + pairToCompare + " is equal: " + result);
        return result;

    }


    /**
     * If algorithm determines that the objects are objects, it compares the fields of the objects
     *
     * @param pairToCompare Pair of objects to compare
     * @param queue         Queue of objects to compare for Algorithm
     * @return true if the fields are equal, false otherwise
     */
    private static boolean compareFields(Pair pairToCompare, Queue<Pair> queue, EqualLibConfig config, Class<?> type) {

        // Get the fields of the objects
        Field[] fieldsA, fieldsB;


        if (type.isAnonymousClass()) {
            fieldsA = getFields(pairToCompare.getFirst().getClass());
            fieldsB = getFields(pairToCompare.getSecond().getClass());

        } else {
            fieldsA = getFields(type);
            fieldsB = getFields(type);
        }


        // Check if the number of fields is equal
        if (fieldsA.length != fieldsB.length) {
            if (config.isDebugEnabled()) System.out.println("Number of fields is not equal");
            return false;
        }

        // Compare each field
        for (Field fieldA : fieldsA) {

            // Find the matching field in the second object
            Field fieldB = findMatching(fieldA, fieldsB, pairToCompare.getFirst().getClass().isAnonymousClass());

            // If no matching field is found, the objects are not equal
            if (fieldB == null) {
                if (config.isDebugEnabled()) System.out.println("Field: " + fieldA.getName() + " is not equal");
                return false;
            }

            if (!config.getIgnoredFieldPaths().isEmpty() && isIgnoredField(type, fieldA, config)) {
                continue;
            }

            try {
                // Get the values of the fields
                Object valueA = fieldA.get(pairToCompare.getFirst());
                Object valueB = fieldB.get(pairToCompare.getSecond());

                if (valueA == valueB) {
                    continue;
                }


                if (valueA == null || valueB == null) {
                    if (config.isDebugEnabled())
                        System.out.println("Field: " + fieldA.getName() + " is not equal " + valueA + " " + valueB);
                    return false;
                }


                if (valueA.getClass().isPrimitive() || isWrapperOrString(valueA.getClass())) {

                    if (!compareWrapperOrString(new Pair(valueA, valueB))) {
                        if (config.isDebugEnabled())
                            System.out.println("Field: " + fieldA.getName() + " is not equal " + valueA + " " + valueB);
                        return false;
                    }

                    if (config.isDebugEnabled())
                        System.out.println("Field: " + fieldA.getName() + " is equal " + valueA + " " + valueB);

                } else {
                    queue.add(new Pair(valueA, valueB, pairToCompare.getDepth() + 1));
                }


            } catch (IllegalAccessException e) {
                System.out.println("Error while accessing field: " + fieldA.getName());
                return false;

            }
        }

        return true;
    }


    /**
     * Check if the field is in the ignored fields
     *
     * @param clazz  Class of the object
     * @param field  Field to check
     * @param config Configuration for the comparison
     * @return true if the field is ignored, false otherwise
     */
    private static boolean isIgnoredField(Class<?> clazz, Field field, EqualLibConfig config) {
        String fieldName = field.getName();
        String fullFieldName = clazz.getName() + "." + fieldName;
        return config.getIgnoredFieldPaths().contains(fullFieldName);
    }

    /**
     * Check if the class or package is in useCustomEquals
     *
     * @param clazz  Class to check
     * @param config Configuration for the comparison
     * @return true if the class is in useCustomEquals, false otherwise
     */
    private static boolean useCustomEquals(Class<?> clazz, EqualLibConfig config) {
        // Get the class name (with package)
        String className = clazz.getName();

        // Split the class name by the dot (.) and loop through the parts and check if the class or package is in the useCustomEquals
        String path = "";
        for (String part : className.split("\\.")) {
            path += part;
            if (config.getCustomEqualsClasses().contains(path)) {
                return true;
            }
            path += ".";
        }
        return false;
    }

    /**
     * Compare two arrays index by index (expanding the object tree)
     *
     * @param queue Queue of objects to compare for Algorithm
     * @return true if the arrays are equal, false otherwise
     */
    private static boolean compareArray(Pair pair, Queue<Pair> queue) {

        // Unwrap the pair
        Object a = pair.getFirst();
        Object b = pair.getSecond();


        // Check if the arrays have the same length
        if (Array.getLength(a) != Array.getLength(b)) {
            return false;
        }

        // Compare each element of the array
        for (int i = 0; i < Array.getLength(a); i++) {
            Object elementA = Array.get(a, i);
            Object elementB = Array.get(b, i);

            queue.add(new Pair(elementA, elementB));
        }

        return true;
    }

    /**
     * Find a matching field in the second object
     *
     * @param fieldA         Field from the first object
     * @param fieldsB        Fields from the second object
     * @param anonymousClass If the class is anonymous field, the last $number is removed from the name
     * @return The matching field or null if no matching field is found
     */
    private static Field findMatching(Field fieldA, Field[] fieldsB, boolean anonymousClass) {
        for (Field fieldB : fieldsB) {

            if (anonymousClass) {
                // Remove the last $number from the name
                String nameA = fieldA.getName().replaceAll("\\$[0-9]+", "");
                String nameB = fieldB.getName().replaceAll("\\$[0-9]+", "");
                if (nameA.equals(nameB)) {
                    return fieldB;
                }
            }

            if (fieldA.equals(fieldB)) {
                return fieldB;
            }
        }
        return null;
    }


    /**
     * This method decides if the field is a wrapper type (Integer, Double, Long, Float, Character, Short, Byte, Boolean) or String
     * These types are considered wrapper types because they are used to wrap the primitive types and can be compared using the equals method
     *
     * @param type - the field to check
     * @return - true if the field is a wrapper type, false otherwise
     */
    private static boolean isWrapperOrString(Class<?> type) {
        return type.equals(Integer.class) ||
                type.equals(Double.class) ||
                type.equals(Long.class) ||
                type.equals(Float.class) ||
                type.equals(Character.class) ||
                type.equals(Short.class) ||
                type.equals(Byte.class) ||
                type.equals(Boolean.class) ||
                type.equals(String.class);
    }

    /**
     * This calls the method equals on the object
     *
     * @return - true if the objects are equal, false otherwise
     */
    private static boolean compareWrapperOrString(Pair pair) {
        Object objA = pair.getFirst();
        Object objB = pair.getSecond();

        return objA.equals(objB);
    }


    /**-------------------------------------------------**/
    /** -------------- COLLECTIONS & MAPS ------------- **/
    /**-------------------------------------------------**/


    /**
     * Check if the class is a collection or map
     *
     * @param type Class to check
     * @return true if the class is a collection, false otherwise
     */
    private static boolean isCollectionOrMap(Class<?> type) {
        return Collection.class.isAssignableFrom(type) || Map.class.isAssignableFrom(type);
    }

    /**
     * Compare two collections of any type.
     *
     * @param pairToCompare Pair of collections to compare
     * @param queue         Queue of objects to compare for Algorithm
     * @param visited       List of visited pairs
     * @return true if the collections are equal, false otherwise
     */
    private static boolean compareCollectionOrMap(Pair pairToCompare, Queue<Pair> queue, Set<Pair> visited, EqualLibConfig config) {
        Object obj1 = pairToCompare.getFirst();
        Object obj2 = pairToCompare.getSecond();

        // Check if the collections are null
        if (obj1 == null || obj2 == null) {
            return obj1 == obj2;
        }

        // Check if the collections are the same instance
        if (!obj1.getClass().equals(obj2.getClass())) {
            return false;
        }
        if (obj1 instanceof List && obj2 instanceof List) {
            return compareList(pairToCompare, queue);

            // Check if the collections is set
        } else if (obj1 instanceof Set && obj2 instanceof Set) {
            return compareSets(pairToCompare, queue, visited, config);

        } else if (obj1 instanceof Map && obj2 instanceof Map) {
            return compareMaps(pairToCompare, queue, visited, config);

        }

        return false;
    }


    /**
     * Compare two lists index by index (expanding the object tree)
     *
     * @param listsToCompare Pair of lists to compare
     * @param queue          Queue of objects to compare for Algorithm
     * @return true if the lists are equal, false otherwise
     */
    private static boolean compareList(Pair listsToCompare, Queue<Pair> queue) {

        // Get the lists
        List<?> listA = (List<?>) listsToCompare.getFirst();
        List<?> listB = (List<?>) listsToCompare.getSecond();

        // Check if the lists have the same size
        if (listA.size() != listB.size()) {
            return false;
        }

        // Compare each element of the list
        for (int i = 0; i < listA.size(); i++) {

            Object elemA = listA.get(i);
            Object elemB = listB.get(i);

            // The depth is increased by 1
            queue.add(new Pair(elemA, elemB, listsToCompare.getDepth() + 1));
        }

        return true;
    }


    /**
     * Compare two sets element by element (recursive call for objects)
     *
     * @param setsToCompare Pair of sets to compare
     * @param visited       List of visited pairs (when null, new list is created)
     * @return true if the sets are equal, false otherwise
     * @hidden
     */
    private static boolean compareSets(Pair setsToCompare, Queue<Pair> queue, Set<Pair> visited, EqualLibConfig config) {

        // Get the sets
        Set<?> set1 = (Set<?>) setsToCompare.getFirst();
        Set<?> set2 = (Set<?>) setsToCompare.getSecond();

        // Check if the sets have the same size
        if (set1.size() != set2.size()) {
            return false;
        }

        // Create a copy of the second set, prevent modification of the original set
        List<Object> unmatched = new ArrayList<>(set2);

        // Compare each element of the first set
        for (Object elem1 : set1) {

            // Check if the element is in the second set
            boolean found = false;

            // Compare the element with the elements of the second set
            for (Object elem2 : unmatched) {

                // The depth is increased by 1
                if (areEqual(new Pair(elem1, elem2, setsToCompare.getDepth() + 1), visited, queue, config)) {

                    // Remove the matched element from the unmatched list
                    unmatched.remove(elem2);
                    found = true;
                    break;
                }
            }

            if (!found) {
                return false;
            }
        }

        return unmatched.isEmpty();
    }


    /**
     * Compare two maps key by key (recursive call for objects)
     *
     * @param queue   Queue of objects to compare for Algorithm
     * @param visited List of visited pairs (when null, new list is created)
     * @return true if the maps are equal, false otherwise
     * @hidden
     */
    private static boolean compareMaps(Pair mapsToCompare, Queue<Pair> queue, Set<Pair> visited, EqualLibConfig config) {

        // Get the maps
        Map<?, ?> mapA = (Map<?, ?>) mapsToCompare.getFirst();
        Map<?, ?> mapB = (Map<?, ?>) mapsToCompare.getSecond();

        // Check if the maps have the same size
        if (mapA.size() != mapB.size()) {
            return false;
        }

        // Compare each key-value pair
        for (Map.Entry<?, ?> entryA : mapA.entrySet()) {

            // Get the key and value of the first map
            Object keyA = entryA.getKey();
            Object valueA = entryA.getValue();

            boolean found = false;

            // Compare the key-value pair with the second map
            for (Object keyB : mapB.keySet()) {

                // If the keys are equal, compare the values (the depth is increased by 1)
                if (areEqual(new Pair(keyA, keyB, mapsToCompare.getDepth() + 1), visited, queue, config)) {

                    // If the values are equal, set found to true and break the loop
                    queue.add(new Pair(valueA, mapB.get(keyB), mapsToCompare.getDepth() + 1));
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }

        return true;
    }


    /**
     * Get all fields from a class (including private fields)
     *
     * @param clazz       - the class to get the fields from
     * @param finalParent - the final parent class where the fields should be taken from (included)
     */
    private static Field[] getFields(Class<?> clazz, Class<?> finalParent) {

        List<Field> fields = new ArrayList<>();

        // If the final parent is null, set it to Object.class (the highest parent)
        if (finalParent == null) {
            finalParent = Object.class;
        }

        // Get all fields from the class and its superclasses
        while (clazz != null) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();

            // If the class is the final parent, break the loop (Superclass of Object.class is null)
            if (clazz == null || clazz.equals(finalParent)) {
                break;
            }
        }

        // Set the fields to accessible
        Field[] fieldArray = fields.toArray(new Field[0]);
        AccessibleObject.setAccessible(fieldArray, true);
        return fieldArray;
    }


    /**
     * Get all fields from a class (including private fields)
     *
     * @param clazz - the class to get the fields from
     */
    private static Field[] getFields(Class<?> clazz) {
        return getCachedFields(clazz);

    }


    /**
     * Get cached fields of a class
     * If the fields are not cached, get the fields and cache them
     * <p>
     * The fields are cached to improve the performance of the deepEquals method
     * The fields are cached in a ConcurrentHashMap
     * The key is the class and the value is the array of fields
     */
    private static Field[] getCachedFields(Class<?> clazz) {
        return FIELD_CACHE.computeIfAbsent(clazz, c -> getFields(c, null));
    }

    /**
     * Clear the field cache to free up memory
     */
    public static void clearFieldCache() {
        FIELD_CACHE.clear();
    }


}
