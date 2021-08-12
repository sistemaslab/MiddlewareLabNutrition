package com.middleware.provider.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 *
 * @author Alejandro Cadena
 */
@Component
public class HttpStatusSerializer extends JsonSerializer<HttpStatus> {
    
    @Override
    public void serialize(HttpStatus value, JsonGenerator jgen,
        SerializerProvider provider) throws IOException,JsonProcessingException {
        jgen.writeNumber(value.value());
    }

    @Override
    public Class<HttpStatus> handledType() {
        return HttpStatus.class;
    }
    
}
