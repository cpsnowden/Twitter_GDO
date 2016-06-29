package com.cps15.service.DataService.TwitterStreams;

import com.cps15.api.data.DatasetInfo;
import com.cps15.service.DataService.TwitterStreams.Filters.HashtagFilter;
import com.cps15.service.DataService.TwitterStreams.Filters.IStatusFilter;
import com.cps15.service.DataService.TwitterStreams.StreamStopper.IStreamStopper;
import com.cps15.service.DataService.TwitterStreams.StreamStopper.StreamStopperFactory;
import com.cps15.service.Database.StatusDAO;
import com.mongodb.DBCollection;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by ChrisSnowden on 23/06/2016.
 */
public class TwitterStreamFilter implements Runnable {

    private static final Logger logger = Logger.getLogger(TwitterStreamFilter.class.getName());
    private List<String> streamingTerms;
    private StatusListener statusListener;
    private IStatusFilter statusFilter;
    private IStreamStopper streamStopper;
    private final Object lock = new Object();
    private StatusDAO statusDAO;
    private DatasetInfo datasetInfo;
    private IFilterManageMethods manageMethods;

    public TwitterStreamFilter(DatasetInfo datasetInfo, DBCollection collection, IFilterManageMethods manageMethods) {

        this.manageMethods = manageMethods;
        this.datasetInfo = datasetInfo;
        this.statusFilter = new HashtagFilter(datasetInfo.getTags());
        this.streamingTerms = statusFilter.getStreamingTermList();
        this.streamStopper = new StreamStopperFactory()
                .setStopperType(datasetInfo.getLimitType())
                .setLimit(datasetInfo.getLimit())
                .build();
        this.statusDAO = new StatusDAO(collection);


        this.statusListener = new StatusListener() {
            @Override
            public void onStatus(Status status) {

                if(statusFilter.consumeStatus(status)) {
                    logger.fine(datasetInfo.getDescription() + " " + status.getText().replace("\n",""));
                    statusDAO.insert(status);

                    if(streamStopper.stop()) {
                        datasetInfo.finished();
                        synchronized (lock) {
                            lock.notify();
                        }
                    }
                }

            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {

            }

            @Override
            public void onTrackLimitationNotice(int i) {

            }

            @Override
            public void onScrubGeo(long l, long l1) {

            }

            @Override
            public void onStallWarning(StallWarning stallWarning) {

            }

            @Override
            public void onException(Exception e) {

                logger.info(e.getMessage());
                e.printStackTrace();
                datasetInfo.error();
                synchronized (lock){
                    lock.notify();
                }

            }
        };
    }

    public List<String> getStreamingTerms() {

        return streamingTerms;

    }

    public StatusListener getStatusListener() {
        return statusListener;
    }

    public DatasetInfo getDatasetInfo() {
        return datasetInfo;
    }

    public void finish() {
        synchronized (lock) {
            lock.notify();
        }
    }

    @Override
    public void run() {

        streamStopper.start();

        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        manageMethods.handleStatus(this, this.datasetInfo.getStatus());

    }

    public StatusDAO getStatusDAO() {
        return statusDAO;
    }
}
