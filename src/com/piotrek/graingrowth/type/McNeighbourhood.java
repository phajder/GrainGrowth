package com.piotrek.graingrowth.type;

/**
 * Enum containing Monte Carlo neighbourhood types used on UI purpose.
 * Created by Piotr on 29.11.2016.
 * @author Piotr Hajder
 */
public enum McNeighbourhood {
    VON_NEUMANN("Von Neumann"),
    MOORE("Moore");

    private String name;

    McNeighbourhood(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return name;
    }
}
