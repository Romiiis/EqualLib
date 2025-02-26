package com.romiis;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        ObjectSet a = new ObjectSet();
        ObjectSet b = new ObjectSet();

        ObjectSet c = new ObjectSet();
        ObjectSet d = new ObjectSet();


        Set<ObjectSet> setA = new HashSet<>(Arrays.asList(a, b));
        Set<ObjectSet> setB = new HashSet<>(Arrays.asList(c, d));

        a.set = setA;
        a.number = 1;
        b.set = setB;
        b.number = 1;

        c.set = setA;
        c.number = 1;
        d.set = setB;
        d.number = 1;



    }

    /**
     * Get all fields from a class (including private fields)
     */
    private static Field[] getFields(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        // Set all fields as accessible
        AccessibleObject.setAccessible(fields, true);
        for (Field field : fields) {
            field.setAccessible(true);
        }
        return fields;
    }
}
