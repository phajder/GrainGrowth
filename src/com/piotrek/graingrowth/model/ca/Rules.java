package com.piotrek.graingrowth.model.ca;

/**
 * Interface used as adapter.
 * Created by Piotr on 25.10.2016.
 * @author Piotr Hajder
 */
interface Rules {
    int rule(Integer[][] neighbours);
}
