package com.cps15.service.AnalyticsService.Graph;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import twitter4j.Status;
import twitter4j.TwitterObjectFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Twitter_GDO
 * Created by chris on 09/06/2016.
 */
public class RetweetFunction implements IStatusGraphFunction {

    private static TweetKeys tweetKeys = new TweetKeys("user.id",
            "retweeted_status.user.id",
            "user.screen_name",
            "retweeted_status.user.screen_name",
            "text");

    @Override
    public GraphCreator getGraphCreator(Stream<Status> statusStream) {

        GraphCreator gc = new GraphCreator();

        statusStream.forEach(status -> {
            String source = String.valueOf(status.getUser().getId());
            String target = String.valueOf(status.getRetweetedStatus().getUser().getId());

            gc.addNode(source, status.getUser().getName());
            gc.addNode(target, status.getRetweetedStatus().getUser().getName());
            gc.addEdge(source,target, status.getText());
        });

        return gc;
    }

    @Override
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
