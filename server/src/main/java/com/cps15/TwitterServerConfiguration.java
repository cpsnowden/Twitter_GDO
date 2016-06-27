package com.cps15;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * ExampleDrop
 * Created by chris on 18/06/2016.
 */
public class TwitterServerConfiguration extends Configuration {

    @NotEmpty
    private String version;

    @NotEmpty
    private String mongohost;

    @NotNull
    private int mongoport;

    @NotEmpty
    private String mongodb;

    @NotEmpty
    private String requestCollection;

    @NotEmpty
    private String streamCollection;

    @NotEmpty
    private String analyticsCollection;

    @NotEmpty
    private String consumerKey;

    @NotEmpty
    private String consumerSecret;

    @NotEmpty
    private String accessKey;

    @NotEmpty
    private String accessSecret;

    @JsonProperty
    public String getMongodb() {
        return mongodb;
    }

    @JsonProperty
    public void setMongodb(String mongodb) {
        this.mongodb = mongodb;
    }

    @JsonProperty
    public int getMongoport() {
        return mongoport;
    }

    @JsonProperty
    public void setMongoport(int mongoport) {
        this.mongoport = mongoport;
    }

    @JsonProperty
    public String getMongohost() {
        return mongohost;
    }

    @JsonProperty
    public void setMongohost(String mongohost) {
        this.mongohost = mongohost;
    }

    @JsonProperty
    public String getVersion(){
        return version;
    }

    @JsonProperty
    public void setVersion(String version) {
        this.version = version;
    }

    @JsonProperty
    public String getRequestCollection() {
        return requestCollection;
    }

    @JsonProperty
    public void setRequestCollection(String requestCollection) {
        this.requestCollection = requestCollection;
    }

    @JsonProperty
    public String getStreamCollection() {
        return streamCollection;
    }

    @JsonProperty
    public void setStreamCollection(String streamCollection) {
        this.streamCollection = streamCollection;
    }

    @JsonProperty
    public String getAnalyticsCollection() {
        return analyticsCollection;
    }

    @JsonProperty
    public void setAnalyticsCollection(String analyticsCollection) {
        this.analyticsCollection = analyticsCollection;
    }

    @JsonProperty
    public String getConsumerKey() {
        return consumerKey;
    }

    @JsonProperty
    public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
    }

    @JsonProperty
    public String getConsumerSecret() {
        return consumerSecret;
    }

    @JsonProperty
    public void setConsumerSecret(String consumerSecret) {
        this.consumerSecret = consumerSecret;
    }

    @JsonProperty
    public String getAccessKey() {
        return accessKey;
    }

    @JsonProperty
    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    @JsonProperty
    public String getAccessSecret() {
        return accessSecret;
    }

    @JsonProperty
    public void setAccessSecret(String accessSecret) {
        this.accessSecret = accessSecret;
    }
}
