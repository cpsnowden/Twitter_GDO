package com.cps15;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.gephi.graph.api.GraphModel;
import org.gephi.project.api.Workspace;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import static com.mongodb.client.model.Filters.exists;
import static com.mongodb.client.model.Projections.*;

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
