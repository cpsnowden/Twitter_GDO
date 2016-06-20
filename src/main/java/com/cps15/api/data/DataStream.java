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
public class DataStream {

    @Id
    private String id = "DS_" + UUID.randomUUID().toString();
    private String description;
    private List<String> tags;
    private STATUS status;

    @NotNull
    private Date startDate;
    @Nullable
    private Date endDate;

    public enum STATUS {ORDERED, RUNNING, FINISHED, ERROR};

    public DataStream() {}

    public DataStream(String description, List<String> tags) {
        this.description = description;
        this.tags = tags;
        this.startDate = new Date();
        this.status = STATUS.ORDERED;
    }

    @JsonProperty
    public String getId() {
        System.out.println(id);
        return id;
    }

    @JsonProperty
    public String getDescription() {
        return description;
    }

    @JsonProperty
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty
    public List<String> getTags() {
        return tags;
    }

    @JsonProperty
    public void setTags(List<String> tags) {
        this.tags = tags;
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
    public STATUS getStatus() {
        return status;
    }

    @JsonProperty
    public void setStatus(STATUS status) {
        this.status = status;
    }

}