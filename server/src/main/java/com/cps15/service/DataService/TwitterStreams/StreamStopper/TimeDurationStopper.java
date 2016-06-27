package com.cps15.service.DataService.TwitterStreams.StreamStopper;

import org.joda.time.DateTime;

import java.util.logging.Logger;

/**
 * Twitter_GDO
 * Created by chris on 17/06/2016.
 */
public class TimeDurationStopper implements IStreamStopper {


    private org.joda.time.DateTime start;
    private org.joda.time.DateTime endPoint;
    private org.joda.time.Duration durationLimit;
    private static final Logger logger = Logger.getLogger(TimeDurationStopper.class.getName());

    public TimeDurationStopper() {};

    public TimeDurationStopper(org.joda.time.Duration durationLimit) {

        this.durationLimit = durationLimit;

    }

    @Override
    public boolean stop() {

        if(start == null || endPoint.isBeforeNow()){
            logger.info("Stopping after " + durationLimit + " at enpoint " + endPoint);
            return true;
        };
        return false;
    }

    @Override
    public void start() {

        this.start = DateTime.now();
        this.endPoint = this.start.plus(this.durationLimit);
    }

    @Override
    public String getDescription() {
        return toString();
    }

    @Override
    public String toString() {
        return "TimeDurationStopper{" +
                "start=" + start +
                ", endPoint=" + endPoint +
                ", durationLimit=" + durationLimit +
                '}';
    }
}