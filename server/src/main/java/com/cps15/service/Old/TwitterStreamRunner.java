//package com.cps15.service.DataService;
//
//import com.cps15.api.data.DataFilter;
//import com.cps15.service.DataService.StreamStopper.CounterStopper;
//import com.cps15.service.DataService.StreamStopper.IStreamStopper;
//import com.cps15.service.DataService.StreamStopper.TimeDurationStopper;
//import com.cps15.service.DataService.TwitterStreamCollector;
//
//import java.time.Duration;
//import java.util.Arrays;
//import java.util.List;
//
///**
// * Twitter_GDO
// * Created by chris on 10/06/2016.
// */
//public class TwitterStreamRunner {
//
//    public static void main(String[] args) {
//
//        String[] auth = {
//                "1FmkDF5T8yiImIEUTgSMVVZDK",
//                "5NmwbBmapP6GfrIRRMQmtnjlxkCruRz8aFaUxZCf0Wgn8tQvlA",
//                "529742116-0GrLOS192HToiIDcZbwZoQubPCOeV1vxVqKBy6OJ",
//                "74E59bkWxpa44TF8ZbAmX81Rt2ArlvR2ziWNUnrhQLuiY"
//        };
//
//        List<String> trackTerms = Arrays.asList("makeamericagreatagain","imwithher","feelthebern");
//        String collectionName = "USElections";
//
//        IStreamStopper c_stopper = new CounterStopper(5);
//        IStreamStopper d_stopper = new TimeDurationStopper(Duration.ofDays(1));
//
//
//        DataFilter dataFilter = new DataFilter(collectionName, trackTerms);
//
//
//
//        TwitterStreamCollector tsc = new TwitterStreamCollector(auth,dataFilter,c_stopper, );
//        Thread collector = new Thread(tsc);
//        collector.start();
//
//    }
//
//}
