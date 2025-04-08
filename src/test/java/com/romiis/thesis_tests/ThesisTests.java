package com.romiis.thesis_tests;

import com.romiis.DeepCopyUtil;
import com.romiis.core.EqualLib;
import com.romiis.core.EqualLibConfig;
import com.romiis.thesis_tests.objects.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ThesisTests {

    @DisplayName("Test 1,2 - Simple objects")
    @Test
    void testSimpleObjects() {
        Simple s1 = new Simple("Alice", 30, 1.7f, 70.0, true, new byte[]{1, 2, 3});
        Simple s2 = new Simple("Alice", 30, 1.7f, 70.0, true, new byte[]{1, 2, 3});

        EqualLibConfig config = new EqualLibConfig();

        assertTrue(EqualLib.areEqual(s1, s2), "Objects should be equal using the default config");
        assertTrue(EqualLib.areEqual(s1, s2, config), "Objects should be equal because the config is default");

        s2 = new Simple("Alice", 30, 1.7f, 70.0, true, new byte[]{1, 2, 4});
        assertFalse(EqualLib.areEqual(s1, s2), "Objects should not be equal because of the data field");
        assertFalse(EqualLib.areEqual(s1, s2, config), "Objects should not be equal because of the data field");
    }


    @Test
    @DisplayName("Test 3: Inheritance without flag (default) – different subclass attributes")
    void testInheritanceDefault() {
        // Child1 and Child2 are defined in your objects package.
        Child1 obj1 = new Child1(10, 20);
        Child2 obj2 = new Child2(10, 30);
        // Default config (compareInheritedFields = false) compares all fields.
        assertFalse(EqualLib.areEqual(obj1, obj2),
                "Objects should not be equal if subclass-specific attributes differ");
    }

    @Test
    @DisplayName("Test 4: Inheritance with compareInheritedFields = true – compare only parent fields")
    void testInheritanceCompareInheritedTrue() {
        Child1 obj1 = new Child1(10, 20);
        Child2 obj2 = new Child2(10, 30);
        EqualLibConfig config = new EqualLibConfig().setCompareInheritedFields(true);
        // Only parent's attribute (a) is compared.
        assertTrue(EqualLib.areEqual(obj1, obj2, config),
                "Objects should be equal if only parent fields are compared");
    }

    @Test
    @DisplayName("Test 5: Inheritance with compareInheritedFields = false – compare all fields")
    void testInheritanceCompareInheritedFalse() {
        Child1 obj1 = new Child1(10, 20);
        Child2 obj2 = new Child2(10, 30);
        EqualLibConfig config = new EqualLibConfig().setCompareInheritedFields(false);
        assertFalse(EqualLib.areEqual(obj1, obj2, config),
                "Objects should not be equal if subclass-specific fields are compared");
    }


    @Test
    @DisplayName("Test 6: Unlimited depth comparison (maxComparisonDepth = -1)")
    void testUnlimitedDepth() {
        // Nested is defined in your objects package.
        Nested level3 = new Nested("level3", null);
        Nested level2 = new Nested("level2", level3);
        Nested level1 = new Nested("level1", level2);

        Nested level3Copy = new Nested("level3", null);
        Nested level2Copy = new Nested("level2", level3Copy);
        Nested level1Copy = new Nested("level1", level2Copy);

        EqualLibConfig config = new EqualLibConfig().setMaxComparisonDepth(-1, false); // -1 means unlimited
        assertTrue(EqualLib.areEqual(level1, level1Copy, config),
                "Unlimited depth should compare all levels");
    }

    @Test
    @DisplayName("Test 7: Limited depth comparison (maxComparisonDepth = 2) – differences deeper than level 2 are ignored")
    void testLimitedDepth() {
        // Difference appears at level 3.
        Nested level3a = new Nested("diff", null);
        Nested level2a = new Nested("level2", level3a);
        Nested level1a = new Nested("level1", level2a);

        Nested level3b = new Nested("same", null);
        Nested level2b = new Nested("level2", level3b);
        Nested level1b = new Nested("level1", level2b);

        EqualLibConfig config = new EqualLibConfig().setMaxComparisonDepth(2, false);
        // Differences below level 2 are ignored.
        assertTrue(EqualLib.areEqual(level1a, level1b, config),
                "Differences below the specified depth should be ignored");
    }

    @Test
    @DisplayName("Test 8: Switch to standard equals after reaching max depth")
    void testStandardEqualsAfterDepth() {
        // CustomNested is defined in your objects package.
        CustomNested level3a = new CustomNested("A", null);
        CustomNested level2a = new CustomNested("level2", level3a);
        CustomNested level1a = new CustomNested("level1", level2a);

        CustomNested level3b = new CustomNested("B", null); // different value
        CustomNested level2b = new CustomNested("level2", level3b);
        CustomNested level1b = new CustomNested("level1", level2b);

        EqualLibConfig config = new EqualLibConfig().setMaxComparisonDepth(2, true);
        // At depth 2, standard equals() is used, which should detect the difference at level 3.
        assertFalse(EqualLib.areEqual(level1a, level1b, config),
                "Standard equals after reaching depth should detect differences");
    }


    @Test
    @DisplayName("Test 9: Ignoring one field (timestamp)")
    void testIgnoreOneField() {
        // WithTimestamp is defined in your objects package.
        WithTimestamp obj1 = new WithTimestamp(1, 1000L);
        WithTimestamp obj2 = new WithTimestamp(1, 2000L);
        EqualLibConfig config = new EqualLibConfig()
                .setIgnoredFieldPaths(WithTimestamp.class.getName() + ".timestamp");
        assertTrue(EqualLib.areEqual(obj1, obj2, config),
                "Objects should be equal when the timestamp field is ignored");
    }

    @Test
    @DisplayName("Test 10: Ignoring multiple fields (timestamp, metadata)")
    void testIgnoreMultipleFields() {
        // WithMultiple is defined in your objects package.
        WithMultiple obj1 = new WithMultiple(1, 1000L, "data1");
        WithMultiple obj2 = new WithMultiple(1, 2000L, "data2");
        EqualLibConfig config = new EqualLibConfig()
                .setIgnoredFieldPaths(
                        WithMultiple.class.getName() + ".timestamp",
                        WithMultiple.class.getName() + ".metadata");
        assertTrue(EqualLib.areEqual(obj1, obj2, config),
                "Objects should be equal when the selected fields are ignored");
    }

    @Test
    @DisplayName("Test 11: Ignoring a non-existent field")
    void testIgnoreNonExistentField() {
        // WithSimpleField is defined in your objects package.
        WithSimpleField obj1 = new WithSimpleField(10);
        WithSimpleField obj2 = new WithSimpleField(10);
        EqualLibConfig config = new EqualLibConfig()
                .setIgnoredFieldPaths(WithSimpleField.class.getName() + ".nonexistent");
        assertTrue(EqualLib.areEqual(obj1, obj2, config),
                "Comparison should work correctly even if a non-existent field is ignored");
    }



    @Test
    @DisplayName("Test 12: Using custom equals for a specific class")
    void testCustomEqualsForClass() {
        // SpecialClass is defined in your objects package.
        SpecialClass obj1 = new SpecialClass(2);
        SpecialClass obj2 = new SpecialClass(4);
        EqualLibConfig config = new EqualLibConfig()
                .setCustomEqualsClasses(SpecialClass.class.getName());
        assertTrue(EqualLib.areEqual(obj1, obj2, config),
                "Objects should be equal according to the custom equals (parity)");

        SpecialClass obj3 = new SpecialClass(2);
        SpecialClass obj4 = new SpecialClass(3);
        assertFalse(EqualLib.areEqual(obj3, obj4, config),
                "Objects should not be equal according to the custom equals (parity)");
    }

    @Test
    @DisplayName("Test 13: Using custom equals for an entire package")
    void testCustomEqualsForPackage() {
        // SpecialPackageClass is defined in your objects package.
        SpecialPackageClass obj1 = new SpecialPackageClass("Test");
        SpecialPackageClass obj2 = new SpecialPackageClass("test");
        EqualLibConfig config = new EqualLibConfig()
                .setCustomEqualsClasses("com.example.special", SpecialPackageClass.class.getName());
        assertTrue(EqualLib.areEqual(obj1, obj2, config),
                "Objects should be equal according to the custom equals defined for the package");
    }



    @Test
    @DisplayName("Test 14: Compare collections as whole")
    void testCompareCollectionsAsWhole() {
        // WithCollection is defined in your objects package.
        WithCollection obj1 = new WithCollection(new ArrayList<>(Arrays.asList(1, 2, 3)));
        WithCollection obj2 = new WithCollection(new ArrayList<>(Arrays.asList(1, 2, 3)));

        obj2.numbers.add(4); // Adding an element to obj2's collection
        obj2.numbers.remove(3); // Removing an element from obj2's collection

        EqualLibConfig config = new EqualLibConfig()
                .setCompareCollectionsByElements(true);

        config.setCompareCollectionsByElements(false);
        assertFalse(EqualLib.areEqual(obj1, obj2, config),
                "Collections compared as whole should not be equal after modification");

    }

    @Test
    @DisplayName("Test 15: Compare collections by elements")
    void testCompareCollectionsItemByItem() {
        // WithCollection is defined in your objects package.
        WithCollection obj1 = new WithCollection(new ArrayList<>(Arrays.asList(1, 2, 3)));
        WithCollection obj2 = new WithCollection(new ArrayList<>(Arrays.asList(1, 2, 3)));

        obj2.numbers.add(4); // Adding an element to obj2's collection
        obj2.numbers.remove(3); // Removing an element from obj2's collection

        EqualLibConfig config = new EqualLibConfig()
                .setCompareCollectionsByElements(true);
        assertTrue(EqualLib.areEqual(obj1, obj2, config),
                "Collections compared item by item should be equal before modification");

    }

    @Test
    @DisplayName("Test 16: Compare empty collections")
    void testEmptyCollections() {
        WithCollection obj1 = new WithCollection(new ArrayList<>());
        WithCollection obj2 = new WithCollection(new ArrayList<>());
        assertTrue(EqualLib.areEqual(obj1, obj2),
                "Empty collections should be equal");
    }



    @Test
    @DisplayName("Test 17: Enable debug mode")
    void testDebugModeEnabled() {
        // Simple is defined in your objects package.
        Simple obj1 = new Simple("Debug", 10, 1.0f, 50.0, true, new byte[]{1});
        Simple obj2 = new Simple("Debug", 10, 1.0f, 50.0, true, new byte[]{1});
        EqualLibConfig config = new EqualLibConfig().setDebugEnabled(true);
        assertTrue(EqualLib.areEqual(obj1, obj2, config),
                "Enabling debug mode should not affect the comparison result");
    }

    @Test
    @DisplayName("Test 18: Disable debug mode")
    void testDebugModeDisabled() {
        Simple obj1 = new Simple("NoDebug", 20, 1.5f, 60.0, false, new byte[]{2});
        Simple obj2 = new Simple("NoDebug", 20, 1.5f, 60.0, false, new byte[]{2});
        EqualLibConfig config = new EqualLibConfig().setDebugEnabled(false);
        assertTrue(EqualLib.areEqual(obj1, obj2, config),
                "Disabling debug mode should not affect the comparison result");
    }


    @Test
    @DisplayName("Test 19: Compare objects of different classes")
    void testDifferentClasses() {
        // ClassA and ClassB are defined in your objects package.
        ClassA a = new ClassA(10);
        ClassB b = new ClassB(10);
        assertFalse(EqualLib.areEqual(a, b),
                "Objects of different classes should not be equal");
    }

    @Test
    @DisplayName("Test 20: Compare two null values")
    void testTwoNulls() {
        assertTrue(EqualLib.areEqual(null, null),
                "Two nulls should be considered equal");
    }

    @Test
    @DisplayName("Test 21: Compare null and non-null values")
    void testNullAndNonNull() {
        Simple obj = new Simple("Test", 5, 1.2f, 45.0, true, new byte[]{5});
        assertFalse(EqualLib.areEqual(obj, null),
                "A null and a non-null value should not be equal");
    }


    @Test
    @DisplayName("Test 22: Compare objects with cyclic references")
    void testCyclicReferences() {
        // Cyclic is defined in your objects package.
        Cyclic a1 = new Cyclic(1);
        Cyclic a2 = new Cyclic(1);
        a1.setPartner(a2);
        a2.setPartner(a1);

        Cyclic b1 = new Cyclic(1);
        Cyclic b2 = new Cyclic(1);
        b1.setPartner(b2);
        b2.setPartner(b1);

        assertTrue(EqualLib.areEqual(a1, b1),
                "Cyclic structures with identical references should be equal");
    }


    @Test
    @DisplayName("Test 23: Compare complex nested structures")
    void testComplexNestedStructures() {
        // ComplexStructure is defined in your objects package.
        Nested nested1 = new Nested("root", new Nested("child", null));
        List<Simple> list1 = Arrays.asList(
                new Simple("Alice", 30, 1.7f, 70.0, true, new byte[]{1, 2}),
                new Simple("Bob", 25, 1.8f, 80.0, false, new byte[]{3, 4})
        );
        ComplexStructure cs1 = new ComplexStructure(nested1, list1);

        Nested nested2 = new Nested("root", new Nested("child", null));
        List<Simple> list2 = Arrays.asList(
                new Simple("Alice", 30, 1.7f, 70.0, true, new byte[]{1, 2}),
                new Simple("Bob", 25, 1.8f, 80.0, false, new byte[]{3, 4})
        );
        ComplexStructure cs2 = new ComplexStructure(nested2, list2);

        assertTrue(EqualLib.areEqual(cs1, cs2),
                "Complex nested structures should be equal");
    }


    @Test
    @DisplayName("Test 24: Combination of inheritance, depth comparison, and custom equals")
    void testCombinationInheritanceDepthCustomEquals() {
        Child1 child = new Child1(10, 20);
        SpecialClass special = new SpecialClass(2);
        // Create a combined object using an inner class.
        class Combined {
            public Parent parent;
            public SpecialClass special;
            public Combined(Parent parent, SpecialClass special) {
                this.parent = parent;
                this.special = special;
            }
        }
        Combined obj1 = new Combined(child, special);
        Combined obj2 = new Combined(new Child1(10, 30), new SpecialClass(4)); // special.equals() treats 2 and 4 as equal
        EqualLibConfig config = new EqualLibConfig()
                .setCompareInheritedFields(true)
                .setMaxComparisonDepth(3, true)
                .setCustomEqualsClasses(SpecialClass.class.getName());
        assertTrue(EqualLib.areEqual(obj1, obj2, config),
                "Combined objects should be equal according to the configuration");
    }


    @Test
    @DisplayName("Test 25: Combination of ignoring fields and comparing collections as wholes")
    void testCombinationIgnoredAndCollectionsWhole() {
        // WithCollectionAndIgnored is defined in your objects package.
        WithCollectionAndIgnored obj1 = new WithCollectionAndIgnored(1, "Info1", Arrays.asList("a", "b"));
        WithCollectionAndIgnored obj2 = new WithCollectionAndIgnored(1, "DifferentInfo", Arrays.asList("a", "b"));
        EqualLibConfig config = new EqualLibConfig()
                .setIgnoredFieldPaths(WithCollectionAndIgnored.class.getName() + ".info")
                .setCompareCollectionsByElements(true);
        assertTrue(EqualLib.areEqual(obj1, obj2, config),
                "Objects should be equal if the differing field is ignored and collections are compared as a whole");
    }

    @Test
    @DisplayName("Test 26: Combination of limited depth and item-by-item collection comparison")
    void testCombinationLimitedDepthCollections() {
        // WithNestedCollection is defined in your objects package.
        WithNestedCollection obj1 = new WithNestedCollection(Arrays.asList(
                new Simple("A", 10, 1.0f, 50.0, true, new byte[]{1}),
                new Simple("B", 20, 1.5f, 60.0, false, new byte[]{2})
        ));
        WithNestedCollection obj2 = new WithNestedCollection(Arrays.asList(
                new Simple("A", 10, 1.0f, 50.0, true, new byte[]{1}),
                new Simple("B", 20, 1.5f, 60.0, false, new byte[]{3}) // difference at deeper level
        ));
        EqualLibConfig config = new EqualLibConfig()
                .setMaxComparisonDepth(1, false)
                .setCompareCollectionsByElements(false);
        // Differences at levels deeper than 1 should be ignored.
        assertTrue(EqualLib.areEqual(obj1, obj2, config),
                "Differences in deeper collection levels should be ignored");
    }


    @Test
    @DisplayName("Test 27: Combination of inheritance and ignoring fields")
    void testCombinationInheritanceAndIgnoredFields() {
        // ChildWithField is defined in your objects package.
        ChildWithField obj1 = new ChildWithField(10, 20);
        ChildWithField obj2 = new ChildWithField(10, 30);
        EqualLibConfig config = new EqualLibConfig()
                .setIgnoredFieldPaths(ChildWithField.class.getName() + ".b");
        assertTrue(EqualLib.areEqual(obj1, obj2, config),
                "Objects should be equal if the differing field is ignored");
    }



    @Test
    @DisplayName("Test 28: Complex combination of inheritance, depth, custom equals, ignored fields, and collection comparison")
    void testComplexCombination() {
        // ComplexObject is defined in your objects package.
        Child1 child1 = new Child1(10, 20);
        Child1 child2 = new Child1(10, 25);
        List<Simple> simples1 = Arrays.asList(
                new Simple("A", 10, 1.0f, 50.0, true, new byte[]{1}),
                new Simple("B", 20, 1.5f, 60.0, false, new byte[]{2})
        );
        // Use DeepCopyUtil to copy the list.
        List<Simple> simples2 = DeepCopyUtil.deepCopy(simples1);
        ComplexObject obj1 = new ComplexObject(child1, simples1, "Extra1");
        ComplexObject obj2 = new ComplexObject(child2, simples2, "DifferentExtra");

        EqualLibConfig config = new EqualLibConfig()
                .setCompareInheritedFields(true)
                .setMaxComparisonDepth(2, true)
                .setCompareCollectionsByElements(false)
                .setCustomEqualsClasses(SpecialClass.class.getName())
                .setIgnoredFieldPaths(ComplexObject.class.getName() + ".extra")
                .setDebugEnabled(true);
        assertTrue(EqualLib.areEqual(obj1, obj2, config),
                "Complex combination of settings should evaluate the objects as equal");
    }
}
