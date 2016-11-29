package com.piotrek.graingrowth.model.ca;

import com.piotrek.graingrowth.type.CaNeighbourhood;

import javax.swing.*;

/**
 * Factory used for creating graingrowth object.
 * Created by Piotrek on 18.10.2016.
 * @author Piotrek
 */
public class CaFactory {
    public static Ca2d getCa2dInstance(boolean periodic, Integer[][] states, CaNeighbourhood type) {
        Ca2d ca;
        switch(type) {
            case VON_NEUMANN:
                ca = new VonNeumann(periodic, states);
                break;
            case MOORE:
                ca = new Moore(periodic, states);
                break;
            case HEXAGONAL_RANDOM:
                ca = new HexagonalRandom(periodic, states);
                break;
            case PENTAGONAL_RANDOM:
                ca = new PentagonalRandom(periodic, states);
                break;
            case SHAPE_CONTROL:
                ca = createShapeControl(periodic, states);
                break;
            default:
                ca = null;
        }
        return ca;
    }

    private static Ca2d createShapeControl(boolean periodic, Integer[][] states) {
        String txt = JOptionPane.showInputDialog(null,
                "Probability value:",
                "Question",
                JOptionPane.QUESTION_MESSAGE);
        try {
            int val = Integer.valueOf(txt);
            if(val > 0 && val < 101) {
                return new ShapeControl(periodic, states, val);
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

        return new ShapeControl(periodic, states);
    }
}
