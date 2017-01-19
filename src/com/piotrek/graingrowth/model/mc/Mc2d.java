package com.piotrek.graingrowth.model.mc;

import com.piotrek.graingrowth.model.GrainStructure;

import java.util.*;
import java.util.List;

/**
 * Monte Carlo method implementation.
 * Created by Piotr on 29.11.2016.
 * @author Piotr Hajder
 */
public abstract class Mc2d extends GrainStructure {
    private static final int MAX_ITERATIONS = 700;
    private static final int Q = 500;
    private int maxIterations;
    private int currentIteration;

    private final Random random;

    Mc2d(boolean periodical, Integer[][] states) {
        super(periodical);
        random = new Random();
        this.states = states;
        maxIterations = MAX_ITERATIONS;
        currentIteration = 0;
    }

    private static int kronecker(int i, int j) {
        return i == j ? 1 : 0;
    }

    protected abstract double probability(double dE);

    private Integer findNewCellIds(Integer[][] neighbours) {
        List<Integer> list = new ArrayList<>();
        for (Integer[] neighbour : neighbours) {
            for (Integer val : neighbour) {
                if (val > boundaryValue)
                    list.add(val);
            }
        }
        return list.isEmpty() ? null :
                list.get(random.nextInt(list.size()));
    }

    @Override
    public void process() {
        double Jgb = 0.6;

        for(int q=0; q<Q; q++) {
            double energyBefore = 0.0, energyAfter = 0.0;
            int x, y;
            int searchCriteria = recrystallizationMode ? 0 : boundaryValue;
            do { //drawing random cell
                x = random.nextInt(states.length);
                y = random.nextInt(states[0].length);
            } while (states[x][y] <= searchCriteria);

            int cellId = states[x][y];

            Integer[][] neighbours = getNeighbours(x, y);
            Integer newCellId = findNewCellIds(neighbours);
            if(newCellId == null) continue;

            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    if (i == 0 && j == 0) continue;
                    energyBefore += 1 - kronecker(cellId, neighbours[1 + i][1 + j]);
                    energyAfter += 1 - kronecker(newCellId, neighbours[1 + i][1 + j]);
                }
            }
            if (recrystallizationMode) {
                energyBefore += storedEnergyH[x][y];
            }

            double probability = probability(Jgb * (energyAfter - energyBefore));
            if (probability == 1) {
                states[x][y] = newCellId;
                if(recrystallizationMode) storedEnergyH[x][y] = 0;
            } else {
                if (Math.random() < probability) {
                    states[x][y] = newCellId;
                    if(recrystallizationMode) storedEnergyH[x][y] = 0;
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

    @Override
    public int getProgress() {
        return Math.round(100.0f * currentIteration / maxIterations);
    }

    public void setMaxIterations(int maxIterations) {
        if(maxIterations > 0) this.maxIterations = maxIterations;
        else this.maxIterations = MAX_ITERATIONS;
    }
}
