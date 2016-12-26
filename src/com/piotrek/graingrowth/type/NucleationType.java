package com.piotrek.graingrowth.type;

/**
 * Nucleation type in static recrystallization.
 * Created by Piotr on 20.12.2016.
 * @author Piotr Hajder
 */
public enum NucleationType {
    CONSTANT("Constant"),
    INCREASING("Increasing"),
    DECREASING("Decreasing"),
    AT_START("At the beginning");

    private String name;

    NucleationType(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return name;
    }
}
