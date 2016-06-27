package com.cps15.api.data;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.io.IOException;

public class AnalyticsTypeDeserializer extends JsonDeserializer {

    @Override
    public AnalyticsType deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {

        AnalyticsType analyticsType = AnalyticsType.fromName(jsonParser.getValueAsString());
        if (analyticsType != null) {
            return analyticsType;
        }

        throw new IOException("Invalid type");
    }
}
