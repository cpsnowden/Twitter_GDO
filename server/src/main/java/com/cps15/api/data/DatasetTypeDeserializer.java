package com.cps15.api.data;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.io.IOException;

public class DatasetTypeDeserializer extends JsonDeserializer {

    @Override
    public DatasetType deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {

        DatasetType datasetType = DatasetType.fromName(jsonParser.getValueAsString());
        if (datasetType != null) {
            return datasetType;
        }

        throw new IOException("Invalid type");
    }
}
