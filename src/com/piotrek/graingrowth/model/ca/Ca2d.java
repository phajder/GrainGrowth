package com.piotrek.graingrowth.model.ca;

import com.piotrek.graingrowth.model.GrainStructure;
import com.piotrek.graingrowth.type.InclusionType;

import java.awt.*;
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

    private void initAutomaton() {
        for(int i=0; i<states.length; i++) {
            for(int j=0; j<states[0].length; j++) {
                this.states[i][j] = 0;
                this.newStates[i][j] = 0;
            }
        }
    }

    Ca2d(boolean periodical, Integer[][] newStates) {
        super(periodical);
        this.periodical = periodical;
        this.newStates = newStates;
        this.states = new Integer[newStates.length][newStates[0].length];
        initAutomaton();

    }

    private void drawInclusions(InclusionType type, int radius, int x, int y) {
        int value = type.equals(InclusionType.SQUARE) ?
                (int) (Math.round(Math.pow(2.0, -2.0) * radius)) : radius;
        for(int i=-value; i<value; i++) {
            for(int j=-value; j<value; j++) {
                if(type.equals(InclusionType.CIRCULAR))
                    if(i*i + j*j > radius) continue;
                if(periodical || (x+i >= 0 && y+j >= 0 && x+i < newStates.length && y+j < newStates[0].length))
                    newStates[modulo(x+i,newStates.length)][modulo(y+j,newStates[0].length)] = INCLUSION_VALUE;
            }
        }
    }

    private List<Point> checkNeighbours(int x, int y) {
        List<Point> result = new ArrayList<>();
        for(int i=-1; i<2; i+=2) {
            if(x+i >= 0 && x+i < newStates[0].length) {
                if(!Objects.equals(newStates[x][y], newStates[x + i][y]))
                    result.add(new Point(x+i, y));
            }
            if(y+i >= 0 && y+i < newStates[0].length) {
                if(!Objects.equals(newStates[x][y], newStates[x][y + i]))
                    result.add(new Point(x, y+i));
            }
        }
        if(!result.isEmpty())
            result.add(new Point(x, y));
        return result;
    }

    private List<Point> findBorderSeeds() {
        List<Point> result = new ArrayList<>();
        List<Point> tmp;
        for(int i=0; i<newStates.length; i++) {
            for(int j=0; j<newStates[i].length; j++) {
                tmp = checkNeighbours(i, j);
                tmp.stream().filter(p -> !result.contains(p)).forEach(result::add);
            }
        }
        return result;
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
        Set<Integer> set = new HashSet<>();
        for(Integer[] tab: newStates) {
            set.addAll(Arrays.asList(tab));
        }
        int max = set.stream().max(Comparator.naturalOrder()).orElse(null);
        Random random = new Random();
        int x, y;
        for(int i=1; i<=numOfGrains; i++) {
            do {
                x = random.nextInt(newStates.length);
                y = random.nextInt(newStates[0].length);
            } while(newStates[x][y] != 0);
            newStates[x][y] = i + max;
        }
    }

    public final void drawInclusionBefore(InclusionType type, int radius) {
        Random random = new Random();
        int x = random.nextInt(newStates.length), y = random.nextInt(newStates[0].length);

        drawInclusions(type, radius, x, y);
    }

    public final void drawInclusionsAfter(InclusionType type, int radius) {
        List<Point> borderSeeds = findBorderSeeds();
        Random random = new Random();
        int val = random.nextInt(borderSeeds.size());

        drawInclusions(type, radius, borderSeeds.get(val).x, borderSeeds.get(val).y);
    }

    public final boolean isNotEnd() {
        for(int i=0; i<newStates.length; i++) {
            for(int j=0; j<newStates[0].length; j++) {
                if(!Objects.equals(newStates[i][j], states[i][j]))
                    return true;
            }
        }
        return false;
    }
}
