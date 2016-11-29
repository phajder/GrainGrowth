package com.piotrek.graingrowth.model.mc;

/**
 * Von Neumann neighbourhood for Monte Carlo method.
 * Created by Piotrek on 29.11.2016.
 * @author Piotrek
 */
public class VonNeumann extends Mc2d {
    private static final int[][] NEIGHBOURHOOD = {
            {0, 1, 0},
            {1, 1, 1},
            {0, 1, 0}
    };

    private final double kbt;

    VonNeumann(Integer[][] states, boolean periodical) {
        super(states, periodical);
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
