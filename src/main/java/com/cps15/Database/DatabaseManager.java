package com.cps15.Database;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import java.util.logging.Logger;

import static com.mongodb.client.model.Filters.exists;

/**
 * Twitter_GDO
 * Created by chris on 08/06/2016.
 */

public abstract class DatabaseManager {

    private static final Logger logger = Logger.getLogger(DatabaseManager.class.getName());
    protected MongoClient mongoClient;
    protected MongoDatabase db;

    public DatabaseManager(String database) {

        this.mongoClient = new MongoClient("localhost", 27017);
        this.db = mongoClient.getDatabase(database);
    }


}
