package com.romiis.complexTests;

import com.romiis.core.EqualLib;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ComplexTests {


    @Test
    public void testPersonEquality() {
        Address address1 = new Address();
        address1.street = "123 Main St";
        address1.city = "Springfield";
        address1.country = "USA";

        Address address2 = new Address();
        address2.street = "123 Main St";
        address2.city = "Springfield";
        address2.country = "USA";

        Person person1 = new Person();
        person1.name = "John Doe";
        person1.age = 30;
        person1.address = address1;

        Person person2 = new Person();
        person2.name = "John Doe";
        person2.age = 30;
        person2.address = address2;

        assertTrue(EqualLib.areEqual(person1, person2), "Persons should be equal");
        // symmetric
        assertTrue(EqualLib.areEqual(person2, person1), "Persons should be equal");
    }

    @Test
    public void testDifferentAges() {
        Person p1 = new Person();
        p1.name = "Alice";
        p1.age = 25;

        Person p2 = new Person();
        p2.name = "Alice";
        p2.age = 26;

        assertFalse(EqualLib.areEqual(p1, p2), "Persons with different ages should not be equal");
        assertFalse(EqualLib.areEqual(p2, p1), "Persons with different ages should not be equal");
    }

    @Test
    public void testNullValues() {
        Person p1 = new Person();
        p1.name = "Bob";
        p1.age = 40;
        p1.address = null;

        Person p2 = new Person();
        p2.name = "Bob";
        p2.age = 40;
        p2.address = null;

        assertTrue(EqualLib.areEqual(p1, p2), "Persons with null addresses should be equal");
        // symmetric
        assertTrue(EqualLib.areEqual(p2, p1), "Persons with null addresses should be equal");
    }

    @Test
    public void testListOrderMatters() {
        List<String> list1 = Arrays.asList("A", "B", "C");
        List<String> list2 = Arrays.asList("C", "B", "A");

        assertFalse(EqualLib.areEqual(list1, list2), "Lists with different orders should not be equal");
        assertFalse(EqualLib.areEqual(list2, list1), "Lists with different orders should not be equal");
    }

    @Test
    public void testSetOrderDoesNotMatter() {
        Set<String> set1 = new HashSet<>(Arrays.asList("A", "B", "C"));
        Set<String> set2 = new HashSet<>(Arrays.asList("C", "B", "A"));

        assertTrue(EqualLib.areEqual(set1, set2), "Sets should be equal regardless of order");
        assertTrue(EqualLib.areEqual(set2, set1), "Sets should be equal regardless of order");
    }

    @Test
    public void testEmptyObjects() {
        Person p1 = new Person();
        Person p2 = new Person();

        assertTrue(EqualLib.areEqual(p1, p2), "Two empty objects should be equal");
        assertTrue(EqualLib.areEqual(p2, p1), "Two empty objects should be equal");
    }


    @Test
    public void testMapsWithSameContent() {
        Map<String, Integer> map1 = new HashMap<>();
        map1.put("A", 1);
        map1.put("B", 2);

        Map<String, Integer> map2 = new HashMap<>();
        map2.put("B", 2);
        map2.put("A", 1);

        assertTrue(EqualLib.areEqual(map1, map2), "Maps with the same content should be equal");
        assertTrue(EqualLib.areEqual(map2, map1), "Maps with the same content should be equal");
    }

    @Test
    public void testPrimitiveEquality() {
        assertTrue(EqualLib.areEqual(10, 10), "Two equal integers should be equal");
        assertFalse(EqualLib.areEqual(10, 20), "Different integers should not be equal");
    }

    @Test
    public void testArrayEquality() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2, 3};

        assertTrue(EqualLib.areEqual(array1, array2), "Arrays with same elements should be equal");
        assertTrue(EqualLib.areEqual(array2, array1), "Arrays with same elements should be equal");
    }

    @Test
    public void testBigUni() {
        University university1 = new University();
        university1.universityName = "University of West Bohemia";
        university1.students = new ArrayList<>();
        university1.professors = new ArrayList<>();
        university1.courses = new HashMap<>();
        university1.campuses = new HashSet<>();


        for (int i = 0; i < 1000; i++) {
            Person student = new Person();
            student.name = "Student " + i;
            student.age = (20 + i) % 40;
            university1.students.add(student);
        }

        for (int i = 0; i < 500; i++) {
            Person professor = new Person();
            professor.name = "Professor " + i;
            professor.age = (30 + i) % 60;
            university1.professors.add(professor);
        }

        for (int i = 0; i < 100; i++) {
            String courseName = "Course " + i;
            List<Person> students = new ArrayList<>();
            for (int j = 0; j < 10; j++) {
                students.add(university1.students.get(i * 10 + j));
            }
            university1.courses.put(courseName, students);
        }


        for (int i = 0; i < 5; i++) {
            university1.campuses.add("Campus " + i);
        }

        University university2 = new University();
        university2.universityName = "University of West Bohemia";
        university2.students = new ArrayList<>();
        university2.professors = new ArrayList<>();
        university2.courses = new HashMap<>();
        university2.campuses = new HashSet<>();


        for (int i = 0; i < 1000; i++) {
            Person student = new Person();
            student.name = "Student " + i;
            student.age = (20 + i) % 40;
            university2.students.add(student);
        }

        for (int i = 0; i < 500; i++) {
            Person professor = new Person();
            professor.name = "Professor " + i;
            professor.age = (30 + i) % 60;
            university2.professors.add(professor);
        }

        for (int i = 0; i < 100; i++) {
            String courseName = "Course " + i;
            List<Person> students = new ArrayList<>();
            for (int j = 0; j < 10; j++) {
                students.add(university2.students.get(i * 10 + j));
            }
            university2.courses.put(courseName, students);
        }

        for (int i = 0; i < 5; i++) {
            university2.campuses.add("Campus " + i);
        }

        assertTrue(EqualLib.areEqual(university1, university2), "Big university should be equal");

    }


    @Test
    public void testBigUniNot() {
        University university1 = new University();
        university1.universityName = "University of West Bohemia";
        university1.students = new ArrayList<>();
        university1.professors = new ArrayList<>();
        university1.courses = new HashMap<>();
        university1.campuses = new HashSet<>();


        for (int i = 0; i < 1200; i++) {
            Person student = new Person();
            student.name = "Student " + i;
            student.age = (20 + i) % 40;
            university1.students.add(student);
        }

        for (int i = 0; i < 500; i++) {
            Person professor = new Person();
            professor.name = "Professor " + i;
            professor.age = (30 + i) % 60;
            university1.professors.add(professor);
        }

        for (int i = 0; i < 100; i++) {
            String courseName = "Course " + i;
            List<Person> students = new ArrayList<>();
            for (int j = 0; j < 10; j++) {
                students.add(university1.students.get(i * 10 + j));
            }
            university1.courses.put(courseName, students);
        }


        for (int i = 0; i < 5; i++) {
            university1.campuses.add("Campus " + i);
        }

        University university2 = new University();
        university2.universityName = "University of West Bohemia";
        university2.students = new ArrayList<>();
        university2.professors = new ArrayList<>();
        university2.courses = new HashMap<>();
        university2.campuses = new HashSet<>();


        for (int i = 0; i < 1200; i++) {
            Person student = new Person();
            student.name = "Student " + (1200 - i);
            student.age = (20 + i) % 40;
            university2.students.add(student);
        }

        for (int i = 0; i < 500; i++) {
            Person professor = new Person();
            professor.name = "Professor " + i;
            professor.age = (30 + i) % 60;
            university2.professors.add(professor);
        }

        for (int i = 0; i < 100; i++) {
            String courseName = "Course " + i;
            List<Person> students = new ArrayList<>();
            for (int j = 0; j < 10; j++) {
                students.add(university2.students.get(i * 10 + j));
            }
            university2.courses.put(courseName, students);
        }

        for (int i = 0; i < 5; i++) {
            university2.campuses.add("Campus " + i);
        }

        assertFalse(EqualLib.areEqual(university1, university2), "Big university should not be equal");
        assertFalse(EqualLib.areEqual(university2, university1), "Big university should not be equal");

    }


    @Test
    public void testPersons() {
        long start = System.currentTimeMillis();
        personLists lists = generateRandomPersons(10_000, true);
        System.out.println("Time to generate random persons: " + (System.currentTimeMillis() - start) + " ms");

        start = System.currentTimeMillis();
        assertTrue(EqualLib.areEqual(lists.list1(), lists.list2()), "Lists should be equal");
        assertTrue(EqualLib.areEqual(lists.list2(), lists.list1()), "Lists should be equal");
        System.out.println("Time to compare lists 2x: " + (System.currentTimeMillis() - start) + " ms");

        start = System.currentTimeMillis();
        personLists lists2 = generateRandomPersons(10_000, false);
        System.out.println("Time to generate random persons: " + (System.currentTimeMillis() - start) + " ms");

        start = System.currentTimeMillis();
        assertFalse(EqualLib.areEqual(lists2.list1(), lists2.list2()), "Lists should not be equal");
        assertFalse(EqualLib.areEqual(lists2.list2(), lists2.list1()), "Lists should not be equal");
        System.out.println("Time to compare lists 2x: " + (System.currentTimeMillis() - start) + " ms");


    }

    @Test
    public void testGraphs() {
        GraphNode root1 = TestGraphGenerator.generateCyclicGraph(10);
        GraphNode root2 = TestGraphGenerator.copyGraph(root1);



        assertTrue(EqualLib.areEqual(root1, root2), "Graphs should be equal");
        assertTrue(EqualLib.areEqual(root2, root1), "Graphs should be equal");

        root1 = TestGraphGenerator.generateCyclicGraph(1000);
        root2 = TestGraphGenerator.copyGraphIterative(root1);

        long start = System.currentTimeMillis();
        assertTrue(EqualLib.areEqual(root1, root2), "Big graphs should be equal");
        assertTrue(EqualLib.areEqual(root2, root1), "Big graphs should be equal");
        System.out.println("Time to compare big graphs 2x: " + (System.currentTimeMillis() - start) + " ms");


        root1 = TestGraphGenerator.generateCyclicGraph(1000);
        root2 = TestGraphGenerator.generateCyclicGraph(1000);

        assertFalse(EqualLib.areEqual(root1, root2), "Graphs should not be equal");
        assertFalse(EqualLib.areEqual(root2, root1), "Graphs should not be equal");


    }


























    private personLists generateRandomPersons(int count, boolean shouldBeEqual) {
        List<Person> list1 = new ArrayList<>();
        List<Person> list2 = new ArrayList<>();
        Random rand = new Random();

        for (int i = 0; i < count; i++) {
            Person person = new Person();
            person.name = generateRandomName();
            person.age = rand.nextInt(60) + 18; // Random age between 18 and 77
            person.gender = (rand.nextBoolean()) ? "Male" : "Female";  // Random gender
            person.email = generateRandomEmail();
            person.address = generateRandomAddress();
            list1.add(person);

            // If shouldBeEqual, make list2 identical to list1
            if (shouldBeEqual) {
                if (rand.nextBoolean()) {
                    list2.add(person);
                } else {
                    list2.add(new Person(person));
                }
            } else {
                // Otherwise, make list2's person slightly different (e.g., change age or name)
                Person newPerson = new Person();
                newPerson.name = generateRandomName();
                newPerson.age = rand.nextInt(60) + 18;  // Random age between 18 and 77
                newPerson.gender = (rand.nextBoolean()) ? "Male" : "Female";  // Random gender
                newPerson.email = generateRandomEmail();
                newPerson.address = generateRandomAddress();
                list2.add(newPerson);
            }
        }

        // Add extra person if the lists are not equal
        if (!shouldBeEqual) {
            Person person = new Person();
            person.name = generateRandomName();
            person.age = rand.nextInt(60) + 18;
            person.gender = (rand.nextBoolean()) ? "Male" : "Female";
            person.email = generateRandomEmail();
            person.address = generateRandomAddress();
            list2.add(person);
        }

        return new personLists(list1, list2);
    }

    private String generateRandomName() {
        String[] firstNames = {"Alice", "Bob", "Charlie", "Diana", "Eve", "Frank"};
        String[] lastNames = {"Smith", "Johnson", "Brown", "Williams", "Jones", "Miller"};
        Random rand = new Random();
        String firstName = firstNames[rand.nextInt(firstNames.length)];
        String lastName = lastNames[rand.nextInt(lastNames.length)];
        return firstName + " " + lastName;
    }

    private String generateRandomEmail() {
        String[] domains = {"example.com", "test.com", "demo.org", "mail.net"};
        Random rand = new Random();
        return "user" + rand.nextInt(1000) + "@" + domains[rand.nextInt(domains.length)];
    }

    private Address generateRandomAddress() {
        Random rand = new Random();

        // Randomly select a street name
        String[] streetNames = {"Main St", "Maple Ave", "Oak Dr", "Pine Blvd", "Elm Rd", "Cedar Lane"};
        String street = rand.nextInt(9999) + " " + streetNames[rand.nextInt(streetNames.length)];

        // Randomly select a city
        String[] cities = {"Springfield", "Riverside", "Madison", "Greenville", "Bristol"};
        String city = cities[rand.nextInt(cities.length)];

        // Randomly select a state
        String[] states = {"CA", "TX", "NY", "FL", "IL"};
        String state = states[rand.nextInt(states.length)];

        // Randomly generate a postal code
        String postalCode = String.format("%05d", rand.nextInt(100000)); // Generate a 5-digit postal code

        // Create and return the address
        Address address = new Address();
        address.street = street;
        address.city = city;
        address.country = state;
        address.postalCode = postalCode;
        return address;
    }



}
