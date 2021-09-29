package com.middleware.provider.utils;

import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 *
 * @author Alejandro Cadena
 */
public class XMLPrint {
    
    /**
     * Temporal para pruebas 
     * @param request
     */
    public static void printRequest(Object request){
        if(request != null){
            JAXBContext jc;
            try {
                jc = JAXBContext.newInstance(request.getClass());
                Marshaller marshaller = jc.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            //    LOG.manageInfo("Request:");
                StringWriter sw = new StringWriter();
                //marshaller.marshal(request, System.out);
                marshaller.marshal(request, sw);
                String xmlString = sw.toString();
            //    LOG.manageInfo(xmlString);
            } catch (JAXBException ex) {
                Logger.getLogger(XMLPrint.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Temporal para pruebas 
     * @param request
     */
    public static void printResponse(Object request){
        if(request != null){
            JAXBContext jc;
            try {
                jc = JAXBContext.newInstance(request.getClass());
                Marshaller marshaller = jc.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            //    LOG.manageInfo("Response:");
                //marshaller.marshal(request, System.out);
                StringWriter sw = new StringWriter();
                marshaller.marshal(request, sw);
                String xmlString = sw.toString();
            //    LOG.manageInfo(xmlString);
            } catch (JAXBException ex) {
                Logger.getLogger(XMLPrint.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
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
            Logger.getLogger(XMLPrint.class.getName()).log(Level.SEVERE, null, ex);
            xmlString = String.valueOf(request);
        }
        return xmlString;
    }
    
}
