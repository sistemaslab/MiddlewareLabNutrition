package com.middleware.provider.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Alejandro Cadena
 */
public class VtexDateSerializer extends JsonSerializer<Date> {

    /**
     * Variable que contiene el formato de fecha
     */
    public static String dateFormatString;

    public VtexDateSerializer(String dateFormatString) {
        VtexDateSerializer.dateFormatString = dateFormatString;
    }   
    
    @Override
    public void serialize(Date value, JsonGenerator jgen,
        SerializerProvider provider) throws IOException,JsonProcessingException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatString);
        jgen.writeString(dateFormat.format(value));
    }

    @Override
    public Class<Date> handledType() {
        return Date.class;
    }
    
}
