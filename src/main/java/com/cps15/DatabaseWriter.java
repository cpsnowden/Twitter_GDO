package com.cps15;

import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.gephi.graph.api.GraphModel;
import org.gephi.project.api.Workspace;

import javax.print.Doc;
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

public class DatabaseWriter extends DatabaseManager {

    private static final Logger logger = Logger.getLogger(DatabaseWriter.class.getName());
    private MongoCollection<Document> collection;


    public DatabaseWriter(String database) {
        super(database);
    }

    public void setCollection(String collectionName) {
        this.collection = this.db.getCollection(collectionName);
    }

    public boolean insertJson(String json){

        return insertDocument(Document.parse(json));

    }

    public boolean insertDocument(Document document) {

        if(this.collection != null) {
            try {
                this.collection.insertOne(document);
            } catch (MongoException ex) {
                logger.severe(ex.toString());
                return false;
            }
            return true;
        } else {
            logger.severe("Attempted to enter document without first setting collection");
            return false;
        }

    }



}
