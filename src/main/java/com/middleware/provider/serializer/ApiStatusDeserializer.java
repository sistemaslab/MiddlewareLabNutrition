package com.middleware.provider.serializer;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.middleware.provider.utils.ApiStatus;

import java.io.IOException;
import org.springframework.stereotype.Component;

/**
 *
 * @author Alejandro Cadena
 */
@Component
public class ApiStatusDeserializer extends JsonDeserializer<ApiStatus> {
    
    @Override
    public ApiStatus deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        String appStatusCode = jp.getText();
        return ApiStatus.fromCode(appStatusCode);
    }

}
