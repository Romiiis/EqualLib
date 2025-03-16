package com.romiis;

import com.romiis.core.EqualLib;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * These tests demonstrate the use of the {@link EqualLib#areEqual(Object, Object)} method
 * For comparing Collections and Maps !!! TREATING COLLECTIONS AS OBJECTS !!! EVERYTHING IS COMPARED
 */
public class CollectionsDeepEqualsTest {



    @DisplayName("Test ArrayList (List) with deepEquals")
    @Test
    void testArrayList() {
        List<String> list1 = new ArrayList<>();
        list1.add("A");
        list1.add("B");
        list1.add("C");

        List<String> list2 = new ArrayList<>();
        list2.add("A");
        list2.add("B");
        list2.add("C");

        assertTrue(EqualLib.areEqual(list1, list2));
        //symmetric
        assertTrue(EqualLib.areEqual(list2, list1));

        list2.remove("C");
        assertFalse(EqualLib.areEqual(list1, list2));
        //symmetric
        assertFalse(EqualLib.areEqual(list2, list1));
    }

    @DisplayName("Test LinkedList (List) with deepEquals")
    @Test
    void testLinkedList() {
        List<String> list1 = new LinkedList<>();
        list1.add("A");
        list1.add("B");
        list1.add("C");

        List<String> list2 = DeepCopyUtil.deepCopy(list1);

        assertTrue(EqualLib.areEqual(list1, list2));
        assertTrue(EqualLib.areEqual(list2, list1));

        list2.remove("C");
        assertFalse(EqualLib.areEqual(list1, list2));
        assertFalse(EqualLib.areEqual(list2, list1));
    }

    @DisplayName("Test HashSet (Set) with deepEquals")
    @Test
    void testHashSet() {
        Set<String> set1 = new HashSet<>(Arrays.asList("A", "B", "C"));
        Set<String> set2 = DeepCopyUtil.deepCopy(set1);

        assertTrue(EqualLib.areEqual(set1, set2));
        //symmetric
        assertTrue(EqualLib.areEqual(set2, set1));

        set2.add("D");
        assertFalse(EqualLib.areEqual(set1, set2));
        //symmetric
        assertFalse(EqualLib.areEqual(set2, set1));
    }

    @DisplayName("Test LinkedHashSet (Set) with deepEquals")
    @Test
    void testLinkedHashSet() {
        LinkedHashSet<String> set1 = new LinkedHashSet<>(Arrays.asList("A", "B", "C"));
        LinkedHashSet<String> set2 = DeepCopyUtil.deepCopy(set1);

        assertTrue(EqualLib.areEqual(set1, set2));
        assertTrue(EqualLib.areEqual(set2, set1));

        set2.remove("C");
        assertFalse(EqualLib.areEqual(set1, set2));
        assertFalse(EqualLib.areEqual(set2, set1));
    }

    @DisplayName("Test TreeSet (Set) with deepEquals")
    @Test
    void testTreeSet() {
        Set<Integer> set1 = new TreeSet<>(Arrays.asList(1, 3, 5));
        Set<Integer> set2 = DeepCopyUtil.deepCopy(set1);

        assertTrue(EqualLib.areEqual(set1, set2));
        assertTrue(EqualLib.areEqual(set2, set1));

        set2.add(4);

        assertFalse(EqualLib.areEqual(set1, set2));
        assertFalse(EqualLib.areEqual(set2, set1));
    }

    @DisplayName("Test HashMap (Map) with deepEquals")
    @Test
    void testHashMap() {
        Map<String, Integer> map1 = new HashMap<>();
        map1.put("A", 1);
        map1.put("B", 2);

        Map<String, Integer> map2 = DeepCopyUtil.deepCopy(map1);

        assertTrue(EqualLib.areEqual(map1, map2));
        assertTrue(EqualLib.areEqual(map2, map1));

        map2.put("C", 3);
        assertFalse(EqualLib.areEqual(map1, map2));
        assertFalse(EqualLib.areEqual(map2, map1));
    }

    @DisplayName("Test LinkedHashMap (Map) with deepEquals")
    @Test
    void testLinkedHashMap() {
        Map<String, Integer> map1 = new LinkedHashMap<>();
        map1.put("A", 1);
        map1.put("B", 2);

        Map<String, Integer> map2 = DeepCopyUtil.deepCopy(map1);
        assertTrue(EqualLib.areEqual(map1, map2));
        assertTrue(EqualLib.areEqual(map2, map1));

        map2.put("C", 3);
        assertFalse(EqualLib.areEqual(map1, map2));
        assertFalse(EqualLib.areEqual(map2, map1));
    }

    @DisplayName("Test TreeMap (Map) with deepEquals")
    @Test
    void testTreeMap() {
        Map<Integer, String> map1 = new TreeMap<>();
        map1.put(1, "A");
        map1.put(2, "B");

        Map<Integer, String> map2 = DeepCopyUtil.deepCopy(map1);

        System.out.println("copied");

        assertTrue(EqualLib.areEqual(map1, map2));
        assertTrue(EqualLib.areEqual(map2, map1));

        map2.put(3, "C");
        assertFalse(EqualLib.areEqual(map1, map2));
        assertFalse(EqualLib.areEqual(map2, map1));
    }

    @DisplayName("Test PriorityQueue (Queue) with deepEquals")
    @Test
    void testPriorityQueue() {
        Queue<Integer> queue1 = new PriorityQueue<>();
        queue1.add(3);
        queue1.add(1);
        queue1.add(2);

        Queue<Integer> queue2 = DeepCopyUtil.deepCopy(queue1);


        assertTrue(EqualLib.areEqual(queue1, queue2));
        assertTrue(EqualLib.areEqual(queue2, queue1));

        queue2.poll();
        assertFalse(EqualLib.areEqual(queue1, queue2));
        assertFalse(EqualLib.areEqual(queue2, queue1));
    }

    @DisplayName("Test LinkedList as Queue with deepEquals")
    @Test
    void testLinkedListAsQueue() {
        Queue<String> queue1 = new LinkedList<>();
        queue1.add("A");
        queue1.add("B");
        queue1.add("C");

        Queue<String> queue2 = DeepCopyUtil.deepCopy(queue1);

        assertTrue(EqualLib.areEqual(queue1, queue2));
        assertTrue(EqualLib.areEqual(queue2, queue1));

        queue2.poll();
        assertFalse(EqualLib.areEqual(queue1, queue2));
        assertFalse(EqualLib.areEqual(queue2, queue1));
    }

    // TODO - DEQUE and PRIORITIZED QUEUE - problem with modules (open with argument --add-opens java.base/java.util=ALL-UNNAMED )
    @DisplayName("Test ArrayDeque (Deque) with deepEquals")
    @Test
    void testArrayDeque() {
        Deque<String> deque1 = new ArrayDeque<>();
        deque1.addFirst("A");
        deque1.addLast("B");
        deque1.addFirst("C");

        Deque<String> deque2 = DeepCopyUtil.deepCopy(deque1);


        assertTrue(EqualLib.areEqual(deque1, deque2));
        assertTrue(EqualLib.areEqual(deque2, deque1));

        deque2.removeFirst();

        assertFalse(EqualLib.areEqual(deque1, deque2));
        assertFalse(EqualLib.areEqual(deque2, deque1));

    }

    @DisplayName("Test Stack (LIFO) with deepEquals")
    @Test
    void testStack() {
        Stack<String> stack1 = new Stack<>();
        stack1.push("A");
        stack1.push("B");
        stack1.push("C");

        Stack<String> stack2 = DeepCopyUtil.deepCopy(stack1);

        assertTrue(EqualLib.areEqual(stack1, stack2));
        assertTrue(EqualLib.areEqual(stack2, stack1));

        stack2.pop();
        assertFalse(EqualLib.areEqual(stack1, stack2));
        assertFalse(EqualLib.areEqual(stack2, stack1));
    }
}

