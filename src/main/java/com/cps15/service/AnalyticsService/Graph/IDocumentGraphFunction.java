package com.cps15.service.AnalyticsService.Graph;

import org.bson.Document;

import java.util.List;
import java.util.stream.Stream;

/**
 * Twitter_GDO
 * Created by chris on 09/06/2016.
 */
public interface IDocumentGraphFunction {

    GraphCreator getGraphCreator(Stream<Document> documentStream);
    List<String> getKeys();


}
