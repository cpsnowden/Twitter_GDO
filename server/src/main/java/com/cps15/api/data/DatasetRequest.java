package com.cps15.api.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Twitter_GDO
 * Created by chris on 19/06/2016.
 */
public class DatasetRequest {

    @JsonProperty("type")
    @JsonDeserialize(using = DatasetTypeDeserializer.class)
    private DatasetType type;

    @JsonProperty
    private String description;

    @JsonProperty
    private String limitType;

    @JsonProperty
    private Long limit;

    private List<String> tags;

    public DatasetRequest() {
    }

    public DatasetRequest(DatasetType type, String description, String limitType, Long limit, List<String> tags) {
        this.type = type;
        this.description = description;
        this.limitType = limitType;
        this.limit = limit;
        this.tags = tags;
    }


    public String getDescription() {
        return description;
    }

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

    public String getLimitType() {
        return limitType;
    }

    public void setLimitType(String limitType) {
        this.limitType = limitType;
    }

    public long getLimit() {
        return limit;
    }

    public void setLimit(Long limit) {
        this.limit = limit;
    }

    public DatasetType getType() {
        return type;
    }

    public void setType(DatasetType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "DatasetRequest{" +
                "type=" + type +
                ", description='" + description + '\'' +
                ", limitType='" + limitType + '\'' +
                ", limit=" + limit +
                ", tags=" + tags +
                '}';
    }
}