package com.romiis.maxDepthTests;

class Person {
    private String name;
    private int age;
    private Address address;

    public Person(String name, int age, Address address) {
        this.name = name;
        this.age = age;
        this.address = address;
    }

}

class Address {
    private String street;
    private String city;

    public Address(String street, String city) {
        this.street = street;
        this.city = city;
    }

}

class City {
    private String name;
    private int population;
    private Country country;

    public City(String name, int population, Country country) {
        this.name = name;
        this.population = population;
        this.country = country;
    }
}


enum Country {
    USA, UK, FRANCE
}






