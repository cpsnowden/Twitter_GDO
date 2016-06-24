package com.cps15.service.DataService;

import com.cps15.api.data.Status;

/**
 * Created by ChrisSnowden on 24/06/2016.
 */
public interface IFilterManageMethods {

    void handleStatus(TwitterStreamFilter twitterStreamFilter, Status.STATUS status);

}
