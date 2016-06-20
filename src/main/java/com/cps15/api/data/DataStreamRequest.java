package com.cps15.api.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Twitter_GDO
 * Created by chris on 19/06/2016.
 */
public class DataStreamRequest implements IDataCollectionRequest {

    private String description;
    private List<String> tags;

    public DataStreamRequest() {}

    public DataStreamRequest(String description, List<String> tags) {
        this.description = description;
        this.tags = tags;
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
}