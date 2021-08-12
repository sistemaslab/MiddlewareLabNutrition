package com.middleware.lab.controller;


import java.io.IOException;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.middleware.provider.bs.SkuBs;
import com.middleware.provider.exception.HeaderNotFoundException;
import com.middleware.provider.exception.ModelMappingException;
import com.middleware.provider.exception.ServiceConnectionException;
import com.middleware.provider.sku.model.rest.RequestSkus;
import io.swagger.annotations.ApiParam;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * @author Natalia Manrique
 */
@RestController
@RequestMapping(value = "lab/api/sku")
public class WarehouseController extends AbstractController<RequestSkus> {

    @Autowired
    SkuBs skuBs;

    private static final Logger LOG = Logger.getLogger(WarehouseController.class.getName());

    @RequestMapping(
            value = {"/{idSku}"},
            method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Object> requestToWarehouse(
            @PathVariable(value = "idSku") String idSku,
            @ApiParam(value = "RequestSkus", required = true) @Valid @RequestBody RequestSkus requestSkus

    ) throws ModelMappingException, HeaderNotFoundException, InterruptedException, ServiceConnectionException, IOException {
        System.out.println("VOY A GUARDAR " + idSku +"EL REF"+ requestSkus.getRefId());

        return new ResponseEntity<Object>(mapperToJson(skuBs.requestToSku(requestSkus, idSku)), getDefaultHeaders(), HttpStatus.OK);
    }

}
