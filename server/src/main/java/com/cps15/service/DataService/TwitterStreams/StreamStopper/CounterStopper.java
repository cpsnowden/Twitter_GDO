package com.cps15.service.DataService.TwitterStreams.StreamStopper;

import java.util.logging.Logger;

/**
 * Twitter_GDO
 * Created by chris on 17/06/2016.
 */
public class CounterStopper implements IStreamStopper {

    private long counter;
    private long counterLimit;
    private static final Logger logger = Logger.getLogger(CounterStopper.class.getName());
    private String description;

    public CounterStopper() {};

    public CounterStopper(long counterLimit) {
        this.counterLimit = counterLimit;
        this.description = this.toString();
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
        counter = 0;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "CounterStopper{" +
                "counter=" + counter +
                ", counterLimit=" + counterLimit +
                '}';
    }
}