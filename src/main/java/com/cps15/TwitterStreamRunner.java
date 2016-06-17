package com.cps15;

import com.cps15.DataService.StreamStopper.CounterStopper;
import com.cps15.DataService.StreamStopper.IStreamStopper;
import com.cps15.DataService.StreamStopper.TimeDurationStopper;
import com.cps15.DataService.TwitterStreamCollector;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

/**
 * Twitter_GDO
 * Created by chris on 10/06/2016.
 */
public class TwitterStreamRunner {

    public static void main(String[] args) {

        String[] auth = {
                "1FmkDF5T8yiImIEUTgSMVVZDK",
                "5NmwbBmapP6GfrIRRMQmtnjlxkCruRz8aFaUxZCf0Wgn8tQvlA",
                "529742116-0GrLOS192HToiIDcZbwZoQubPCOeV1vxVqKBy6OJ",
                "74E59bkWxpa44TF8ZbAmX81Rt2ArlvR2ziWNUnrhQLuiY"
        };

        List<String> trackTerms = Arrays.asList("makeamericagreatagain","imwithher","feelthebern");
        String collectionName = "USElections";

        IStreamStopper c_stopper = new CounterStopper(5);
        IStreamStopper d_stopper = new TimeDurationStopper(Duration.ofDays(1));

        TwitterStreamCollector tsc = new TwitterStreamCollector("Twitter", trackTerms, auth, collectionName, d_stopper);
        Thread collector = new Thread(tsc);
        collector.start();

    }

}
