package com.middleware.provider.consumer.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.middleware.lab.model.rest.Product;
import com.middleware.provider.catalog.model.rest.RequestCreateProduct;
import com.middleware.provider.catalog.model.rest.RequestCreateProductSpecification;
import com.middleware.provider.catalog.model.rest.RequestCreateSku;
import com.middleware.provider.catalog.model.rest.ResponseCreateProduct;
import com.middleware.provider.catalog.model.rest.ResponseCreateProductSpecification;
import com.middleware.provider.catalog.model.rest.ResponseCreateSku;
import com.middleware.provider.catalog.model.rest.ResponseSearchWarehouses;
import com.middleware.provider.catalog.model.rest.SpecificationsRequest;
import com.middleware.provider.catalog.model.rest.SpecificationsResponse;
import com.middleware.provider.catalog.model.rest.SpecificationsValuesListResponse;
import com.middleware.provider.catalog.model.rest.SpecificationsValuesRequest;
import com.middleware.provider.catalog.model.rest.SpecificationsValuesResponse;
import com.middleware.provider.exception.ModelMappingException;
import com.middleware.provider.exception.ServiceConnectionException;
import com.middleware.provider.inventory.model.rest.RequestVtexUpdateInventory;
import com.middleware.provider.inventory.model.rest.ResponseInventoryLab;
import com.middleware.provider.orders.model.rest.ListFeed;
import com.middleware.provider.orders.model.rest.RequestTicket;
import com.middleware.provider.orders.model.rest.RequestTracking;
import com.middleware.provider.orders.model.rest.RequestTrackingEvents;
import com.middleware.provider.pricing.model.rest.RequestVtexUpdatePricing;
import com.middleware.provider.sku.model.rest.Sku;
import com.middleware.provider.vtex.model.rest.Client;
import com.middleware.provider.vtex.model.rest.Price;
import com.middleware.provider.vtex.model.rest.ResponseCheckout;
import com.middleware.provider.vtex.model.rest.ResponseOrderVtex;
import java.io.File;
import java.io.FileOutputStream;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * @author Natalia Manrique
 *
 */
@Service("VtexApiRestClient")
public class VtexApiRestClient extends VtexApiRestAbstractClient {

    private static final String URL_CREATE_SPECIFICATION_VALUES = "https://labnutrition.vtexcommercestable.com.br/api/catalog_system/pvt/specification/fieldValue";
    private static final String URL_GET_SPECIFICATION_VALUES = "https://labnutrition.vtexcommercestable.com.br/api/catalog_system/pub/specification/fieldvalue/";
    private static final String URL_SKU_SPECIFICATION = "https://labnutrition.vtexcommercestable.com.br/api/catalog/pvt/stockkeepingunit/";
    private static final String URL_GET_SKU_ID_BY_REF_ID = "https://labnutrition.vtexcommercestable.com.br/api/catalog_system/pvt/sku/stockkeepingunitidbyrefid/{refId}";
    private static final String URL_UPDATE_SKU = "https://labnutrition.vtexcommercestable.com.br/api/catalog/pvt/stockkeepingunit/";

    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(VtexApiRestClient.class);
    private static final Logger LOG = Logger.getLogger(VtexApiRestClient.class.getName());

    public ObjectMapper getMapper() {
        return mapper;
    }

    @Value("${spring.profiles.active}")
    private String profile;

    public ResponseEntity<String> setInventoryBySKU(String requestUrl, RequestVtexUpdateInventory body, String whiteLabel) throws ServiceConnectionException, IOException, ModelMappingException {

        System.out.println("Request Url: " + requestUrl);
        String bodyRequest = mapper.writeValueAsString(body);
        ResponseEntity<String> response = null;
        LOGGER.info("Request Url: " + requestUrl + "Body Request log " + bodyRequest);
        response = this.callVtexService(buildUri(requestUrl), HttpMethod.PUT, bodyRequest);

        LOGGER.warn("Response Status " + response.getStatusCode());

        return response;

    }

