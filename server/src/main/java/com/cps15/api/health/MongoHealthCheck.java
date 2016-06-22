package com.cps15.api.health;

import com.codahale.metrics.health.HealthCheck;
import com.mongodb.Mongo;

/**
 * Twitter_GDO
 * Created by chris on 19/06/2016.
 */
public class MongoHealthCheck extends HealthCheck{

    private Mongo mongo;

    public MongoHealthCheck(Mongo mongo) {
        this.mongo = mongo;
    }

    protected Result check() throws Exception {
        mongo.getDatabaseNames();
        return Result.healthy("All ok");
    }

}
