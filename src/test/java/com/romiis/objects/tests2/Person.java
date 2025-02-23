package com.romiis.objects.tests2;

import java.util.ArrayList;
import java.util.List;

public class Person {
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

    public void addFriend(Person friend) {
        this.friends.add(friend);
    }

    public void setAge(int age) {
        this.age = age;
    }
}