    public ResponseEntity<String> setPricingBySKU(String requestUrl, RequestVtexUpdatePricing body) throws ServiceConnectionException, IOException, ModelMappingException {

        System.out.println("Request Url: " + requestUrl);
        String bodyRequest = mapper.writeValueAsString(body);
        LOGGER.info("Body Request log " + bodyRequest);
        ResponseEntity<String> response = null;
        response = this.callVtexService(buildUri(requestUrl), HttpMethod.PUT, bodyRequest);

        LOGGER.info("Response Status " + response.getStatusCode());

        return response;
    }

    public ResponseEntity<String> getAllSkus(String requestUrl) throws ServiceConnectionException, IOException, ModelMappingException {

        System.out.println("Request Url: " + requestUrl);

        ResponseEntity<String> response;

        // Guardar la respuesta del refIds
        response = this.callVtexService(buildUri(requestUrl), HttpMethod.GET);

        LOGGER.warn("Body Response log " + response.getBody().toString());
        LOGGER.warn("Response Status " + response.getStatusCode());

        return response;

    }

    public Price getPrice(String requestUrl) throws ServiceConnectionException, IOException, ModelMappingException {

        System.out.println("Request Url: " + requestUrl);
        Price price = new Price();

        ResponseEntity<String> response;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        response = this.callVtexService(buildUri(requestUrl), HttpMethod.GET);

        price = objectMapper.readValue(response.getBody(), Price.class);

        LOGGER.warn("Body Response log " + response.getBody().toString());
        LOGGER.warn("Response Status " + response.getStatusCode());

        return price;

    }

    public com.middleware.provider.sku.model.rest.SkuDetail getRefBySku(String requestUrl) throws ServiceConnectionException, IOException, ModelMappingException {
        com.middleware.provider.sku.model.rest.SkuDetail skuDetail = new com.middleware.provider.sku.model.rest.SkuDetail();

        System.out.println("Request Url: " + requestUrl);

        ResponseEntity<String> response;

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        response = this.callVtexService(buildUri(requestUrl), HttpMethod.GET);

        skuDetail = objectMapper.readValue(response.getBody(), com.middleware.provider.sku.model.rest.SkuDetail.class);
        // Guardar la respuesta del refIds

        LOGGER.warn("Body Response log " + response.getBody().toString());
        LOGGER.warn("Response Status " + response.getStatusCode());

        return skuDetail;

    }

    public ResponseEntity<String> getRefs(String requestUrl, ArrayList<String> body) throws ServiceConnectionException, IOException, ModelMappingException {

        System.out.println("Request Url: " + requestUrl);
        String bodyRequest = mapper.writeValueAsString(body);
        LOGGER.warn("Body Request log " + bodyRequest);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
        ResponseEntity<String> response;

        // Guardar la respuesta del refIds
        response = this.callVtexService(buildUri(requestUrl), HttpMethod.POST, bodyRequest);

        LOGGER.warn("Body Response log " + response.getBody().toString());
        LOGGER.warn("Response Status " + response.getStatusCode());

        return response;

    }

    public boolean getFeed(String requestUrl) throws ServiceConnectionException, IOException, ModelMappingException {

        System.out.println("Request Url: " + requestUrl);

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
        Date date = new Date();
        String actualDate = formatter.format(date);

        ResponseEntity<String> response;

        // Guardar la respuesta de la call
        response = this.callVtexService(buildUri(requestUrl), HttpMethod.GET);

        LOGGER.warn("Body Response log " + response.getBody());
        LOGGER.warn("Response Status " + response.getStatusCode());

        ArrayList<ListFeed> listFeed = new ArrayList<ListFeed>();

        actualDate = formatter.format(date);

        if (response.getStatusCode() == HttpStatus.OK) {
            return true;
        } else {
            return false;
        }
    }

