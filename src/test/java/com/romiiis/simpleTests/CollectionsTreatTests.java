package com.romiiis.simpleTests;

import com.romiiis.core.EqualLib;
import com.romiiis.core.EqualLibConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CollectionsTreatTests {

    @DisplayName("Test arrays")
    @Test
    void testCollections() {
        EqualLibConfig config = new EqualLibConfig();
        config.setCompareCollectionsByElements(true);
        List<Integer> list1 = Arrays.asList(1, 2, 3);
        List<Integer> list2 = Arrays.asList(1, 2, 3);
        List<Integer> list3 = Arrays.asList(3, 2, 1);

        assertTrue(EqualLib.areEqual(list1, list2, config));
        assertFalse(EqualLib.areEqual(list1, list3, config));
    }

    @DisplayName("Test sets")
    @Test
    void testSets() {
        Set<String> set1 = new HashSet<>(Arrays.asList("A", "B", "C"));
        Set<String> set2 = new HashSet<>(Arrays.asList("C", "A", "B"));

        EqualLibConfig config = new EqualLibConfig();
        config.setCompareCollectionsByElements(true);
        assertTrue(EqualLib.areEqual(set1, set2, config));

        set2.add("D");
        assertFalse(EqualLib.areEqual(set1, set2, config));
    }

    @DisplayName("Test maps")
    @Test
    void testMaps() {
        Map<String, Integer> map1 = new HashMap<>();
        map1.put("A", 1);
        map1.put("B", 2);

        Map<String, Integer> map2 = new HashMap<>();
        map2.put("B", 2);
        map2.put("A", 1);

        EqualLibConfig config = new EqualLibConfig();
        config.setCompareCollectionsByElements(true);

        assertTrue(EqualLib.areEqual(map1, map2, config));

        map2.put("C", 3);
        assertFalse(EqualLib.areEqual(map1, map2, config));
    }

    @DisplayName("Test maps with objects")
    @Test
    void testMapsWithObjects() {
        Person p1 = new Person("Alice", 30, new Address("Prague", "Main Street"));
        Person p2 = new Person("Alice", 30, new Address("Prague", "Main Street"));

        Map<Person, String> map1 = new HashMap<>();
        map1.put(p1, "Developer");

        Map<Person, String> map2 = new HashMap<>();
        map2.put(p2, "Developer");

        EqualLibConfig config = new EqualLibConfig();
        config.setCompareCollectionsByElements(false);

        assertFalse(EqualLib.areEqual(map1, map2, config));

        config.setCompareCollectionsByElements(true);

        assertTrue(EqualLib.areEqual(map1, map2, config));

    }

    @DisplayName("Test cyclic references")
    @Test
    void testCyclicReferences() {
        Node node1 = new Node(1);
        Node node2 = new Node(1);

        node1.setNext(node1);
        node2.setNext(node2);

        EqualLibConfig config = new EqualLibConfig();
        config.setCompareCollectionsByElements(true);

        assertTrue(EqualLib.areEqual(node1, node2, config));

        node2.setNext(new Node(2));
        assertFalse(EqualLib.areEqual(node1, node2, config));
    }

    @DisplayName("Test deeply nested structures")
    @Test
    void testDeeplyNestedStructures() {
        Person p1 = new Person("Alice", 30, new Address("Prague", "Main Street"));
        Person p2 = new Person("Bob", 25, new Address("Brno", "Second Street"));

        p1.addFriend(p2);
        p2.addFriend(p1);

        Person p3 = new Person("Alice", 30, new Address("Prague", "Main Street"));
        Person p4 = new Person("Bob", 25, new Address("Brno", "Second Street"));

        p3.addFriend(p4);
        p4.addFriend(p3);

        EqualLibConfig config = new EqualLibConfig();
        config.setCompareCollectionsByElements(true);

        assertTrue(EqualLib.areEqual(p1, p3, config));
    }

    @DisplayName("Test deeply nested maps and lists")
    @Test
    void testDeeplyNestedCollections() {
        // Map with nested lists and maps
        Map<String, Map<String, List<Integer>>> complexMap1 = new HashMap<>();
        List<Integer> list1 = Arrays.asList(1, 2, 3);
        List<Integer> list2 = Arrays.asList(4, 5, 6);
        Map<String, List<Integer>> nestedMap1 = new HashMap<>();
        nestedMap1.put("first", list1);
        nestedMap1.put("second", list2);
        complexMap1.put("level1", nestedMap1);

        Map<String, Map<String, List<Integer>>> complexMap2 = new HashMap<>();
        Map<String, List<Integer>> nestedMap2 = new HashMap<>();
        nestedMap2.put("first", list1);
        nestedMap2.put("second", list2);
        complexMap2.put("level1", nestedMap2);

        EqualLibConfig config = new EqualLibConfig();
        config.setCompareCollectionsByElements(true);

        assertTrue(EqualLib.areEqual(complexMap1, complexMap2, config));

        nestedMap2.put("third", Arrays.asList(7, 8, 9)); // Modify one part of the map
        assertFalse(EqualLib.areEqual(complexMap1, complexMap2, config));
    }

    @DisplayName("Test cyclic graph with multiple nodes")
    @Test
    void testCyclicGraph() {
        // A cyclic graph with multiple nodes
        Node node1 = new Node(1);
        Node node2 = new Node(2);
        Node node3 = new Node(3);

        node1.setNext(node2);
        node2.setNext(node3);
        node3.setNext(node1); // Creating a cycle

        Node node4 = new Node(1);
        Node node5 = new Node(2);
        Node node6 = new Node(3);

        node4.setNext(node5);
        node5.setNext(node6);
        node6.setNext(node4); // Same cycle as node1 -> node2 -> node3

        EqualLibConfig config = new EqualLibConfig();
        config.setCompareCollectionsByElements(true);



        assertTrue(EqualLib.areEqual(node1, node4, config)); // Identical cycle structure, should be true


        node6.setNext(new Node(4)); // Modify one node in the cycle

        assertFalse(EqualLib.areEqual(node1, node4, config)); // Should return false
    }

    @DisplayName("Test objects with mutable and immutable fields")
    @Test
    void testMutableImmutableFields() {
        // Person object with immutable Address and mutable attributes
        Address addr1 = new Address("New York", "5th Avenue");
        Address addr2 = new Address("New York", "5th Avenue");

        Person p1 = new Person("John", 40, addr1);
        Person p2 = new Person("John", 40, addr2);

        EqualLibConfig config = new EqualLibConfig();
        config.setCompareCollectionsByElements(true);

        assertTrue(EqualLib.areEqual(p1, p2, config)); // Should return true because the fields are the same


        p2.setAge(41); // Changing a mutable field

        assertFalse(EqualLib.areEqual(p1, p2, config)); // Should return false because age is different

    }

    @DisplayName("Test complex objects with anonymous classes")
    @Test
    void testAnonymousClasses() {
        // Using anonymous class for complex structures
        Object obj1 = new Object() {
            int a = 5;
            String name = "Test";
        };

        Object obj2 = new Object() {
            int a = 5;
            String name = "Test";
        };


        EqualLibConfig config = new EqualLibConfig();
        config.setCompareCollectionsByElements(true).setDebugEnabled(true);


        assertTrue(EqualLib.areEqual(obj1, obj2, config)); // Should return true since the fields are the same

        Object obj3 = new Object() {
            int a = 5;
            String name = "Different";
        };

        assertFalse(EqualLib.areEqual(obj1, obj3)); // Should return false because the 'name' field differs
    }

    @DisplayName("Test deeply nested objects with lists and maps")
    @Test
    void testNestedObjectsWithCollections() {
        // A deeply nested structure with lists and maps inside objects
        Person p1 = new Person("Alice", 30, new Address("Prague", "Main Street"));
        Person p2 = new Person("Bob", 25, new Address("Brno", "Second Street"));
        Person p3 = new Person("Alice", 30, new Address("Prague", "Main Street"));
        Person p4 = new Person("Bob", 25, new Address("Brno", "Second Street"));

        p1.addFriend(p2);
        p2.addFriend(p1);
        p3.addFriend(p4);
        p4.addFriend(p3);

        List<Person> list1 = Arrays.asList(p1, p2);
        Map<String, List<Person>> map1 = new HashMap<>();
        map1.put("friends", list1);

        List<Person> list2 = Arrays.asList(p3, p4);
        Map<String, List<Person>> map2 = new HashMap<>();
        map2.put("friends", list2);

        EqualLibConfig config = new EqualLibConfig();
        config.setCompareCollectionsByElements(true);

        assertTrue(EqualLib.areEqual(map1, map2, config)); // Same nested structures, should return true
    }
}
