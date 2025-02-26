package com.romiis.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Utility class for deep copying objects.
 */
public class DeepCopyUtil {

    // Cache for copied objects to prevent circular references
    private static final Map<Object, Object> copyCache = new IdentityHashMap<>();

    /**
     * Deep copy an object.
     *
     * @param object the object to copy
     * @param <T>    the type of the object
     * @return the copied object
     */
    @SuppressWarnings("unchecked")
    public static <T> T deepCopy(T object) {

        // Handle null
        if (object == null) return null;

        // Check if the object was already copied to prevent circular references
        if (copyCache.containsKey(object)) {
            return (T) copyCache.get(object);
        }

        try {
            Class<?> clazz = object.getClass();

            // Handle primitive and immutable types
            if (isImmutable(clazz)) return object;

            // Create a new instance without calling constructors
            T newObject = (T) allocateInstance(clazz);
            copyCache.put(object, newObject);

            // Copy all fields (including inherited ones)
            copyAllFields(object, newObject);

            return newObject;
        } catch (Exception e) {
            throw new RuntimeException("Error during deep copy", e);
        }
    }

    /**
     * Check if a class is immutable.
     *
     * @param clazz the class to check
     * @return true if the class is immutable, false otherwise
     */
    private static boolean isImmutable(Class<?> clazz) {
        return clazz.isPrimitive() || clazz.equals(String.class) || clazz.equals(Integer.class)
                || clazz.equals(Double.class) || clazz.equals(Boolean.class)
                || clazz.equals(Float.class) || clazz.equals(Long.class) || clazz.equals(Short.class)
                || clazz.equals(Byte.class) || clazz.equals(Character.class) || clazz.isEnum();
    }

    /**
     * Copy all fields of an object to another object.
     *
     * @param original the original object
     * @param copy     the object to copy to
     * @throws Exception if an error occurs during copying
     */
    private static void copyAllFields(Object original, Object copy) throws Exception {
        Class<?> clazz = original.getClass();
        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);

                int modifiers = field.getModifiers();
                if (Modifier.isStatic(modifiers)) continue;  // Ignore static fields

                Object fieldValue = field.get(original);

                if (fieldValue != null && fieldValue.getClass().isArray()) {
                    // Handle array separately
                    Object arrayCopy = Array.newInstance(fieldValue.getClass().getComponentType(), Array.getLength(fieldValue));
                    for (int i = 0; i < Array.getLength(fieldValue); i++) {
                        Array.set(arrayCopy, i, deepCopy(Array.get(fieldValue, i)));
                    }
                    field.set(copy, arrayCopy);
                } else {
                    // Regular field
                    field.set(copy, deepCopy(fieldValue));
                }
            }
            clazz = clazz.getSuperclass();
        }
    }


    /**
     * Set a final field to a new value.
     *
     * @param field  the field to set
     * @param target the object containing the field
     * @param value  the new value
     * @throws Exception if an error occurs during setting
     */
    private static void setFinalField(Field field, Object target, Object value) throws Exception {
        field.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(target, value);
    }

    /**
     * Allocate a new instance of a class without calling constructors.
     *
     * @param clazz the class to allocate an instance of
     * @return the new instance
     * @throws Exception if an error occurs during allocation
     */
    private static Object allocateInstance(Class<?> clazz) throws Exception {
        Method unsafeConstructor = UnsafeHolder.UNSAFE.getClass().getDeclaredMethod("allocateInstance", Class.class);
        return unsafeConstructor.invoke(UnsafeHolder.UNSAFE, clazz);
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