    public ResponseOrderVtex sendRequestOrderToVTEX(String orderId, String requestUrl) throws ServiceConnectionException, IOException, ModelMappingException {
        ResponseOrderVtex responseOrder = new ResponseOrderVtex();
        System.out.println("Request Url: " + requestUrl);

        ResponseEntity<String> response;

        // Guardar la respuesta del call order
        try {
            response = this.callVtexService(buildUri(requestUrl + orderId), HttpMethod.GET);
        } catch (Exception e) {
            System.out.println("Problema al enviar a VTEX");
        }
        response = this.callVtexService(buildUri(requestUrl + orderId), HttpMethod.GET);
        LOGGER.warn("PEDIDO ******  " + response.getBody());
        writeUsingOutputStream(response.getBody());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        responseOrder = objectMapper.readValue(response.getBody(), ResponseOrderVtex.class);

        LOGGER.warn("Response Status " + response.getStatusCode());

        return responseOrder;
    }

    public boolean cancelOrder(String requestUrl) throws ServiceConnectionException, IOException, ModelMappingException {

        System.out.println("Request Url: " + requestUrl);
        ResponseEntity<String> response;

        // Guardar la respuesta de la call
        response = this.callVtexService(buildUri(requestUrl), HttpMethod.POST);

        LOGGER.warn("Body Response log " + response.getBody());
        LOGGER.warn("Response Status " + response.getStatusCode());

        if (response.getStatusCode() == HttpStatus.OK) {
            return true;
        } else {
            return false;
        }
    }

    public boolean startHandling(String requestUrl) throws ServiceConnectionException, IOException, ModelMappingException {
        try {
            System.out.println("Request Url: " + requestUrl);
            ResponseEntity<String> response;

            // Guardar la respuesta de la call
            response = this.callVtexService(buildUri(requestUrl), HttpMethod.POST);
        } catch (Exception e) {
            LOGGER.warn("ERROR START");
        }

        return true;
    }

    public boolean invoiceOrder(String requestUrl, RequestTicket requestTicket) throws ServiceConnectionException, IOException, ModelMappingException {

        System.out.println("Request Url: " + requestUrl);
        ResponseEntity<String> response;
        String bodyRequest = mapper.writeValueAsString(requestTicket);

        // Guardar la respuesta de la call
        response = this.callVtexService(buildUri(requestUrl), HttpMethod.POST, bodyRequest);

        LOGGER.warn("Body Response log " + response.getBody());
        LOGGER.warn("Response Status " + response.getStatusCode());

        if (response.getStatusCode() == HttpStatus.OK) {
            return true;
        } else {
            return false;
        }
    }

    public boolean trackingOrder(String requestUrl, RequestTracking requestTracking) throws ServiceConnectionException, IOException, ModelMappingException {

        System.out.println("Request Url: " + requestUrl);
        ResponseEntity<String> response;
        String bodyRequest = mapper.writeValueAsString(requestTracking);

        // Guardar la respuesta de la call
        response = this.callVtexServicePatch(buildUri(requestUrl), HttpMethod.PATCH, bodyRequest);

        LOGGER.warn("Body Response log " + response.getBody());
        LOGGER.warn("Response Status " + response.getStatusCode());

        if (response.getStatusCode() == HttpStatus.OK) {
            return true;
        } else {
            return false;
        }
    }

    public boolean trackingEventsOrder(String requestUrl, RequestTrackingEvents requestTrackingEvents) throws ServiceConnectionException, IOException, ModelMappingException {

        System.out.println("Request Url: " + requestUrl);
        ResponseEntity<String> response;
        String bodyRequest = mapper.writeValueAsString(requestTrackingEvents);

        // Guardar la respuesta de la call
        response = this.callVtexService(buildUri(requestUrl), HttpMethod.PUT, bodyRequest);

        LOGGER.warn("Body Response log " + response.getBody());
        LOGGER.warn("Response Status " + response.getStatusCode());

        if (response.getStatusCode() == HttpStatus.OK) {
            return true;
        } else {
            return false;
        }
    }

