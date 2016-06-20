package com.cps15.service.AnalyticsService.Graph;

import org.bson.Document;

import java.util.List;
import java.util.stream.Stream;

/**
 * Twitter_GDO
 * Created by chris on 09/06/2016.
 */
public class RetweetFunction implements IDocumentGraphFunction {

    private static TweetKeys tweetKeys = new TweetKeys("user.id_str",
            "retweeted_status.user.id_str",
            "user.screen_name",
            "retweeted_status.user.screen_name",
            "text");

    @Override
    public GraphCreator getGraphCreator(Stream<Document> documentStream) {

        GraphCreator gc = new GraphCreator();

        documentStream.forEach(document -> {

            TweetKeys values = tweetKeys.getValues(document);
            gc.addNode(values.getSourceKey(),values.getSourceLabel());
            gc.addNode(values.getTargetKey(), values.getTargetLabel());
            gc.addEdge(values.getSourceKey(), values.getTargetKey(), values.getEdgeLabel());
        });
        return gc;
    }

    @Override
    public List<String> getKeys() {
        return tweetKeys.getKeys();
    }

}
