package com.cps15.api.data;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created by ChrisSnowden on 27/06/2016.
 */
public enum  DatasetType {

    TWITTER_STREAM("Twitter_Stream");

    private String name;

    DatasetType(String name) {
        this.name = name;
    }

    ;

    @JsonValue
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static DatasetType fromName(String name) {

        for (DatasetType type : DatasetType.values()) {
            if (name.equals(type.getName())) {
                return type;
            }
        }
        return null;
    }
}