    public ResponseEntity<String> searchSkuVtex(String requestUrl) throws ServiceConnectionException, IOException, ModelMappingException {

        System.out.println("Request Url: " + requestUrl);

        ResponseEntity<String> response;

        // Guardar la respuesta del detalle del SKU
        response = this.callVtexService(buildUri(requestUrl), HttpMethod.GET);

        LOGGER.warn("Body Response log " + response.getBody().toString());
        LOGGER.warn("Response Status " + response.getStatusCode());

        return response;

    }

    public ResponseCheckout validateCheckout(String id, String requestUrl) throws ServiceConnectionException, IOException, ModelMappingException {
        boolean resp = true;

        System.out.println("Request Url: " + requestUrl + id);

        ResponseEntity<String> response;

        // Guardar la respuesta del detalle del SKU
        response = this.callVtexService(buildUri(requestUrl + id), HttpMethod.GET);
        writeUsingOutputStream(response.getBody());
        LOGGER.info("Body Response log " + response.getBody().toString());
        LOGGER.info("Response Status " + response.getStatusCode());
        //validar response
        ResponseCheckout responseCheckout = new ResponseCheckout();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        responseCheckout = objectMapper.readValue(response.getBody(), ResponseCheckout.class);

        return responseCheckout;

    }

    public List<ResponseSearchWarehouses> searchWarehouses(String requestUrl) throws ServiceConnectionException, IOException, ModelMappingException {
        boolean resp = true;

        System.out.println("Request Url: " + requestUrl);

        ResponseEntity<String> response;

        // Guardar la respuesta del detalle del SKU
        response = this.callVtexService(buildUri(requestUrl), HttpMethod.GET);
        writeUsingOutputStream(response.getBody());
        LOGGER.info("Body Response log " + response.getBody().toString());
        LOGGER.info("Response Status " + response.getStatusCode());
        //validar response
        List<ResponseSearchWarehouses> responseSearchWarehouses = new ArrayList<ResponseSearchWarehouses>();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        responseSearchWarehouses = objectMapper.readValue(response.getBody(), objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, ResponseSearchWarehouses.class));

