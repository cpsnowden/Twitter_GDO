package com.cps15.service.Database;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mysql.fabric.xmlrpc.base.Data;

import java.util.logging.Logger;

/**
 * Twitter_GDO
 * Created by chris on 08/06/2016.
 */

public class DatabaseManager {

    private static final Logger logger = Logger.getLogger(DatabaseManager.class.getName());
    protected MongoClient mongoClient;
    protected DB db;
    private static final String localhost = "localhost";
    private static final int port = 27017;

    private static final String remoteHost = "146.169.45.131";
    

    public DatabaseManager(String database) {

        this(database,false);

    }

    public DatabaseManager(String database, boolean remote){

        this.mongoClient = new MongoClient(remote?remoteHost:localhost, port);
        this.db = mongoClient.getDB(database);

    }

    public DB getDb() {
        return db;
    }

    public DB getAltDb(String dbName) {
        return mongoClient.getDB(dbName);
    }


}
