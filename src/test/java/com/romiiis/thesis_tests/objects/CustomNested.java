package com.romiiis.thesis_tests.objects;

import java.util.Objects;

public class CustomNested {
    public String value;
    public CustomNested child;

    public CustomNested(String value, CustomNested child) {
        this.value = value;
        this.child = child;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomNested)) return false;
        CustomNested that = (CustomNested) o;
        return Objects.equals(this.value, that.value);
    }
}
