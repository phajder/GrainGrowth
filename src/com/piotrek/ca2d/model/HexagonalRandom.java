package com.piotrek.ca2d.model;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by pioot on 18.10.2016.
 */
class HexagonalRandom extends Ca2d {
    private static List<int[][]> neighCases = init();

    private static List<int[][]> init() {
        List<int[][]> list = new ArrayList<>();
        int[][] tab1 = {
                {0, 1, 1},
                {1, 1, 1},
                {1, 1, 0}
        }, tab2 = {
                {1, 1, 0},
                {1, 1, 1},
                {0, 1, 1}
        };
        list.add(tab1);
        list.add(tab2);

        return list;
    }

    HexagonalRandom(Dimension size, boolean periodic, Integer[][] states) {
        super(size, periodic, states);
    }

    @Override
    protected List<Integer> getNeighbours(Point current) {
        Random random = new Random();
        int val = random.nextInt(2);

        return getNeighbours(neighCases.get(val), current);
    }
}
