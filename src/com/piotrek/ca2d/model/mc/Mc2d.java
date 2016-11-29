package com.piotrek.ca2d.model.mc;

import com.piotrek.ca2d.model.GrainStructure;

import java.util.Random;

/**
 * Monte Carlo method implementation.
 * Created by Piotrek on 29.11.2016.
 * @author Piotrek
 */
public abstract class Mc2d extends GrainStructure {
    private static final int MAX_ITERATIONS = 200;
    private int maxIterations;
    private int currentIteration;

    private Mc2d() {
        super(true);
        maxIterations = MAX_ITERATIONS;
        currentIteration = 0;
    }

    private Mc2d(Integer[][] states) {
        this();
        this.states =states;
    }

    Mc2d(Integer[][] states, boolean periodical) {
        this(states);
        this.periodical = periodical;
    }

    private static int kronecker(int i, int j) {
        return i == j ? 1 : 0;
    }

    protected abstract double probability(double dE);

    @Override
    public void process() {
        Random random = new Random();

        double Jgb = 1.0; //TODO: change this value

        double energyBefore = 0, energyAfter = 0;
        int x = random.nextInt(states.length), y = random.nextInt(states[0].length);
        int cellId = states[x][y];
        int newX, newY;

        do {
            newX = x - random.nextInt(3) + 1;
            newY = y - random.nextInt(3) + 1;
        } while(!(periodical || (newX >= 0 && newX < states.length && newY >= 0 && newY < states[0].length)));

        newX = modulo(newX, states.length); newY = modulo(newY, states[0].length);
        int newCellId = states[newX][newY];

        Integer[][] neighbours = getNeighbours(x, y);
        for(int i=-1; i<2; i++) {
            for(int j=-1; j<2; j++) {
                if(i == 0 && j == 0) continue;
                energyBefore += Jgb * (1-kronecker(cellId, neighbours[1+i][1+j]));
                energyAfter += Jgb * (1-kronecker(newCellId, neighbours[1+i][1+j]));
            }
        }
        double probability = probability(energyAfter - energyBefore);
        if(probability == 1) {
            states[x][y] = newCellId;
        } else {
            if(Math.random() < probability) {
                states[x][y] = newCellId;
            }
        }

        currentIteration++;
    }

    @Override
    public boolean isNotEnd() {
        return currentIteration < maxIterations;
    }

    @Override
    public void drawGrains(int numOfGrains) {
        Random random = new Random();
        if(currentIteration == 0) {
            for (int i = 0; i < states.length; i++) {
                for (int j = 0; j < states[0].length; j++) {
                    states[i][j] = random.nextInt(numOfGrains);
                }
            }
        }
    }

    public void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
    }
}
