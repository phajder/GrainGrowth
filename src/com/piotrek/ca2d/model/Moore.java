package com.piotrek.ca2d.model;

import java.awt.*;
import java.util.List;

/**
 * Created by pioot on 18.10.2016.
 */
class Moore extends Ca2d {
    private static final int[][] neigh = {
            {1,1,1},
            {1,1,1},
            {1,1,1}
    };

    Moore(Dimension size, boolean periodic, Integer[][] states) {
        super(size, periodic, states);
    }

    @Override
    protected List<Integer> getNeighbours(Point current) {
        return getNeighbours(neigh, current);
    }

    static int[][] getMooreNeighbourhood() {
        return neigh;
    }
}
