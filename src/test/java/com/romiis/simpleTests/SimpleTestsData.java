package com.romiis.simpleTests;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

class PrimitiveObject {
    private int number;
    private String name;

    private float floatNumber;
    private double doubleNumber;

    private boolean isTrue;

    private char character;

    public PrimitiveObject(int number, String name, float floatNumber, double doubleNumber, boolean isTrue, char character) {
        this.number = number;
        this.name = name;
        this.floatNumber = floatNumber;
        this.doubleNumber = doubleNumber;
        this.isTrue = isTrue;
        this.character = character;
    }
}

class ObjectSet {

    public Set<ObjectSet> set;
    public int number;
}

class ObjectMap {

    public Map<?, ObjectMap> next;
    public int number;
}

class ObjectList {

    public List<ObjectList> next;
    public int number;
}

class ObjectC {
    public String name;

    public ObjectC next;

    public ObjectC(String name, ObjectC next) {
        this.name = name;
        this.next = next;
    }

    public void setNext(ObjectC next) {
        this.next = next;
    }

}

class ObjectB {
    public String name;

    private ObjectA objectA;

    public ObjectB(String name, ObjectA objectA) {
        this.name = name;
        this.objectA = objectA;
    }

}

class ObjectA {
    public int weight;
    public String name;

    public ObjectA(int weight, String name) {
        this.weight = weight;
        this.name = name;
    }
}


class Address {
    String city;
    public String street;

    public Address(String city, String street) {
        this.city = city;
        this.street = street;
    }
}


class Node {
    int value;
    Node next;

    public void setNext(Node next) {
        this.next = next;
    }

    public Node(int value) {
        this.value = value;
    }

}

class Person {
    String name;
    int age;
    public Address address;
    List<Person> friends;

    public Person(String name, int age, Address address) {
        this.name = name;
        this.age = age;
        this.address = address;
        this.friends = new ArrayList<>();
    }

    public void setAge(int age) {
        this.age = age;
    }
    public void addFriend(Person friend) {
        this.friends.add(friend);
    }

}

enum Direction {
    NORTH, SOUTH, EAST, WEST
}


