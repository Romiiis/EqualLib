package com.romiis.thesis_tests.objects;

public class ChildWithField extends ParentWithField {
    public int b;

    public ChildWithField(int a, int b) {
        super(a);
        this.b = b;
    }
}
