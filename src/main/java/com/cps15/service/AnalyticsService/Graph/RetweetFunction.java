package com.cps15.service.AnalyticsService.Graph;

import twitter4j.Status;

import java.util.List;
import java.util.stream.Stream;

/**
 * Twitter_GDO
 * Created by chris on 09/06/2016.
 */
public class RetweetFunction implements IStatusGraphFunction {

    private static TweetKeys tweetKeys = new TweetKeys("user.id_str",
            "retweeted_status.user.id_str",
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
            gc.addEdge(source,target);
        });

        return gc;
    }

    @Override
    public List<String> getKeys() {
        return tweetKeys.getKeys();
    }

}
