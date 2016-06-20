package com.cps15.service.DataService.StreamStopper;

import java.time.Duration;
import java.time.Instant;
import java.util.logging.Logger;

/**
 * Twitter_GDO
 * Created by chris on 17/06/2016.
 */
public class TimeDurationStopper implements IStreamStopper {

    private Instant start;
    private Duration durationLimit;
    private static final Logger logger = Logger.getLogger(TimeDurationStopper.class.getName());
    private String description;

    public TimeDurationStopper() {};

    public TimeDurationStopper(Duration durationLimit) {

        this.durationLimit = durationLimit;
        this.description = this.toString();
    }

    @Override
    public boolean stop() {
        Duration duration = Duration.between(this.start, Instant.now());
        if(start == null || duration.compareTo(durationLimit) > 0){
            logger.info("Stopping after " + duration.toString());
            return true;
        };
        return false;
    }

    @Override
    public void start() {

        this.start = Instant.now();

    }

    @Override
    public String getDescription() {
        return description;
    }


    @Override
    public String toString() {
        return "TimeDurationStopper{" +
                "start=" + start +
                ", durationLimit=" + durationLimit +
                '}';
    }
}