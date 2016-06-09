package com.cps15.DataService;


import com.cps15.Database.DatabaseWriter;
import twitter4j.*;

import java.util.List;
import java.util.logging.Logger;

/**
 * Twitter_GDO
 * Created by chris on 08/06/2016.
 */
public class TwitterStreamCollector extends TwitterCollector implements StreamCollector{

    private static final Logger logger = Logger.getLogger(TwitterStreamCollector.class.getName());

    private DatabaseWriter dbw;
    private TwitterStream twitterStream;

    private List<String> trackTerms;

    public TwitterStreamCollector(String databaseName, String collectionName, List<String> trackTerms, String[] auth){
        super(auth[0], auth[1], auth[2], auth[3], databaseName, collectionName);

        this.trackTerms = trackTerms;
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
                        logger.info("Failed");
                    } else {
                        logger.info("Inserted into database");
                    }
                } catch (IllegalStateException ex) {
                    ex.printStackTrace();
                    twitterStream.shutdown();
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

            }
        };
    }

    @Override
    public void startCollection() {

        twitterStream.filter(getStatusFilter(this.trackTerms.toArray(new String[this.trackTerms.size()])));

    }

    @Override
    public void stopCollection() {

        twitterStream.shutdown();

    }
}
