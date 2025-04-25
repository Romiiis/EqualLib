package com.romiiis.customEqualsTests;

public class Address {
    String street;
    City city;

    public Address(String street, City city) {
        this.street = street;
        this.city = city;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        Address other = (Address) obj;
        return this.street.equals(other.street) && this.city.equals(other.city);
    }
}
