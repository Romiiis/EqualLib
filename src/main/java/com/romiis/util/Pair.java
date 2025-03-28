package com.romiis.util;

import com.romiis.core.EqualLib;

/**
 * A class that stores two objects (Pair) for easier storage and comparison.
 *
 * @author Roman Pejs
 * @version 1.0
 * @see EqualLib
 */

public class Pair {

    private final Object first;
    private final Object second;
    private final int depth;

    /**
     * Constructor for the Pair class.
     *
     * @param first  The first object.
     * @param second The second object.
     */
    public Pair(Object first, Object second) {
        this.first = first;
        this.second = second;
        this.depth = -1;
    }

    /**
     * Constructor for the Pair class.
     *
     * @param first  The first object.
     * @param second The second object.
     * @param depth  The depth of the comparison.
     */
    public Pair(Object first, Object second, int depth) {
        this.first = first;
        this.second = second;
        this.depth = depth;
    }

    /**
     * Equals method that compares the identity of the objects.
     *
     * @param obj the reference object with which to compare.
     * @return True if objects in this pair are the same reference, false otherwise.
     */
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }
        if (obj instanceof Pair other) {
            return first == other.first && second == other.second;
        }
        return false;
    }

    /**
     * Hash code method that returns a stable, reference-based hash for each object.
     *
     * @return The hash code of the pair.
     */
    @Override
    public int hashCode() {
        // identityHashCode(...) returns a stable, reference-based hash for each object
        int h1 = System.identityHashCode(first);
        int h2 = System.identityHashCode(second);
        return 31 * h1 + h2;
    }


    /**
     * Checks if both objects in the pair are of the same type.
     *
     * @param pair The pair to check.
     * @param equalitySuperclass If true, the common superclass of the two objects is returned. If false, the objects must be of the same type.
     * @return The common class type if both objects are of the same type, null otherwise.
     */
    public static Class<?> getCommonType(Pair pair, boolean equalitySuperclass) {
        if (isNullPresent(pair)) {
            return null;
        }

        Class<?> class1 = pair.first.getClass();
        Class<?> class2 = pair.second.getClass();


        if (equalitySuperclass) {

            // If equalitySuperclass is true, find the common superclass of both objects
            Class<?> superClass = null;

            // If the classes are the same, try to find the superclass
            if (class1.equals(class2)) {
                superClass = class1.getSuperclass();
                if (superClass.equals(Object.class)) {
                    return class1; // No common superclass other than Object
                }
                return superClass;
            }

            // Start by checking both classes' hierarchies
            for (Class<?> clazz = class1; clazz != null; clazz = clazz.getSuperclass()) {
                if (clazz.isAssignableFrom(class2)) {
                    superClass = clazz;
                    if (superClass.equals(Object.class)) {
                        return null;
                    }
                    break;
                }
            }

            // If no common superclass found, check the other direction (from class2 to class1)
            if (superClass == null) {
                for (Class<?> clazz = class2; clazz != null; clazz = clazz.getSuperclass()) {
                    if (clazz.isAssignableFrom(class1)) {
                        superClass = clazz;
                        if (superClass.equals(Object.class)) {
                            return null; // No common superclass other than Object
                        }
                        break;
                    }
                }
            }

            return superClass;

        } else {
            // If equalitySuperclass is false, check if both objects are of the same type
            return class1.equals(class2) ? class1 : null;
        }
    }



    /**
     * Determines if either object in the pair is null.
     *
     * @param pair The pair to check.
     * @return True if at least one object is null, false otherwise.
     */
    public static boolean isNullPresent(Pair pair) {
        return pair == null || pair.first == null || pair.second == null;
    }


    /**
     * Checks if both objects in the pair refer to the same instance.
     *
     * @param pair The pair to check.
     * @return True if both references point to the same object, false otherwise.
     */
    public static boolean isIdentical(Pair pair) {
        return pair.first == pair.second;
    }

    /**
     * Determines if both objects in the pair are instances of anonymous classes.
     *
     * @param pair The pair to check.
     * @return The class type if both objects are anonymous classes, null otherwise.
     */
    public static Class<?> getAnonymousClassType(Pair pair) {
        if (isNullPresent(pair)) {
            return null;
        }
        return pair.first.getClass().isAnonymousClass() && pair.second.getClass().isAnonymousClass() ? pair.first.getClass() : null;
    }


    @Override
    public String toString() {
        return "Pair{" +
                "first=" + first +
                ", second=" + second +
                ", depth=" + depth +
                '}';
    }


    /**
     * Get the first object in the pair.
     *
     * @return The first object.
     */
    public Object getFirst() {
        return first;
    }


    /**
     * Get the second object in the pair.
     *
     * @return The second object.
     */
    public Object getSecond() {
        return second;
    }


    /**
     * Get the depth of the comparison.
     *
     * @return The depth of the comparison.
     */
    public int getDepth() {
        return depth;
    }


}
