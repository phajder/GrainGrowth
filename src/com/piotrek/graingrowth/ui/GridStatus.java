package com.piotrek.graingrowth.ui;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Used for grid panel implementation, containing important data for rendering.
 * Created by Piotr on 18.10.2016.
 * @author Piotr Hajder
 */
class GridStatus {
    private final Random random = new Random();
    private final Dimension dim;
    private Integer[][] states;
    private volatile List<Color> colorList;
    private boolean proceeded;

    GridStatus(Dimension dim) {
        this.dim = dim;
        states = new Integer[dim.height][dim.width];
        for(Integer[] tab: states) {
            Arrays.fill(tab, 0);
        }
        colorList = null;
        proceeded = false;
    }

    GridStatus(Integer[][] states) {
        this.states = states;
        dim = new Dimension(states[0].length, states.length);
        colorList = null;
        proceeded = false;
    }

    private int findMax() {
        Set<Integer> set = new HashSet<>();
        for(Integer[] tab: states) {
            set.addAll(Arrays.asList(tab));
        }
        return set.stream().max(Comparator.naturalOrder()).orElse(null);
    }

    /**
     * Generates random color using RGB fractions. Which ones should be used is determined by parameters.
     * @param r Determines if red fraction should be included.
     * @param g Determines if green fraction should be included.
     * @param b Determines if blue fraction should be included.
     */
    private void addColor(int r, int g, int b) {
        colorList.add(new Color(
                r*random.nextInt(255),
                g*random.nextInt(255),
                b*random.nextInt(255))
        );
    }

    /**
     * Generates random color including all RGB fractions.
     */
    private void addColor() {
        addColor(1,0,0);
    }

    private void removeAllColors() {
        if(colorList!= null) colorList.clear();
    }

    private void generateColors(int r, int g, int b) {
        if(colorList == null) colorList = new ArrayList<>();
        for(int i=colorList.size(); i<findMax(); i++) {
            addColor(r,g,b);
        }
    }

    private void resetColors() {
        removeAllColors();
        generateColors(0,0,1);
    }

    void generateRandomColors() {
        generateColors(0,1,0);
    }

    void generateRecrystallizationColors() {
        generateColors(1, 0, 0);
    }

    void addSeed(Point p) {
        if(this.states[p.x][p.y] == 0) {
            states[p.x][p.y] = findMax() + 1;
            addColor();
        }
    }

    void reset() {
        removeAllColors();
        for(int i=0; i<dim.height; i++) {
            for(int j=0; j<dim.width; j++) {
                states[i][j] = 0;
            }
        }
        proceeded = false;
    }

    void createSubstructure(List<Integer> values) {
        int counter;
        for(int i=0; i<dim.height; i++) {
            for(int j=0; j<dim.width; j++) {
                counter = 0;
                for(int v=0; v<values.size(); v++) {
                    if(Objects.equals(states[i][j], values.get(v))) {
                        states[i][j] = v + 1;
                        break;
                    }
                    counter++;
                }
                if(counter == values.size()) states[i][j] = 0;
            }
        }
        resetColors();
    }

    void createDualPhase(List<Integer> values) {
        int counter;
        for(int i=0; i<dim.height; i++) {
            for(int j=0; j<dim.width; j++) {
                counter = 0;
                for(int val: values) {
                    if(states[i][j] == val) {
                        states[i][j] = 1;
                        break;
                    }
                    counter++;
                }
                if(counter == values.size()) states[i][j] = 0;
            }
        }
        resetColors();
    }

    public void setStates(Integer[][] states) {
        this.states = states;
    }

    Dimension getDim() {
        return dim;
    }

    Integer[][] getStates() {
        return states;
    }

    int getGrainId(int x, int y) {
        return states[x][y];
    }

    List<Color> getColorList() {
        return colorList;
    }

    void proceed() {
        proceeded = true;
    }

    boolean isProceeded() {
        return proceeded;
    }
}
