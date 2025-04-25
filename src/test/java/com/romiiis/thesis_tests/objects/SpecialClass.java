package com.romiiis.thesis_tests.objects;

/* ================================
   Scénáře 13–14: Vlastní porovnání tříd
   ================================ */
public class SpecialClass {
    public int x;

    public SpecialClass(int x) {
        this.x = x;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SpecialClass)) return false;
        SpecialClass other = (SpecialClass) o;
        // Například: rovnost na základě sudosti (parita)
        return (this.x % 2) == (other.x % 2);
    }
}
