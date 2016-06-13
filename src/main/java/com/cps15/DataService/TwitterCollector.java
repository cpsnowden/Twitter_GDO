package com.cps15.DataService;

import com.cps15.Database.DatabaseWriter;
import twitter4j.conf.ConfigurationBuilder;

import javax.print.DocFlavor;

/**
 * Twitter_GDO
 * Created by chris on 09/06/2016.
 */
public abstract class TwitterCollector {

    private String ConsumerKey;
    private String ConsumerSecret;
    private String AccessToken;
    private String AccessTokenSecret;
    protected DatabaseWriter dbw;

    public TwitterCollector(String ConsumerKey, String ConsumerSecret, String AccessToken, String AccessTokenSecret, String databaseName, String collectionName) {

        this.ConsumerKey = ConsumerKey;
        this.ConsumerSecret = ConsumerSecret;
        this.AccessToken = AccessToken;
        this.AccessTokenSecret = AccessTokenSecret;
        this.dbw = new DatabaseWriter(databaseName, collectionName, false);
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
}
