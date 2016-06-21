package com.cps15.service.DataService;

import com.cps15.api.data.DataStream;
import com.cps15.api.data.Status;
import com.cps15.api.persistence.DataStreamDAO;

import java.util.HashMap;
import java.util.Map;

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

    private final Map<String, Thread> workers = new HashMap<>();
    private final Map<String, TwitterStreamCollector> tasks = new HashMap<>();
    private DataStreamDAO dataStreamDAO;
    public DataServiceManager(DataStreamDAO dataStreamDAO) {
        this.dataStreamDAO = dataStreamDAO;
    }

    public boolean addDataStream(DataStream dataStream) {

        TwitterStreamCollector tsc = new TwitterStreamCollector(auth, dataStream, dataStreamDAO);
        Thread worker = new Thread(tsc);

        if(workers.containsKey(tsc.getId())) {
            return false;
        } else {
            workers.put(tsc.getId(), worker);
            tasks.put(tsc.getId(), tsc);
            worker.start();
        }

        return true;
    }

    public boolean stopDataService(DataStream dataStream) {

        String id = dataStream.getId();

        if(dataStream.getStatus() == Status.STATUS.RUNNING) {
            TwitterStreamCollector tsc = tasks.get(id);
            if(id != null) {
                tsc.requestStop();
                return true;
            }
        }

        return false;
    }

    public boolean attemptRestartDataService(DataStream dataStream, boolean force) {

        String id = dataStream.getId();

        if(!force) {
            switch (dataStream.getStatus()) {
                case ERROR:
                    return false;
                case ORDERED:
                    return false;
                case RUNNING:
                    return false;
            }
        }

        TwitterStreamCollector tsc = tasks.get(id);
        if(tsc == null) {
            tsc = new TwitterStreamCollector(auth, dataStream, dataStreamDAO);
        }
        tsc.reset();

        Thread newThread = new Thread(tsc);
        workers.replace(id, newThread);
        newThread.start();


        return true;


    }

}
