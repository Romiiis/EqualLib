package com.romiis.complexTests;

import java.util.*;

class Address {
    String street;
    String city;
    String country;
    String postalCode;

    public Address(Address address) {
        this.street = address.street;
        this.city = address.city;
        this.country = address.country;
        this.postalCode = address.postalCode;
    }

    public Address() {}
}

class Person {
    String name;
    int age;
    String gender;
    String email;
    Address address;

    public Person(Person person) {
        this.name = person.name;
        this.age = person.age;
        this.gender = person.gender;
        this.email = person.email;
        Random random = new Random();
        if (random.nextBoolean()) {
            this.address = new Address(person.address);
        } else {
            this.address = person.address;
        }
    }

    public Person() {}
}


class University {
    String universityName;
    List<Person> students;
    List<Person> professors;
    Map<String, List<Person>> courses;
    Set<String> campuses;
}

record personLists(List<Person> list1, List<Person> list2) {
}



