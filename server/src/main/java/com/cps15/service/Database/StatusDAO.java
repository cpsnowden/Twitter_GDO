package com.cps15.service.Database;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import com.mongodb.util.JSON;
import org.bson.Document;
import org.mongojack.DBCursor;
import org.mongojack.DBProjection;
import org.mongojack.DBQuery;
import org.mongojack.JacksonDBCollection;
import org.mongojack.internal.MongoJackModule;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterObjectFactory;
import twitter4j.User;
import twitter4j.json.DataObjectFactory;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.SynchronousQueue;
import java.util.logging.Logger;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Twitter_GDO
 * Created by chris on 21/06/2016.
 */

public class StatusDAO{

    private static final Logger logger = Logger.getLogger(StatusDAO.class.getName());

    private DBCollection dbc;
    public StatusDAO(DBCollection dbCollection) {

        dbc = dbCollection;

    }

    public void insert(Status status) {

        try {
            String json = TwitterObjectFactory.getRawJSON(status);
            dbc.insert((DBObject) JSON.parse(json));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public DBCollection getCollection() {
        return dbc;
    }


    public Stream<DBObject> getDBObjectStream(DBObject query, int maxNumber) {

        return StreamSupport.stream(dbc.find(query).limit(maxNumber).spliterator(),false);

    }

    public Stream<Status> getStream(DBObject query, int maxNumber){

        logger.info("Getting stream " + query.toString());

        Stream<Status> s = StreamSupport.stream(dbc.find(query).limit(maxNumber).spliterator(),false).map(f -> {
            try {
                return TwitterObjectFactory.createStatus(f.toString());
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            return null;
        });

        return s;

    }








}

