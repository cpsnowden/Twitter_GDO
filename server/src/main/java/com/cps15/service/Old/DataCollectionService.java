//package com.cps15.service;
//
//import com.codahale.metrics.annotation.Timed;
//import com.cps15.service.DataService.TwitterStreams.StreamStopper.TimeDurationStopper;
//import com.cps15.service.DataService.TwitterStreamCollector;
//
//import javax.ws.rs.*;
//import javax.ws.rs.core.MediaType;
//import java.time.Duration;
//import java.util.Arrays;
//import java.util.List;
//
///**
// * Twitter_GDO
// * Created by chris on 18/06/2016.
// */
//@Path("/collect")
//public class DataCollectionService {
//
//    String[] auth = {
//            "1FmkDF5T8yiImIEUTgSMVVZDK",
//            "5NmwbBmapP6GfrIRRMQmtnjlxkCruRz8aFaUxZCf0Wgn8tQvlA",
//            "529742116-0GrLOS192HToiIDcZbwZoQubPCOeV1vxVqKBy6OJ",
//            "74E59bkWxpa44TF8ZbAmX81Rt2ArlvR2ziWNUnrhQLuiY"
//    };
//    public DataCollectionService(){}
//
//    @GET
//    @Timed
//    @Path("/get/{id}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public CollectionRequest getDataCollection(@PathParam("id") int id) {
//        return new CollectionRequest("1","2",Arrays.asList("one","two"));
//    }
//
//    @GET
//    @Timed
//    @Path("/all")
//    @Produces(MediaType.APPLICATION_JSON)
//    public List<CollectionRequest> getDataCollections() {
//        return Arrays.asList(new CollectionRequest("1","2",Arrays.asList("one","two","three")));
//    }
//
//    @POST
//    @Timed
//    @Path("/new")
//    @Produces(MediaType.TEXT_PLAIN)
//    @Consumes({MediaType.APPLICATION_JSON})
//    public String addDataCollectionRequest(CollectionRequest collectionRequest){
//
//        TwitterStreamCollector twitterStreamCollector = new TwitterStreamCollector("Twitter",
//                collectionRequest.getTags(),
//                auth,
//                collectionRequest.getDescription(),
//                new TimeDurationStopper(Duration.ofSeconds(60)));
//
//        Thread thread = new Thread(twitterStreamCollector);
//        thread.start();
//
//        return "Started Collection " + twitterStreamCollector.getUniqueID();
//    }
//
//}
//
