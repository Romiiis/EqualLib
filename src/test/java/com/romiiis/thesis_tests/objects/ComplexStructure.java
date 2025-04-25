package com.romiiis.thesis_tests.objects;

import java.util.List;

/* ================================
   Scénář 24: Složitě zanořené struktury
   ================================ */
public class ComplexStructure {
    public Nested nested;
    public List<Simple> simples;

    public ComplexStructure(Nested nested, List<Simple> simples) {
        this.nested = nested;
        this.simples = simples;
    }
}
