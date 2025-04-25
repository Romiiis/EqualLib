package com.romiiis.thesis_tests.objects;

import java.util.List;

/* ================================
   Scénář 26: Kombinace ignorování polí a kolekcí jako celků
   ================================ */
public class WithCollectionAndIgnored {
    public int id;
    public String info;
    public List<String> items;

    public WithCollectionAndIgnored(int id, String info, List<String> items) {
        this.id = id;
        this.info = info;
        this.items = items;
    }
}
