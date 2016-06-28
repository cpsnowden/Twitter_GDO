package com.cps15.api.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.persistence.Id;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.UUID;

/**
 * Created by ChrisSnowden on 25/06/2016.
 */
public class Analytics {


    @Id
    private String id = "AN_" + UUID.randomUUID().toString();

    @JsonProperty
    private String datasetId;

    @JsonProperty
    private String dataPath;

    @JsonProperty
    private int layoutTime;

    @JsonProperty
    private int nodeLimit;

    @JsonProperty
    private boolean filterGiant;

    @JsonProperty
    private Status.STATUS status;

    @JsonProperty("type")
    @JsonDeserialize(using = AnalyticsTypeDeserializer.class)
    private AnalyticsType type;

    public Analytics() {
    }

    public Analytics(AnalyticsRequest analyticsRequest, String dsid) {

        switch (analyticsRequest.getType()) {
            case GRAPH:
                setNodeLimit(analyticsRequest.getNodeLimit());
                setLayoutTime(analyticsRequest.getLayoutTime());
                setType(analyticsRequest.getType());
                setFilterGiant(analyticsRequest.isFilterGiant());
                setDatasetId(dsid);
                break;
            default:
                throw new InvalidParameterException();
        }

        this.status = Status.STATUS.ORDERED;

    }

    public AnalyticsType getType() {
        return type;
    }

    public void setType(AnalyticsType type) {
        this.type = type;
    }

    public String getDataPath() {
        return dataPath;
    }

    public void setDataPath(String dataPath) {
        this.dataPath = dataPath;
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

    public String getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(String datasetId) {
        this.datasetId = datasetId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Status.STATUS getStatus() {
        return status;
    }

    public void setStatus(Status.STATUS status) {
        this.status = status;
    }

    @JsonIgnore
    public boolean isOldDataFormat() {

        return Arrays.asList("DS_0995c935-80b2-4890-8954-43e9341b41be",
                "DS_df6fd789-6728-4903-8f1a-b29c12ea4928",
                "DS_2db92824-be5d-47ee-b746-a3a2ddda1863",
                "DS_c57a239b-31fd-440d-97bd-e4838a07a9f6",
                "DS_cc231c68-53ee-412a-a2a7-7826eb3b27f1",
                "DS_112f4ed2-04df-4bca-a557-2e7560e8cb9d",
                "DS_0995c935-80b2-4890-8954-43e9341b41be",
                "DS_a82bf8bf-7996-41fc-b67b-97d77e6b989b",
                "DS_50473a60-cc94-44c7-a281-0e28e59297e9",
                "DS_0f1cea43-ccaa-410b-bad6-143909a4600c",
                "DS_5c2332c7-4afc-45ba-985d-ea04a0edb052",
                "DS_ed0199bb-96fd-4896-9dd6-f78a0a946dd0").contains(datasetId);

    }

    @Override
    public String toString() {
        return "Analytics{" +
                "id='" + id + '\'' +
                ", datasetId='" + datasetId + '\'' +
                ", dataPath='" + dataPath + '\'' +
                ", layoutTime=" + layoutTime +
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
