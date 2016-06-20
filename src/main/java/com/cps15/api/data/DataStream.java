package com.cps15.api.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nullable;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
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
    private IDataCollection.STATUS status;

    @NotNull
    private Date startDate;
    @Nullable
    private Date endDate;

    public DataStream() {}

    public DataStream(String description, List<String> tags) {
        super(description, tags);
        this.startDate = new Date();
        this.status = IDataCollection.STATUS.ORDERED;
    }

    public DataStream(DataStreamRequest dataStreamRequest) {
        this(dataStreamRequest.getDescription(), dataStreamRequest.getTags());
    }

    @JsonProperty
    public String getId() {
        System.out.println(id);
        return id;
    }

    @JsonProperty
    public Date getStartDate() {
        return startDate;
    }

    @JsonProperty
    @Nullable
    public Date getEndDate() {
        return endDate;
    }

    @JsonProperty
    public void setEndDate(@Nullable Date endDate) {
        this.endDate = endDate;
    }

    @JsonProperty
    public IDataCollection.STATUS getStatus() {
        return status;
    }

    @JsonProperty
    public void setStatus(IDataCollection.STATUS status) {
        this.status = status;
    }

}