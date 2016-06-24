//package com.cps15.service.DataService;
//
//import com.cps15.api.data.DataFilter;
//import com.cps15.api.data.Status;
//import com.cps15.api.persistence.DataFilterDAO;
//import com.cps15.service.Database.DatabaseManager;
//import org.joda.time.DateTime;
//import twitter4j.conf.ConfigurationBuilder;
//
//import java.util.logging.Logger;
//
///**
// * Twitter_GDO
// * Created by chris on 09/06/2016.
// */
//public abstract class TwitterCollector implements IDataCollector {
//
//    private static final Logger logger = Logger.getLogger(TwitterCollector.class.getName());
//
//    private String ConsumerKey;
//    private String ConsumerSecret;
//    private String AccessToken;
//    private String AccessTokenSecret;
//
//    private DataFilterDAO dataFilterDAO;
//
//    protected DataFilter dataFilter;
//    protected boolean requestStop;
//    protected DatabaseManager dbm;
//
//    public TwitterCollector(String[] auth, DataFilter dataFilter, DataFilterDAO dataFilterDAO) {
//
//        this.ConsumerKey = auth[0];
//        this.ConsumerSecret = auth[1];
//        this.AccessToken = auth[2];
//        this.AccessTokenSecret = auth[3];
//        this.dataFilter = dataFilter;
//
//        this.dataFilterDAO = dataFilterDAO;
//        this.dbm = new DatabaseManager("TwitterDataCollections");
//
//    }
//
//    protected ConfigurationBuilder getBaseConfigurationBuilder() {
//
//        ConfigurationBuilder cb = new ConfigurationBuilder();
//        cb.setOAuthConsumerKey(ConsumerKey);
//        cb.setOAuthConsumerSecret(ConsumerSecret);
//        cb.setOAuthAccessToken(AccessToken);
//        cb.setOAuthAccessTokenSecret(AccessTokenSecret);
//        cb.setJSONStoreEnabled(true);
//
//        return cb;
//    }
//
//    protected void registerStarted() {
//
//        dataFilter.setStatus(Status.STATUS.RUNNING);
//        dataFilterDAO.update(dataFilter);
//        logger.info("Collection " + dataFilter.getDescription() + " registered as started");
//
//    }
//
//    protected void registerFinished() {
//
//        dataFilter.setEndDate(DateTime.now());
//        dataFilter.setStatus(Status.STATUS.FINISHED);
//        dataFilterDAO.update(dataFilter);
//        logger.info("Collection " + dataFilter.getDescription() + " registered as finished");
//    }
//
//    protected void registerError() {
//
//        dataFilter.setStatus(Status.STATUS.ERROR);
//        dataFilterDAO.update(dataFilter);
//        logger.severe("Collection " + dataFilter.getDescription() + " registered as ERROR");
//
//    }
//
//    public void requestStop() {
//        this.requestStop = true;
//    }
//
//    public void reset() {this.requestStop = false;}
//
//    public String getId() {
//        return this.dataFilter.getId();
//    }
//
//}