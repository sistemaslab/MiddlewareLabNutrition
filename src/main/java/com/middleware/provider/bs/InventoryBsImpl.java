package com.middleware.provider.bs;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import com.middleware.lab.repository.InventoryRepository;
import com.middleware.provider.consumer.services.VtexApiRestClient;
import com.middleware.provider.exception.ModelMappingException;
import com.middleware.provider.exception.ServiceConnectionException;
import com.middleware.provider.inventory.model.rest.RequestUpdateInventory;
import com.middleware.provider.inventory.model.rest.ResponseSearchInventory;
import com.middleware.provider.inventory.model.rest.ResponseSearchInventoryComplete;
import com.middleware.provider.inventory.model.rest.ResponseUpdateInventory;
import com.middleware.provider.utils.Parser;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import com.middleware.lab.client.LabClient;
import com.middleware.lab.data.mapper.Mapper;
import com.middleware.lab.data.mapper.MapperInventory;
import com.middleware.lab.data.mapper.MapperPrices;
import com.middleware.lab.model.db.SkuHomologation;
import com.middleware.lab.repository.InventoryLogRepository;
import com.middleware.lab.repository.SkuHomologationRepository;
import com.middleware.provider.consumer.navasoft.SoapClient;
import com.middleware.provider.inventory.model.rest.ResponseInventoryLab;
import com.middleware.provider.pricing.model.rest.ResponsePricingLab;
import com.middleware.provider.pricing.model.rest.ResponseUpdatePricing;
import java.util.ArrayList;
import lab.navasoft.model.soap.GetpreciolistaResponse;
import lab.navasoft.model.soap.GetstockalmResponse;

@Service("InventoryBsImpl")
public class InventoryBsImpl implements InventoryBs {

    @Autowired
    SoapClient soapClient;

    @Autowired
    JmsTemplate jmsTemplate;

    @Autowired
    InventoryRepository inventoryRepository;

    @Autowired
    InventoryLogRepository inventoryLogRepository;

    @Value("${lab.url}")
    private String url;

    @Value("${lab.callbackStock}")
    private String callbackStock;

    @Autowired
    SkuHomologationRepository skuHomologationRepository;

    @Value("${lab.activemq.broker-url}")
    private String brokerUrl;

    @Autowired
    VtexApiRestClient vtexApiRestClient;

    private final EntityManager entityManager;

    @Autowired
    public InventoryBsImpl(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    private static final Logger LOG = Logger.getLogger(InventoryBsImpl.class.getName());

    @Override
    public ResponseUpdateInventory setInventory(RequestUpdateInventory requestUpdateInventory)
            throws InterruptedException, ServiceConnectionException, IOException, ModelMappingException {
        ResponseUpdateInventory responseUpdateInventory = new ResponseUpdateInventory();
        responseUpdateInventory.setTransactionId(requestUpdateInventory.getTransactionId());

        try {
            LOG.info(" Enviando mensaje a la cola");
            responseUpdateInventory.setMessage("Mensaje Encolado");
            responseUpdateInventory.setCodeId("200 exitoso");

            jmsTemplate.setDefaultDestinationName("InventoryQueue");
            jmsTemplate.convertAndSend(Parser.objToJsonString(requestUpdateInventory));
        } catch (JmsException e) {
            LOG.warning("ERROR ENCOLANDO - >" + e.getMessage());
            responseUpdateInventory.setMessage("Error al encolar");
            responseUpdateInventory.setCodeId("500 error interno");
            LOG.info("ERROR ENCOLANDO");

        }
        return responseUpdateInventory;
    }

    @Override
    public void updates()
            throws InterruptedException, ServiceConnectionException, IOException, ModelMappingException {
        List<SkuHomologation> skus = skuHomologationRepository.findAllSkus();
        LOG.info("RESPUESTAS skus" + skus.size());
        String skusString = "";
        for (SkuHomologation sku : skus) {
            GetstockalmResponse getstockalmResponse = soapClient.getStock(MapperInventory.toInventoryLab(sku.getRefId()), this.url, this.callbackStock);
            LOG.info("RESPUESTAS UPDATES" + getstockalmResponse.toString());
            //Con la respuestas actualizar cada almacen
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
            objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            String response = "[" + getstockalmResponse.getGetstockalmResult() + "]";
            ArrayList<ResponseInventoryLab> responseInventoryLab;
            try {
                responseInventoryLab = objectMapper.readValue(response, objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, ResponseInventoryLab.class));
                LOG.info("TAM" + responseInventoryLab.size());
                boolean r = false;
                for (ResponseInventoryLab responseInventoryLab1 : responseInventoryLab) {
                    if (responseInventoryLab1.getError() == null) {
                        LOG.info(sku.getRefId() + "ACTUALIZAR " + responseInventoryLab1.getStoc().toString() + " " + responseInventoryLab1.getCodalm() + "Prod: " + responseInventoryLab1.getCodf());
                        r = true;
                    } else {
                        inventoryLogRepository.save(MapperInventory.toInventoryLog(sku, responseInventoryLab1.getError()));
                        LOG.info("RESPUESTAS ERROR" + responseInventoryLab1.getError());
                    }
                }
                if (r) {
                    ResponseUpdateInventory responseUpdateInventory = setInventory(MapperInventory.toRequestInventory(responseInventoryLab, "job"));

                }
            } catch (Exception e) {
                LOG.warning("RESPUESTAS ERROR" + response + "DEL SKU" + sku.getRefId());
                inventoryLogRepository.save(MapperInventory.toInventoryLog(sku, "ERROR MAPEO"));

            }

        }
    }

