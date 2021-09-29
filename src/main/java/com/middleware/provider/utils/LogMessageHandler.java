package com.middleware.provider.utils;

import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author alejandro cadena
 */
public class LogMessageHandler implements SOAPHandler<SOAPMessageContext> {

    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(LogMessageHandler.class);
    
    @Override
    public Set<QName> getHeaders() {
        return Collections.EMPTY_SET;
    }

    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        SOAPMessage msg = context.getMessage();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            msg.writeTo(out);
            LOGGER.debug(new String(out.toByteArray()));
        } catch (Exception ex) {
            LOGGER.error("LogMessageHandler-handleMessage", ex);
            Logger.getLogger(LogMessageHandler.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return true;
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        return true;
    }

    @Override
    public void close(MessageContext context) {
    }
}
