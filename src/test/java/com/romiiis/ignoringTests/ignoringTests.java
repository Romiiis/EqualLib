package com.romiiis.ignoringTests;

import com.romiiis.core.EqualLib;
import com.romiiis.core.EqualLibConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ignoringTests {


    @DisplayName("Nothing ignored")
    @Test
    void test1() {
        Person person1 = new Person("John", 30, new Address("Main St", new City("New York", 1000000, Country.USA)));
        Person person2 = new Person("John", 30, new Address("Main St", new City("New York", 1000000, Country.USA)));

        assertTrue(EqualLib.areEqual(person1, person2));
        assertTrue(EqualLib.areEqual(person2, person1));

        person2 = new Person("John", 30, new Address("Main St", new City("New York", 1000000, Country.UK)));
        assertFalse(EqualLib.areEqual(person1, person2));
        assertFalse(EqualLib.areEqual(person2, person1));
    }

    @DisplayName("Ignore City")
    @Test
    void test2() {
        Person person1 = new Person("John", 30, new Address("Main St", new City("Florida", 1000000, Country.USA)));
        Person person2 = new Person("John", 30, new Address("Main St", new City("New York", 1000000, Country.USA)));

        assertFalse(EqualLib.areEqual(person1, person2));
        assertFalse(EqualLib.areEqual(person2, person1));


        EqualLibConfig config = new EqualLibConfig();
        config.setIgnoredFieldPaths("com.romiiis.ignoringTests.Address.city");

        assertTrue(EqualLib.areEqual(person1, person2, config));
        assertTrue(EqualLib.areEqual(person2, person1, config));
    }






}
