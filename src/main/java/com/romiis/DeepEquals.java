package com.romiis;

import java.util.Map;

public class DeepEquals {

    public static final String IGNORE_CUSTOM_EQUALS = "ignoreCustomEquals";
    public static final String ALLOW_STRINGS_TO_MATCH_NUMBERS = "ignoreCustomHashCode";
    public static final String DIFF = "ignoreCustomHashCode";
    public static boolean deepEquals(Object a, Object b) {
        EqualLib equalLib = new EqualLib();
        return equalLib.areEqual(a, b);
    }

    public static boolean deepEquals(Object a, Object b, Map<String, ?> options) {
        return deepEquals(a, b);
    }

}
