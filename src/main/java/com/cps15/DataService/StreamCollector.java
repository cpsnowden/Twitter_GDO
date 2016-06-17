package com.cps15.DataService;

/**
 * Twitter_GDO
 * Created by chris on 08/06/2016.
 */
public interface StreamCollector {

    void startCollection();
    void stopCollection();
    void reportError();

}
