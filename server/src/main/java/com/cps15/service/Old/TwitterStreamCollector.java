//package com.cps15.service.DataService;
//
//
//import com.cps15.api.data.DatasetInfo;
//import com.cps15.api.persistence.DatasetInfoDAO;
//import com.cps15.service.DataService.TwitterStreams.StreamStopper.IStreamStopper;
//import com.cps15.service.DataService.TwitterStreams.StreamStopper.StreamStopperFactory;
//import com.cps15.service.Database.StatusDAO;
//import twitter4j.*;
//
//import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
///**
// * Twitter_GDO
// * Created by chris on 08/06/2016.
// */
//public class TwitterStreamCollector extends TwitterCollector implements Runnable{
//
//    private static final Logger logger = Logger.getLogger(TwitterStreamCollector.class.getName());
//    private TwitterStream twitterStream;
//    private IStreamStopper streamStopper;
//    private StatusDAO statusDAO;
//
//    public TwitterStreamCollector(String[] auth, DatasetInfo dataFilter, DatasetInfoDAO dataFilterDAO){
//        super(auth, dataFilter, dataFilterDAO);
//
//        this.streamStopper = new StreamStopperFactory()
//                .setStopperType(dataFilter.getLimitType())
//                .setLimit(dataFilter.getLimit())
//                .build();
//        this.statusDAO = new StatusDAO(dbm.getDb().getCollection(dataFilter.getId()));
//        this.twitterStream = new TwitterStreamFactory(getBaseConfigurationBuilder().build()).getInstance();
//        this.twitterStream.addListener(getStatusListener());
//
//    }
//
//    private StatusListener getStatusListener(){
//
//        return new StatusListener() {
//
//            @Override
//            public void onStatus(Status status) {
//
//
//                logger.fine(dataFilter.getDescription() + " " + status.getUser().getScreenName() + status.getText().replace("\n",""));
//                statusDAO.insert(status);
//
//                if(requestStop || streamStopper.stop()) {
//                    stopCollection();
//                }
//
//            }
//
//            @Override
//            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
//
//            }
//
//            @Override
//            public void onTrackLimitationNotice(int i) {
//
//            }
//
//            @Override
//            public void onScrubGeo(long l, long l1) {
//
//            }
//
//            @Override
//            public void onStallWarning(StallWarning stallWarning) {
//                logger.log(Level.SEVERE, "Ex", stallWarning);
//            }
//
//            @Override
//            public void onException(Exception e) {
//
//                logger.log(Level.SEVERE, "Ex", e);
//                reportError();
//            }
//        };
//    }
//
//    private void startCollection() {
//        logger.info("Stating collection " + dataFilter.toString());
//        registerStarted();
//        streamStopper.start();
//        logger.info(dataFilter.getDescription() + " limiter" + streamStopper.toString());
//        List<String> tags = dataFilter.getTags();
//        twitterStream.filter(new FilterQuery().track(tags.toArray(new String[tags.size()])));
//
//    }
//
//    private void stopCollection() {
//
//        registerFinished();
//        twitterStream.shutdown();
//        twitterStream.cleanUp();
//
//    }
//
//    private void reportError() {
//
//        registerError();
//        twitterStream.shutdown();
//    }
//
//    @Override
//    public void run() {
//        startCollection();
//    }
//
//}
