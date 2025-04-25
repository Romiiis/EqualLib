package com.romiiis.inheritanceTests;

import com.romiiis.core.EqualLib;
import com.romiiis.core.EqualLibConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class inheritanceTests {

    private EqualLibConfig config;

    @BeforeEach
    void setUp() {
        config = new EqualLibConfig();
        config.setCompareCollectionsByElements(true);
    }

    @Test
    void testEqualObjects() {
        String str1 = "Hello";
        String str2 = "Hello";
        assertTrue(EqualLib.areEqual(str1, str2, config), "Identické řetězce by měly být shodné");
        assertTrue(EqualLib.areEqual(str2, str1, config), "Identické řetězce by měly být shodné");
    }

    @Test
    void testNotEqualObjects() {
        String str1 = "Hello";
        String str2 = "World";
        assertFalse(EqualLib.areEqual(str1, str2, config), "Rozdílné řetězce by neměly být shodné");
        assertFalse(EqualLib.areEqual(str2, str1, config), "Rozdílné řetězce by neměly být shodné");
    }

    @Test
    void testEqualityByInheritance() {
        Parent parent = new Parent();
        Child child = new Child();

        config.setCompareInheritedFields(true); // Povolit porovnání jen podle zděděných atributů

        assertTrue(EqualLib.areEqual(parent, child, config), "Dědičnost by měla stačit pro rovnost");
        assertTrue(EqualLib.areEqual(child, parent, config), "Dědičnost by měla stačit pro rovnost");
    }

    @Test
    void testNotEqualByInheritance() {
        Parent parent = new Parent();
        Child child = new Child();

        config.setCompareInheritedFields(false); // Musí se porovnávat všechny atributy

        assertFalse(EqualLib.areEqual(parent, child, config), "Dědičnost nestačí, měly by se porovnávat i potomkovské atributy");
        assertFalse(EqualLib.areEqual(child, parent, config), "Dědičnost nestačí, měly by se porovnávat i potomkovské atributy");
    }

    @Test
    void testTwoChildrenSameParent() {
        Child child1 = new Child();
        AnotherChild child2 = new AnotherChild();

        config.setCompareInheritedFields(true);

        assertTrue(EqualLib.areEqual(child1, child2, config), "Dva potomci stejného rodiče by měli být považováni za stejné při zapnuté dědičnosti");
        assertTrue(EqualLib.areEqual(child2, child1, config), "Dva potomci stejného rodiče by měli být považováni za stejné při zapnuté dědičnosti");
    }

    @Test
    void testTwoParentsDifferentValues() {
        Parent parent1 = new Parent();
        Parent parent2 = new Parent();
        parent2.value = 30; // Jiná hodnota

        config.setCompareInheritedFields(true);

        assertFalse(EqualLib.areEqual(parent1, parent2, config), "Rodiče s různými hodnotami by neměli být považováni za stejné");
        assertFalse(EqualLib.areEqual(parent2, parent1, config), "Rodiče s různými hodnotami by neměli být považováni za stejné");
    }

    @Test
    void testParentWithDifferentClass() {
        Parent parent = new Parent();
        UnrelatedClass unrelated = new UnrelatedClass();

        config.setCompareInheritedFields(true);

        assertFalse(EqualLib.areEqual(parent, unrelated, config), "Rodič a zcela jiná třída by neměly být považovány za stejné");
        assertFalse(EqualLib.areEqual(unrelated, parent, config), "Rodič a zcela jiná třída by neměly být považovány za stejné");
    }

    @Test
    void testMapEquality() {
        Map<String, Integer> map1 = new HashMap<>();
        Map<String, Integer> map2 = new HashMap<>();

        map1.put("a", 1);
        map1.put("b", 2);

        map2.put("a", 1);
        map2.put("b", 2);

        assertTrue(EqualLib.areEqual(map1, map2, config), "Mapy se stejnými klíči a hodnotami by měly být shodné");
        assertTrue(EqualLib.areEqual(map2, map1, config), "Mapy se stejnými klíči a hodnotami by měly být shodné");
    }

    @Test
    void testMapNotEqual() {
        Map<String, Integer> map1 = new HashMap<>();
        Map<String, Integer> map2 = new HashMap<>();

        map1.put("a", 1);
        map1.put("b", 2);

        map2.put("a", 1);
        map2.put("b", 3); // Rozdílná hodnota

        assertFalse(EqualLib.areEqual(map1, map2, config), "Mapy s rozdílnými hodnotami by neměly být shodné");
        assertFalse(EqualLib.areEqual(map2, map1, config), "Mapy s rozdílnými hodnotami by neměly být shodné");
    }

    @Test
    void testNullEquality() {
        assertTrue(EqualLib.areEqual(null, null, config), "Dvě null hodnoty by měly být shodné");
        assertFalse(EqualLib.areEqual(null, "test", config), "Null a neprázdný objekt by neměly být shodné");
        assertFalse(EqualLib.areEqual("test", null, config), "Null a neprázdný objekt by neměly být shodné");
    }

    @Test
    @DisplayName("Test inheritance with different classes")
    void carTests() {
        Car sedan = new Sedan("Toyota", "Corolla", 2020, 5);
        Car truck = new Truck("Toyota", "Corolla", 2020, 1000);

        assertFalse(EqualLib.areEqual(sedan, truck, config), "Different classes should not be equal");
        assertFalse(EqualLib.areEqual(truck, sedan, config), "Different classes should not be equal");

        config.setCompareInheritedFields(true);

        assertTrue(EqualLib.areEqual(sedan, truck, config), "Inherited fields should be equal");
        assertTrue(EqualLib.areEqual(truck, sedan, config), "Inherited fields should be equal");
    }

    @Test
    @DisplayName("Edge case")
    void edgeCase() {
        // Parent is superclass of Child
        Parent parent = new Parent();

        // Child is subclass of Parent
        Child child = new Child();

        config.setCompareInheritedFields(true);

        assertTrue(EqualLib.areEqual(parent, child, config), "Inherited fields should be equal");

        assertTrue(EqualLib.areEqual(child, parent, config), "Inherited fields should be equal");
    }

    static class Parent {
        int value = 10;
    }

    static class Child extends Parent {
        int extraValue = 20;
    }

    static class AnotherChild extends Parent {
        int anotherValue = 30;
    }

    static class UnrelatedClass {
        int differentValue = 50;
    }

    static abstract class Car {
        String brand;
        String model;
        int year;

        public Car(String brand, String model, int year) {
            this.brand = brand;
            this.model = model;
            this.year = year;
        }
    }

    static class Sedan extends Car {
        int seats;

        public Sedan(String brand, String model, int year, int seats) {
            super(brand, model, year);
            this.seats = seats;
        }

    }

    static class Truck extends Car {
        int loadCapacity;

        public Truck(String brand, String model, int year, int loadCapacity) {
            super(brand, model, year);
            this.loadCapacity = loadCapacity;
        }


    }



}
