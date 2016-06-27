package com.cps15.api.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;

import javax.annotation.Nullable;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.UUID;

/**
 * Twitter_GDO
 * Created by chris on 19/06/2016.
 */
public class DatasetInfo extends DatasetRequest{


    @Id
    @JsonProperty
    private String id = "DS_" + UUID.randomUUID().toString();

    @JsonProperty
    private Status.STATUS status;

    @NotNull
    @JsonProperty
    private DateTime startDate;

    @Nullable
    @JsonProperty
    private DateTime endDate;

    @Nullable
    @JsonProperty
    private Long filterSize = null;

    public DatasetInfo() {
    }

    public DatasetInfo(DatasetType datasetType, String description, String limitType, Long limit, List<String> tags) {
        super(datasetType, description, limitType, limit, tags);
        this.startDate = DateTime.now();
        this.status = Status.STATUS.ORDERED;
    }

    public DatasetInfo(DatasetRequest d) throws InvalidParameterException {

        this(d.getType(),d.getDescription(),d.getLimitType(),d.getLimit(),d.getTags());

    }


    public String getId() {
        return id;
    }

    public DateTime getStartDate() {
        return startDate;
    }

    public DateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(@Nullable DateTime endDate) {
        this.endDate = endDate;
    }

    public Status.STATUS getStatus() {
        return status;
    }

    public void setStatus(Status.STATUS status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "DatasetInfo{" +
                "id='" + id + '\'' +
                ", status=" + status +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }

    public DatasetInfo ordered() {
        status = Status.STATUS.ORDERED;
        return this;
    }

    public DatasetInfo running() {
        status = Status.STATUS.RUNNING;
        filterSize = null;
        endDate = null;
        return this;
    }

    public DatasetInfo finished() {
        status = Status.STATUS.FINISHED;
        endDate = DateTime.now();
        return this;
    }

    public DatasetInfo error() {
        status = Status.STATUS.ERROR;
        return this;
    }


    public DatasetInfo stopped() {
        status = Status.STATUS.STOPPED;
        endDate = DateTime.now();
        return this;
    }

    @JsonIgnore
    public boolean isRunning(){
        return Status.STATUS.RUNNING == status;
    }

    @JsonIgnore
    public boolean isStopped() {
        return Status.STATUS.STOPPED == status;
    }

    @JsonIgnore
    public boolean isError() {
        return Status.STATUS.ERROR == status;
    }

    @JsonIgnore
    public boolean isOrdered() {
        return Status.STATUS.ORDERED == status;
    }

    public Long getFilterSize() {
        return filterSize;
    }

    public void setFilterSize(long filterSize) {
        this.filterSize = filterSize;
    }
}