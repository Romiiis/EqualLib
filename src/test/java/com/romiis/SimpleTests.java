package com.romiis;


import com.romiis.objects.simpleTests.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class SimpleTests {


    @BeforeEach
    public void setUp() {
    }

    @DisplayName("Set Equality Test - cycle")
    @Test
    public void areEqualSetsCycle() {
        EqualLib equalLib = new EqualLib();

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

        assertTrue(equalLib.areEqual(setA, setB));

        a.number = 2;
        d.number = 3;
        assertFalse(equalLib.areEqual(setA, setB));
    }

    @DisplayName("Primitive Object test")
    @Test
    public void areEqualPrimitiveObject() {
        PrimitiveObject a = new PrimitiveObject(1, "a", 1.0f, 1.0, true, 'a');
        PrimitiveObject b = new PrimitiveObject(1, "a", 1.0f, 1.0, true, 'a');

        EqualLib equalLib = new EqualLib();

        assert equalLib.areEqual(a, b);
    }


    @DisplayName("Simple Equality Test")
    @Test
    public void areEqual() {
        ObjectA a = new ObjectA(1, "a");
        ObjectA b = new ObjectA(1, "a");
        EqualLib equalLib = new EqualLib();

        assert equalLib.areEqual(a, b);
    }


    @DisplayName("Simple Inequality Test")
    @Test
    public void areNotEqual() {
        ObjectA a = new ObjectA(1, "a");
        ObjectA b = new ObjectA(2, "b");
        EqualLib equalLib = new EqualLib();

        assert !equalLib.areEqual(a, b);
    }

    @DisplayName("Two objects structure - same reference")
    @Test
    public void areEqualStructure() {
        ObjectA a1 = new ObjectA(1, "a");

        ObjectB a = new ObjectB("a", a1);
        ObjectB b = new ObjectB("a", a1);

        EqualLib equalLib = new EqualLib();

        assert equalLib.areEqual(a, b);
    }

    @DisplayName("Two objects structure - different reference - same values")
    @Test
    public void areEqualStructureDifferentReference() {
        ObjectA a1 = new ObjectA(1, "a");
        ObjectA a2 = new ObjectA(1, "a");

        ObjectB a = new ObjectB("a", a1);
        ObjectB b = new ObjectB("a", a2);

        EqualLib equalLib = new EqualLib();

        assert equalLib.areEqual(a, b);
    }

    @DisplayName("Two objects structure - different reference - different values")
    @Test
    public void areNotEqualStructureDifferentReference() {
        ObjectA a1 = new ObjectA(1, "a");
        ObjectA a2 = new ObjectA(2, "b");

        ObjectB a = new ObjectB("a", a1);
        ObjectB b = new ObjectB("a", a2);

        EqualLib equalLib = new EqualLib();

        assert !equalLib.areEqual(a, b);
    }

    @DisplayName("Cyclic reference - same references")
    @Test
    public void areEqualCyclicReference() {
        ObjectC c1 = new ObjectC("a", null);
        ObjectC c2 = new ObjectC("a", null);
        ObjectC c3 = new ObjectC("a", null);

        c1.setNext(c2);
        c2.setNext(c3);
        c3.setNext(c2);

        ObjectC c4 = new ObjectC("a", null);
        c4.setNext(c2);

        EqualLib equalLib = new EqualLib();

        assert equalLib.areEqual(c1, c4);


    }

    @DisplayName("Cyclic reference - diff references - same values")
    @Test
    public void areEqualCyclicSameReferenceSameVals() {
        ObjectC c1 = new ObjectC("a", null);
        ObjectC c2 = new ObjectC("a", null);
        ObjectC c3 = new ObjectC("a", null);

        c1.setNext(c2);
        c2.setNext(c3);
        c3.setNext(c2);

        ObjectC c4 = new ObjectC("a", null);
        ObjectC c5 = new ObjectC("a", null);
        ObjectC c6 = new ObjectC("a", null);
        c4.setNext(c5);
        c5.setNext(c6);
        c6.setNext(c5);


        EqualLib equalLib = new EqualLib();

        assert equalLib.areEqual(c1, c4);


    }

    @DisplayName("Cyclic reference - diff references - diff values")
    @Test
    public void areEqualCyclicReferenceDiffVals() {
        ObjectC c1 = new ObjectC("a", null);
        ObjectC c2 = new ObjectC("b", null);
        ObjectC c3 = new ObjectC("c", null);

        c1.setNext(c2);
        c2.setNext(c3);
        c3.setNext(c2);

        ObjectC c4 = new ObjectC("a", null);
        ObjectC c5 = new ObjectC("c", null);
        ObjectC c6 = new ObjectC("b", null);
        c4.setNext(c5);
        c5.setNext(c6);
        c6.setNext(c5);


        EqualLib equalLib = new EqualLib();

        assert !equalLib.areEqual(c1, c4);


    }

    @DisplayName("Primitive arrays")
    @Test
    public void areEqualPrimitiveArrays() {
        int[] a = {1, 2, 3};
        int[] b = {1, 2, 3};

        EqualLib equalLib = new EqualLib();

        assert equalLib.areEqual(a, b);

        b = new int[]{1, 2, 4};
        assert !equalLib.areEqual(a, b);
    }

    @DisplayName("Object arrays")
    @Test
    public void areEqualObjectArrays() {
        ObjectA a1 = new ObjectA(1, "a");
        ObjectA a2 = new ObjectA(2, "b");
        ObjectA a3 = new ObjectA(3, "c");

        ObjectA[] a = {a1, a2, a3};
        ObjectA[] b = {a1, a2, a3};

        EqualLib equalLib = new EqualLib();

        assert equalLib.areEqual(a, b);

        b = new ObjectA[]{a1, a2, new ObjectA(4, "d")};
        assert !equalLib.areEqual(a, b);
    }

    @DisplayName("Object arrays - complex structure")
    @Test
    public void areEqualObjectArraysComplex() {
        ObjectA a1 = new ObjectA(1, "a");
        ObjectA a2 = new ObjectA(2, "b");
        ObjectA a3 = new ObjectA(3, "c");

        ObjectA[] a = {a1, a2, a3};
        ObjectA[] b = {a1, a2, a3};

        ObjectB b1 = new ObjectB("a", a1);
        ObjectB b2 = new ObjectB("b", a2);
        ObjectB b3 = new ObjectB("c", a3);

        ObjectB b4 = new ObjectB("a", a1);
        ObjectB b5 = new ObjectB("b", a2);
        ObjectB b6 = new ObjectB("c", a3);

        ObjectB[] c = {b1, b2, b3};
        ObjectB[] d = {b1, b2, b3};
        ObjectB[] e = {b4, b5, b6};

        EqualLib equalLib = new EqualLib();

        assert equalLib.areEqual(a, b);
        assert equalLib.areEqual(c, d);
        assert equalLib.areEqual(c, e);



        b = new ObjectA[]{a1, a2, new ObjectA(4, "d")};
        assert !equalLib.areEqual(a, b);

        d = new ObjectB[]{b1, b2, new ObjectB("d", new ObjectA(4, "d"))};
        assert !equalLib.areEqual(c, d);
    }

    @DisplayName("Primitive int")
    @Test
    public void areEqualPrimitive_int() {
        int a = 1;
        int b = 1;

        EqualLib equalLib = new EqualLib();

        // a and b are equal
        assert equalLib.areEqual(a, b);

        // a and b are not equal
        b = 2;
        assert !equalLib.areEqual(a, b);
    }


    @DisplayName("Primitive long")
    @Test
    public void areEqualPrimitive_long() {
        long a = 1;
        long b = 1;

        EqualLib equalLib = new EqualLib();

        // a and b are equal
        assert equalLib.areEqual(a, b);

        // a and b are not equal
        b = 2;
        assert !equalLib.areEqual(a, b);
    }

    @DisplayName("Primitive float")
    @Test
    public void areEqualPrimitive_float() {
        float a = 1.0f;
        float b = 1.0f;

        EqualLib equalLib = new EqualLib();

        // a and b are equal
        assert equalLib.areEqual(a, b);

        // a and b are not equal
        b = 2.0f;
        assert !equalLib.areEqual(a, b);
    }

    @DisplayName("Primitive double")
    @Test
    public void areEqualPrimitive_double() {
        double a = 1.0;
        double b = 1.0;

        EqualLib equalLib = new EqualLib();

        // a and b are equal
        assert equalLib.areEqual(a, b);

        // a and b are not equal
        b = 2.0;
        assert !equalLib.areEqual(a, b);
    }

    @DisplayName("Primitive char")
    @Test
    public void areEqualPrimitive_char() {
        char a = 'a';
        char b = 'a';

        EqualLib equalLib = new EqualLib();

        // a and b are equal
        assert equalLib.areEqual(a, b);

        // a and b are not equal
        b = 'b';
        assert !equalLib.areEqual(a, b);
    }

    @DisplayName("Primitive byte")
    @Test
    public void areEqualPrimitive_byte() {
        byte a = 1;
        byte b = 1;

        EqualLib equalLib = new EqualLib();

        // a and b are equal
        assert equalLib.areEqual(a, b);

        // a and b are not equal
        b = 2;
        assert !equalLib.areEqual(a, b);
    }

    @DisplayName("Primitive short")
    @Test
    public void areEqualPrimitive_short() {
        short a = 1;
        short b = 1;

        EqualLib equalLib = new EqualLib();

        // a and b are equal
        assert equalLib.areEqual(a, b);

        // a and b are not equal
        b = 2;
        assert !equalLib.areEqual(a, b);
    }

    @DisplayName("Primitive boolean")
    @Test
    public void areEqualPrimitive_boolean() {
        boolean a = true;
        boolean b = true;

        EqualLib equalLib = new EqualLib();

        // a and b are equal
        assert equalLib.areEqual(a, b);

        // a and b are not equal
        b = false;
        assert !equalLib.areEqual(a, b);
    }

    @DisplayName("String comparison")
    @Test
    public void areEqualString() {
        String a = "Easy string for Bachelor thesis";
        String b = "Easy string for Bachelor thesis";

        EqualLib equalLib = new EqualLib();

        // a and b are equal
        assert equalLib.areEqual(a, b);

        // a and b are not equal
        b = "This is a different string";
        assert !equalLib.areEqual(a, b);
    }




    @DisplayName("Wrapper Integer")
    @Test
    public void areEqualWrapperInteger() {
        Integer a = 1;
        Integer b = 1;

        EqualLib equalLib = new EqualLib();

        // a and b are equal
        assert equalLib.areEqual(a, b);

        // a and b are not equal
        b = 2;
        assert !equalLib.areEqual(a, b);
    }

    @DisplayName("Wrapper Long")
    @Test
    public void areEqualWrapperLong() {
        Long a = 1L;
        Long b = 1L;

        EqualLib equalLib = new EqualLib();

        // a and b are equal
        assert equalLib.areEqual(a, b);

        // a and b are not equal
        b = 2L;
        assert !equalLib.areEqual(a, b);
    }


    @DisplayName("Wrapper Float")
    @Test
    public void areEqualWrapperFloat() {
        Float a = 1.0f;
        Float b = 1.0f;

        EqualLib equalLib = new EqualLib();

        // a and b are equal
        assert equalLib.areEqual(a, b);

        // a and b are not equal
        b = 2.0f;
        assert !equalLib.areEqual(a, b);
    }


    @DisplayName("Wrapper Double")
    @Test
    public void areEqualWrapperDouble() {
        Double a = 1.0;
        Double b = 1.0;

        EqualLib equalLib = new EqualLib();

        // a and b are equal
        assert equalLib.areEqual(a, b);

        // a and b are not equal
        b = 2.0;
        assert !equalLib.areEqual(a, b);
    }

    @DisplayName("Wrapper Character")
    @Test
    public void areEqualWrapperCharacter() {
        Character a = 'a';
        Character b = 'a';

        EqualLib equalLib = new EqualLib();

        // a and b are equal
        assert equalLib.areEqual(a, b);

        // a and b are not equal
        b = 'b';
        assert !equalLib.areEqual(a, b);
    }

    @DisplayName("Wrapper Byte")
    @Test
    public void areEqualWrapperByte() {
        Byte a = 1;
        Byte b = 1;

        EqualLib equalLib = new EqualLib();

        // a and b are equal
        assert equalLib.areEqual(a, b);

        // a and b are not equal
        b = 2;
        assert !equalLib.areEqual(a, b);
    }

    @DisplayName("Wrapper Short")
    @Test
    public void areEqualWrapperShort() {
        Short a = 1;
        Short b = 1;

        EqualLib equalLib = new EqualLib();

        // a and b are equal
        assert equalLib.areEqual(a, b);

        // a and b are not equal
        b = 2;
        assert !equalLib.areEqual(a, b);
    }

    @DisplayName("Wrapper Boolean")
    @Test
    public void areEqualWrapperBoolean() {
        Boolean a = true;
        Boolean b = true;

        EqualLib equalLib = new EqualLib();

        // a and b are equal
        assert equalLib.areEqual(a, b);

        // a and b are not equal
        b = false;
        assert !equalLib.areEqual(a, b);
    }

    @DisplayName("List Equality Test - Same Values")
    @Test
    public void areEqualLists() {
        EqualLib equalLib = new EqualLib();
        List<ObjectA> listA = new ArrayList<>(Arrays.asList(new ObjectA(1, "a"), new ObjectA(2, "b")));
        List<ObjectA> listB = new ArrayList<>(Arrays.asList(new ObjectA(1, "a"), new ObjectA(2, "b")));

        assertTrue(equalLib.areEqual(listA, listB));
    }

    @DisplayName("List Inequality Test - Different Values")
    @Test
    public void areNotEqualLists() {
        EqualLib equalLib = new EqualLib();
        List<ObjectA> listA = new ArrayList<>(Arrays.asList(new ObjectA(1, "a"), new ObjectA(2, "b")));
        List<ObjectA> listB = new ArrayList<>(Arrays.asList(new ObjectA(1, "a"), new ObjectA(3, "c")));

        assertFalse(equalLib.areEqual(listA, listB));
    }

    @DisplayName("Set Equality Test - Same Values")
    @Test
    public void areEqualSets() {
        EqualLib equalLib = new EqualLib();
        Set<ObjectA> setA = new HashSet<>(Arrays.asList(new ObjectA(1, "a"), new ObjectA(2, "b")));
        Set<ObjectA> setB = new HashSet<>(Arrays.asList(new ObjectA(2, "b"), new ObjectA(1, "a")));

        assertTrue(equalLib.areEqual(setA, setB));
    }



    @DisplayName("Set Inequality Test - Different Values")
    @Test
    public void areNotEqualSets() {
        EqualLib equalLib = new EqualLib();
        Set<ObjectA> setA = new HashSet<>(Arrays.asList(new ObjectA(1, "a"), new ObjectA(2, "b")));
        Set<ObjectA> setB = new HashSet<>(Arrays.asList(new ObjectA(1, "a"), new ObjectA(3, "c")));

        assertFalse(equalLib.areEqual(setA, setB));
    }

    @DisplayName("Map Equality Test - Same Key-Value Pairs")
    @Test
    public void areEqualMaps() {
        EqualLib equalLib = new EqualLib();
        Map<String, ObjectA> mapA = new HashMap<>();
        mapA.put("key1", new ObjectA(1, "a"));
        mapA.put("key2", new ObjectA(2, "b"));

        Map<String, ObjectA> mapB = new HashMap<>();
        mapB.put("key1", new ObjectA(1, "a"));
        mapB.put("key2", new ObjectA(2, "b"));

        assertTrue(equalLib.areEqual(mapA, mapB));
    }

    @DisplayName("Map Inequality Test - Different Key-Value Pairs")
    @Test
    public void areNotEqualMaps() {
        EqualLib equalLib = new EqualLib();
        Map<String, ObjectA> mapA = new HashMap<>();
        mapA.put("key1", new ObjectA(1, "a"));
        mapA.put("key2", new ObjectA(2, "b"));

        Map<String, ObjectA> mapB = new HashMap<>();
        mapB.put("key1", new ObjectA(1, "a"));
        mapB.put("key2", new ObjectA(3, "c"));

        assertFalse(equalLib.areEqual(mapA, mapB));
    }

    @DisplayName("List with Null Values")
    @Test
    public void areEqualListsWithNulls() {
        EqualLib equalLib = new EqualLib();
        List<ObjectA> listA = new ArrayList<>(Arrays.asList(new ObjectA(1, "a"), null));
        List<ObjectA> listB = new ArrayList<>(Arrays.asList(new ObjectA(1, "a"), null));

        assertTrue(equalLib.areEqual(listA, listB));
    }

    @DisplayName("Set with Null Values")
    @Test
    public void areEqualSetsWithNulls() {

        EqualLib equalLib = new EqualLib();
        Set<ObjectA> setA = new HashSet<>(Arrays.asList(new ObjectA(1, "a"), null));
        Set<ObjectA> setB = new HashSet<>(Arrays.asList(new ObjectA(1, "a"), null));

        assertTrue(equalLib.areEqual(setA, setB));
    }

    @DisplayName("Map with Null Values")
    @Test
    public void areEqualMapsWithNulls() {
        EqualLib equalLib = new EqualLib();
        Map<String, ObjectA> mapA = new HashMap<>();
        mapA.put("key1", new ObjectA(1, "a"));
        mapA.put("key2", null);

        Map<String, ObjectA> mapB = new HashMap<>();
        mapB.put("key1", new ObjectA(1, "a"));
        mapB.put("key2", null);

        assertTrue(equalLib.areEqual(mapA, mapB));
    }

    @DisplayName("Compare null values")
    @Test
    public void areEqualNullValues() {
        EqualLib equalLib = new EqualLib();
        assertTrue(equalLib.areEqual(null, null));
    }

    @DisplayName("Cyclic list")
    @Test
    public void areEqualCyclicList() {
        EqualLib equalLib = new EqualLib();
        List<ObjectList> listA = new ArrayList<>();
        List<ObjectList> listB = new ArrayList<>();

        ObjectList a = new ObjectList();
        ObjectList b = new ObjectList();
        ObjectList c = new ObjectList();

        a.next = listA;
        a.number = 1;

        b.next = listB;
        b.number = 1;

        c.next = listA;
        c.number = 1;

        listA.add(a);
        listA.add(b);

        listB.add(c);
        listB.add(b);

        assertTrue(equalLib.areEqual(listA, listB));

        a.number = 2;
        assertFalse(equalLib.areEqual(listA, listB));

    }


    @DisplayName("Map with cyclic reference")
    @Test
    public void areEqualMapWithCyclicReference() {
        EqualLib equalLib = new EqualLib();
        Map<String, ObjectMap> mapA = new HashMap<>();
        Map<String, ObjectMap> mapB = new HashMap<>();

        ObjectMap a = new ObjectMap();
        ObjectMap b = new ObjectMap();
        ObjectMap c = new ObjectMap();

        a.next = mapA;
        a.number = 1;

        b.next = mapB;
        b.number = 1;

        c.next = mapA;
        c.number = 1;

        mapA.put("a", a);
        mapA.put("b", b);

        mapB.put("a", c);
        mapB.put("b", b);

        assertTrue(equalLib.areEqual(mapA, mapB));

        a.number = 2;
        assertFalse(equalLib.areEqual(mapA, mapB));
    }

    @DisplayName("Map with cyclic reference 2 Object key")
    @Test
    public void areEqualMapWithCyclicReference2() {
        EqualLib equalLib = new EqualLib();
        Map<ObjectA, ObjectMap> mapA = new HashMap<>();
        Map<ObjectA, ObjectMap> mapB = new HashMap<>();

        ObjectA a = new ObjectA(1, "a");
        ObjectA b = new ObjectA(2, "b");
        ObjectA c = new ObjectA(1, "a");

        ObjectMap aMap = new ObjectMap();
        ObjectMap bMap = new ObjectMap();
        ObjectMap cMap = new ObjectMap();

        aMap.next = mapA;
        aMap.number = 1;

        bMap.next = mapB;
        bMap.number = 1;

        cMap.next = mapA;
        cMap.number = 1;

        mapA.put(a, aMap);
        mapA.put(b, bMap);

        mapB.put(c, cMap);
        mapB.put(b, bMap);

        assertTrue(equalLib.areEqual(mapA, mapB));

        a.weight = 2;
        assertFalse(equalLib.areEqual(mapA, mapB));

    }








}