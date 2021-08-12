package com.middleware.provider.listener;

import java.io.IOException;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.middleware.provider.bs.AsynchronousTasks;
import com.middleware.provider.exception.ModelMappingException;
import com.middleware.provider.exception.ServiceConnectionException;
import com.middleware.provider.pricing.model.rest.RequestUpdatePricing;

@Component
public class PricingListener {

    @Autowired
    AsynchronousTasks asynchronousTasks;

    private static final Logger LOG = Logger.getLogger(PricingListener.class.getName());

    @JmsListener(destination = "PricingQueue")
    public void receiveMessage(String msj) throws ModelMappingException, ServiceConnectionException, IOException, InterruptedException {

        System.out.println("Received <" + msj + ">");
        ObjectMapper objectMapper = new ObjectMapper();
        RequestUpdatePricing requestUpdatePricing = objectMapper.readValue(msj, RequestUpdatePricing.class);
        LOG.info("REQUEST - >" + msj);
        asynchronousTasks.UpdatePricing(requestUpdatePricing);
        LOG.info("EJECUTANDO - >" + msj);

    }

}
