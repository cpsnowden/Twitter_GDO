package com.cps15.service.DataService;

import com.cps15.api.data.DatasetInfo;

/**
 * Created by ChrisSnowden on 27/06/2016.
 */
public interface IDataCollector {

    void start(DatasetInfo datasetInfo);

    boolean stop(DatasetInfo datasetInfo);

    boolean restart(DatasetInfo datasetInfo);

    boolean delete(DatasetInfo datasetInfo);


}
