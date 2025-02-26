package com.romiis.util;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReflectionUtil {
    /**
     * Get all fields from a class (including private fields)
     *
     * @param clazz       - the class to get the fields from
     * @param finalParent - the final parent class where the fields should be taken from (included)
     *                    TODO - INHERITANCE
     */
    public static Field[] getFields(Class<?> clazz, Class<?> finalParent) {

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
    public static Field[] getFields(Class<?> clazz) {
        return getFields(clazz, null);
    }


}
