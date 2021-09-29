package com.middleware.lab.controller;

import java.util.List;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.GenericTypeResolver;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.middleware.provider.exception.HeaderNotFoundException;
import com.middleware.provider.exception.ModelMappingException;
import com.middleware.provider.serializer.CustomObjectMapper;


/**
 *
 * @author Natalia Manrique
 */
@Component
public abstract class AbstractController<Model> {

    protected static final String X_VTEX_API_APP_KEY = "X-VTEX-API-AppKey";
    protected static final String X_VTEX_API_APP_TOKEN = "X-VTEX-API-AppToken";
    protected static final String X_VTEX_API_APP_KEY_PROV = "X-PROVIDER-API-AppKey";
    protected static final String X_VTEX_API_APP_TOKEN_PROV = "X-PROVIDER-API-AppToken";
    protected static final String Accept = "Accept";
    protected static final String Content_Type ="Content-Type";
    /**
     * Modelo sobre el cual se aplica el controlador rest
     */
    protected final Class<Model> classModel;

    /**
     * Objeto personalizado para el mapeo json/objeto
     */
    @Autowired
    protected CustomObjectMapper customObjectMapper;

    /**
     * Número máximo de elementos por página por defecto
     */
    protected static final int DEFAULT_PAGE_SIZE = 500;

    private static final Logger LOGGER = LogManager.getLogger(AbstractController.class);

    public AbstractController() {
        this.classModel = (Class<Model>) GenericTypeResolver.resolveTypeArgument(getClass(), AbstractController.class);
    }

    /**
     * Se inyecta instancia del objeto que permite mapear objetos a json
     *
     * @param customObjectMapper
     */
    @Autowired
    protected void setCustomObjectMapper(CustomObjectMapper customObjectMapper) {
        this.customObjectMapper = customObjectMapper;
    }

    /**
     * Cabeceras Http por defecto
     */
    protected HttpHeaders getDefaultHeaders() {
    	HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        
        return httpHeaders;
    }
/**
     * Cabeceras Http por defecto
     */
    protected HttpHeaders getGiftHeaders() {
    	 HttpHeaders httpHeaders = new HttpHeaders();
         httpHeaders.add("REST-AcceptRanges", "resources");
         httpHeaders.add("REST-Content-Range", "resources 0-1/5");
         httpHeaders.add("Vary", "REST-Range,Accept");
         
        return httpHeaders;
    }
    
       
    protected HttpHeaders getGiftDetailHeaders() {
    	HttpHeaders httpHeaders = new HttpHeaders();
//      httpHeaders.add("Content-Type", "application/x-java-object");
//      httpHeaders.add("Content-Length", "603");
        
    	return httpHeaders;
    }
    
    /**
     *
     * @param requestBody
     * @return
     * @throws ModelMappingException
     */
    protected Model mapperToModel(String requestBody) throws ModelMappingException {
        try {
            return customObjectMapper.readValue(requestBody, classModel);
        } catch (IOException ex) {
            ModelMappingException mmEx = new ModelMappingException(ex, classModel);
            LOGGER.error("ERROR mmEx"+mmEx.getMessage());
            throw mmEx;
        }
    }

    /**
     *
     * @param object
     * @return
     * @throws ModelMappingException
     */
    protected String mapperToJson(Object object) throws ModelMappingException {
        try {
            if (object == null) {
                return null;
            } else if (!(object instanceof String)) {
                return customObjectMapper.writeValueAsString(object);
            }
            return String.valueOf(object);
        } catch (IOException ex) {
            ModelMappingException mmEx = new ModelMappingException(ex, classModel);
            LOGGER.error(mmEx.getMessage());
            throw mmEx;
        }
    }

    /**
     *
     * @param requestBody
     * @return
     * @throws ModelMappingException
     */
    protected List<Model> mapperToListModel(String requestBody) throws ModelMappingException {
        try {
            return customObjectMapper.readValue(
                    requestBody,
                    customObjectMapper.getTypeFactory().constructCollectionType(List.class, classModel)
            );
        } catch (IOException ex) {
            ModelMappingException mmEx = new ModelMappingException(ex, classModel);
            LOGGER.error(mmEx.getMessage());
            throw mmEx;
        }
    }

    
     /**
     * Metodo estandar para evaluar las cabeceras de Vtex en los diferentes
     * metodos expuestos
     *
     * @author Jonathan Briceno
     * @param vTexAppKey
     * @param vTexAppToken
     * @return ResponseEntity
     */
    protected void validateVtexHeaders(String vTexAppKey, String vTexAppToken) throws HeaderNotFoundException {
        JSONObject responseBody = new JSONObject();
        StringBuilder exceptionMessage = new StringBuilder();
        boolean missingHeader = false;
        ResponseEntity response;

        System.out.println("vTexAppKey " + vTexAppKey);
        System.out.println("vTexAppToken " + vTexAppToken);
        if (vTexAppKey == null) {
            exceptionMessage.append(X_VTEX_API_APP_KEY);
            System.out.println("1 -> " + exceptionMessage.toString());
            missingHeader = true;
        }
        if (vTexAppToken == null) {
            if (missingHeader) {
                exceptionMessage.append(", ");
                System.out.println("2 -> " + exceptionMessage.toString());
            } else {
                missingHeader = true;
            }
            exceptionMessage.append(X_VTEX_API_APP_TOKEN);
            System.out.println("3 -> " + exceptionMessage.toString());
        }

        if (missingHeader) {
            throw new HeaderNotFoundException(exceptionMessage.toString());
        }

       
    }


}
