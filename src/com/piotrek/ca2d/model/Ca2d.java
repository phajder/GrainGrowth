package com.piotrek.ca2d.model;

import com.piotrek.ca2d.type.InclusionType;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.System.arraycopy;

/**
 * Created by pioot on 18.10.2016.
 */
public abstract class Ca2d {
    private static final int INCLUSION_VALUE = -1;
    private Integer[][] previous;
    private Integer[][] states;
    private final Dimension size;
    private final boolean periodical;
    private int boundaryValue;

    private void initAutomaton() {
        for(int i=0; i<size.height; i++) {
            for(int j=0; j<size.width; j++) {
                this.states[i][j] = 0;
                this.previous[i][j] = 0;
            }
        }
    }

    protected abstract List<Integer> getNeighbours(Point current);

    private static int modulo(int n, int p) {
        int tmp = n%p;
        if(tmp < 0)
            tmp += p;
        return tmp;
    }

    private Ca2d(Dimension size, boolean periodical) {
        this.size = size;
        this.periodical = periodical;
        states = new Integer[size.height][size.width];
        previous = new Integer[size.height][size.width];
        initAutomaton();
        boundaryValue = INCLUSION_VALUE;
    }

    Ca2d(Dimension size, boolean periodical, Integer[][] states) {
        this(size, periodical);
        if(states != null) {
            this.states = states;
        }
    }

    List<Integer> getNeighbours(int[][] tab, Point current) {
        List<Integer> list = new ArrayList<>();
        int val;

        for(int i=-1; i<2; i++) {
            for(int j=-1; j<2; j++) {
                if(i==0 && j== 0) {
                    list.add(0);
                    continue;
                }
                if(periodical || (current.x+i >= 0 && current.y+j >= 0 && current.x+i < size.height && current.y+j < size.width)) {
                    val = tab[1 + i][1 + j] * previous[modulo(current.x + i, size.height)][modulo(current.y + j, size.width)];
                    if (val > boundaryValue)
                        list.add(val);
                    else
                        list.add(0);
                } else {
                    list.add(0);
                }
            }
        }
        return list;
    }

    private void drawInclusions(InclusionType type, int radius, int x, int y) {
        int value = type.equals(InclusionType.SQUARE) ?
                (int) (Math.round(Math.pow(2.0, -2.0) * radius)) : radius;
        for(int i=-value; i<value; i++) {
            for(int j=-value; j<value; j++) {
                if(type.equals(InclusionType.CIRCULAR))
                    if(i*i + j*j > radius) continue;
                if(periodical || (x+i >= 0 && y+j >= 0 && x+i < size.height && y+j < size.width))
                    states[modulo(x+i,size.height)][modulo(y+j,size.width)] = INCLUSION_VALUE;
            }
        }
    }

    private List<Point> checkNeighbours(int x, int y) {
        List<Point> result = new ArrayList<>();
        for(int i=-1; i<2; i+=2) {
            if(x+i >= 0 && x+i < states[0].length) {
                if(states[x][y] != states[x+i][y])
                    result.add(new Point(x+i, y));
            }
            if(y+i >= 0 && y+i < states[0].length) {
                if(states[x][y] != states[x][y+i])
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
        for(int i=0; i<states.length; i++) {
            for(int j=0; j<states[i].length; j++) {
                tmp = checkNeighbours(i, j);
                tmp.stream().filter(p -> !result.contains(p)).forEach(result::add);
            }
        }
        return result;
    }

    public final void process() {
        for(int i=0; i<size.height; i++) {
            arraycopy(this.states[i], 0, this.previous[i], 0, size.width);
        }
        List<Integer> neighbours;
        for(int i=0; i<size.height; i++) {
            for(int j=0; j<size.width; j++) {
                if(previous[i][j] == 0) {
                    neighbours = getNeighbours(new Point(i, j));
                    if (!neighbours.isEmpty()) {
                        Map<Integer, Long> occurrences = neighbours.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
                        occurrences.remove(0);
                        if(occurrences.size() > 0) {
                            Integer val = occurrences.entrySet().stream().max((o1, o2) -> o1.getValue().compareTo(o2.getValue())).get().getKey();
                            states[i][j] = val;
                        }
                    }
                }
            }
        }
    }

    public final void drawRandomGrains(int numOfGrains) {
        Set<Integer> set = new HashSet<>();
        for(Integer[] tab: states) {
            set.addAll(Arrays.asList(tab));
        }
        int max = set.stream().max(Comparator.naturalOrder()).get();
        Random random = new Random();
        int x, y;
        for(int i=1; i<=numOfGrains; i++) {
            do {
                x = random.nextInt(size.height);
                y = random.nextInt(size.width);
            } while(states[x][y] != 0);
            states[x][y] = i + max;
        }
    }

    public final void drawInclusionBefore(InclusionType type, int radius) {
        Random random = new Random();
        int x = random.nextInt(size.height), y = random.nextInt(size.width);

        drawInclusions(type, radius, x, y);
    }

    public final void drawInclusionsAfter(InclusionType type, int radius) {
        List<Point> borderSeeds = findBorderSeeds();
        Random random = new Random();
        int val = random.nextInt(borderSeeds.size());

        drawInclusions(type, radius, borderSeeds.get(val).x, borderSeeds.get(val).y);
    }

    public final boolean isNotEnd() {
        for(int i=0; i<size.height; i++) {
            for(int j=0; j<size.width; j++) {
                if(previous[i][j] != states[i][j])
                    return true;
            }
        }
        return false;
    }

    public void setBoundaryValue(int boundaryValue) {
        this.boundaryValue = boundaryValue;
    }
}
