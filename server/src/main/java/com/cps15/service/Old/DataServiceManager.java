//package com.cps15.service.DataService;
//
//import com.cps15.api.data.DatasetInfo;
//import com.cps15.api.data.Status;
//import com.cps15.api.persistence.DatasetInfoDAO;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * Twitter_GDO
// * Created by chris on 18/06/2016.
// */
//public class DataServiceManager {
//
//    private String[] auth = {
//            "1FmkDF5T8yiImIEUTgSMVVZDK",
//            "5NmwbBmapP6GfrIRRMQmtnjlxkCruRz8aFaUxZCf0Wgn8tQvlA",
//            "529742116-0GrLOS192HToiIDcZbwZoQubPCOeV1vxVqKBy6OJ",
//            "74E59bkWxpa44TF8ZbAmX81Rt2ArlvR2ziWNUnrhQLuiY"
//    };
//
//    private final Map<String, Thread> workers = new HashMap<>();
//    private final Map<String, TwitterStreamCollector> tasks = new HashMap<>();
//    private DatasetInfoDAO dataFilterDAO;
//    public DataServiceManager(DatasetInfoDAO dataFilterDAO) {
//        this.dataFilterDAO = dataFilterDAO;
//    }
//
//    public boolean addDataStream(DatasetInfo dataFilter) {
//
//        TwitterStreamCollector tsc = new TwitterStreamCollector(auth, dataFilter, dataFilterDAO);
//        Thread worker = new Thread(tsc);
//
//        if(workers.containsKey(tsc.getId())) {
//            return false;
//        } else {
//            workers.put(tsc.getId(), worker);
//            tasks.put(tsc.getId(), tsc);
//            worker.start();
//        }
//
//        return true;
//    }
//
//    public boolean stopDataService(DatasetInfo dataFilter) {
//
//        String id = dataFilter.getId();
//
//        if(dataFilter.getStatus() == Status.STATUS.RUNNING) {
//            TwitterStreamCollector tsc = tasks.get(id);
//            if(tsc != null) {
//                tsc.requestStop();
//                return true;
//            } else {
//                dataFilter.setStatus(Status.STATUS.STOPPED);
//                dataFilterDAO.update(dataFilter);
//                return true;
//
//            }
//        }
//
//        return false;
//    }
//
//    public boolean attemptRestartDataService(DatasetInfo dataFilter, boolean force) {
//
//        String id = dataFilter.getId();
//
//        if(!force) {
//            switch (dataFilter.getStatus()) {
//                case ERROR:
//                    return false;
//                case ORDERED:
//                    return false;
//                case RUNNING:
//                    return false;
//            }
//        }
//
//        TwitterStreamCollector tsc = tasks.get(id);
//        if(tsc == null) {
//            tsc = new TwitterStreamCollector(auth, dataFilter, dataFilterDAO);
//        }
//        tsc.reset();
//
//        Thread newThread = new Thread(tsc);
//        workers.replace(id, newThread);
//        newThread.start();
//
//
//        return true;
//
//
//    }
//
//}
