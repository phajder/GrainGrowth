package com.piotrek.graingrowth.model.ca;

/**
 * Von Neumann neighbourhood for cellular automata method.
 * Created by Piotr on 18.10.2016.
 * @author Piotr Hajder
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

    VonNeumann(boolean periodical, Integer[][] states) {
        super(periodical, states);
    }

    @Override
    protected Integer[][] getNeighbours(int x, int y) {
        return getNeighbours(x, y, neighClose);
    }

    static int[][] geVonNeumannClose() {
        return neighClose;
    }

    static int[][] getVonNeumannFurther() {
        return neighFurther;
    }
}
