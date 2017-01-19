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
    private static final int DEFAULT_ITER_BREAK = 1;
    private static final int DEFAULT_INTERVAL = 1;

    private final Random random;

    private Set<Point> recrystallizedSet;

    private boolean nucleationOnBoundaries;
    private NucleationType nucleationType;

    /**
     * Maximum number of generated nucleons
     */
    private int maxNucleons = DEFAULT_NUCLEONS;

    /**
     * Every n iterations where nucleons are generated.
     * Default value is 1, because every number modulo 1 returns 0.
     * It means nucleons are generated in each iteration.
     */
    private int iterBreak = DEFAULT_ITER_BREAK;

    /**
     * Coefficient. Increases/decreases number of generated nucleons
     * in each iteration
     */
    private int interval = DEFAULT_INTERVAL;

    private int currentIter = 0;

    /**
     * Nucleons generated in iteration
     */
    private int nucleonsPerIter = 0;

    public RecrystallizationParams() {
        random = new Random();
        recrystallizedSet = new HashSet<>();
    }

    private void nextNucleons(int length) {
        if(length == 0) nucleonsPerIter = 0;
        if(nucleonsPerIter > 0 && !nucleationType.equals(NucleationType.CONSTANT)) {
            if (nucleationType.equals(NucleationType.INCREASING)) {
                nucleonsPerIter += interval;
            } else if (nucleationType.equals(NucleationType.DECREASING)) {
                nucleonsPerIter -= interval;
            } else if (nucleationType.equals(NucleationType.AT_START)) {
                nucleonsPerIter = 0;
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
            nucleonsPerIter = interval;
        } else {
            nucleonsPerIter = maxNucleons;
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
        if(currentIter++ % iterBreak == 0) {
            Point p;
            int max = borderSeeds.size();
            for (int i = 0; i < nucleonsPerIter; i++) {
                p = borderSeeds.get(random.nextInt(max));
                if (!recrystallizedSet.contains(p)) {
                    recrystallizedSet.add(p);
                    nucleons.add(p);
                }
            }
            nextNucleons(nucleons.size());
        }
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
        if(currentIter++ % iterBreak == 0) {
            Point p;
            for (int i = 0; i < nucleonsPerIter; i++) {
                p = new Point(random.nextInt(x),
                        random.nextInt(y));
                if (!recrystallizedSet.contains(p)) {
                    recrystallizedSet.add(p);
                    nucleons.add(p);
                }
            }
        }
        nextNucleons(nucleons.size());
        return nucleons;
    }

    public void setDefaultValues() {
        iterBreak = DEFAULT_ITER_BREAK;
        interval = DEFAULT_INTERVAL;
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

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public void setIterBreak(int iterBreak) {
        this.iterBreak = iterBreak;
    }
}
