package com.cps15.DataService;

import com.cps15.Database.DatabaseWriter;
import com.mongodb.BasicDBObject;
import org.bson.Document;
import twitter4j.conf.ConfigurationBuilder;

import javax.print.DocFlavor;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

/**
 * Twitter_GDO
 * Created by chris on 09/06/2016.
 */
public abstract class TwitterCollector {

    private static final Logger logger = Logger.getLogger(TwitterCollector.class.getName());
    private String ConsumerKey;
    private String ConsumerSecret;
    private String AccessToken;
    private String AccessTokenSecret;
    private String uniqueID;
    protected DatabaseWriter dbw;
    private STATUS status;
    protected enum STATUS{READY, RUNNING, FINISHED, ERROR};

    public void requestStop() {
        this.requestStop = true;
    }

    protected boolean requestStop;

    protected STATUS getStatus() {
        return status;
    }


    public TwitterCollector(String ConsumerKey, String ConsumerSecret, String AccessToken, String AccessTokenSecret, String databaseName) {

        this.ConsumerKey = ConsumerKey;
        this.ConsumerSecret = ConsumerSecret;
        this.AccessToken = AccessToken;
        this.AccessTokenSecret = AccessTokenSecret;
        this.uniqueID = "TWIT_"+UUID.randomUUID().toString();
        this.dbw = new DatabaseWriter(databaseName, this.uniqueID, false);
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

    protected String registerCollectionExists(String description, List<String> tags) {

        String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        String user = System.getProperty("user.name");

        Document document = new Document("desciption",description)
                .append("collection", uniqueID)
                .append("start", timestamp)
                .append("end", null)
                .append("status", STATUS.READY.toString())
                .append("user", user)
                .append("tags", tags);

        this.dbw.insertDocument(document, "Admin");
        this.status = STATUS.READY;
        return this.uniqueID;

    }

    protected void registerCollectionStarted() {
        this.dbw.getCollection("Admin").updateOne(eq("collection", this.uniqueID), set("status", STATUS.RUNNING.toString()));
        this.status = STATUS.RUNNING;
    }

    protected void registerCollectionFinished() {

        String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        BasicDBObject updateFields = new BasicDBObject();
        updateFields.append("status", STATUS.FINISHED.toString());
        updateFields.append("end", timestamp);
        BasicDBObject setQuery = new BasicDBObject();
        setQuery.append("$set", updateFields);
        this.dbw.getCollection("Admin").updateOne(eq("collection", this.uniqueID), setQuery);
        this.status = STATUS.FINISHED;
        logger.info("Collection registered as finished");
    }

    protected void registerCollectionError() {
        this.dbw.getCollection("Admin").updateOne(eq("collection", this.uniqueID), set("status", STATUS.ERROR.toString()));
        this.status = STATUS.ERROR;
    }
}
