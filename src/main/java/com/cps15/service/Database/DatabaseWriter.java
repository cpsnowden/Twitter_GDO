package com.cps15.service.Database;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.mongojack.JacksonDBCollection;

import java.util.logging.Logger;

import static com.mongodb.client.model.Filters.exists;

/**
 * Twitter_GDO
 * Created by chris on 08/06/2016.
 */

public class DatabaseWriter extends DatabaseManager {

    private static final Logger logger = Logger.getLogger(DatabaseWriter.class.getName());
    private MongoCollection<Document> collection;

    public DatabaseWriter(String database, String collectionName, boolean remote) {
        super(database, remote);
        this.collection = this.db.getCollection(collectionName);


    }

    public boolean insertJson(String json){

        return insertDocument(Document.parse(json));

    }

    public boolean insertDocument(Document document) {

        return this.insertDocument(document, this.collection);

    }

    private boolean insertDocument(Document document, MongoCollection<Document> collection) {

        try {
            collection.insertOne(document);
        } catch (MongoException ex) {
            logger.severe(ex.toString());
            return false;
        }
        return true;

    }

    public boolean insertDocument(Document document, String collection) {

        return insertDocument(document, this.db.getCollection(collection));

    }

    public MongoCollection<Document> getCollection(String collection) {
        return this.db.getCollection(collection);
    }



}
