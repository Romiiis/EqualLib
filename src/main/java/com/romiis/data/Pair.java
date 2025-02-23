package com.romiis.data;

import lombok.Getter;

/**
 * This is a simple pair class that holds two objects.
 * For the DFS algorithm, we need to keep track of the current object and the field that we are currently analyzing.
 */
@Getter
public class Pair {
    private final Object a;
    private final Object b;

    public Pair(Object a, Object b) {
        this.a = a;
        this.b = b;
    }


    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Pair) {
            Pair other = (Pair) obj;
            return a.equals(other.a) && b.equals(other.b);
        }
        return false;
    }
}
