package com.middleware.lab.controller;

import java.io.IOException;
import java.util.logging.Logger;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.middleware.provider.bs.InventoryBs;
import com.middleware.provider.bs.OrdersBs;
import com.middleware.provider.exception.HeaderNotFoundException;
import com.middleware.provider.exception.ModelMappingException;
import com.middleware.provider.exception.ServiceConnectionException;
import com.middleware.provider.orders.model.rest.RequestHook;

import io.swagger.annotations.ApiParam;
import java.util.logging.Level;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 *
 * @author Natalia Manrique
 */
@RestController
@RequestMapping(value = "lab/api")
public class OrdersController extends AbstractController<RequestHook> {

    @Autowired
    OrdersBs ordersBs;
    InventoryBs inventoryBs;
    private static final Logger LOG = Logger.getLogger(OrdersController.class.getName());

    
    @CrossOrigin()
    @RequestMapping(
            value = {"/hook/alert"},
            method = RequestMethod.POST
    )
    public void setHook(
            @ApiParam(value = "Hook", required = true) @Valid @RequestBody String hook
    ) throws ModelMappingException, HeaderNotFoundException, InterruptedException, ServiceConnectionException, IOException {

        LOG.log(Level.WARNING, "BODY: " + hook, hook);
        ordersBs.setHook(hook);

    }
 
}
