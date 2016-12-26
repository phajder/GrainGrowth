package com.piotrek.graingrowth.type;

/**
 * Enum used in UI to obtain grain growth method for substructures and dual phase
 * Created by Piotr on 29.11.2016.
 * @author Piotr Hajder
 */
public enum MethodType {
    CA("Cellular Automata"),
    MC("Monte Carlo");

    private String name;

    MethodType(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return name;
    }
}
