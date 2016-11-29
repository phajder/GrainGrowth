package com.piotrek.graingrowth.model.ca;

import java.util.*;
import java.util.List;

/**
 * Pentagonal random neighbourhood for cellular automata.
 * Created by Piotrek on 18.10.2016.
 * @author Piotrek
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

    PentagonalRandom(boolean periodic, Integer[][] states) {
        super(periodic, states);
    }

    @Override
    protected Integer[][] getNeighbours(int x, int y) {
        Random random = new Random();
        int val = random.nextInt(4);

        return getNeighbours(x, y, neighCases.get(val));
    }
}
