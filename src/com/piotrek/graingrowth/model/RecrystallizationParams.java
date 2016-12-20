package com.piotrek.graingrowth.model;

import com.piotrek.graingrowth.type.NucleationType;

/**
 * Created by Piotrek on 20.12.2016.
 */
public class RecrystallizationParams {
    private boolean energyOnBoundaries;
    private boolean nucleationOnBoundaries;
    private NucleationType nucleationType;

    public void setEnergyOnBoundaries(boolean energyOnBoundaries) {
        this.energyOnBoundaries = energyOnBoundaries;
    }

    public void setNucleationOnBoundaries(boolean nucleationOnBoundaries) {
        this.nucleationOnBoundaries = nucleationOnBoundaries;
    }

    public void setNucleationType(NucleationType nucleationType) {
        this.nucleationType = nucleationType;
    }

    public RecrystallizationParams() {

    }

    public boolean isEnergyOnBoundaries() {
        return energyOnBoundaries;
    }

    public boolean isNucleationOnBoundaries() {
        return nucleationOnBoundaries;
    }

    public NucleationType getNucleationType() {
        return nucleationType;
    }
}
