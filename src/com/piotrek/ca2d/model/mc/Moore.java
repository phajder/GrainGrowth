package com.piotrek.ca2d.model.mc;

/**
 * Moore neighbourhood for Monte Carlo grain growth.
 * Created by Piotrek on 29.11.2016.
 * @author Piotrek
 */
public class Moore extends Mc2d {
    private static final int[][] NEIGHBOURHOOD = {
            {1, 1, 1},
            {1, 1, 1},
            {1, 1, 1}
    };

    protected Moore(Integer[][] states, boolean periodical) {
        super(states, periodical);
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
