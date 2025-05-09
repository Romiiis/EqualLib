package com.romiiis;

import java.lang.reflect.*;
import java.util.*;

public class DeepCopyUtil {

    /**
     * Deep copy an object using a new, local cache.
     *
     * @param object the object to copy
     * @param <T>    the type of the object
     * @return the deep copy of the object, including all internal fields
     */
    @SuppressWarnings("unchecked")
    public static <T> T deepCopy(T object) {
        // Create a new cache for each deep copy operation
        Map<Object, Object> copyCache = new IdentityHashMap<>();
        return deepCopy(object, copyCache);
    }

    /**
     * Internal recursive deep copy that uses a provided cache to handle circular references.
     *
     * @param object    the object to copy
     * @param copyCache the cache for already copied objects
     * @param <T>       the type of the object
     * @return the deep copy of the object, including every internal field
     */
    @SuppressWarnings("unchecked")
    private static <T> T deepCopy(T object, Map<Object, Object> copyCache) {
        // Handle null
        if (object == null) return null;

        // Force lazy initialization on the object by invoking its public no-arg getters
        object = forceLazyInitialization(object);

        // Check if the object was already copied to prevent circular references
        if (copyCache.containsKey(object)) {
            return (T) copyCache.get(object);
        }

        try {
            Class<?> clazz = object.getClass();

            // Handle primitive and immutable types
            if (isImmutable(clazz)) return object;

            // Create a new instance without calling any constructors
            T newObject = (T) allocateInstance(clazz);
            // Put the new object into the cache before copying fields to handle circular references
            copyCache.put(object, newObject);

            // Copy all fields (including inherited ones), without filtering out any lazy or cached fields.
            copyAllFields(object, newObject, copyCache);

            return newObject;
        } catch (Exception e) {
            throw new RuntimeException("Error during deep copy", e);
        }
    }

    /**
     * Force lazy initialization on an object by invoking all public no-argument getters.
     * This will (hopefully) trigger the computation of any lazy fields.
     *
     * @param object the object to force lazy initialization on
     * @param <T>    the type of the object
     * @return the object with (hopefully) all lazy fields initialized
     */
    @SuppressWarnings("unchecked")
    private static <T> T forceLazyInitialization(T object) {
        if (object == null) return object;

        // If the object is one of the known collection types, call a method that forces initialization.
        if (object instanceof Map) {
            // For maps, call entrySet() and keySet()
            ((Map<?, ?>) object).entrySet();
            ((Map<?, ?>) object).keySet();
        } else if (object instanceof Set) {
            // For sets, iterate through them.
            ((Set<?>) object).iterator().hasNext();
        } else if (object instanceof Collection) {
            // For other collections, call size() and toArray()
            ((Collection<?>) object).size();
            ((Collection<?>) object).toArray();
        }

        // For any object, try to invoke all public no-argument getters.
        // WARNING: This might trigger side effects if getters perform actions beyond lazy initialization.
        for (Method method : object.getClass().getMethods()) {
            if (method.getParameterCount() == 0 &&
                    method.getName().startsWith("get") &&
                    !method.getName().equals("getClass")) {
                try {
                    method.invoke(object);
                } catch (Exception e) {
                    // Ignore exceptions from getters.
                }
            }
        }
        return object;
    }

    /**
     * Check if a class is immutable.
     *
     * @param clazz the class to check
     * @return true if the class is immutable, false otherwise
     */
    private static boolean isImmutable(Class<?> clazz) {
        return clazz.isPrimitive() ||
                clazz.equals(String.class) ||
                clazz.equals(Integer.class) ||
                clazz.equals(Double.class) ||
                clazz.equals(Boolean.class) ||
                clazz.equals(Float.class) ||
                clazz.equals(Long.class) ||
                clazz.equals(Short.class) ||
                clazz.equals(Byte.class) ||
                clazz.equals(Character.class) ||
                clazz.isEnum();
    }

    /**
     * Copy all fields of an object to another object using the given cache.
     * This version copies every field, including those that are normally considered
     * lazy or cached.
     *
     * @param original  the original object
     * @param copy      the object to copy to
     * @param copyCache the cache of already copied objects
     * @throws Exception if an error occurs during copying
     */
    private static void copyAllFields(Object original, Object copy, Map<Object, Object> copyCache) throws Exception {
        Class<?> clazz = original.getClass();
        while (clazz != null) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);

                // Skip only static fields; we copy every non-static field.
                if (Modifier.isStatic(field.getModifiers())) continue;

                Object fieldValue = field.get(original);
                if (fieldValue != null && fieldValue.getClass().isArray()) {
                    // Handle arrays separately.
                    int length = Array.getLength(fieldValue);
                    Object arrayCopy = Array.newInstance(fieldValue.getClass().getComponentType(), length);
                    for (int i = 0; i < length; i++) {
                        Array.set(arrayCopy, i, deepCopy(Array.get(fieldValue, i), copyCache));
                    }
                    field.set(copy, arrayCopy);
                } else {
                    // Deep copy the field value.
                    field.set(copy, deepCopy(fieldValue, copyCache));
                }
            }
            clazz = clazz.getSuperclass();
        }
    }

    /**
     * Allocate a new instance of a class without calling its constructors.
     *
     * @param clazz the class to instantiate
     * @return the new instance of the class
     * @throws Exception if instance allocation fails
     */
    private static Object allocateInstance(Class<?> clazz) throws Exception {
        Method unsafeConstructor = UnsafeHolder.UNSAFE.getClass().getDeclaredMethod("allocateInstance", Class.class);
        return unsafeConstructor.invoke(UnsafeHolder.UNSAFE, clazz);
    }

    /**
     * Utility method to print object fields for debugging.
     */
    public static void printObjects(Object obj1, Object obj2) {
        Class<?> clazz = obj1.getClass();
        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    Object value1 = field.get(obj1);
                    Object value2 = field.get(obj2);
                    System.out.println(field.getName() + ": " + value1 + " - " + value2);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            clazz = clazz.getSuperclass();
        }
    }

    /**
     * Holder for the Unsafe instance.
     */
    private static class UnsafeHolder {
        private static final sun.misc.Unsafe UNSAFE;
        static {
            try {
                Field field = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
                field.setAccessible(true);
                UNSAFE = (sun.misc.Unsafe) field.get(null);
            } catch (Exception e) {
                throw new RuntimeException("Could not access Unsafe", e);
            }
        }
    }
}
