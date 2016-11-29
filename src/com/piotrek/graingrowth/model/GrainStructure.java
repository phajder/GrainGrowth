package com.piotrek.graingrowth.model;

/**
 * Basic model for grain growth algorithms.
 * Created by Piotrek on 29.11.2016.
 * @author Piotrek
 */
public abstract class GrainStructure {
    protected static final int INCLUSION_VALUE = -1;

    protected int boundaryValue;
    protected boolean periodical;

    protected Integer[][] states;

    protected GrainStructure(boolean periodical) {
        this.periodical = periodical;
        boundaryValue = INCLUSION_VALUE;
    }

    protected static int modulo(int n, int p) {
        int tmp = n%p;
        if(tmp < 0)
            tmp += p;
        return tmp;
    }

    protected Integer[][] getNeighbours(int x, int y, int[][] neighbourhood) {
        Integer[][] result = new Integer[3][3];

        for(int i=-1; i<2; i++) {
            for(int j=-1; j<2; j++) {
                if(i == j) {
                    result[1+i][1+j] = 0;
                    continue;
                }

                if(periodical || (x+i >= 0 && y+j >= 0 && x+i < states.length && y+j < states[0].length)) {
                    int val = states[modulo(x+i, states.length)][modulo(y+j, states[0].length)];
                    if(val > boundaryValue) {
                        result[1 + i][1 + j] = neighbourhood[1 + i][1 + j] * states[modulo(x + i, states.length)][modulo(y + j, states[0].length)];
                        continue;
                    }
                }
                result[1+i][1+j] = 0;
            }
        }

        return result;
    }

    protected abstract Integer[][] getNeighbours(int x, int y);

    public abstract void process();
    public abstract boolean isNotEnd();
    public abstract void drawGrains(int numOfGrains);

    public void setBoundaryValue(int boundaryValue) {
        this.boundaryValue = boundaryValue;
    }

    public void setDefaultBoundaryValue() {
        boundaryValue = INCLUSION_VALUE;
    }
}
