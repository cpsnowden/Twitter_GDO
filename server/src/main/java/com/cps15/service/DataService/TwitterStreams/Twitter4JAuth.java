package com.cps15.service.DataService.TwitterStreams;

/**
 * Created by ChrisSnowden on 27/06/2016.
 */
public class Twitter4JAuth {

    private final String consumerKey;
    private final String consumerSecret;
    private final String accessKey;
    private final String accessSecret;

    public Twitter4JAuth(String consumerKey, String consumerSecret, String accessKey, String accessSecret) {
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.accessKey = accessKey;
        this.accessSecret = accessSecret;
    }

    public String getConsumerKey() {
        return consumerKey;
    }

    public String getConsumerSecret() {
        return consumerSecret;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public String getAccessSecret() {
        return accessSecret;
    }
}
