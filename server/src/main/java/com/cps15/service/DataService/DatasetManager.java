package com.cps15.service.DataService;

import com.cps15.api.data.DatasetInfo;
import com.cps15.api.data.DatasetRequest;
import com.cps15.api.data.Status;
import com.cps15.api.persistence.DatasetInfoDAO;
import com.cps15.api.persistence.DatasetRequestDAO;
import com.cps15.service.DataService.TwitterStreams.Twitter4JAuth;
import com.cps15.service.DataService.TwitterStreams.TwitterStreamManager;
import com.cps15.service.Database.DatabaseManager;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by ChrisSnowden on 27/06/2016.
 */
public class DatasetManager {

    private static final Logger logger = Logger.getLogger(DatasetManager.class.getName());
    private DatasetInfoDAO datasetInfoDAO;
    private DatasetRequestDAO datasetRequestDAO;
    private TwitterStreamManager twitterStreamManager;

    public DatasetManager(DatabaseManager databaseManager, DatasetInfoDAO datasetInfoDAO, DatasetRequestDAO datasetRequestDAO, Twitter4JAuth twitter4JAuth) {

        this.datasetInfoDAO = datasetInfoDAO;
        this.datasetRequestDAO = datasetRequestDAO;

        this.twitterStreamManager = new TwitterStreamManager(datasetInfoDAO, databaseManager, twitter4JAuth);

    }

    public DatasetInfo getDataset(String id) {

        return datasetInfoDAO.findByID(id);

    }

    public List<DatasetInfo> getDatasets() {

        return datasetInfoDAO.dataStreams();

    }

    public List<DatasetInfo> getDatasetsByStatus(List<Status.STATUS> status) {

        return datasetInfoDAO.getDataStreamsByStatus(status);

    }

    public boolean deleteDataset(String id) {

        DatasetInfo datasetInfo = datasetInfoDAO.findByID(id);
        switch (datasetInfo.getType()) {
            case TWITTER_STREAM:
                return twitterStreamManager.delete(datasetInfo);
            default:

        }

        return false;

    }

    public DatasetInfo addDataset(DatasetRequest datasetRequest) {

        datasetRequestDAO.addDataStreamRequest(datasetRequest);

        DatasetInfo datasetInfo;
        try {
            datasetInfo = new DatasetInfo(datasetRequest);
        } catch (InvalidParameterException ex) {
            return null;
        }

        datasetInfoDAO.addDataStream(datasetInfo);

        switch (datasetInfo.getType()) {
            case TWITTER_STREAM:
                twitterStreamManager.start(datasetInfo);
                break;
            default:
                throw new InvalidParameterException();
        }



        return datasetInfo;
    }

    public boolean toggleStatus(String id, Status status){

        DatasetInfo datasetInfo = datasetInfoDAO.findByID(id);
        System.out.println(datasetInfo.getType());
        if(null == datasetInfo) {
            return false;
        }

        switch (datasetInfo.getType()) {
            case TWITTER_STREAM:
                if(status.getStatus() == Status.STATUS.STOPPED) {
                    return twitterStreamManager.stop(datasetInfo);
                } else if(status.getStatus() == Status.STATUS.ORDERED) {
                    return twitterStreamManager.restart(datasetInfo);
                }
            break;
            default:
        }

        return false;
    }

    public Status getStatus(String id){

        DatasetInfo datasetInfo = datasetInfoDAO.findByID(id);
        if(null == datasetInfo) {
            return null;
        } else {
            return new Status(datasetInfo.getStatus());
        }

    }


}
