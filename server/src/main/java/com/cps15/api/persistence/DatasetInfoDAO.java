package com.cps15.api.persistence;

import com.cps15.api.data.DatasetInfo;
import com.cps15.api.data.Status;
import org.mongojack.DBCursor;
import org.mongojack.JacksonDBCollection;
import org.mongojack.WriteResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Twitter_GDO
 * Created by chris on 20/06/2016.
 */
public class DatasetInfoDAO {

    private JacksonDBCollection<DatasetInfo, String> collection;

    public DatasetInfoDAO(JacksonDBCollection<DatasetInfo, String> collection) {
        this.collection = collection;
    }

    public List<DatasetInfo> dataStreams() {
        List<DatasetInfo> datasetInfos = new ArrayList<>();
        DBCursor<DatasetInfo> cursor = collection.find();
        while (cursor.hasNext()) {
            datasetInfos.add(cursor.next());
        }

        return datasetInfos;
    }

    public List<DatasetInfo> getDataStreamsByStatus(List<Status.STATUS> status) {

        List<DatasetInfo> datasetInfos = new ArrayList<>();
        DBCursor<DatasetInfo> cursor = collection.find().in("status", status);
        while (cursor.hasNext()) {
            datasetInfos.add(cursor.next());
        }

        return datasetInfos;
    }

    public DatasetInfo findByID(String id) {
        return collection.findOneById(id);
    }

    public DatasetInfo addDataStream(DatasetInfo datasetInfo) {
        collection.insert(datasetInfo);
        return datasetInfo;
    }

    public boolean update(DatasetInfo datasetInfo) {
        WriteResult<DatasetInfo, String> result = collection.updateById(datasetInfo.getId(), datasetInfo);
        return result.isUpdateOfExisting();
    }

    public int deleteByID(String id) {
        WriteResult<DatasetInfo, String> result = collection.removeById(id);
        System.out.println(result.getN());
        return result.getN();
    }

}
