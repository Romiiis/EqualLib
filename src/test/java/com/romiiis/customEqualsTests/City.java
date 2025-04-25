package com.romiiis.customEqualsTests;

public class City {
    String name;
    int population;
    Country country;

    public City(String name, int population, Country country) {
        this.name = name;
        this.population = population;
        this.country = country;
    }

    @Override
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
        City other = (City) obj;
        return this.name.equals(other.name) && this.country == other.country;
    }
}
