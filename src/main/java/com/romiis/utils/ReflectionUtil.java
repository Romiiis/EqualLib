package com.romiis.utils;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;


public class ReflectionUtil {


    /**
     * Get all fields from a class (including private fields)
     */
    public static Field[] getFields(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        // Set all fields as accessible
        AccessibleObject.setAccessible(fields, true);
        for (Field field : fields) {
            field.setAccessible(true);
        }
        return fields;
    }

    public static Object getFieldValue(Field field, Object object) {
        try {
            return field.get(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


}
