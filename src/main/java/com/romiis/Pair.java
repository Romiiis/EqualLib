package com.romiis;

/**
 * A record that stores two objects (Pair) for easier storage and comparison.
 *
 * @param a The first object.
 * @param b The second object.
 * @author Roman Pejs
 * @version 1.0
 * @see EqualLib
 */
public record Pair(Object a, Object b) {

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
            return a.equals(other.a) && b.equals(other.b);
        }
        return false;
    }
}
