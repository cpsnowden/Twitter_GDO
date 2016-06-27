package com.cps15.service.AnalyticsService.Graph;

import com.mongodb.DBObject;
import twitter4j.Status;

import java.util.List;
import java.util.stream.Stream;

/**
 * Twitter_GDO
 * Created by chris on 09/06/2016.
 */
public interface IStatusGraphFunction {

    GraphCreator getGraphCreator(Stream<Status> documentStream);
    List<String> getKeys();
    DBObject getQuery();

}
