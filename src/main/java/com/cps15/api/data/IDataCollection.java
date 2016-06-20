package com.cps15.api.data;

import org.joda.time.DateTime;

import javax.annotation.Nullable;
import java.util.Date;

/**
 * Twitter_GDO
 * Created by chris on 20/06/2016.
 */
public interface IDataCollection {

    String getId();

    Date getStartDate();
    Date getEndDate();
    void setEndDate(@Nullable Date endDate);

    Status.STATUS getStatus();
    void setStatus(Status.STATUS status);

}
