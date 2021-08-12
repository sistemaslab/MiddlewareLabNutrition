package com.middleware.provider.serializer;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.middleware.provider.utils.ApiStatus;

import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 *
 * @author Alejandro Cadena
 */
@Primary
@Component
public class CustomObjectMapper extends ObjectMapper {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CustomObjectMapper() {
        this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.addAppStatusModule();
        this.addHttpStatusModule();
    }
    
    /**
     * 
     */
    private void addAppStatusModule(){
        SimpleModule module = new SimpleModule("AppStatusModule", new Version(2, 0, 0, null, null, null));
        module.addSerializer(ApiStatus.class, new ApiStatusSerializer());//new DateSerializer());
        module.addDeserializer(ApiStatus.class, new ApiStatusDeserializer());
        registerModule(module);
    }
    
    /**
     * 
     */
    private void addHttpStatusModule(){
        SimpleModule module = new SimpleModule("HttpStatusModule", new Version(2, 0, 0, null, null, null));
        module.addSerializer(HttpStatus.class, new HttpStatusSerializer());//new DateSerializer());
        module.addDeserializer(HttpStatus.class, new HttpStatusDeserializer());
        registerModule(module);
    }
    
}