package com.romiiis.maxDepthTests;

import com.romiiis.core.EqualLib;
import com.romiiis.core.EqualLibConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MaxDepthTests {
    @Test
    @DisplayName("Depth -1")
    void unlimited() {
        Person person1 = new Person("John", 30, new Address("Main St", new City("New York", 1000000, Country.USA)));
        Person person2 = new Person("John", 30, new Address("Main St", new City("New York", 1000000, Country.USA)));

        assertTrue(EqualLib.areEqual(person1, person2));
        // Symetric
        assertTrue(EqualLib.areEqual(person2, person1));

        person2 = new Person("John", 30, new Address("Main St", new City("New York", 1000000, Country.UK)));
        assertFalse(EqualLib.areEqual(person1, person2));
        assertFalse(EqualLib.areEqual(person2, person1));

    }

    @Test
    @DisplayName("Depth 1")
    void depth1() {
        Person person1 = new Person("John", 30, new Address("Main St", new City("New York", 1000000, Country.USA)));
        Person person2 = new Person("John", 30, new Address("Second", new City("New York", 1000000, Country.USA)));

        // The depth is set to 1, so the comparison will only check the first level of the object. The address field will not be compared
        EqualLibConfig config = new EqualLibConfig();
        config.setMaxComparisonDepth(1, false);

        assertTrue(EqualLib.areEqual(person1, person2, config));
        assertTrue(EqualLib.areEqual(person2, person1, config));

        config.setMaxComparisonDepth(1, true);
        assertFalse(EqualLib.areEqual(person1, person2, config));
        assertFalse(EqualLib.areEqual(person2, person1, config));


    }

    @Test
    @DisplayName("Depth 2")
    void depth2() {
        Person person1 = new Person("John", 30, new Address("Main St", new City("New York", 1000000, Country.USA)));
        Person person2 = new Person("John", 30, new Address("Second", new City("New York", 1000000, Country.USA)));

        // The depth is set to 2, so the comparison will check the first and second level of the object. The address field will be compared
        EqualLibConfig config = new EqualLibConfig();
        config.setMaxComparisonDepth(2, true);

        assertFalse(EqualLib.areEqual(person1, person2, config));
        assertFalse(EqualLib.areEqual(person2, person1, config));

        person2 = new Person("John", 30, new Address("Main St", new City("Florida", 1000000, Country.USA)));

        config.setMaxComparisonDepth(2, true);
        assertFalse(EqualLib.areEqual(person1, person2, config));
        assertFalse(EqualLib.areEqual(person2, person1, config));


        config.setMaxComparisonDepth(2, false);
        assertTrue(EqualLib.areEqual(person1, person2, config));
        assertTrue(EqualLib.areEqual(person2, person1, config));
    }

    @Test
    @DisplayName("Depth arraylist")
    void depthArrayList() {
        Address florida = new Address("Main St", new City("Florida", 1000000, Country.USA));
        Address newYork = new Address("Main St", new City("New York", 1000000, Country.USA));

        List<Address> addresses1 = List.of(florida, newYork);
        List<Address> addresses2 = List.of(florida, newYork);

        // The depth is set to 1, so the comparison will only check the first level of the object. The address field will not be compared
        EqualLibConfig config = new EqualLibConfig();
        config.setMaxComparisonDepth(1, false);

        // The depth is set to 1, so the comparison will only check the first level of the object. The address field will not be compared only the object of the array list
        assertTrue(EqualLib.areEqual(addresses1, addresses2, config));
        assertTrue(EqualLib.areEqual(addresses2, addresses1, config));

        config.setMaxComparisonDepth(1, true);
        addresses2 = List.of(florida, new Address("Main St", new City("New York", 1000000, Country.UK)));

        assertFalse(EqualLib.areEqual(addresses1, addresses2, config));
        assertFalse(EqualLib.areEqual(addresses2, addresses1, config));


        config.setMaxComparisonDepth(1, false);
        assertTrue(EqualLib.areEqual(addresses1, addresses2, config));
        assertTrue(EqualLib.areEqual(addresses2, addresses1, config));


    }

    @Test
    @DisplayName("Depth 0")
    void depth0() {

        // Override the equals method in the Person class

        Person person1 = new Person("John", 30, new Address("Main St", new City("New York", 1000000, Country.USA)));
        Person person2 = new Person("John", 30, new Address("Main St", new City("New York", 1000000, Country.USA)));

        // The depth is set to 0, so the comparison will only check the first level of the object. The address field will not be compared
        EqualLibConfig config = new EqualLibConfig();
        config.setMaxComparisonDepth(0, false);

        assertTrue(EqualLib.areEqual(person1, person2, config));
        assertTrue(EqualLib.areEqual(person2, person1, config));

        config.setMaxComparisonDepth(0, true);
        assertFalse(EqualLib.areEqual(person1, person2, config));
        assertFalse(EqualLib.areEqual(person2, person1, config));
    }




}
