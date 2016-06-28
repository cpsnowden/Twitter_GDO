package com.cps15.service.AnalyticsService.Graph;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import twitter4j.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by ChrisSnowden on 28/06/2016.
 */
public class AltRetweetFunction {

    private static TweetKeys tweetKeys = new TweetKeys("user.id",
            "retweetedStatus.user.id",
            "user.screenName",
            "retweetedStatus.user.screenName",
            "text");


    public GraphCreator getGraphCreator(Stream<DBObject> documentStream) {

        GraphCreator gc = new GraphCreator();

        documentStream.forEach(status->{

            TweetKeys tweetKeysValues = tweetKeys.getValues(status);

            gc.addNode(tweetKeysValues.getSourceKey(), tweetKeysValues.getSourceLabel());
            gc.addNode(tweetKeysValues.getTargetKey(), tweetKeysValues.getTargetLabel());
            gc.addEdge(tweetKeysValues.getSourceKey(),
                    tweetKeysValues.getTargetKey(),
                    tweetKeysValues.getEdgeLabel());

        });
        return gc;
    }

    public List<String> getKeys() {
        return tweetKeys.getKeys();
    }

    public DBObject getQuery() {
        BasicDBObject query = new BasicDBObject();
        for(String k:getKeys()) {
            query.put(k, new BasicDBObject("$ne",null));
            query.put(k, new BasicDBObject("$exists",true));
        }

        return query;
    }


}
