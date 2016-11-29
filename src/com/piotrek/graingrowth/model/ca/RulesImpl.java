package com.piotrek.graingrowth.model.ca;

import java.util.*;

/**
 * Shape control rules implementation.
 * Created by Piotrek on 25.10.2016.
 * @author Piotrek
 */
class RulesImpl {
    static final int DEFAULT_PROBABILITY = 10;
    private static final int NO_RESULT = 0;
    private int probability;
    private Rules[] rules;
    private Random random;
    private List<Integer> list;
    private Set<Integer> set;

    RulesImpl() {
        rules = new Rules[] {
                this::rule1,
                this::rule2,
                this::rule3,
                this::rule4
        };
        probability = DEFAULT_PROBABILITY;
        random = new Random();
        list = new ArrayList<>();
        set = new HashSet<>();
    }

    RulesImpl(int probability) {
        this();
        this.probability = probability;
    }

    private int rule1(Integer[][] neighbours) {
        for(Integer[] tab: neighbours) {
            list.addAll(Arrays.asList(tab));
        }
        set.addAll(list);
        set.remove(0);
        for(int val: set) {
            if(Collections.frequency(list, val) > 4) return val;
        }
        return NO_RESULT;
    }

    private int rule2(Integer[][] neighbours) {
        return rule2And3(neighbours, VonNeumann.geVonNeumannClose());
    }

    private int rule3(Integer[][] neighbours) {
        return rule2And3(neighbours, VonNeumann.getVonNeumannFurther());
    }

    private int rule2And3(Integer[][] neighbours, int[][] tab) {
        for(int i=0; i<3; i++) {
            for(int j=0; j<3; j++) {
                list.add(neighbours[i][j]*tab[i][j]);
            }
        }
        set.addAll(list);
        set.remove(0);
        for(int val: set) {
            if(Collections.frequency(list, val) > 2) return val;
        }
        return NO_RESULT;
    }

    private int rule4(Integer[][] neighbours) {
        if(random.nextInt(100) < probability) {
            for(Integer[] tab : neighbours) {
                list.addAll(Arrays.asList(tab));
            }
            set.addAll(list);
            set.remove(0);
            int max = NO_RESULT;
            for(int val: set) {
                if(Collections.frequency(list, val) > max)
                    max = val;
            }
            return max;
        }
        return NO_RESULT;
    }

    private void reset() {
        set.clear();
        list.clear();
    }

    int rule(Integer[][] neighbours) {
        int val;
        for(int i=0; i<4; i++) {
            reset();
            val = rules[i].rule(neighbours);
            if(val != NO_RESULT) return val;
        }
        return NO_RESULT;
    }
}
