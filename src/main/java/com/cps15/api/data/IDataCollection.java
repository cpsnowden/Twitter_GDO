package com.cps15.api.data;

import javax.annotation.Nullable;
import java.util.Date;

/**
 * Twitter_GDO
 * Created by chris on 20/06/2016.
 */
public interface IDataCollection {

    enum STATUS {ORDERED, RUNNING, FINISHED, ERROR};

    String getId();

    Date getStartDate();
    Date getEndDate();
    void setEndDate(@Nullable Date endDate);

    STATUS getStatus();
    void setStatus(STATUS status);

}
