package com.piotrek.graingrowth.model.mc;

import com.piotrek.graingrowth.model.GrainStructure;

import java.util.*;

/**
 * Monte Carlo method implementation.
 * Created by Piotrek on 29.11.2016.
 * @author Piotrek
 */
public abstract class Mc2d extends GrainStructure {
    private static final int MAX_ITERATIONS = 700;
    private static final int Q = 50;
    private int maxIterations;
    private int currentIteration;

    Mc2d(boolean periodical, Integer[][] states) {
        super(periodical);
        this.states = states;
        maxIterations = MAX_ITERATIONS;
        currentIteration = 0;
    }

    private static int kronecker(int i, int j) {
        return i == j ? 1 : 0;
    }

    protected abstract double probability(double dE);

    @Override
    public void process() {
        Random random = new Random();

        //double Jgb = 0.9 * Math.random() + 0.1;
        double Jgb = 0.6;

        for(int q=0; q<Q; q++) {
            double energyBefore = 0, energyAfter = 0;
            int x, y;
            do {
                x = random.nextInt(states.length);
                y = random.nextInt(states[0].length);
            } while (states[x][y] <= boundaryValue);

            int cellId = states[x][y], newCellId;
            int newX, newY;

            while (true) {
                newX = x - random.nextInt(3) + 1;
                newY = y - random.nextInt(3) + 1;

                if (periodical || (newX >= 0 && newX < states.length && newY >= 0 && newY < states[0].length)) {
                    newX = modulo(newX, states.length);
                    newY = modulo(newY, states[0].length);
                    newCellId = states[newX][newY];
                    if (newCellId > boundaryValue) break;
                }
            }

            Integer[][] neighbours = getNeighbours(x, y);
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    if (i == 0 && j == 0) continue;
                    energyBefore += 1 - kronecker(cellId, neighbours[1 + i][1 + j]);
                    energyAfter += 1 - kronecker(newCellId, neighbours[1 + i][1 + j]);
                }
            }

            double probability = probability(Jgb * (energyAfter - energyBefore));
            if (probability == 1) {
                states[x][y] = newCellId;
            } else {
                if (Math.random() < probability) {
                    states[x][y] = newCellId;
                }
            }
        }

        currentIteration++;
    }

    @Override
    public void drawGrains(int numOfGrains) {
        Random random = new Random();
        for (int i = 0; i < states.length; i++) {
            for (int j = 0; j < states[0].length; j++) {
                if(states[i][j] == 0)
                    states[i][j] = random.nextInt(numOfGrains) + boundaryValue + 1;
            }
        }
    }

    @Override
    public boolean isNotEnd() {
        if(currentIteration >= maxIterations) {
            currentIteration = 0;
            return false;
        }
        return true;
    }

    public void setMaxIterations(int maxIterations) {
        if(maxIterations > 0) this.maxIterations = maxIterations;
        else this.maxIterations = MAX_ITERATIONS;
    }
}
