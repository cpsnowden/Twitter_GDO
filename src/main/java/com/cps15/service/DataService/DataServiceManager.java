package com.cps15.service.DataService;

import com.cps15.api.data.DataStream;
import com.cps15.api.persistence.DataStreamDAO;
import com.cps15.service.DataService.StreamStopper.TimeDurationStopper;
import com.mysql.fabric.xmlrpc.base.Data;
import org.mongojack.JacksonDBCollection;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

/**
 * Twitter_GDO
 * Created by chris on 18/06/2016.
 */
public class DataServiceManager {

    private String[] auth = {
            "1FmkDF5T8yiImIEUTgSMVVZDK",
            "5NmwbBmapP6GfrIRRMQmtnjlxkCruRz8aFaUxZCf0Wgn8tQvlA",
            "529742116-0GrLOS192HToiIDcZbwZoQubPCOeV1vxVqKBy6OJ",
            "74E59bkWxpa44TF8ZbAmX81Rt2ArlvR2ziWNUnrhQLuiY"
    };

    private final Set<Thread> workers = new HashSet<>();
    private DataStreamDAO dataStreamDAO;
    public DataServiceManager(DataStreamDAO dataStreamDAO) {
        this.dataStreamDAO = dataStreamDAO;
    }

    public void addDataStream(DataStream dataStream) {

        TwitterStreamCollector tsc = new TwitterStreamCollector(auth, dataStream, new TimeDurationStopper(Duration.ofSeconds(30)), dataStreamDAO);
        Thread worker = new Thread(tsc);
        workers.add(worker);

        worker.start();
    }
}
