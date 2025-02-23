package com.romiis.objects.simpleTests;

import lombok.Setter;

public class ObjectC {
    public String name;
    @Setter
    public ObjectC next;

    public ObjectC(String name, ObjectC next) {
        this.name = name;
        this.next = next;
    }

}
