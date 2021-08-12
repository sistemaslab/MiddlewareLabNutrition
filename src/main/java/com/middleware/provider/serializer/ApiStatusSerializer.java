package com.middleware.provider.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.middleware.provider.utils.ApiStatus;

import java.io.IOException;
import org.springframework.stereotype.Component;

/**
 *
 * @author Alejandro Cadena
 */
@Component
public class ApiStatusSerializer extends JsonSerializer<ApiStatus> {
    
    @Override
    public void serialize(ApiStatus value, JsonGenerator jgen,
        SerializerProvider provider) throws IOException,JsonProcessingException {
        jgen.writeString(value.getAppCode());
    }

    @Override
    public Class<ApiStatus> handledType() {
        return ApiStatus.class;
    }
    
}
