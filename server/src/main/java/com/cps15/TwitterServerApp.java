package com.cps15;

import com.cps15.api.auth.Authenticator;
import com.cps15.api.auth.Authorizer;
import com.cps15.api.data.DataFilter;
import com.cps15.api.data.DataStreamRequest;
import com.cps15.api.data.User;
import com.cps15.api.health.MongoHealthCheck;
import com.cps15.api.persistence.DataFilterDAO;
import com.cps15.api.persistence.DataStreamRequestDAO;
import com.cps15.api.persistence.MongoManaged;
import com.cps15.api.resources.DataStreamResource;
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


//    @Override
//    public void initialize(io.dropwizard.setup.Bootstrap<TwitterServerConfiguration> bootstrap) {
//        super.initialize(bootstrap);
//
////        MongoJackModule.configure(bootstrap.getObjectMapper());
//        bootstrap.getObjectMapper().registerModule(new JodaModule());
//        bootstrap.getObjectMapper().setDateFormat(new ISO8601DateFormat());
////        bootstrap.getObjectMapper().registerModule(new MongoJackModule());
//
//    }

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
        JacksonDBCollection<DataFilter,String> streamCollection = JacksonDBCollection.wrap(db.getCollection(twitterServerConfiguration.getStreamCollection()),DataFilter.class,String.class, mapper);
        JacksonDBCollection<DataStreamRequest, String> requestCollection = JacksonDBCollection.wrap(db.getCollection(twitterServerConfiguration.getRequestCollection()),DataStreamRequest.class, String.class);


        environment.jersey().register(new DataStreamResource(new DataFilterDAO(streamCollection), new DataStreamRequestDAO(requestCollection)));

    }
}
