package com.romiis;

import com.romiis.data.Pair;
import com.romiis.utils.ReflectionUtil;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

public class EqualLib implements IEqualLib {


    /**
     * This method compares two objects and determines if they are equal.
     * The method uses a Depth-First Search algorithm to compare the objects.
     *
     * @param a - the first object
     * @param b - the second object
     * @return - true if the objects are equal, false otherwise
     */
    @Override
    public boolean areEqual(Object a, Object b) {
        return areEqual(a, b, null, null);
    }


    /**
     * This method compares two objects and determines if they are equal.
     * @param a - the first object
     * @param b - the second object
     * @param visited - list of visited pairs
     * @param queue - queue of objects to compare
     * @return - true if the objects are equal, false otherwise
     */
    private boolean areEqual(Object a, Object b, List<Pair> visited, Queue<Pair> queue) {

        // Quick check
        if (a == b) return true;
        if (a == null || b == null) return false;

        // If there is no passed visited list or queue, create new ones
        if (visited == null) visited = new ArrayList<>();
        if (queue == null) queue = new LinkedList<>();

        // Add the first pair to the queue
        queue.add(new Pair(a, b));

        // While the queue is not empty, compare the objects
        while (!queue.isEmpty()) {

            // Get the next pair from the queue
            Pair pair = queue.poll();

            // If the pair is null, continue (it is like they are equal)
            if (pair.getA() == null && pair.getB() == null) {
                continue;
            }

            // If the pair is already visited, continue
            if (visited.contains(pair)) {
                continue;
            }

            // Get the objects from the pair
            Object objA = pair.getA();
            Object objB = pair.getB();

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
     * If the algorithm determines that the objects are objects, it compares the fields of the objects
     * @param objA - the first object
     * @param objB - the second object
     * @param queue - queue of objects to compare
     * @param visited - list of visited pairs
     * @return - true if the fields are equal, false otherwise
     */
    private boolean compareObject(Object objA, Object objB, Queue<Pair> queue, List<Pair> visited) {

        // Quick checks
        if (objA == objB) return true;
        if (objA == null || objB == null) return false;

        // Type check
        // TODO - INHERITANCE
        if (!objA.getClass().equals(objB.getClass())) {
            if (!isAnonymousClass(objA, objB)) {
                return false;
            }
        }

        // Get the type of the object
        Class<?> type = objA.getClass();

        // Decide what to do based on the type
        if (type.isPrimitive() || isWrapperOrString(type)) {

            // Wrappers and String have equals method implemented
            return compareWrapperOrString(objA, objB);

        } else if (type.isArray()) {

            // Arrays => Compare arrays index by index
            return compareArray(objA, objB, queue);

        } else if (List.class.isAssignableFrom(type)) {
            // List => Similar to arrays, compare lists index by index
            return compareList((List<?>) objA, (List<?>) objB, queue);

        } else if (Map.class.isAssignableFrom(type)) {

            // Maps => Compare maps key by key
            return compareMaps((Map<?, ?>) objA, (Map<?, ?>) objB, queue, visited);

        } else if (Set.class.isAssignableFrom(type)) {

            // Sets => Compare sets element by element
            return compareSets((Set<?>) objA, (Set<?>) objB, queue, visited);

        } else {

            // Objects => Compare fields of the objects
            return compareFields(objA, objB, queue);

        }

    }

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
        Field[] fieldsA = ReflectionUtil.getFields(a.getClass());
        Field[] fieldsB = ReflectionUtil.getFields(b.getClass());

        // Check if the number of fields is equal
        // TODO - This will be problematic for inheritance
        if (fieldsA.length != fieldsB.length) {
            return false;
        }

        // Compare each field
        for (Field fieldA : fieldsA) {

            // Find the matching field in the second object
            Field fieldB = findMatching(fieldA, fieldsB);

            // If no matching field is found, the objects are not equal
            if (fieldB == null) {
                return false;
            }

            try {
                // Get the values of the fields
                Object valueA = fieldA.get(a);
                Object valueB = fieldB.get(b);

                if (valueA == null && valueB == null) {
                    continue;
                }

                if (valueA == null || valueB == null) {
                    return false;
                }

                if (valueA.getClass().isPrimitive() || isWrapperOrString(valueA.getClass())) {
                    if (!compareWrapperOrString(valueA, valueB)) {
                        return false;
                    }
                    // TODO - other types
                } else {
                    queue.add(new Pair(valueA, valueB));
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return false;
            }
        }

        return true;
    }



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

    public boolean compareSets(Set<?> set1, Set<?> set2, Queue<Pair> queue, List<Pair> visited) {
        if (set1.size() != set2.size()) {
            return false;
        }

        List<Object> unmatched = new ArrayList<>(set2); // Kopie set2, budeme odebírat párované prvky

        for (Object elem1 : set1) {
            boolean found = false;

            for (Object elem2 : unmatched) {
                if (areEqual(elem1, elem2, visited, queue)) {
                    unmatched.remove(elem2); // Odstraníme párovaný prvek
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

    private boolean compareMaps(Map<?, ?> mapA, Map<?, ?> mapB, Queue<Pair> queue, List<Pair> visited) {
        if (mapA.size() != mapB.size()) {
            return false;
        }

        for (Map.Entry<?, ?> entryA : mapA.entrySet()) {
            Object keyA = entryA.getKey();
            Object valueA = entryA.getValue();

            boolean found = false;
            for (Object keyB : mapB.keySet()) {
                if (areEqual(keyA, keyB, visited, queue)) { // Použij sdílený visited a queue
                    queue.add(new Pair(valueA, mapB.get(keyB))); // Přidáme spárované hodnoty
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false; // Pokud nenajdeme odpovídající klíč, mapy nejsou stejné
            }
        }

        return true;
    }

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
     * @param objA - the first object
     * @param objB - the second object
     * @return - true if the objects are equal, false otherwise
     */
    private boolean compareWrapperOrString(Object objA, Object objB) {
        return objA.equals(objB);
    }


}
