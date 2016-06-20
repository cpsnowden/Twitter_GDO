package com.cps15.service.DataService;

import com.cps15.api.data.DataStream;
import com.cps15.api.data.IDataCollection;
import com.cps15.api.data.Status;
import com.cps15.api.persistence.DataStreamDAO;
import com.cps15.service.Database.DatabaseWriter;

import org.joda.time.DateTime;
import twitter4j.conf.ConfigurationBuilder;

import java.util.Date;
import java.util.logging.Logger;

/**
 * Twitter_GDO
 * Created by chris on 09/06/2016.
 */
public abstract class TwitterCollector implements IDataCollector {

    private static final Logger logger = Logger.getLogger(TwitterCollector.class.getName());

    private String ConsumerKey;
    private String ConsumerSecret;
    private String AccessToken;
    private String AccessTokenSecret;

    private DataStreamDAO dataStreamDAO;

    protected DataStream dataStream;
    protected boolean requestStop;
    protected DatabaseWriter dbw;

    public TwitterCollector(String[] auth, DataStream dataStream, DataStreamDAO dataStreamDAO) {

        this.ConsumerKey = auth[0];
        this.ConsumerSecret = auth[1];
        this.AccessToken = auth[2];
        this.AccessTokenSecret = auth[3];
        this.dataStream = dataStream;

        this.dataStreamDAO = dataStreamDAO;
        this.dbw = new DatabaseWriter("Twitter", dataStream.getId(), false);
    }

    protected ConfigurationBuilder getBaseConfigurationBuilder() {

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setOAuthConsumerKey(ConsumerKey);
        cb.setOAuthConsumerSecret(ConsumerSecret);
        cb.setOAuthAccessToken(AccessToken);
        cb.setOAuthAccessTokenSecret(AccessTokenSecret);
        cb.setJSONStoreEnabled(true);

        return cb;
    }

    protected void registerStarted() {

        dataStream.setStatus(Status.STATUS.RUNNING);
        dataStreamDAO.update(dataStream);
        logger.info("Collection " + dataStream.getDescription() + " registered as started");

    }

    protected void registerFinished() {

        dataStream.setEndDate(new Date());
        dataStream.setStatus(Status.STATUS.FINISHED);
        dataStreamDAO.update(dataStream);
        logger.info("Collection " + dataStream.getDescription() + " registered as finished");
    }

    protected void registerError() {

        dataStream.setStatus(Status.STATUS.ERROR);
        dataStreamDAO.update(dataStream);
        logger.severe("Collection " + dataStream.getDescription() + " registered as ERROR");

    }

    public void requestStop() {
        this.requestStop = true;
    }

    public void reset() {this.requestStop = false;}

    public String getId() {
        return this.dataStream.getId();
    }

}