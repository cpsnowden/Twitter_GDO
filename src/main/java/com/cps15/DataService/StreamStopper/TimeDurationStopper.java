package com.cps15.DataService.StreamStopper;

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

    public TimeDurationStopper(Duration durationLimit) {
        this.durationLimit = durationLimit;
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

}