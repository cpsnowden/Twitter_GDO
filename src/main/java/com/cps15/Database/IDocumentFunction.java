package com.cps15.Database;

import com.cps15.AnalyticsService.Graph.GraphCreator;
import org.bson.Document;

import java.util.List;
import java.util.stream.Stream;

/**
 * Twitter_GDO
 * Created by chris on 09/06/2016.
 */
public interface IDocumentFunction {
    GraphCreator getGraphCreator(Stream<Document> documentStream);
    List<String> getKeys();


}
