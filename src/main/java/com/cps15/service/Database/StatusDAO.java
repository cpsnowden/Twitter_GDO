package com.cps15.service.Database;

import com.mongodb.DBCollection;
import org.mongojack.DBProjection;
import org.mongojack.DBQuery;
import org.mongojack.JacksonDBCollection;
import twitter4j.Status;

import java.util.List;
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
    private JacksonDBCollection<Status, String> collection;

    public StatusDAO(DBCollection dbCollection) {
        collection = JacksonDBCollection.wrap(dbCollection, Status.class, String.class);
    }

    public void insert(Status status) {
        collection.insert(status);
    }

    public JacksonDBCollection<Status, String> getCollection() {
        return collection;
    }

    public Stream<Status> getStream(DBQuery.Query query, DBProjection.ProjectionBuilder projection, int maxNumber){
        logger.info("Getting stream " + query.toString() + " " + projection.toString());
        return StreamSupport.stream(collection.find(query,projection).limit(maxNumber).spliterator(),false);

    }




}
