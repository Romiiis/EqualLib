package com.romiis;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;


/**
 * Main functionality of the library. Whole deepEquals implementation is in this class.
 *
 * @author Roman Pejs
 * @version 1.0
 * @see IEqualLib
 * @see Pair
 */
public class EqualLib implements IEqualLib {


    /**
     * Method that takes two objects and deep compares them to determine if they are equal
     *
     * @param obj1 First object to compare (can be null)
     * @param obj2 Second object to compare (can be null)
     * @return True if the objects are deeply equal, false otherwise
     */
    @Override
    public boolean areEqual(Object obj1, Object obj2) {
        return areEqual(obj1, obj2, null, null);
    }


    /**
     * Internal method that takes two objects and deep compares them to determine if they are equal
     * First check if the objects are identical (reference check), the if one of them is null, return false
     * <p>
     * Then the BFS algorithm is used to compare the objects for
     *
     * @param obj1    First object to compare
     * @param obj2    Second object to compare
     * @param visited List of visited pairs (when null, new list is created)
     * @param queue   Queue of objects to compare (when null, new queue is created)
     * @return - True if the objects are deeply equal, false otherwise
     */
    private boolean areEqual(Object obj1, Object obj2, List<Pair> visited, Queue<Pair> queue) {

        // Quick check
        if (obj1 == obj2) return true;
        if (obj1 == null || obj2 == null) return false;


        // If there is no passed visited list or queue, create new ones
        if (visited == null) visited = new ArrayList<>();
        if (queue == null) queue = new LinkedList<>();

        // Add the first pair to the queue
        queue.add(new Pair(obj1, obj2));

        // While the queue is not empty, compare the objects
        while (!queue.isEmpty()) {

            // Get the next pair from the queue
            Pair pair = queue.poll();

            // If the pair is null, continue (it is like they are equal)
            if (pair.a() == null && pair.b() == null) {
                continue;
            }

            // If the pair is already visited, continue
            if (visited.contains(pair)) {
                continue;
            }

            // Get the objects from the pair
            Object objA = pair.a();
            Object objB = pair.b();

            // Compare all fields of the object
            if (!compareObject(objA, objB, queue, visited)) {
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
     * @param obj1    First object to compare
     * @param obj2    Second object to compare
     * @param visited List of visited pairs (when null, new list is created)
     * @param queue   Queue of objects to compare (when null, new queue is created)
     * @return - True if the objects are deeply equal, false otherwis
     */
    private boolean compareObject(Object obj1, Object obj2, Queue<Pair> queue, List<Pair> visited) {

        // Quick checks
        if (obj1 == obj2) return true;
        if (obj1 == null || obj2 == null) return false;

        // Type check
        // TODO - INHERITANCE
        if (!obj1.getClass().equals(obj2.getClass())) {
            if (!isAnonymousClass(obj1, obj2)) {
                System.out.println("Types are not equal");
                return false;
            }
        }

        // Get the type of the object
        Class<?> type = obj1.getClass();

        // Decide what to do based on the type
        if (type.isPrimitive() || isWrapperOrString(type)) {

            // Wrappers and String have equals method implemented
            return compareWrapperOrString(obj1, obj2);

        } else if (type.isArray()) {
            // Arrays => Compare arrays index by index
            return compareArray(obj1, obj2, queue);
        } else {
            // Objects => Compare fields of the objects (Default collections also)
            return compareFields(obj1, obj2, queue);

        }

    }


    /**
     * Check if the objects are anonymous classes
     *
     * @param a First object
     * @param b Second object
     * @return true if both objects are anonymous classes, false otherwise
     */
    private boolean isAnonymousClass(Object a, Object b) {
        return a.getClass().isAnonymousClass() && b.getClass().isAnonymousClass();
    }

    /**
     * If algorithm determines that the objects are objects, it compares the fields of the objects
     *
     * @param a     First object
     * @param b     Second object
     * @param queue Queue of objects to compare for Algorithm
     * @return true if the fields are equal, false otherwise
     */
    private boolean compareFields(Object a, Object b, Queue<Pair> queue) {

        // Get all declared fields of the objects
        Field[] fieldsA = getFields(a.getClass());
        Field[] fieldsB = getFields(b.getClass());

        // Check if the number of fields is equal
        // TODO - This will be problematic for inheritance
        if (fieldsA.length != fieldsB.length) {
            System.out.println("Number of fields is not equal");
            return false;
        }

        // Compare each field
        for (Field fieldA : fieldsA) {

            // Find the matching field in the second object
            Field fieldB = findMatching(fieldA, fieldsB);

            // If no matching field is found, the objects are not equal
            if (fieldB == null) {
                System.out.println("Field: " + fieldA.getName() + " not found in the second object");
                return false;
            }

            try {
                // Get the values of the fields
                Object valueA = fieldA.get(a);
                Object valueB = fieldB.get(b);

                if (valueA == valueB) {
                    continue;
                }



                if (valueA == null || valueB == null) {
                    System.out.println("Field: " + fieldA.getName() + " is null");
                    return false;
                }

                if (valueA.getClass().isPrimitive() || isWrapperOrString(valueA.getClass())) {
                    if (!compareWrapperOrString(valueA, valueB)) {
                        System.out.println("Field: " + fieldA.getName() + " is not equal");
                        return false;
                    }
                } else {
                    queue.add(new Pair(valueA, valueB));
                }


            } catch (IllegalAccessException e) {
                e.printStackTrace();
                System.out.println("Error while accessing field: " + fieldA.getName());
                return false;

            }
        }

        return true;
    }


    /**
     * Compare two lists index by index (expanding the object tree)
     *
     * @param listA List A
     * @param listB List B
     * @param queue Queue of objects to compare for Algorithm
     * @return true if the lists are equal, false otherwise
     */
    private boolean compareList(List<?> listA, List<?> listB, Queue<Pair> queue) {
        if (listA.size() != listB.size()) {
            return false;
        }
        for (int i = 0; i < listA.size(); i++) {
            Object elemA = listA.get(i);
            Object elemB = listB.get(i);
            queue.add(new Pair(elemA, elemB));
        }
        return true;
    }


    /**
     * Compare two sets element by element (recursive call for objects)
     *
     * @param set1    Set A
     * @param set2    Set B
     * @param queue   Queue of objects to compare for Algorithm
     * @param visited List of visited pairs (when null, new list is created)
     * @return true if the sets are equal, false otherwise
     *
     * @hidden
     */
    public boolean compareSets(Set<?> set1, Set<?> set2, Queue<Pair> queue, List<Pair> visited) {
        if (set1.size() != set2.size()) {
            return false;
        }

        // Create a copy of the second set, prevent modification of the original set
        List<Object> unmatched = new ArrayList<>(set2);

        for (Object elem1 : set1) {
            boolean found = false;

            for (Object elem2 : unmatched) {
                if (areEqual(elem1, elem2, visited, queue)) {
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
     * @param mapA    Map A
     * @param mapB    Map B
     * @param queue   Queue of objects to compare for Algorithm
     * @param visited List of visited pairs (when null, new list is created)
     * @return true if the maps are equal, false otherwise
     *
     * @hidden
     */
    private boolean compareMaps(Map<?, ?> mapA, Map<?, ?> mapB, Queue<Pair> queue, List<Pair> visited) {
        if (mapA.size() != mapB.size()) {
            return false;
        }

        for (Map.Entry<?, ?> entryA : mapA.entrySet()) {
            Object keyA = entryA.getKey();
            Object valueA = entryA.getValue();

            boolean found = false;
            for (Object keyB : mapB.keySet()) {
                if (areEqual(keyA, keyB, visited, queue)) {
                    queue.add(new Pair(valueA, mapB.get(keyB)));
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
     * Compare two arrays index by index (expanding the object tree)
     *
     * @param a     Array A
     * @param b     Array B
     * @param queue Queue of objects to compare for Algorithm
     * @return true if the arrays are equal, false otherwise
     */
    private boolean compareArray(Object a, Object b, Queue<Pair> queue) {

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
     * @param fieldA  Field from the first object
     * @param fieldsB Fields from the second object
     * @return The matching field or null if no matching field is found
     */
    private Field findMatching(Field fieldA, Field[] fieldsB) {
        for (Field fieldB : fieldsB) {
            if (fieldA.getName().equals(fieldB.getName())) {
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
    private boolean isWrapperOrString(Class<?> type) {
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
     * @param obj1 - the first object
     * @param obj2 - the second object
     * @return - true if the objects are equal, false otherwise
     */
    private boolean compareWrapperOrString(Object obj1, Object obj2) {
        return obj1.equals(obj2);
    }


    /**
     * Get all fields from a class (including private fields)
     */
    private Field[] getFields(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        // Set all fields as accessible
        AccessibleObject.setAccessible(fields, true);
        for (Field field : fields) {
            field.setAccessible(true);
        }
        return fields;
    }


}
