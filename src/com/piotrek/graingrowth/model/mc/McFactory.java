package com.piotrek.graingrowth.model.mc;

import com.piotrek.graingrowth.type.McNeighbourhood;

/**
 * Util used for creating Mc2d objects.
 * Created by Piotrek on 29.11.2016.
 * @author Piotrek
 */
public class McFactory {
    public static Mc2d getMc2dInstance(Integer[][] states, boolean periodical, McNeighbourhood neighbourhood) {
        Mc2d mc2d;
        switch(neighbourhood) {
            case VON_NEUMANN:
                mc2d = new VonNeumann(states, periodical);
                break;
            case MOORE:
                mc2d = new Moore(states, periodical);
                break;
            default:
                mc2d = null;
        }
        return mc2d;
    }
}
