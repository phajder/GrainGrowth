package com.piotrek.graingrowth.model;

import com.piotrek.graingrowth.type.NucleationType;

import java.util.*;
import java.awt.Point;

/**
 * Parameters for recrystallization, set in UI.
 * Generates nucleons in each iteration of recrystallization.
 * Created by Piotr on 20.12.2016.
 * @author Piotr Hajder
 */
public class RecrystallizationParams {
    private static final int DEFAULT_NUCLEONS = 60;

    private final Random random;

    private Set<Point> recrystallizedSet;

    private boolean nucleationOnBoundaries;
    private NucleationType nucleationType;

    private int maxNucleons = DEFAULT_NUCLEONS;
    private int interval = 10;
    private int currentIter = 0;

    public RecrystallizationParams() {
        random = new Random();
        recrystallizedSet = new HashSet<>();
    }

    private void nextNucleons(int length) {
        if(length == 0) currentIter = 0;
        if(currentIter > 0 && !nucleationType.equals(NucleationType.CONSTANT)) {
            if (nucleationType.equals(NucleationType.INCREASING)) {
                currentIter += interval;
            } else if (nucleationType.equals(NucleationType.DECREASING)) {
                currentIter -= interval;
            } else if (nucleationType.equals(NucleationType.AT_START)) {
                currentIter = 0;
            }
        }
    }

    /**
     * Required for proper nucleons generations.
     * This method has to be called before first generation.
     */
    public void setup() {
        if(nucleationType.equals(NucleationType.CONSTANT)
                || nucleationType.equals(NucleationType.INCREASING)) {
            currentIter = interval;
        } else {
            currentIter = maxNucleons;
        }
    }

    /**
     * Generates new nucleons in each recrystallization step
     * Used when energy has to be distributed on grain boundaries.
     * @param borderSeeds border seeds of microstructure
     * @return generated nucleons
     */
    List<Point> generateNucleons(List<Point> borderSeeds) {
        List<Point> nucleons = new ArrayList<>();
        Point p;
        int max = borderSeeds.size();
        for(int i=0; i<currentIter; i++) {
            p = borderSeeds.get(random.nextInt(max));
            if(!recrystallizedSet.contains(p)) {
                recrystallizedSet.add(p);
                nucleons.add(p);
            }
        }
        nextNucleons(nucleons.size());
        return nucleons;
    }

    /**
     * Generates new nucleons in each recrystallization step.
     * Used when energy can be distributed anywhere.
     * @param x Width of grain microstructure array
     * @param y Height of grain microstructure array
     * @return generated nucleons
     */
    List<Point> generateNucleons(int x, int y) {
        List<Point> nucleons = new ArrayList<>();
        Point p;
        for(int i=0; i<currentIter; i++) {
            p = new Point(random.nextInt(x),
                    random.nextInt(y));
            if(!recrystallizedSet.contains(p)) {
                recrystallizedSet.add(p);
                nucleons.add(p);
            }
        }
        nextNucleons(nucleons.size());
        return nucleons;
    }

    public void setNucleationOnBoundaries(boolean nucleationOnBoundaries) {
        this.nucleationOnBoundaries = nucleationOnBoundaries;
    }

    public void setNucleationType(NucleationType nucleationType) {
        this.nucleationType = nucleationType;
    }

    public void setMaxNucleons(int maxNucleons) {
        this.maxNucleons = maxNucleons;
    }

    boolean isNucleationOnBoundaries() {
        return nucleationOnBoundaries;
    }
}
