package com.cps15;

import com.cps15.api.data.DataStream;
import com.cps15.api.data.DataStreamRequest;
import com.cps15.api.health.MongoHealthCheck;
import com.cps15.api.persistence.DataStreamDAO;
import com.cps15.api.persistence.DataStreamRequestDAO;
import com.cps15.api.persistence.MongoManaged;
import com.cps15.api.resources.DataStreamResource;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import org.mongojack.JacksonDBCollection;


/**
 * ExampleDrop
 * Created by chris on 18/06/2016.
 */
public class TwitterServerApp extends Application<TwitterServerConfiguration> {

    public static void main(String[] args) throws Exception {
        new TwitterServerApp().run(args);
    }

    @Override
    public void run(TwitterServerConfiguration twitterServerConfiguration, Environment environment) throws Exception {

        Mongo mongo = new MongoClient(twitterServerConfiguration.getMongohost(),
                twitterServerConfiguration.getMongoport());

        MongoManaged mongoManaged = new MongoManaged(mongo);
        environment.lifecycle().manage(mongoManaged);
        environment.getObjectMapper().disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        environment.healthChecks().register("MongoHealthCheck", new MongoHealthCheck(mongo));

        DB db = mongo.getDB(twitterServerConfiguration.getMongodb());
        JacksonDBCollection<DataStream,String> streamCollection = JacksonDBCollection.wrap(db.getCollection(twitterServerConfiguration.getStreamCollection()),DataStream.class,String.class);
        JacksonDBCollection<DataStreamRequest, String> requestCollection = JacksonDBCollection.wrap(db.getCollection(twitterServerConfiguration.getRequestCollection()),DataStreamRequest.class, String.class);


        environment.jersey().register(new DataStreamResource(new DataStreamDAO(streamCollection), new DataStreamRequestDAO(requestCollection)));

    }
}
