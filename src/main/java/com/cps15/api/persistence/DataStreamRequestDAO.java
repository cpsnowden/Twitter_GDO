package com.cps15.api.persistence;

import com.cps15.api.data.DataStreamRequest;
import org.mongojack.DBCursor;
import org.mongojack.JacksonDBCollection;

import java.util.ArrayList;
import java.util.List;

/**
 * Twitter_GDO
 * Created by chris on 20/06/2016.
 */
public class DataStreamRequestDAO {


        private JacksonDBCollection<DataStreamRequest, String> collection;

        public DataStreamRequestDAO(JacksonDBCollection<DataStreamRequest, String> collection) {
            this.collection = collection;
        }

        public List<DataStreamRequest> dataStreamRequests() {
            List<DataStreamRequest> dataStreamRequests = new ArrayList<>();
            DBCursor<DataStreamRequest> cursor = collection.find();
            while(cursor.hasNext()) {
                dataStreamRequests.add(cursor.next());
            }

            return dataStreamRequests;
        }

        public DataStreamRequest findByID(String id) {
            return collection.findOneById(id);
        }

        public DataStreamRequest addDataStreamRequest(DataStreamRequest dataStreamRequests) {
            collection.insert(dataStreamRequests);
            return dataStreamRequests;
        }
}

