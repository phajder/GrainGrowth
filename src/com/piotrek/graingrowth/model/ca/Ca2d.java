package com.piotrek.graingrowth.model.ca;

import com.piotrek.graingrowth.model.GrainStructure;
import com.piotrek.graingrowth.type.InclusionType;

import java.util.*;
import java.util.List;

import static java.lang.System.arraycopy;

/**
 * Cellular Automata method implementation.
 * Created by Piotrek on 18.10.2016.
 * @author Piotrek
 */
public abstract class Ca2d extends GrainStructure {
    private Integer[][] newStates;

    Ca2d(boolean periodical, Integer[][] newStates) {
        super(periodical);
        this.periodical = periodical;
        this.newStates = newStates;
        this.states = new Integer[newStates.length][newStates[0].length];
        for(Integer[] tab: states) {
            for(int i=0; i<tab.length; i++) {
                tab[i] = 0;
            }
        }
    }

    @Override
    public final void process() {
        for(int i=0; i<newStates.length; i++) {
            arraycopy(this.newStates[i], 0, this.states[i], 0, newStates[0].length);
        }
        Integer[][] neighbours;
        int freq, val;
        for(int i=0; i<newStates.length; i++) {
            for(int j=0; j<newStates[0].length; j++) {
                if(states[i][j] == 0) {
                    neighbours = getNeighbours(i, j);
                    List<Integer> list = new ArrayList<>();
                    Set<Integer> set = new HashSet<>();
                    for(Integer[] t: neighbours) {
                        list.addAll(Arrays.asList(t));
                        set.addAll(Arrays.asList(t));
                    }
                    val = 0;
                    if(!set.isEmpty()) {
                        freq = 0;
                        for(Integer v: set) {
                            if(v == 0) continue;
                            if(Collections.frequency(list, v) > freq) {
                                freq = Collections.frequency(list, v);
                                val = v;
                            }
                        }
                    }
                    newStates[i][j] = val;
                }
            }
        }
    }

    @Override
    public final void drawGrains(int numOfGrains) {
        Random random = new Random();
        int x, y;
        for(int i=1; i<=numOfGrains; i++) {
            do {
                x = random.nextInt(newStates.length);
                y = random.nextInt(newStates[0].length);
            } while(newStates[x][y] != 0);
            newStates[x][y] = i + boundaryValue;
        }
    }

    @Override
    public final boolean isNotEnd() {
        for(int i=0; i<newStates.length; i++) {
            for(int j=0; j<newStates[0].length; j++) {
                if(!Objects.equals(newStates[i][j], states[i][j]))
                    return true;
            }
        }
        setDefaultBoundaryValue();
        return false;
    }

    @Override
    protected void drawInclusions(InclusionType type, int radius, int x, int y) {
        super.drawInclusions(type, radius, x, y);
        for(int i=0; i<states.length; i++) {
            arraycopy(states[i], 0, newStates[i], 0, states[i].length);
        }
    }
}
