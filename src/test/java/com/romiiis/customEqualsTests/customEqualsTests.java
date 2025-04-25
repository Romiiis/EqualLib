package com.romiiis.customEqualsTests;

import com.romiiis.core.EqualLib;
import com.romiiis.core.EqualLibConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class customEqualsTests {

    @Test
    @DisplayName("Custom equals")
    void test1() {


        Person person1 = new Person("John", 30, new Address("Main St", new City("New York", 1000000, Country.USA)));
        Person person2 = new Person("John", 30, new Address("Main St", new City("New York", 1000000, Country.USA)));

        assertTrue(EqualLib.areEqual(person1, person2));
        assertTrue(EqualLib.areEqual(person2, person1));

        person2 = new Person("John", 105, new Address("Main St", new City("New York", 1000000, Country.UK)));
        assertFalse(EqualLib.areEqual(person1, person2));
        assertFalse(EqualLib.areEqual(person2, person1));

        EqualLibConfig config = new EqualLibConfig();
        config.setCustomEqualsClasses("com.romiiis.customEqualsTests");
        assertTrue(EqualLib.areEqual(person1, person2, config));
        assertTrue(EqualLib.areEqual(person2, person1, config));

        config.setCustomEqualsClasses("com.romiiis");
        assertTrue(EqualLib.areEqual(person1, person2, config));
        assertTrue(EqualLib.areEqual(person2, person1, config));

        config.setCustomEqualsClasses("com.romiiis.customEqualsTests.Person");
        assertTrue(EqualLib.areEqual(person1, person2, config));
        assertTrue(EqualLib.areEqual(person2, person1, config));


    }
}
