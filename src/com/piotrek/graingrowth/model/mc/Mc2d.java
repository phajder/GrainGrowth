package com.piotrek.graingrowth.model.mc;

import com.piotrek.graingrowth.model.GrainStructure;

import java.util.*;

/**
 * Monte Carlo method implementation.
 * Created by Piotrek on 29.11.2016.
 * @author Piotrek
 */
public abstract class Mc2d extends GrainStructure {
    private static final int MAX_ITERATIONS = 10000000;
    private int maxIterations;
    private int currentIteration;

    Mc2d(Integer[][] states, boolean periodical) {
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

        double energyBefore = 0, energyAfter = 0;
        int x, y;
        do {
            x = random.nextInt(states.length); y = random.nextInt(states[0].length);
        } while(states[x][y] <= boundaryValue);

        int cellId = states[x][y], newCellId;
        int newX, newY;

        while(true) {
            newX = x - random.nextInt(3) + 1;
            newY = y - random.nextInt(3) + 1;

            if(periodical || (newX >= 0 && newX < states.length && newY >= 0 && newY < states[0].length)) {
                newX = modulo(newX, states.length);
                newY = modulo(newY, states[0].length);
                newCellId = states[newX][newY];
                if(newCellId > boundaryValue) break;
            }
        }

        Integer[][] neighbours = getNeighbours(x, y);
        for(int i=-1; i<2; i++) {
            for(int j=-1; j<2; j++) {
                if(i == 0 && j == 0) continue;
                energyBefore += 1-kronecker(cellId, neighbours[1+i][1+j]);
                energyAfter += 1-kronecker(newCellId, neighbours[1+i][1+j]);
            }
        }

        energyBefore *= Jgb;
        energyAfter *= Jgb;

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
        Set<Integer> set = new HashSet<>();
        for(Integer[] tab: states) {
            set.addAll(Arrays.asList(tab));
        }
        int max = set.stream().max(Comparator.naturalOrder()).orElse(null);

        Random random = new Random();
        for (int i = 0; i < states.length; i++) {
            for (int j = 0; j < states[0].length; j++) {
                if(states[i][j] == 0)
                    states[i][j] = random.nextInt(numOfGrains) + max + 1;
            }
        }
    }

    public void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
    }

    public void resetIterations() {
        currentIteration = 0;
    }
}
