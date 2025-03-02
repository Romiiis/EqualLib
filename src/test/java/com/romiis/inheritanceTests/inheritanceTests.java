package com.romiis.inheritanceTests;

import com.romiis.core.EqualLib;
import com.romiis.core.EqualLibConfig;
import org.junit.jupiter.api.BeforeEach;
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
        config.setCompareByElementsAndKeys(true);
    }

    // ✅ 1. Test základního porovnání stejných objektů
    @Test
    void testEqualObjects() {
        String str1 = "Hello";
        String str2 = "Hello";
        assertTrue(EqualLib.areEqual(str1, str2, config), "Identické řetězce by měly být shodné");
    }

    // ✅ 2. Test rozdílných objektů
    @Test
    void testNotEqualObjects() {
        String str1 = "Hello";
        String str2 = "World";
        assertFalse(EqualLib.areEqual(str1, str2, config), "Rozdílné řetězce by neměly být shodné");
    }

    // ✅ 3. Test pro porovnání s dědičností (equivalenceByInheritance = true)
    @Test
    void testEqualityByInheritance() {
        Parent parent = new Parent();
        Child child = new Child();

        config.equivalenceByInheritance(true); // Povolit porovnání jen podle zděděných atributů

        assertTrue(EqualLib.areEqual(parent, child, config), "Dědičnost by měla stačit pro rovnost");
    }

    // ✅ 4. Test pro porovnání s dědičností vypnutou (equivalenceByInheritance = false)
    @Test
    void testNotEqualByInheritance() {
        Parent parent = new Parent();
        Child child = new Child();

        config.equivalenceByInheritance(false); // Musí se porovnávat všechny atributy

        assertFalse(EqualLib.areEqual(parent, child, config), "Dědičnost nestačí, měly by se porovnávat i potomkovské atributy");
    }

    // ✅ 5. Test porovnání dvou potomků stejného rodiče
    @Test
    void testTwoChildrenSameParent() {
        Child child1 = new Child();
        AnotherChild child2 = new AnotherChild();

        config.equivalenceByInheritance(true);

        assertTrue(EqualLib.areEqual(child1, child2, config), "Dva potomci stejného rodiče by měli být považováni za stejné při zapnuté dědičnosti");
    }

    // ✅ 6. Test porovnání dvou rodičů s různými hodnotami
    @Test
    void testTwoParentsDifferentValues() {
        Parent parent1 = new Parent();
        Parent parent2 = new Parent();
        parent2.value = 30; // Jiná hodnota

        config.equivalenceByInheritance(true);

        assertFalse(EqualLib.areEqual(parent1, parent2, config), "Rodiče s různými hodnotami by neměli být považováni za stejné");
    }

    // ✅ 7. Test porovnání rodiče s jinou třídou
    @Test
    void testParentWithDifferentClass() {
        Parent parent = new Parent();
        UnrelatedClass unrelated = new UnrelatedClass();

        config.equivalenceByInheritance(true);

        assertFalse(EqualLib.areEqual(parent, unrelated, config), "Rodič a zcela jiná třída by neměly být považovány za stejné");
    }

    // ✅ 8. Test porovnání map (složitější struktura)
    @Test
    void testMapEquality() {
        Map<String, Integer> map1 = new HashMap<>();
        Map<String, Integer> map2 = new HashMap<>();

        map1.put("a", 1);
        map1.put("b", 2);

        map2.put("a", 1);
        map2.put("b", 2);

        assertTrue(EqualLib.areEqual(map1, map2, config), "Mapy se stejnými klíči a hodnotami by měly být shodné");
    }

    // ✅ 9. Test porovnání map s různými hodnotami
    @Test
    void testMapNotEqual() {
        Map<String, Integer> map1 = new HashMap<>();
        Map<String, Integer> map2 = new HashMap<>();

        map1.put("a", 1);
        map1.put("b", 2);

        map2.put("a", 1);
        map2.put("b", 3); // Rozdílná hodnota

        assertFalse(EqualLib.areEqual(map1, map2, config), "Mapy s rozdílnými hodnotami by neměly být shodné");
    }

    // ✅ 10. Test porovnání null hodnot
    @Test
    void testNullEquality() {
        assertTrue(EqualLib.areEqual(null, null, config), "Dvě null hodnoty by měly být shodné");
        assertFalse(EqualLib.areEqual(null, "test", config), "Null a neprázdný objekt by neměly být shodné");
    }

    // ✅ Statické třídy pro testování dědičnosti
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

}
