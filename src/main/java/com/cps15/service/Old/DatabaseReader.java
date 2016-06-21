//package com.cps15.service.Database;
//
//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.model.Filters;
//import org.bson.Document;
//import org.bson.conversions.Bson;
//
//import java.util.List;
//import java.util.logging.Logger;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//import java.util.stream.StreamSupport;
//
//import static com.mongodb.client.model.Filters.and;
//import static com.mongodb.client.model.Filters.exists;
//import static com.mongodb.client.model.Projections.excludeId;
//import static com.mongodb.client.model.Projections.fields;
//import static com.mongodb.client.model.Projections.include;
//
///**
// * Twitter_GDO
// * Created by chris on 08/06/2016.
// */
//
//public class DatabaseReader extends DatabaseManager{
//
//    private static final Logger logger = Logger.getLogger(DatabaseReader.class.getName());
//
//    public DatabaseReader(String database, boolean remote) {
//        super(database, remote);
//    }
//
//    public Stream<Document> getStream(String collectionName, List<String> keys, int maxNumber) {
//
//        MongoCollection<Document> collection = db.getCollection(collectionName);
//        Bson query = and(keys.stream().map(Filters::exists).collect(Collectors.toList()));
//        Bson projection = fields(include(keys), excludeId());
//        return StreamSupport.stream(collection.find(query).limit(maxNumber).projection(projection).spliterator(), false);
//
//    }
//
//}
