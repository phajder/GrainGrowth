package com.piotrek.ca2d.model;

import java.awt.*;
import java.util.List;

/**
 * Created by pioot on 18.10.2016.
 */
class VonNeumann extends Ca2d {
    private static final int[][] neighClose = {
            {0,1,0},
            {1,1,1},
            {0,1,0}
    };
    private static final int[][] neighFurther = {
            {1,0,1},
            {0,1,0},
            {1,0,1}
    };

    VonNeumann(Dimension size, boolean periodical, Integer[][] states) {
        super(size, periodical, states);
    }

    @Override
    protected List<Integer> getNeighbours(Point current) {
        return getNeighbours(neighClose, current);
    }

    static int[][] geVonNeumannClose() {
        return neighClose;
    }

    static int[][] getVonNeumannFurther() {
        return neighFurther;
    }
}
