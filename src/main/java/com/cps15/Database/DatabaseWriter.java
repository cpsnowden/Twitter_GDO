package com.cps15.Database;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.logging.Logger;

import static com.mongodb.client.model.Filters.exists;

/**
 * Twitter_GDO
 * Created by chris on 08/06/2016.
 */

public class DatabaseWriter extends DatabaseManager {

    private static final Logger logger = Logger.getLogger(DatabaseWriter.class.getName());
    private MongoCollection<Document> collection;


    public DatabaseWriter(String database, String collectionName) {
        super(database);
        this.collection = this.db.getCollection(collectionName);
    }

    public boolean insertJson(String json){

        return insertDocument(Document.parse(json));

    }

    public boolean insertDocument(Document document) {

        try {
            this.collection.insertOne(document);
        } catch (MongoException ex) {
            logger.severe(ex.toString());
            return false;
        }
        return true;

    }



}
