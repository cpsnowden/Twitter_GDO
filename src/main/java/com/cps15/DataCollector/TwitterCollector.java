package com.cps15.DataCollector;


import com.cps15.DatabaseWriter;
import twitter4j.*;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import java.util.logging.Logger;

/**
 * Twitter_GDO
 * Created by chris on 08/06/2016.
 */
public class TwitterCollector {

    private static final String CONSUMER_SECRET = "zyc5BC1voOMPmJs3G7o1hHv4jI8ii6WGctf4yzdg43xQzxPH0U";
    private static final String CONSUMER_KEY = "uxMqOuKT66k0ihHJ9aQRoCq2x";
    private static final String ACCESS_TOKEN = "529742116-XJ1FPnd2ANhx8UDsv3espl1OAlufhzQT5G6yQDxA";
    private static final String ACCESS_TOKEN_SECRET = "sHoqlokqwo0OPz2RRBjxBL3E8CKszf9dibi8ao4xLykMS";

    private static final Logger logger = Logger.getLogger(TwitterCollector.class.getName());

    private DatabaseWriter dbw;
    private Twitter twitter;
    private TwitterStream twitterStream;

    public static void main(String[] args) {

        TwitterCollector twitterCollector = new TwitterCollector("Twitter");
        twitterCollector.runTracker(new String[] {"Trump2016","trump","hilary2016","Clinton","hiliaryclinton"}, "Trump_Clinton");

    }

    public TwitterCollector(String databaseName){

        twitterStream = getTwitterStream();
        twitterStream.addListener(getStatusListener());
        dbw = new DatabaseWriter(databaseName);

    }

    private void runTracker(String[] query, String collectionName){

        dbw.setCollection(collectionName);
        twitterStream.filter(getStatusFilter(query));

    }

    private FilterQuery getStatusFilter(String[] query){
        FilterQuery filterQuery = new FilterQuery();
        filterQuery.track(query);
        return  filterQuery;
    }

    private Configuration getConfiguration(){

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setOAuthConsumerKey(CONSUMER_KEY);
        cb.setOAuthConsumerSecret(CONSUMER_SECRET);
        cb.setOAuthAccessToken(ACCESS_TOKEN);
        cb.setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET);
        cb.setJSONStoreEnabled(true);

        return cb.build();
    }

    private TwitterStream getTwitterStream(){

        return new TwitterStreamFactory(getConfiguration()).getInstance();
    }

    private Twitter getTwitterRest(){

        return new TwitterFactory(getConfiguration()).getInstance();

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





    }