        return responseSearchWarehouses;

    }

    public ResponseCreateProduct setNewProduct(RequestCreateProduct requestCreateProduct, String requestUrl) throws ServiceConnectionException, IOException, ModelMappingException {
        LOGGER.info("Request Url: " + requestUrl);
        ResponseEntity<String> response;
        // Guardar la respuesta del detalle del SKU
        String bodyRequest = mapper.writeValueAsString(requestCreateProduct);
        // Guardar la respuesta de la call
        response = this.callVtexService(buildUri(requestUrl), HttpMethod.POST, bodyRequest);
        LOGGER.info("Body Response log " + response.getBody().toString());
        LOGGER.info("Response Status " + response.getStatusCode());
        //validar response
        ResponseCreateProduct responseCreateProduct = new ResponseCreateProduct();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        responseCreateProduct = objectMapper.readValue(response.getBody(), ResponseCreateProduct.class);
        return responseCreateProduct;

    }

    public ResponseCreateSku setNewSku(RequestCreateSku requestCreateSku, String requestUrl) throws ServiceConnectionException, IOException, ModelMappingException {
        LOGGER.info("Request Url: " + requestUrl);
        ResponseEntity<String> response;
        // Guardar la respuesta del detalle del SKU
        String bodyRequest = mapper.writeValueAsString(requestCreateSku);
        // Guardar la respuesta de la call
        response = this.callVtexService(buildUri(requestUrl), HttpMethod.POST, bodyRequest);
        LOGGER.info("Body Response log " + response.getBody().toString());
        LOGGER.info("Response Status " + response.getStatusCode());
        //validar response
        ResponseCreateSku responseCreateSku = new ResponseCreateSku();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        responseCreateSku = objectMapper.readValue(response.getBody(), ResponseCreateSku.class);
        return responseCreateSku;

    }

    public String getSkuByRefId(String refId)
            throws JsonProcessingException, ServiceConnectionException {
        Map<String, String> uriParams = new HashMap();
        uriParams.put("refId", refId);
        URI uri = buildUri(URL_GET_SKU_ID_BY_REF_ID, uriParams, null);
        ResponseEntity<String> response;
        response = this.callVtexService(uri, HttpMethod.GET, String.class);
        System.out.println("RESPONSE" + response);
        return response.getBody();
    }

    public ResponseCreateSku updateSku(RequestCreateSku sku, String skuId)
            throws JsonProcessingException, ServiceConnectionException, IOException {

        ResponseEntity<String> response;
        System.out.println("REQUEST" + sku.toString());

        String bodyRequest = mapper.writeValueAsString(sku);

        response = this.callVtexService(buildUri(URL_UPDATE_SKU + skuId), HttpMethod.PUT, bodyRequest);
        LOGGER.info("Body Response log " + response.getBody().toString());
        LOGGER.info("Response Status " + response.getStatusCode());
        //validar response
        ResponseCreateSku responseCreateSku = new ResponseCreateSku();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        responseCreateSku = objectMapper.readValue(response.getBody(), ResponseCreateSku.class);
        return responseCreateSku;

    }

    public ResponseCreateProductSpecification setNewSpecification(RequestCreateProductSpecification requestCreateProductSpecification, String requestUrl) throws ServiceConnectionException, IOException, ModelMappingException {
        LOGGER.info("Request Url: " + requestUrl);
        ResponseEntity<String> response;
        // Guardar la respuesta del detalle del SKU
        String bodyRequest = mapper.writeValueAsString(requestCreateProductSpecification);
        // Guardar la respuesta de la call
        response = this.callVtexService(buildUri(requestUrl), HttpMethod.POST, bodyRequest);
        LOGGER.info("Body Response log " + response.getBody().toString());
        LOGGER.info("Response Status " + response.getStatusCode());
        //validar response
        ResponseCreateProductSpecification responseCreateProductSpecification = new ResponseCreateProductSpecification();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        responseCreateProductSpecification = objectMapper.readValue(response.getBody(), ResponseCreateProductSpecification.class);
        return responseCreateProductSpecification;

    }

    public SpecificationsValuesResponse createSpecificationField(SpecificationsValuesRequest specificationsValuesRequest)
            throws JsonProcessingException, ServiceConnectionException, IOException {

        ResponseEntity<String> response;
        // Guardar la respuesta del detalle del SKU
        String bodyRequest = mapper.writeValueAsString(specificationsValuesRequest);
        // Guardar la respuesta de la call
        response = this.callVtexService(buildUri(URL_CREATE_SPECIFICATION_VALUES), HttpMethod.POST, bodyRequest);
        LOGGER.info("Body Response log " + response.getBody().toString());
        LOGGER.info("Response Status " + response.getStatusCode());
        //validar response
        SpecificationsValuesResponse specificationsValuesResponse = new SpecificationsValuesResponse();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        specificationsValuesResponse = objectMapper.readValue(response.getBody(), SpecificationsValuesResponse.class);
        return specificationsValuesResponse;
    }

    public ResponseCreateProduct searchProduct(String requestUrl) throws ServiceConnectionException, IOException, ModelMappingException {

        LOG.info("Request Url: " + requestUrl);
        ResponseEntity<String> response;

        // Guardar la respuesta del detalle del SKU
        response = this.callVtexService(buildUri(requestUrl), HttpMethod.GET);
        LOG.info("Body Response log " + response.getBody().toString());
        LOG.info("Response Status " + response.getStatusCode());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        ResponseCreateProduct emp = objectMapper.readValue(response.getBody(), ResponseCreateProduct.class);

        return emp;

    }

    public Client[] searchDni(String requestUrl) throws ServiceConnectionException, IOException, ModelMappingException {

        LOG.info("Request Url: " + requestUrl);
        ResponseEntity<String> response;

        // Guardar la respuesta del detalle del SKU
        response = this.callVtexService(buildUri(requestUrl), HttpMethod.GET);
        LOG.info("Body Response log " + response.getBody().toString());
        LOG.info("Response Status " + response.getStatusCode());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Client[] emp = objectMapper.readValue(response.getBody(), Client[].class);

        for (Client cliente : emp) {
            System.out.println("VOY A MOSTRAR " + cliente.getEmail() + " " + cliente.getId() + ";");
        }
        return emp;

    }

    public SpecificationsResponse createSkuSpecifications(SpecificationsRequest specificationsRequest, String skuId) throws JsonProcessingException, ServiceConnectionException, IOException {
        String bodyRequest = mapper.writeValueAsString(specificationsRequest);
        ResponseEntity<String> response;

        // Guardar la respuesta del detalle del SKU
        response = this.callVtexService(buildUri(URL_SKU_SPECIFICATION + skuId + "/specification"), HttpMethod.POST, bodyRequest);
        LOG.info("Body Response log " + response.getBody().toString());
        LOG.info("Response Status " + response.getStatusCode());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        SpecificationsResponse specificationsResponse = objectMapper.readValue(response.getBody(), SpecificationsResponse.class);

        return specificationsResponse;
    }

    public SpecificationsResponse updateSkuSpecifications(SpecificationsRequest specificationsRequest, String skuId) throws JsonProcessingException, ServiceConnectionException, IOException {
        String bodyRequest = mapper.writeValueAsString(specificationsRequest);
        ResponseEntity<String> response;

        // Guardar la respuesta del detalle del SKU
        response = this.callVtexService(buildUri(URL_SKU_SPECIFICATION + skuId + "/specification"), HttpMethod.PUT, bodyRequest);
        LOG.info("Body Response log " + response.getBody().toString());
        LOG.info("Response Status " + response.getStatusCode());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        SpecificationsResponse specificationsResponse = objectMapper.readValue(response.getBody(), SpecificationsResponse.class);

        return specificationsResponse;
    }

    public List<SpecificationsResponse> getSkuSpecifications(String skuId) throws JsonProcessingException, ServiceConnectionException, IOException {
        ResponseEntity<String> response;

        // Guardar la respuesta del detalle del SKU
        response = this.callVtexService(buildUri(URL_SKU_SPECIFICATION + skuId + "/specification"), HttpMethod.GET);
        LOG.info("Response Status " + response.getStatusCode());
        List<SpecificationsResponse> specificationsResponse = new ArrayList<SpecificationsResponse>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            specificationsResponse = objectMapper.readValue(response.getBody(), objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, SpecificationsResponse.class));
            System.out.println("ESPECIFICACIONES " + specificationsResponse.toString());
            return specificationsResponse;
        } catch (Exception e) {
            return specificationsResponse;
        }

    }

    public List<SpecificationsValuesListResponse> getSpecificationsValues(String fieldId)
            throws JsonProcessingException, ServiceConnectionException, IOException {

        ResponseEntity<String> response;

        // Guardar la respuesta del detalle del SKU
        response = this.callVtexService(buildUri(URL_GET_SPECIFICATION_VALUES + fieldId), HttpMethod.GET);
        LOG.info("Body Response log " + response.getBody().toString());
        LOG.info("Response Status " + response.getStatusCode());

        List<SpecificationsValuesListResponse> specificationsValuesListResponse = new ArrayList<SpecificationsValuesListResponse>();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        specificationsValuesListResponse = objectMapper.readValue(response.getBody(), objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, SpecificationsValuesListResponse.class));

        return specificationsValuesListResponse;

    }

    private static void writeUsingOutputStream(String data) {
        OutputStream os = null;
        try {
            os = new FileOutputStream(new File("D:\\parsejson\\json.txt"));
            os.write(data.getBytes(), 0, data.length());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
