package com.romiiis.thesis_tests.objects;


/* ================================
   Scénář 23: Cykliké odkazy
   ================================ */
public class Cyclic {
    public int id;
    public Cyclic partner;

    public Cyclic(int id) {
        this.id = id;
    }

    public void setPartner(Cyclic partner) {
        this.partner = partner;
    }
}
