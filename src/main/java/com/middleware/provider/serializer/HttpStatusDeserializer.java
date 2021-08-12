package com.middleware.provider.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 *
 * @author Alejandro Cadena
 */
@Component
public class HttpStatusDeserializer extends JsonDeserializer<HttpStatus> {
    
    @Override
    public HttpStatus deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        int appStatusCode = jp.getIntValue();
        return HttpStatus.valueOf(appStatusCode);
    }

}
