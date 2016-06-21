package com.cps15.service.DataService.StreamStopper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;

import java.time.Duration;
import java.time.Instant;
import java.util.logging.Logger;

/**
 * Twitter_GDO
 * Created by chris on 17/06/2016.
 */
public class TimeDurationStopper implements IStreamStopper {

    @JsonIgnore
    private Instant start;

    @JsonIgnore
    private Duration durationLimit;
    private static final Logger logger = Logger.getLogger(TimeDurationStopper.class.getName());


    private String description;
    private DateTime startv2;


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
        this.startv2 = DateTime.now();
        this.description = toString();
    }

    @Override
    public String getDescription() {
        return toString();
    }



    @Override
    public String toString() {
        return "TimeDurationStopper{" +
                "start=" + start +
                ", durationLimit=" + durationLimit +
                ", startv2 " + startv2 +
                '}';
    }
}