package com.cps15.api.persistence;

import com.cps15.api.data.DatasetRequest;
import org.mongojack.DBCursor;
import org.mongojack.JacksonDBCollection;

import java.util.ArrayList;
import java.util.List;

/**
 * Twitter_GDO
 * Created by chris on 20/06/2016.
 */
public class DatasetRequestDAO {


    private JacksonDBCollection<DatasetRequest, String> collection;

    public DatasetRequestDAO(JacksonDBCollection<DatasetRequest, String> collection) {
        this.collection = collection;
    }

    public List<DatasetRequest> dataStreamRequests() {
        List<DatasetRequest> datasetRequests = new ArrayList<>();
        DBCursor<DatasetRequest> cursor = collection.find();
        while (cursor.hasNext()) {
            datasetRequests.add(cursor.next());
        }

        return datasetRequests;
    }

    public DatasetRequest findByID(String id) {
        return collection.findOneById(id);
    }

    public DatasetRequest addDataStreamRequest(DatasetRequest datasetRequests) {
        collection.insert(datasetRequests);
        return datasetRequests;
    }
}

