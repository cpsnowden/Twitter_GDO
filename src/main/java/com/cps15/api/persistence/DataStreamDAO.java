package com.cps15.api.persistence;

import com.cps15.api.data.DataStream;
import org.mongojack.DBCursor;
import org.mongojack.JacksonDBCollection;
import org.mongojack.WriteResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Twitter_GDO
 * Created by chris on 20/06/2016.
 */
public class DataStreamDAO {

    private JacksonDBCollection<DataStream, String> collection;

    public DataStreamDAO(JacksonDBCollection<DataStream, String> collection) {
        this.collection = collection;
    }

    public List<DataStream> dataStreams() {
        List<DataStream> dataStreams = new ArrayList<>();
        DBCursor<DataStream> cursor = collection.find();
        while(cursor.hasNext()) {
            dataStreams.add(cursor.next());
        }

        return dataStreams;
    }

    public DataStream findByID(String id) {
        return collection.findOneById(id);
    }

    public DataStream addDataStream(DataStream dataStream) {
        collection.insert(dataStream);
        return dataStream;
    }

    public boolean update(DataStream dataStream) {
        WriteResult<DataStream, String> result = collection.updateById(dataStream.getId(), dataStream);
        return result.isUpdateOfExisting();
    }
}
