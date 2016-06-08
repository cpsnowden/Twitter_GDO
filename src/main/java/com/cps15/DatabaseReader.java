package com.cps15;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.gephi.graph.api.GraphModel;
import org.gephi.project.api.Workspace;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import static com.mongodb.client.model.Filters.exists;
import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;

/**
 * Twitter_GDO
 * Created by chris on 08/06/2016.
 */

public class DatabaseReader {

    private static final Logger logger = Logger.getLogger(DatabaseReader.class.getName());
    private MongoClient mongoClient;
    private MongoDatabase db;

    public DatabaseReader(String database) {

        this.mongoClient = new MongoClient("localhost", 27017);
        this.db = mongoClient.getDatabase(database);

    }

    public GraphModel getRetweetGraph(String collectionName, Workspace workspace) {

        return getRetweetGraph(collectionName, workspace, -1);

    }

    public GraphModel getRetweetGraph(String collectionName, Workspace workspace, int upperLimit) {

        GraphConstructor gc = new GraphConstructor();

        List<String> keys = Arrays.asList("user.id_str", "retweeted_status.user.id_str",
                "user.screen_name", "retweeted_status.user.screen_name");

        MongoCollection<Document> collection = db.getCollection(collectionName);
        Bson query = exists("retweeted_status");
        Bson projection = fields(include(keys), excludeId());
        int counter = 0;
        try (MongoCursor<Document> cursor = collection.find(query).projection(projection).iterator()) {

            while (cursor.hasNext()) {
                Document document = cursor.next();
                List<Object> values = getValues(keys, document);

                gc.addNode(values.get(0).toString(), values.get(2).toString());
                gc.addNode(values.get(1).toString(), values.get(3).toString());
                gc.addEdge(values.get(0).toString(), values.get(1).toString());

                counter++;
                if(upperLimit > 0 && counter > upperLimit) {
                    break;
                }
            }
            logger.info("Looked at " + counter + " entries");
        }

        return gc.getGraphModel(workspace);
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
