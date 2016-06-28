package com.cps15.api.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Created by ChrisSnowden on 26/06/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnalyticsRequest {

    @JsonProperty
    private int layoutTime;

    @JsonProperty
    private int nodeLimit;

    @JsonProperty
    private boolean filterGiant;


    @JsonProperty("type")
    @JsonDeserialize(using = AnalyticsTypeDeserializer.class)
    private AnalyticsType type;

    public AnalyticsRequest() {
    }

    public AnalyticsType getType() {
        return type;
    }

    public void setType(AnalyticsType type) {
        this.type = type;
    }

    public int getLayoutTime() {
        return layoutTime;
    }

    public void setLayoutTime(int layoutTime) {
        this.layoutTime = layoutTime;
    }

    public int getNodeLimit() {
        return nodeLimit;
    }

    public void setNodeLimit(int nodeLimit) {
        this.nodeLimit = nodeLimit;
    }

    @Override
    public String toString() {
        return "AnalyticsRequest{" +
                "layoutTime=" + layoutTime +
                ", nodeLimit=" + nodeLimit +
                ", type=" + type +
                '}';
    }

    public boolean isFilterGiant() {
        return filterGiant;
    }

    public void setFilterGiant(boolean filterGiant) {
        this.filterGiant = filterGiant;
    }
}
