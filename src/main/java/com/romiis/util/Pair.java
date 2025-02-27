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

    private final Object objA;
    private final Object objB;
    private final int depth;

    /**
     * Constructor for the Pair class.
     *
     * @param objA The first object.
     * @param objB The second object.
     */
    public Pair(Object objA, Object objB) {
        this.objA = objA;
        this.objB = objB;
        this.depth = -1;
    }

    /**
     * Constructor for the Pair class.
     *
     * @param objA The first object.
     * @param objB The second object.
     * @param depth The depth of the comparison.
     */
    public Pair(Object objA, Object objB, int depth) {
        this.objA = objA;
        this.objB = objB;
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
            return objA.equals(other.objA) && objB.equals(other.objB);
        }
        return false;
    }




    /**
     * Checks if the two objects in the pair are of the same type.
     *
     * @param pair The pair of objects.
     * @return True if the objects are of the same type, false otherwise.
     */
    public static Class<?> typeCheck(Pair pair) {
        if (nullCheck(pair)) {
            return null;
        }

        if (pair.objA.getClass().equals(pair.objB.getClass())) {
            return pair.objA.getClass();
        }

        return null;
    }


    /**
     * Checks if one of the objects in the pair is null.
     * @param pair The pair of objects.
     * @return True if one of the objects is null, false otherwise.
     */
    public static boolean nullCheck(Pair pair) {
        if (pair == null) {
            return true;
        }
        return pair.objA == null || pair.objB == null;
    }



    public static boolean identicalCheck(Pair pair) {
        return pair.objA == pair.objB;
    }

    /**
     * Check if the objects are anonymous classes
     *
     * @return true if both objects are anonymous classes, false otherwise
     */
    public static Class<?> isAnonymousClass(Pair pair) {
        if (nullCheck(pair)) {
            return null;
        }

        if (pair.objA.getClass().isAnonymousClass() && pair.objB.getClass().isAnonymousClass()) {
            return pair.objA.getClass();
        }

        return null;
    }


    public String toString() {
        return "Pair{" +
                "objA=" + objA +
                ", objB=" + objB +
                ", depth=" + depth +
                '}';
    }


    public Object getObjA() {
        return objA;
    }

    public Object getObjB() {
        return objB;
    }

    public int getDepth() {
        return depth;
    }



}
