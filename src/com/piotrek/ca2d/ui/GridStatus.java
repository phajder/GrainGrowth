package com.piotrek.ca2d.ui;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Used for grid panel implementation, containing important data for rendering.
 * Created by Piotrek on 18.10.2016.
 * @author Piotrek
 */
class GridStatus {
    private final Dimension dim;
    private Integer[][] states;
    private ArrayList<Color> colorList;
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

    private int findMax() {
        Set<Integer> set = new HashSet<>();
        for(Integer[] tab: states) {
            set.addAll(Arrays.asList(tab));
        }
        return set.stream().max(Comparator.naturalOrder()).get();
    }

    private void addColor() {
        Random random = new Random();
        colorList.add(new Color(random.nextInt(255),
                random.nextInt(255),
                random.nextInt(255))
        );
    }

    private void removeAllColors() {
        if(colorList!= null) colorList.clear();
    }

    void generateColors() {
        if(colorList == null) colorList = new ArrayList<>();
        for(int i=colorList.size(); i<findMax(); i++) {
            addColor();
        }
    }

    void addSeed(Point p) {
        if(this.states[p.x][p.y] == 0) {
            states[p.x][p.y] = this.findMax() + 1;
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

    private void resetColors() {
        removeAllColors();
        generateColors();
    }

    void createSubstructure(List<Integer> values) {
        int counter;
        for(int i=0; i<dim.height; i++) {
            for(int j=0; j<dim.width; j++) {
                counter = 0;
                for(int v=0; v<values.size(); v++) {
                    if(states[i][j] == values.get(v)) {
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

    Dimension getDim() {
        return dim;
    }

    Integer[][] getStates() {
        return states;
    }

    int getGrainId(int x, int y) {
        return states[x][y];
    }

    ArrayList<Color> getColorList() {
        return colorList;
    }

    void proceed() {
        proceeded = true;
    }

    boolean isProceeded() {
        return proceeded;
    }
}
