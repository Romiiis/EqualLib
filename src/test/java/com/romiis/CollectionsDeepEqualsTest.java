package com.romiis;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class CollectionsDeepEqualsTest {

    EqualLib deepEquals = new EqualLib();

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

        assertTrue(deepEquals.areEqual(list1, list2));

        list2.remove("C");
        assertFalse(deepEquals.areEqual(list1, list2));
    }

    @DisplayName("Test LinkedList (List) with deepEquals")
    @Test
    void testLinkedList() {
        List<String> list1 = new LinkedList<>();
        list1.add("A");
        list1.add("B");
        list1.add("C");

        List<String> list2 = new LinkedList<>();
        list2.add("A");
        list2.add("B");
        list2.add("C");

        assertTrue(deepEquals.areEqual(list1, list2));

        list2.remove("C");
        assertFalse(deepEquals.areEqual(list1, list2));
    }

    @DisplayName("Test HashSet (Set) with deepEquals")
    @Test
    void testHashSet() {
        Set<String> set1 = new HashSet<>(Arrays.asList("A", "B", "C"));
        Set<String> set2 = new HashSet<>(Arrays.asList("C", "A", "B"));

        assertTrue(deepEquals.areEqual(set1, set2)); // HashSet nemění pořadí, ale hodnoty jsou stejné

        set2.add("D");
        assertFalse(deepEquals.areEqual(set1, set2)); // D se přidalo, takže rozdíl
    }

    @DisplayName("Test LinkedHashSet (Set) with deepEquals")
    @Test
    void testLinkedHashSet() {
        Set<String> set1 = new LinkedHashSet<>(Arrays.asList("A", "B", "C"));
        Set<String> set2 = new LinkedHashSet<>(Arrays.asList("A", "B", "C"));

        assertTrue(deepEquals.areEqual(set1, set2)); // Pořadí by mělo být stejné

        set2.remove("C");
        assertFalse(deepEquals.areEqual(set1, set2)); // Po změně rozdíl
    }

    @DisplayName("Test TreeSet (Set) with deepEquals")
    @Test
    void testTreeSet() {
        Set<Integer> set1 = new TreeSet<>(Arrays.asList(1, 3, 5));
        Set<Integer> set2 = new TreeSet<>(Arrays.asList(5, 1, 3));

        assertTrue(deepEquals.areEqual(set1, set2)); // TreeSet by měl mít stejné prvky a v pořadí 1, 3, 5
    }

    @DisplayName("Test HashMap (Map) with deepEquals")
    @Test
    void testHashMap() {
        Map<String, Integer> map1 = new HashMap<>();
        map1.put("A", 1);
        map1.put("B", 2);

        Map<String, Integer> map2 = new HashMap<>();
        map2.put("B", 2);
        map2.put("A", 1);

        assertTrue(deepEquals.areEqual(map1, map2)); // HashMap nezáleží na pořadí klíčů

        map2.put("C", 3);
        assertFalse(deepEquals.areEqual(map1, map2)); // "C" byla přidána
    }

    @DisplayName("Test LinkedHashMap (Map) with deepEquals")
    @Test
    void testLinkedHashMap() {
        Map<String, Integer> map1 = new LinkedHashMap<>();
        map1.put("A", 1);
        map1.put("B", 2);

        Map<String, Integer> map2 = new LinkedHashMap<>();
        map2.put("A", 1);
        map2.put("B", 2);

        assertTrue(deepEquals.areEqual(map1, map2)); // Pořadí klíčů je zachováno

        map2.put("C", 3);
        assertFalse(deepEquals.areEqual(map1, map2)); // "C" byla přidána
    }

    @DisplayName("Test TreeMap (Map) with deepEquals")
    @Test
    void testTreeMap() {
        Map<Integer, String> map1 = new TreeMap<>();
        map1.put(1, "A");
        map1.put(2, "B");

        Map<Integer, String> map2 = new TreeMap<>();
        map2.put(2, "B");
        map2.put(1, "A");

        assertTrue(deepEquals.areEqual(map1, map2)); // TreeMap by měl být seřazen

        map2.put(3, "C");
        assertFalse(deepEquals.areEqual(map1, map2)); // "3" byla přidána
    }

    @DisplayName("Test PriorityQueue (Queue) with deepEquals")
    @Test
    void testPriorityQueue() {
        Queue<Integer> queue1 = new PriorityQueue<>();
        queue1.add(3);
        queue1.add(1);
        queue1.add(2);

        Queue<Integer> queue2 = new PriorityQueue<>();
        queue2.add(1);
        queue2.add(2);
        queue2.add(3);

        // Compare using sorted collections to ignore internal order
        List<Integer> list1 = new ArrayList<>(queue1);
        List<Integer> list2 = new ArrayList<>(queue2);
        Collections.sort(list1);
        Collections.sort(list2);

        assertTrue(deepEquals.areEqual(list1, list2)); // Compare sorted lists

        queue2.poll();
        list2 = new ArrayList<>(queue2);
        Collections.sort(list2);

        assertFalse(deepEquals.areEqual(list1, list2)); // After polling, the lists should differ
    }

    @DisplayName("Test LinkedList as Queue with deepEquals")
    @Test
    void testLinkedListAsQueue() {
        Queue<String> queue1 = new LinkedList<>();
        queue1.add("A");
        queue1.add("B");
        queue1.add("C");

        Queue<String> queue2 = new LinkedList<>();
        queue2.add("A");
        queue2.add("B");
        queue2.add("C");

        assertTrue(deepEquals.areEqual(queue1, queue2)); // Obě fronty mají stejné prvky

        queue2.poll();
        assertFalse(deepEquals.areEqual(queue1, queue2)); // Po vyjmutí prvku jsou fronty různé
    }

    // TODO - DEQUE and PRIORITIZED QUEUE - problem with modules (open with argument --add-opens java.base/java.util=ALL-UNNAMED )
    @DisplayName("Test ArrayDeque (Deque) with deepEquals")
    @Test
    void testArrayDeque() {
        Deque<String> deque1 = new ArrayDeque<>();
        deque1.addFirst("A");
        deque1.addLast("B");
        deque1.addFirst("C");

        Deque<String> deque2 = new ArrayDeque<>();
        deque2.addFirst("A");
        deque2.addLast("B");
        deque2.addFirst("C");

        // Convert deque to lists to avoid accessing internal fields directly
        List<String> list1 = new ArrayList<>(deque1);
        List<String> list2 = new ArrayList<>(deque2);

        assertTrue(deepEquals.areEqual(list1, list2)); // Compare based on content

        deque2.removeFirst();
        list2 = new ArrayList<>(deque2);

        assertFalse(deepEquals.areEqual(list1, list2)); // After removal, the lists should differ
    }

    @DisplayName("Test Stack (LIFO) with deepEquals")
    @Test
    void testStack() {
        Stack<String> stack1 = new Stack<>();
        stack1.push("A");
        stack1.push("B");
        stack1.push("C");

        Stack<String> stack2 = new Stack<>();
        stack2.push("A");
        stack2.push("B");
        stack2.push("C");

        assertTrue(deepEquals.areEqual(stack1, stack2)); // Obě zásobníky mají stejné prvky

        stack2.pop();
        assertFalse(deepEquals.areEqual(stack1, stack2)); // Po popnutí jsou zásobníky různé
    }
}

