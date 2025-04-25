package com.romiiis.thesis_tests.objects;

import java.util.List;

public class ComplexObject {
    public Parent parent;
    public List<Simple> simples;
    public String extra;
    public ComplexObject(Parent parent, List<Simple> simples, String extra) {
        this.parent = parent;
        this.simples = simples;
        this.extra = extra;
    }
}
