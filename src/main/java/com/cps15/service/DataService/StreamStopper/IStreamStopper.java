package com.cps15.service.DataService.StreamStopper;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Twitter_GDO
 * Created by chris on 17/06/2016.
 */
@JsonDeserialize(as=TimeDurationStopper.class)
public interface IStreamStopper {

    boolean stop();
    void start();
    String toString();
    String getDescription();

}
