package com.cps15.service.DataService.StreamStopper;

/**
 * Twitter_GDO
 * Created by chris on 17/06/2016.
 */
public interface IStreamStopper {

    boolean stop();
    void start();
    String getDesciption();

}
