package com.cps15.Database;

import com.cps15.AnalyticsService.Graph.GraphCreator;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * Twitter_GDO
 * Created by chris on 09/06/2016.
 */
public class RetweetFunction implements IDocumentFunction {

    private List<String> keys = Arrays.asList("user.id_str",
            "retweeted_status.user.id_str",
            "user.screen_name",
            "retweeted_status.user.screen_name");

    @Override
    public GraphCreator getGraphCreator(Stream<Document> documentStream) {

        GraphCreator gc = new GraphCreator();
        documentStream.forEach(document -> {
            List<Object> values = getValues(keys, document);

            gc.addNode(values.get(0).toString(), values.get(2).toString());
            gc.addNode(values.get(1).toString(), values.get(3).toString());
            gc.addEdge(values.get(0).toString(), values.get(1).toString());

        });


        return gc;
    }

    @Override
    public List<String> getKeys() {
        return keys;
    }

    private static List<Object> getValues(List<String> keys, Document document) {

        List<Object> values = new ArrayList<>();

        for (String key : keys) {
            String[] subKeys = key.split("[.]");
            Object value = document.get(subKeys[0]);
            for (int i = 1; i < subKeys.length; i++) {
                value = ((Document) value).get(subKeys[i]);
            }
            values.add(value);
        }
        return values;
    }
}
