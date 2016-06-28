package com.cps15.api.persistence;

import com.cps15.api.data.Analytics;
import com.cps15.api.data.DatasetInfo;
import org.mongojack.DBCursor;
import org.mongojack.JacksonDBCollection;
import org.mongojack.WriteResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChrisSnowden on 26/06/2016.
 */
public class AnalyticsDAO {

    private JacksonDBCollection<Analytics, String> collection;

    public AnalyticsDAO(JacksonDBCollection<Analytics, String> collection) {
        this.collection = collection;
    }

    public Analytics findByID(String id, String dsid) {

        Analytics analytics =  collection.findOneById(id);
        assert analytics.getDatasetId().equals(dsid);
        return analytics;
    }

    public Analytics add(Analytics analytics) {
        collection.insert(analytics);
        return analytics;
    }

    public List<Analytics> findByDSID(String id) {

        List<Analytics> analytics = new ArrayList<>();
        DBCursor<Analytics> cursor = collection.find().is("datasetId", id);
        while (cursor.hasNext()) {
            analytics.add(cursor.next());
        }

        return analytics;
    }

    public boolean update(Analytics analytics) {
        WriteResult<Analytics, String> result = collection.updateById(analytics.getId(), analytics);
        return result.isUpdateOfExisting();
    }

    public int deleteByID(String id) {
        WriteResult<Analytics, String> result = collection.removeById(id);
        System.out.println(result.getN());
        return result.getN();
    }
}
