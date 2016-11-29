package com.piotrek.graingrowth.model.ca;

/**
 * Interface used as adapter.
 * Created by Piotrek on 25.10.2016.
 * @author Piotrek
 */
interface Rules {
    int rule(Integer[][] neighbours);
}
