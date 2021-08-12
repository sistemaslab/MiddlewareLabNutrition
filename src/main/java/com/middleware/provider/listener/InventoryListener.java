package com.middleware.provider.listener;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.middleware.provider.bs.AsynchronousTasks;
import com.middleware.provider.consumer.services.VtexApiRestClient;
import com.middleware.provider.exception.ModelMappingException;
import com.middleware.provider.exception.ServiceConnectionException;
import com.middleware.provider.inventory.model.rest.RequestUpdateInventory;
import java.util.logging.Logger;
import com.middleware.lab.client.LabClient;

@Component
public class InventoryListener {

    @Autowired
    AsynchronousTasks asynchronousTasks;

    @Autowired
    LabClient coolboxClient;

    @Autowired
    VtexApiRestClient vtexApiRestClient;

    private static final Logger LOG = Logger.getLogger(InventoryListener.class.getName());

    @JmsListener(destination = "InventoryQueue")

    public void receiveMessage(String msj)
            throws ModelMappingException, IOException, ServiceConnectionException, JmsException, InterruptedException {

        System.out.println("Received <" + msj + ">");
        ObjectMapper objectMapper = new ObjectMapper();
        RequestUpdateInventory requestUpdateInventory = objectMapper.readValue(msj, RequestUpdateInventory.class);
        LOG.info("REQUEST - >" + msj);
        // Homologar el SKU
        asynchronousTasks.UpdateInventory(requestUpdateInventory);
        LOG.info("EJECUTANDO - >" + msj);

    }
}
