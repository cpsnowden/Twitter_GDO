package com.cps15.DataService;


import com.cps15.DataService.StreamStopper.IStreamStopper;
import twitter4j.*;

import java.util.List;
import java.util.logging.Logger;

/**
 * Twitter_GDO
 * Created by chris on 08/06/2016.
 */
public class TwitterStreamCollector extends TwitterCollector implements StreamCollector, Runnable{

    private static final Logger logger = Logger.getLogger(TwitterStreamCollector.class.getName());
    private TwitterStream twitterStream;
    private List<String> trackTerms;
    private IStreamStopper streamStopper;

    public TwitterStreamCollector(String databaseName, List<String> trackTerms, String[] auth, String description, IStreamStopper streamStopper){
        super(auth[0], auth[1], auth[2], auth[3], databaseName);
        this.trackTerms = trackTerms;
        this.streamStopper = streamStopper;
        super.registerCollectionExists(description, trackTerms);
        this.twitterStream = new TwitterStreamFactory(getBaseConfigurationBuilder().build()).getInstance();
        this.twitterStream.addListener(getStatusListener());

    }

    private FilterQuery getStatusFilter(String[] query){
        FilterQuery filterQuery = new FilterQuery();
        filterQuery.track(query);
        return  filterQuery;
    }

    private StatusListener getStatusListener(){

        return new StatusListener() {

            @Override
            public void onStatus(Status status) {

                logger.info(status.getCreatedAt() + " " + status.getUser().getScreenName() + status.getText().replace("\n",""));
                try {
                    String statusJson = TwitterObjectFactory.getRawJSON(status);
                    if(!dbw.insertJson(statusJson)){
                        logger.warning("Failed to enter tweet " + status.getId() + " into database");
                    }
                } catch (IllegalStateException ex) {
                    ex.printStackTrace();
                    reportError();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    reportError();
                }

                if(requestStop || streamStopper.stop()) {
                    stopCollection();
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
                reportError();
            }
        };
    }

    @Override
    public void startCollection() {

        super.registerCollectionStarted();
        streamStopper.start();
        twitterStream.filter(getStatusFilter(this.trackTerms.toArray(new String[this.trackTerms.size()])));

    }

    @Override
    public void stopCollection() {
        logger.info("Shutting collection down");
        registerCollectionFinished();
        twitterStream.shutdown();
        twitterStream.cleanUp();
        logger.info("Shutdown");
    }

    @Override
    public void reportError() {
        logger.severe("Error Shutting Down");
        registerCollectionError();
        twitterStream.shutdown();
    }

    @Override
    public void run() {
        this.startCollection();

    }


}
