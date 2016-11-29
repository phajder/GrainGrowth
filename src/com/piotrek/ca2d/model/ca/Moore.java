package com.piotrek.ca2d.model.ca;

/**
 * Moore neighbourhood for cellular automata.
 * Created by Piotrek on 18.10.2016.
 * @author Piotrek
 */
class Moore extends Ca2d {
    private static final int[][] neigh = {
            {1,1,1},
            {1,1,1},
            {1,1,1}
    };

    Moore(boolean periodic, Integer[][] states) {
        super(periodic, states);
    }

    @Override
    protected Integer[][] getNeighbours(int x, int y) {
        return getNeighbours(x, y, neigh);
    }

    static int[][] getMooreNeighbourhood() {
        return neigh;
    }
}