    @Override
    public void updatesInvoices(List<SkuHomologation> skus, String job)
            throws InterruptedException, ServiceConnectionException, IOException, ModelMappingException {
        String skusString = "";
        for (SkuHomologation sku : skus) {
            GetstockalmResponse getstockalmResponse = soapClient.getStock(MapperInventory.toInventoryLab(sku.getRefId()), this.url, this.callbackStock);
            LOG.info("RESPUESTAS UPDATES" + getstockalmResponse.toString());
            //Con la respuestas actualizar cada almacen
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
            objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            String response = "[" + getstockalmResponse.getGetstockalmResult() + "]";
            ArrayList<ResponseInventoryLab> responseInventoryLab;
            responseInventoryLab = objectMapper.readValue(response, objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, ResponseInventoryLab.class));
            System.out.println("TAM" + responseInventoryLab.size());
            boolean r = false;
            for (ResponseInventoryLab responseInventoryLab1 : responseInventoryLab) {
                if (responseInventoryLab1.getError() == null) {
                    System.out.println(sku.getRefId() + "ACTUALIZAR " + responseInventoryLab1.getStoc().toString() + " " + responseInventoryLab1.getCodalm() + "Prod: " + responseInventoryLab1.getCodf());
                    r = true;
                } else {
                    inventoryLogRepository.save(MapperInventory.toInventoryLog(sku, responseInventoryLab1.getError()));
                    System.out.println("RESPUESTAS ERROR" + responseInventoryLab1.getError());
                }
            }
            if (r) {
                ResponseUpdateInventory responseUpdateInventory = setInventory(MapperInventory.toRequestInventory(responseInventoryLab, job));

            }
        }
    }

    @Override
    public ResponseSearchInventoryComplete searchInventory(String productId, String warehouse, int pag, int num) {
        ResponseSearchInventoryComplete response = new ResponseSearchInventoryComplete();
        StoredProcedureQuery storedProcedureQuery = entityManager.createStoredProcedureQuery("lab_nutrition_db.search_inventory");
        storedProcedureQuery.registerStoredProcedureParameter("sku", String.class, ParameterMode.IN);
        storedProcedureQuery.registerStoredProcedureParameter("warehouse", String.class, ParameterMode.IN);
        storedProcedureQuery.registerStoredProcedureParameter("pag", int.class, ParameterMode.IN);
        storedProcedureQuery.registerStoredProcedureParameter("num", int.class, ParameterMode.IN);
        storedProcedureQuery.setParameter("sku", productId);
        storedProcedureQuery.setParameter("warehouse", warehouse);
        storedProcedureQuery.setParameter("pag", pag);
        storedProcedureQuery.setParameter("num", (num - 1) * pag);
        storedProcedureQuery.execute();
        List<Object[]> results = storedProcedureQuery.getResultList();
        List<ResponseSearchInventory> list = results.stream().map(result -> new ResponseSearchInventory(
                (String) result[0],
                (Integer) result[1],
                (Date) result[2],
                (String) result[3],
                (String) result[4],
                (String) result[5]
        )).collect(Collectors.toList());
        response.setData(list);
        response.setTotal(inventoryRepository.count());
        System.out.println("TOTAL" + inventoryRepository.count());
        System.out.println("LISTA" + list.toString());
        return response;

    }

}
