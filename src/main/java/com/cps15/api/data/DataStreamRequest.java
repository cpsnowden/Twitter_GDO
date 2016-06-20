package com.cps15.api.data;

import com.cps15.service.DataService.StreamStopper.IStreamStopper;
import com.fasterxml.jackson.annotation.JsonProperty;
import gnu.trove.TObjectIdentityHashingStrategy;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.Duration;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Twitter_GDO
 * Created by chris on 19/06/2016.
 */
public class DataStreamRequest implements IDataCollectionRequest {

    @NotEmpty
    private String description;
    @NotEmpty
    private String limitType;
    @NotNull
    private int limit;

    private List<String> tags;

    public DataStreamRequest() {}

    public DataStreamRequest(String description, String limitType, int limit, List<String> tags) {
        this.description = description;
        this.limitType = limitType;
        this.limit = limit;
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

    @JsonProperty
    public String getLimitType() {
        return limitType;
    }

    @JsonProperty
    public void setLimitType(String limitType) {
        this.limitType = limitType;
    }

    @JsonProperty
    public int getLimit() {
        return limit;
    }

    @JsonProperty
    public void setLimit(int limit) {
        this.limit = limit;
    }
}