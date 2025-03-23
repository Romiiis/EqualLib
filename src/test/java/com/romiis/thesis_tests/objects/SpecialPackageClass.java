package com.romiis.thesis_tests.objects;

public class SpecialPackageClass {
    public String data;

    public SpecialPackageClass(String data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SpecialPackageClass)) return false;
        SpecialPackageClass that = (SpecialPackageClass) o;
        // Rovnost ignorující velikost písmen
        return data.equalsIgnoreCase(that.data);
    }
}
