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

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Natalia Manrique
 */
@RestController
@RequestMapping(value = "lab/api/catalog")
public class CatalogController extends AbstractController<RequestSkus> {

    @Autowired
    SkuBs skuBs;

    private static final Logger LOG = Logger.getLogger(CatalogController.class.getName());

    @RequestMapping(
            value = {"/brand/{idBrand}"},
            method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Object> setBrand(
            @RequestParam(name = "name", required = true) String name,
            @PathVariable(value = "idBrand") Integer idBrand
    ) throws ModelMappingException, HeaderNotFoundException, InterruptedException, ServiceConnectionException, IOException {

        return new ResponseEntity<Object>(mapperToJson(skuBs.setBrand(idBrand, name)), getDefaultHeaders(), HttpStatus.OK);
    }

    @RequestMapping(
            value = {"/category/{idCategory}"},
            method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Object> setCategory(
            @RequestParam(name = "name", required = true) String name,
            @RequestParam(name = "department", required = true) Integer department,
            @RequestParam(name = "department_name", required = true) String department_name,
            @PathVariable(value = "idCategory") Integer idCategory
    ) throws ModelMappingException, HeaderNotFoundException, InterruptedException, ServiceConnectionException, IOException {

        return new ResponseEntity<Object>(mapperToJson(skuBs.setCategory(idCategory, name, department , department_name)), getDefaultHeaders(), HttpStatus.OK);
    }

    @RequestMapping(
            value = {"/product/{productId}"},
            method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Object> syncProduct(
            @PathVariable(value = "productId") String productId
    ) throws ModelMappingException, HeaderNotFoundException, InterruptedException, ServiceConnectionException, IOException {

        return new ResponseEntity<Object>(mapperToJson(skuBs.syncProduct(productId)), getDefaultHeaders(), HttpStatus.OK);
    }

}
