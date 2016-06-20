package com.cps15.api.persistence;

import com.cps15.api.data.IDataCollection;

import java.util.List;

/**
 * Twitter_GDO
 * Created by chris on 20/06/2016.
 */
public interface IDataCollectionDAO {

    List<IDataCollection> getAll();
    IDataCollection findByID(String id);
    IDataCollection add(IDataCollection iDataCollection);
    boolean update(IDataCollection iDataCollection);


}
