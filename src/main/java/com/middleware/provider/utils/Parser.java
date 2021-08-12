package com.middleware.provider.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Parser {
    
    /**
     * Temporal para pruebas 
     * @param request
     */
    public static String xmlToString(Object request){
        if(request == null){
            return null;
        }
        JAXBContext jc;
        String xmlString;
        try {
            jc = JAXBContext.newInstance(request.getClass());
            Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            StringWriter sw = new StringWriter();
            marshaller.marshal(request, sw);
            xmlString = sw.toString();
        } catch (JAXBException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
            xmlString = String.valueOf(request);
        }
        return xmlString;
    }
    
    public static String objToJsonString(Object object){
        ObjectMapper objectMapper = new ObjectMapper();
        String indented;
        try {
            indented = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException ex) {
            indented = "{\"error\":\"Error parsing object to json: "+Arrays.toString(ex.getStackTrace())+"\"}";
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return indented;
    }
    
}