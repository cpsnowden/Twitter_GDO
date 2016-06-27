package com.cps15.service.DataService.TwitterStreams.Filters;


import twitter4j.Status;

import java.util.List;

/**
 * Created by ChrisSnowden on 23/06/2016.
 */
public interface IStatusFilter {

    boolean consumeStatus(Status status);
    public String[] getStreamingTerms();
    public List<String> getStreamingTermList();
}
