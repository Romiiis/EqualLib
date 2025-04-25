package com.romiiis.thesis_tests.objects;

public class Nested {
    public String value;
    public Nested child;
    public Nested(String value, Nested child) { this.value = value; this.child = child; }
}
