package com.cps15.api.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;

import javax.annotation.Nullable;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.security.InvalidParameterException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Twitter_GDO
 * Created by chris on 19/06/2016.
 */
public class DataStream extends DataStreamRequest implements IDataCollection {


    @Id
    private String id = "DS_" + UUID.randomUUID().toString();

    private Status.STATUS status;

    @NotNull
    private DateTime startDate;
    @Nullable
    private DateTime endDate;

    public DataStream() {
    }

    public DataStream(String description, String limitType, Long limit, List<String> tags) {
        super(description, limitType, limit, tags);
        this.startDate = DateTime.now();
        this.status = Status.STATUS.ORDERED;
    }

    public DataStream(DataStreamRequest dataStreamRequest) throws InvalidParameterException {

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
        return "DataStream{" +
                "id='" + id + '\'' +
                ", status=" + status +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}