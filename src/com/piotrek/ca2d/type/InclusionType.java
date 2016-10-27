package com.piotrek.ca2d.type;

/**
 * Created by pioot on 19.10.2016.
 */
public enum InclusionType {
    SQUARE("Square"), CIRCULAR("Circular");
    private String name;

    InclusionType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
