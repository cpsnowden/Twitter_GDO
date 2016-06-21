package com.cps15.service.DataService;


import com.cps15.api.data.DataStream;
import com.cps15.api.persistence.DataStreamDAO;
import com.cps15.service.DataService.StreamStopper.IStreamStopper;
import com.cps15.service.DataService.StreamStopper.StreamStopperFactory;
import com.cps15.service.Database.StatusDAO;
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
    private StatusDAO statusDAO;

    public TwitterStreamCollector(String[] auth, DataStream dataStream, DataStreamDAO dataStreamDAO){
        super(auth, dataStream, dataStreamDAO);

        this.streamStopper = new StreamStopperFactory()
                .setStopperType(dataStream.getLimitType())
                .setLimit(dataStream.getLimit())
                .build();
        logger.info(dataStream.getDescription() + streamStopper.toString());
        this.statusDAO = new StatusDAO(dbm.getDb().getCollection(dataStream.getId()));
        this.twitterStream = new TwitterStreamFactory(getBaseConfigurationBuilder().build()).getInstance();
        this.twitterStream.addListener(getStatusListener());

    }

    private StatusListener getStatusListener(){

        return new StatusListener() {

            @Override
            public void onStatus(Status status) {


                logger.fine(dataStream.getDescription() + status.getCreatedAt() + " " + status.getUser().getScreenName() + status.getText().replace("\n",""));
                statusDAO.insert(status);

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
