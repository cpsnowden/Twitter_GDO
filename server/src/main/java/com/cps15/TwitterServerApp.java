package com.cps15;

import com.cps15.api.auth.Authenticator;
import com.cps15.api.auth.Authorizer;
import com.cps15.api.data.Analytics;
import com.cps15.api.data.DatasetInfo;
import com.cps15.api.data.DatasetRequest;
import com.cps15.api.data.User;
import com.cps15.api.health.MongoHealthCheck;
import com.cps15.api.persistence.AnalyticsDAO;
import com.cps15.api.persistence.DatasetInfoDAO;
import com.cps15.api.persistence.DatasetRequestDAO;
import com.cps15.api.persistence.MongoManaged;
import com.cps15.api.resources.DatasetResource;
import com.cps15.service.AnalyticsService.AnalyticsManager;
import com.cps15.service.DataService.DatasetManager;
import com.cps15.service.DataService.TwitterStreams.Twitter4JAuth;
import com.cps15.service.Database.DatabaseManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.mongojack.JacksonDBCollection;
import org.mongojack.internal.MongoJackModule;


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


        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        MongoJackModule.configure(mapper);
        Mongo mongo = new MongoClient(twitterServerConfiguration.getMongohost(),
                twitterServerConfiguration.getMongoport());

        MongoManaged mongoManaged = new MongoManaged(mongo);
        environment.lifecycle().manage(mongoManaged);
        environment.getObjectMapper().disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        environment.healthChecks().register("MongoHealthCheck", new MongoHealthCheck(mongo));

        environment.jersey().register(new AuthDynamicFeature(
                new BasicCredentialAuthFilter.Builder<User>()
                .setAuthenticator(new Authenticator())
                .setAuthorizer(new Authorizer())
                .setRealm("Secure")
                .buildAuthFilter()));

        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
        environment.jersey().register(RolesAllowedDynamicFeature.class);

        DB db = mongo.getDB(twitterServerConfiguration.getMongodb());
        JacksonDBCollection<DatasetInfo,String> streamCollection = JacksonDBCollection.wrap(db.getCollection(twitterServerConfiguration.getStreamCollection()),DatasetInfo.class,String.class, mapper);
        JacksonDBCollection<DatasetRequest, String> requestCollection = JacksonDBCollection.wrap(db.getCollection(twitterServerConfiguration.getRequestCollection()),DatasetRequest.class, String.class);
        JacksonDBCollection<Analytics, String> analyticsCollection = JacksonDBCollection.wrap(db.getCollection(twitterServerConfiguration.getAnalyticsCollection()), Analytics.class, String.class);

        DatabaseManager dbm = new  DatabaseManager("TwitterDataCollections");

        Twitter4JAuth twitter4JAuth = new Twitter4JAuth(twitterServerConfiguration.getConsumerKey(),
                twitterServerConfiguration.getConsumerSecret(),
                twitterServerConfiguration.getAccessKey(),
                twitterServerConfiguration.getAccessSecret());

        environment.jersey().register(new DatasetResource(new DatasetManager(dbm,new DatasetInfoDAO(streamCollection),
                                                                                    new DatasetRequestDAO(requestCollection),
                                                                                        twitter4JAuth),
                                                            new AnalyticsManager(new AnalyticsDAO(analyticsCollection),dbm)));

    }
}
