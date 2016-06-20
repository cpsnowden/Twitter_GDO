package com.cps15.service.DataService.StreamStopper;

import java.util.logging.Logger;

/**
 * Twitter_GDO
 * Created by chris on 17/06/2016.
 */
public class CounterStopper implements IStreamStopper {

    private long counter = 0;
    private long counterLimit;
    private static final Logger logger = Logger.getLogger(CounterStopper.class.getName());

    public CounterStopper(long counterLimit) {
        this.counterLimit = counterLimit;
    }

    @Override
    public boolean stop() {
        if(++counter >= counterLimit){
            logger.info("Stopping after " + counter  + " cycles");
            return true;
        };

        return false;
    }

    @Override
    public void start() {
    }

    @Override
    public String getDesciption() {
        return "Count<"+counterLimit;
    }

}