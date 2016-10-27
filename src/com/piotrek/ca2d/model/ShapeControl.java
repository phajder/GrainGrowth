package com.piotrek.ca2d.model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pioot on 25.10.2016.
 */
class ShapeControl extends Ca2d {
    private RulesImpl rules;

    ShapeControl(Dimension size, boolean periodical, Integer[][] states) {
        super(size, periodical, states);
        rules = new RulesImpl();
    }

    ShapeControl(Dimension size, boolean periodical, Integer[][] states, int probability) {
        super(size, periodical, states);
        rules = new RulesImpl(probability);
    }

    @Override
    protected List<Integer> getNeighbours(Point current) {
        List<Integer> result = new ArrayList<>();
        final int size = 3;
        List<Integer> tab = getNeighbours(Moore.getMooreNeighbourhood(), current);
        Integer[][] neighbours = new Integer[size][size];
        for(int i=0; i<size; i++) {
            for(int j=0; j<size; j++) {
                neighbours[i][j] = tab.get(size * i + j);
            }
        }
        result.add(rules.rule(neighbours));
        return result;
    }
}
