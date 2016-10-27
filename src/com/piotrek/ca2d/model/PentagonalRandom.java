package com.piotrek.ca2d.model;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by pioot on 18.10.2016.
 */
class PentagonalRandom extends Ca2d {
    private static List<int[][]> neighCases = init();

    private static List<int[][]> init() {
        List<int[][]> list = new ArrayList<>();
        int[][] tab1 = {
                {1, 1, 0},
                {1, 1, 0},
                {1, 1, 0}
        }, tab2 = {
                {0, 1, 1},
                {0, 1, 1},
                {0, 1, 1}
        }, tab3 = {
                {0, 0, 0},
                {1, 1, 1},
                {1, 1, 1}
        }, tab4 = {
                {1, 1, 1},
                {1, 1, 1},
                {0, 0, 0}
        };
        list.add(tab1);
        list.add(tab2);
        list.add(tab3);
        list.add(tab4);

        return list;
    }

    PentagonalRandom(Dimension size, boolean periodic, Integer[][] states) {
        super(size, periodic, states);
    }

    @Override
    protected List<Integer> getNeighbours(Point current) {
        Random random = new Random();
        int val = random.nextInt(4);

        return getNeighbours(neighCases.get(val), current);
    }
}
