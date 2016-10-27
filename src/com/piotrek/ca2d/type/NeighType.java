package com.piotrek.ca2d.type;

/**
 * Created by pioot on 18.10.2016.
 */
public enum NeighType {
    VON_NEUMANN("Von Neumann"), MOORE("Moore"), HEXAGONAL_RANDOM("Hexagonal random"), PENTAGONAL_RANDOM("Pentagonal random"), SHAPE_CONTROL("Shape control");

    private String name;

    NeighType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
