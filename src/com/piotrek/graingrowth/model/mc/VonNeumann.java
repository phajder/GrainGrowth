package com.piotrek.graingrowth.model.mc;

/**
 * Von Neumann neighbourhood for Monte Carlo method.
 * Created by Piotr on 29.11.2016.
 * @author Piotr Hajder
 */
class VonNeumann extends Mc2d {
    private static final int[][] NEIGHBOURHOOD = {
            {0, 1, 0},
            {1, 1, 1},
            {0, 1, 0}
    };

    private final double kbt;

    VonNeumann(boolean periodical, Integer[][] states) {
        super(periodical, states);
        //kbt = 0.9 * Math.random() + 0.1;
        kbt = 0.6;
    }

    @Override
    protected Integer[][] getNeighbours(int x, int y) {
        return getNeighbours(x, y, NEIGHBOURHOOD);
    }

    @Override
    protected double probability(double dE) {
        return dE < 0 ? 1 : Math.exp(-dE/kbt);
    }
}
