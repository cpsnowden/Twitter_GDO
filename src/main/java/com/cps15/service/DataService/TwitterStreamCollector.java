package com.cps15.service.DataService;


import com.cps15.api.data.DataStream;
import com.cps15.api.persistence.DataStreamDAO;
import com.cps15.service.DataService.StreamStopper.IStreamStopper;
import com.cps15.service.DataService.StreamStopper.StreamStopperFactory;
import org.bson.Document;
import org.mongojack.JacksonDBCollection;
import twitter4j.*;

import java.util.List;
import java.util.logging.Logger;

/**
 * Twitter_GDO
 * Created by chris on 08/06/2016.
 */
public class TwitterStreamCollector extends TwitterCollector implements Runnable{

    private static final Logger logger = Logger.getLogger(TwitterStreamCollector.class.getName());
    private TwitterStream twitterStream;
    private IStreamStopper streamStopper;

    JacksonDBCollection<Status,String> trial;

    public TwitterStreamCollector(String[] auth, DataStream dataStream, DataStreamDAO dataStreamDAO){
        super(auth, dataStream, dataStreamDAO);

        this.streamStopper = new StreamStopperFactory()
                .setStopperType(dataStream.getLimitType())
                .setLimit(dataStream.getLimit())
                .build();

        this.twitterStream = new TwitterStreamFactory(getBaseConfigurationBuilder().build()).getInstance();
        this.twitterStream.addListener(getStatusListener());

        trial =  JacksonDBCollection.wrap(this.dbw.getDBCollection(),Status.class,String.class);

    }

    private StatusListener getStatusListener(){

        return new StatusListener() {

            @Override
            public void onStatus(Status status) {

                trial.insert(status);
                logger.info(status.getCreatedAt() + " " + status.getUser().getScreenName() + status.getText().replace("\n",""));
//                try {
//                    String statusJson = TwitterObjectFactory.getRawJSON(status);
//                    if(!dbw.insertJson(statusJson)){
//                        logger.warning("Failed to enter tweet " + status.getId() + " into database");
//                    }
//                } catch (IllegalStateException ex) {
//                    ex.printStackTrace();
//                    reportError();
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                    reportError();
//                }

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

                e.printStackTrace();
                reportError();
            }
        };
    }

    private void startCollection() {
        logger.info("Stating collection " + dataStream.toString());
        registerStarted();
        streamStopper.start();
        List<String> tags = dataStream.getTags();
        twitterStream.filter(new FilterQuery().track(tags.toArray(new String[tags.size()])));

    }

    private void stopCollection() {

        registerFinished();
        twitterStream.shutdown();
        twitterStream.cleanUp();

    }

    private void reportError() {

        registerError();
        twitterStream.shutdown();
    }

    @Override
    public void run() {
        startCollection();
    }

}
