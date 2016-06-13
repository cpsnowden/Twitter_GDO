package com.cps15.Database;

import com.cps15.AnalyticsService.Graph.GraphCreator;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.gephi.graph.api.GraphModel;
import org.gephi.project.api.Workspace;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.exists;
import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;

/**
 * Twitter_GDO
 * Created by chris on 08/06/2016.
 */

public class DatabaseReader extends DatabaseManager{

    private static final Logger logger = Logger.getLogger(DatabaseReader.class.getName());

    public DatabaseReader(String database, boolean remote) {
        super(database, remote);
    }

    public GraphModel getGraph(String collectionName, Workspace workspace, IDocumentFunction documentFunction, int maxNumber) {

        MongoCollection<Document> collection = db.getCollection(collectionName);
        Bson query = and(documentFunction.getKeys().stream().map(Filters::exists).collect(Collectors.toList()));
        Bson projection = fields(include(documentFunction.getKeys()), excludeId());
        Stream<Document> documentStream = StreamSupport.stream(collection.find(query).limit(maxNumber).projection(projection).spliterator(), false);

        return documentFunction.getGraphCreator(documentStream).getGraphModel(workspace);
    }

}
