package com.cps15.api.persistence;

import com.mongodb.Mongo;
import io.dropwizard.lifecycle.Managed;

/**
 * ExampleDrop
 * Created by chris on 19/06/2016.
 */
public class MongoManaged implements Managed {

    private Mongo mongo;

    public MongoManaged(Mongo mongo) {
        this.mongo = mongo;
    }


    public void start() throws Exception {

    }

    public void stop() throws Exception {
        mongo.close();
    }
}
