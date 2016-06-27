package com.cps15.api.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.IOException;

/**
 * Created by ChrisSnowden on 26/06/2016.
 */
public enum AnalyticsType {

    GRAPH("Graph");

    private String name;

    AnalyticsType(String name) {
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

    public static AnalyticsType fromName(String name) {

        for (AnalyticsType type : AnalyticsType.values()) {
            if (name.equals(type.getName())) {
                return type;
            }
        }
        return null;
    }
}
