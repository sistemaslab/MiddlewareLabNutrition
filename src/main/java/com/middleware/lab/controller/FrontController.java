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

import com.middleware.provider.bs.InventoryBs;
import com.middleware.provider.bs.OrdersBs;
import com.middleware.provider.bs.PricingBs;
import com.middleware.provider.bs.SkuBs;
import com.middleware.provider.exception.HeaderNotFoundException;
import com.middleware.provider.exception.ModelMappingException;
import com.middleware.provider.exception.ServiceConnectionException;
import com.middleware.provider.inventory.model.rest.RequestUpdateInventory;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Natalia Manrique
 */
@RestController
@RequestMapping(value = "lab/api/front")
public class FrontController extends AbstractController<RequestUpdateInventory> {

    @Autowired
    InventoryBs inventoryBs;

    @Autowired
    PricingBs pricingBs;

    @Autowired
    OrdersBs ordersBs;

    @Autowired
    SkuBs skuBs;

    private static final Logger LOG = Logger.getLogger(FrontController.class.getName());

    @CrossOrigin()
    @RequestMapping(
            value = {"/inventory/search"},
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Object> searchInventory(
            @RequestParam("take") int take,
            @RequestParam("skip") int skip,
            @RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize,
            @RequestParam(name = "filter", required = false) String filter,
            @RequestParam(name = "filter[logic]", required = false) String logic,
            @RequestParam(name = "filter[filters][0][field]", required = false) String field,
            @RequestParam(name = "filter[filters][0][operator]", required = false) String operator,
            @RequestParam(name = "filter[filters][0][value]", required = false) String value,
            @RequestParam(name = "filter[filters][1][field]", required = false) String field1,
            @RequestParam(name = "filter[filters][1][operator]", required = false) String operator1,
            @RequestParam(name = "filter[filters][1][value]", required = false) String value1
    ) throws ModelMappingException, HeaderNotFoundException, InterruptedException, ServiceConnectionException, IOException {
        LOG.info("LLEGUE");
        String productId = "";
        String warehouse = "";
        if (logic == null) {
            warehouse = "null";
        } else {
            warehouse = value;
        }
        if (value1 == null) {
            productId = "null";
        } else {
            productId = value1;
        }
        return new ResponseEntity<Object>(mapperToJson(inventoryBs.searchInventory(productId, warehouse, pageSize, page)), getDefaultHeaders(), HttpStatus.OK);

    }

    @CrossOrigin()
    @RequestMapping(
            value = {"order/search"},
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Object> searchOrders(
            @RequestParam("take") int take,
            @RequestParam("skip") int skip,
            @RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize,
            @RequestParam(name = "filter", required = false) String filter,
            @RequestParam(name = "filter[logic]", required = false) String logic,
            @RequestParam(name = "filter[filters][0][field]", required = false) String field,
            @RequestParam(name = "filter[filters][0][operator]", required = false) String operator,
            @RequestParam(name = "filter[filters][0][value]", required = false) String value
    ) throws ModelMappingException, HeaderNotFoundException, InterruptedException, ServiceConnectionException, IOException {
        LOG.info("LLEGUE" + filter);
        String buscar = "";
        if (logic == null) {
            buscar = "null";
        } else {
            buscar = value;
        }
        return new ResponseEntity<Object>(mapperToJson(ordersBs.searchOrders(pageSize, page, filter, buscar)), getDefaultHeaders(), HttpStatus.OK);
    }

     @CrossOrigin()
    @RequestMapping(
            value = {"order/searchFailed"},
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Object> searchOrdersFailed(
            @RequestParam("take") int take,
            @RequestParam("skip") int skip,
            @RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize,
            @RequestParam(name = "filter", required = false) String filter,
            @RequestParam(name = "filter[logic]", required = false) String logic,
            @RequestParam(name = "filter[filters][0][field]", required = false) String field,
            @RequestParam(name = "filter[filters][0][operator]", required = false) String operator,
            @RequestParam(name = "filter[filters][0][value]", required = false) String value
    ) throws ModelMappingException, HeaderNotFoundException, InterruptedException, ServiceConnectionException, IOException {
        LOG.info("LLEGUE" + filter);
        String buscar = "";
        if (logic == null) {
            buscar = "null";
        } else {
            buscar = value;
        }
        return new ResponseEntity<Object>(mapperToJson(ordersBs.searchOrdersFailed(pageSize, page, filter, buscar)), getDefaultHeaders(), HttpStatus.OK);
    }

    
    @CrossOrigin()
    @RequestMapping(
            value = {"order/search/{numPedido}"},
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Object> searchOrder(
            @PathVariable(value = "numPedido") String numPedido
    ) throws ModelMappingException, HeaderNotFoundException, InterruptedException, ServiceConnectionException, IOException {
        LOG.info("LLEGUE");
        return new ResponseEntity<Object>(mapperToJson(ordersBs.searchOrder(numPedido)), getDefaultHeaders(), HttpStatus.OK);
    }

    @CrossOrigin()
    @RequestMapping(
            value = {"/prices/search"},
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Object> searchPricing(
            @RequestParam("take") int take,
            @RequestParam("skip") int skip,
            @RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize,
            @RequestParam(name = "filter", required = false) String filter,
            @RequestParam(name = "filter[logic]", required = false) String logic,
            @RequestParam(name = "filter[filters][0][field]", required = false) String field,
            @RequestParam(name = "filter[filters][0][operator]", required = false) String operator,
            @RequestParam(name = "filter[filters][0][value]", required = false) String value
    ) throws ModelMappingException, HeaderNotFoundException, InterruptedException, ServiceConnectionException, IOException {
        LOG.info("LLEGUE" + filter);
        String buscar = "";
        if (logic == null) {
            buscar = "null";
        } else {
            buscar = value;
        }
        return new ResponseEntity<Object>(mapperToJson(pricingBs.searchPricing(buscar, pageSize, page)), getDefaultHeaders(), HttpStatus.OK);

    }

    @CrossOrigin()
    @RequestMapping(
            value = {"/sku"},
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Object> searchInventory(
            @RequestParam("take") int take,
            @RequestParam("skip") int skip,
            @RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize,
            @RequestParam(name = "filter", required = false) String filter,
            @RequestParam(name = "filter[logic]", required = false) String logic,
            @RequestParam(name = "filter[filters][0][field]", required = false) String field,
            @RequestParam(name = "filter[filters][0][operator]", required = false) String operator,
            @RequestParam(name = "filter[filters][0][value]", required = false) String value
    ) throws ModelMappingException, HeaderNotFoundException, InterruptedException, ServiceConnectionException, IOException {
        LOG.info("LLEGUE");
        return new ResponseEntity<Object>(mapperToJson(skuBs.getSkus()), getDefaultHeaders(), HttpStatus.OK);

    }
    /*
    @CrossOrigin()
    @RequestMapping(
            value = {"sku/all"},
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Object> requestSkus() throws ModelMappingException, HeaderNotFoundException, InterruptedException, ServiceConnectionException, IOException {
        LOG.info("LLEGUE");
        return new ResponseEntity<Object>(mapperToJson(skuBs.requestSkus()), getDefaultHeaders(), HttpStatus.OK);
    }*/

}
