package com.piotrek.graingrowth.model.mc;

/**
 * Moore neighbourhood for Monte Carlo grain growth.
 * Created by Piotr on 29.11.2016.
 * @author Piotr Hajder
 */
class Moore extends Mc2d {
    private static final int[][] NEIGHBOURHOOD = {
            {1, 1, 1},
            {1, 1, 1},
            {1, 1, 1}
    };

    Moore(boolean periodical, Integer[][] states) {
        super(periodical, states);
    }

    @Override
    protected Integer[][] getNeighbours(int x, int y) {
        return getNeighbours(x, y, NEIGHBOURHOOD);
    }

    @Override
    protected double probability(double dE) {
        return dE < 0 ? 1 : 0;
    }
}
