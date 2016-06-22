package com.cps15.service.DataService.StreamStopper;

import java.security.InvalidParameterException;
import java.time.Duration;

/**
 * Twitter_GDO
 * Created by chris on 20/06/2016.
 */
public class StreamStopperFactory {

    private enum StopperType{
        DURATION("Duration"),
        COUNTER("Counter");

        private final String text;
        private StopperType(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    private StopperType stopperType;
    private Long limit;

    public StreamStopperFactory(){}

    public StreamStopperFactory setStopperType(String type) throws InvalidParameterException {
        for(StopperType s: StopperType.values()) {
            if(type.equals(s.toString())) {
                stopperType = s;
                return this;
            }
        }

        throw new InvalidParameterException();
    }

    public StreamStopperFactory setLimit(Long limit) {
        this.limit = limit;
        return this;
    }

    public StopperType getStopperType() {
        return stopperType;
    }

    public Long getLimit() {
        return limit;
    }

    public IStreamStopper build() {
        switch(stopperType) {
            case DURATION:
                limit = limit==null?60*1000:limit;
                return new TimeDurationStopper(org.joda.time.Duration.standardMinutes(limit));
            case COUNTER:
                limit = limit==null?50:limit;
                return new CounterStopper(limit);
            default:
                return null;
        }
    }
}
