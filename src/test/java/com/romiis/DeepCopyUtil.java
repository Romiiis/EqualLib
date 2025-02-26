package com.romiis;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

import static sun.reflect.misc.FieldUtil.getFields;


public class DeepCopyUtil {
    public static void printAllAttributes(Object object) {

        // Get all fields and values from the object and print them if it is not primitive, wrapper or string type
        // go through the object and print all fields and values

        for (Field field : getFields(object.getClass())) {
            try {
                Object value = field.get(object);
                if (value != null) {
                    if (value.getClass().isArray()) {
                        System.out.println("Array: " + field.getName());
                        for (int i = 0; i < Array.getLength(value); i++) {
                            System.out.println(Array.get(value, i));
                        }
                    } else if (value instanceof Collection) {
                        System.out.println("Collection: " + field.getName());
                        for (Object o : (Collection) value) {
                            System.out.println(o);
                        }
                    } else if (value instanceof Map) {
                        System.out.println("Map: " + field.getName());
                        for (Map.Entry entry : ((Map<?, ?>) value).entrySet()) {
                            System.out.println(entry.getKey() + " : " + entry.getValue());
                        }
                    } else {
                        System.out.println(field.getName() + " : " + value);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }


    }
}
