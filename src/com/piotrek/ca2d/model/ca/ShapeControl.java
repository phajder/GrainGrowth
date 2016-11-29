package com.piotrek.ca2d.model.ca;

/**
 * Shape control rules for cellular automata method.
 * Created by Piotrek on 25.10.2016.
 * @author Piotrek
 */
class ShapeControl extends Ca2d {
    private RulesImpl rules;

    ShapeControl(boolean periodical, Integer[][] states) {
        super(periodical, states);
        rules = new RulesImpl();
    }

    ShapeControl(boolean periodical, Integer[][] states, int probability) {
        super(periodical, states);
        rules = new RulesImpl(probability);
    }

    @Override
    protected Integer[][] getNeighbours(int x, int y) {
        Integer[][] result = new Integer[1][1];
        Integer[][] neighbours = getNeighbours(x, y, Moore.getMooreNeighbourhood());
        result[0][0] = rules.rule(neighbours);
        return result;
    }
}
