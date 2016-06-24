package com.cps15.api.persistence;

import com.cps15.api.data.DataFilter;
import org.mongojack.DBCursor;
import org.mongojack.JacksonDBCollection;
import org.mongojack.WriteResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Twitter_GDO
 * Created by chris on 20/06/2016.
 */
public class DataFilterDAO {

    private JacksonDBCollection<DataFilter, String> collection;

    public DataFilterDAO(JacksonDBCollection<DataFilter, String> collection) {
        this.collection = collection;
    }

    public List<DataFilter> dataStreams() {
        List<DataFilter> dataFilters = new ArrayList<>();
        DBCursor<DataFilter> cursor = collection.find();
        while(cursor.hasNext()) {
            dataFilters.add(cursor.next());
        }

        return dataFilters;
    }

    public DataFilter findByID(String id) {
        return collection.findOneById(id);
    }

    public DataFilter addDataStream(DataFilter dataFilter) {
        collection.insert(dataFilter);
        return dataFilter;
    }

    public boolean update(DataFilter dataFilter) {
        WriteResult<DataFilter, String> result = collection.updateById(dataFilter.getId(), dataFilter);
        return result.isUpdateOfExisting();
    }

    public int deleteByID(String id){
        WriteResult<DataFilter, String> result = collection.removeById(id);
        System.out.println(result.getN());
        return result.getN();
    }

}
