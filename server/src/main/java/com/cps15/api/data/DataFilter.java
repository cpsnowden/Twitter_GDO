package com.cps15.api.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;

import javax.annotation.Nullable;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.xml.crypto.Data;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.UUID;

/**
 * Twitter_GDO
 * Created by chris on 19/06/2016.
 */
public class DataFilter extends DataStreamRequest implements IDataCollection {


    @Id
    private String id = "DS_" + UUID.randomUUID().toString();

    private Status.STATUS status;

    @NotNull
    private DateTime startDate;
    @Nullable
    private DateTime endDate;

    public DataFilter() {
    }

    public DataFilter(String description, String limitType, Long limit, List<String> tags) {
        super(description, limitType, limit, tags);
        this.startDate = DateTime.now();
        this.status = Status.STATUS.ORDERED;
    }

    public DataFilter(DataStreamRequest dataStreamRequest) throws InvalidParameterException {

        this(dataStreamRequest.getDescription(),
                dataStreamRequest.getLimitType(),
                dataStreamRequest.getLimit(),
                dataStreamRequest.getTags());
    }

    @JsonProperty
    public String getId() {
        return id;
    }

    @JsonProperty
    public DateTime getStartDate() {
        return startDate;
    }

    @JsonProperty
    @Nullable
    public DateTime getEndDate() {
        return endDate;
    }

    @JsonProperty
    public void setEndDate(@Nullable DateTime endDate) {
        this.endDate = endDate;
    }

    @JsonProperty
    public Status.STATUS getStatus() {
        return status;
    }

    @JsonProperty
    public void setStatus(Status.STATUS status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "DataFilter{" +
                "id='" + id + '\'' +
                ", status=" + status +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }

    @JsonIgnore
    public DataFilter ordered() {
        status = Status.STATUS.ORDERED;
        return this;
    }

    @JsonIgnore
    public DataFilter running() {
        status = Status.STATUS.RUNNING;
        endDate = null;
        return this;
    }

    @JsonIgnore
    public DataFilter finished() {
        status = Status.STATUS.FINISHED;
        endDate = DateTime.now();
        return this;
    }

    @JsonIgnore
    public DataFilter error() {
        status = Status.STATUS.ERROR;
        return this;
    }

    @JsonIgnore
    public DataFilter stopped() {
        status = Status.STATUS.STOPPED;
        endDate = DateTime.now();
        return this;
    }

    @JsonIgnore
    public boolean isRuning(){
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


}