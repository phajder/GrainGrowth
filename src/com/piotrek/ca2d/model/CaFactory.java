package com.piotrek.ca2d.model;

import com.piotrek.ca2d.type.NeighType;

import javax.swing.*;
import java.awt.*;

/**
 * Created by pioot on 18.10.2016.
 */
public class CaFactory {
    public static Ca2d getCa2dInstance(Dimension size, boolean periodic, Integer[][] states, NeighType type) {
        Ca2d ca;
        switch(type) {
            case VON_NEUMANN:
                ca = new VonNeumann(size, periodic, states);
                break;
            case MOORE:
                ca = new Moore(size, periodic, states);
                break;
            case HEXAGONAL_RANDOM:
                ca = new HexagonalRandom(size, periodic, states);
                break;
            case PENTAGONAL_RANDOM:
                ca = new PentagonalRandom(size, periodic, states);
                break;
            case SHAPE_CONTROL:
                ca = createShapeControl(size, periodic, states);
                break;
            default:
                ca = null;
        }
        return ca;
    }

    private static final Ca2d createShapeControl(Dimension size, boolean periodic, Integer[][] states) {
        String txt = JOptionPane.showInputDialog(null,
                "Probability value:",
                "Question",
                JOptionPane.QUESTION_MESSAGE);
        try {
            int val = Integer.valueOf(txt);
            if(val > 0 && val < 101) {
                return new ShapeControl(size, periodic, states, Integer.valueOf(val));
            } else {
                JOptionPane.showMessageDialog(
                        null,
                        "Probability out of range. Assuming default probability value " + RulesImpl.DEFAULT_PROBABILITY,
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch(NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Invalid number format. Assuming default probability value: " + RulesImpl.DEFAULT_PROBABILITY,
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        return new ShapeControl(size, periodic, states);
    }
}
