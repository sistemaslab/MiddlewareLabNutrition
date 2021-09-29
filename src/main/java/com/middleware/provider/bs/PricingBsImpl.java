package com.middleware.provider.bs;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.middleware.lab.data.mapper.Mapper;
import com.middleware.lab.data.mapper.MapperInventory;
import com.middleware.lab.data.mapper.MapperPrices;
import com.middleware.lab.model.db.SkuHomologation;
import com.middleware.lab.repository.PriceLogRepository;
import java.io.IOException;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import com.middleware.lab.repository.PricingRepository;
import com.middleware.lab.repository.SkuHomologationRepository;
import com.middleware.provider.consumer.navasoft.SoapClient;
import com.middleware.provider.exception.ModelMappingException;
import com.middleware.provider.exception.ServiceConnectionException;
import com.middleware.provider.inventory.model.rest.ResponseInventoryLab;
import com.middleware.provider.inventory.model.rest.ResponseUpdateInventory;
import com.middleware.provider.pricing.model.rest.RequestUpdatePricing;
import com.middleware.provider.pricing.model.rest.ResponsePricingLab;
import com.middleware.provider.pricing.model.rest.ResponseSearchPricing;
import com.middleware.provider.pricing.model.rest.ResponseSearchPricingComplete;
import com.middleware.provider.pricing.model.rest.ResponseUpdatePricing;
import com.middleware.provider.utils.Parser;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import lab.navasoft.model.soap.GetpreciolistaResponse;

@Service("PricingBsImpl")

public class PricingBsImpl implements PricingBs {

    @Autowired
    PricingRepository pricingRepository;

    @Autowired
    PriceLogRepository priceLogRepository;

    @Autowired
    SoapClient soapClient;

    @Autowired
    SkuHomologationRepository skuHomologationRepository;

    @Autowired
    JmsTemplate jmsTemplate;

    @Value("${lab.url}")
    private String url;

    @Value("${lab.callbackPrices}")
    private String callbackPrices;

    @Value("${lab.activemq.broker-url}")
    private String brokerUrl;

    private final EntityManager entityManager;

    @Autowired
    public PricingBsImpl(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    private static final Logger LOG = Logger.getLogger(PricingBsImpl.class.getName());

    public ResponseUpdatePricing setPricing(RequestUpdatePricing requestUpdatePricing)
            throws InterruptedException, ServiceConnectionException, IOException, ModelMappingException {
        ResponseUpdatePricing responseUpdatePricing = new ResponseUpdatePricing();
        responseUpdatePricing.setTransactionId(requestUpdatePricing.getTransactionId());
        LOG.info("Enviando mensaje a la cola de precios");
        try {
            jmsTemplate.setDefaultDestinationName("PricingQueue");
            jmsTemplate.convertAndSend(Parser.objToJsonString(requestUpdatePricing));
            responseUpdatePricing.setMessage("Mensaje Encolado");
            responseUpdatePricing.setCodeId("200 exitoso");
        } catch (JmsException e) {
            LOG.warning("ERROR ENCOLANDO - >" + e.getMessage());
            responseUpdatePricing.setMessage("Error al encolar");
            responseUpdatePricing.setCodeId("500 error interno");
        }
        return responseUpdatePricing;

    }

    @Override
    public void updates()
            throws InterruptedException, ServiceConnectionException, IOException, ModelMappingException {
        List<SkuHomologation> skus = skuHomologationRepository.findAllSkus();
        for (SkuHomologation sku : skus) {
            GetpreciolistaResponse getpreciolistaResponse = soapClient.getPrice(MapperPrices.toPricesLab(sku.getRefId()), this.url, this.callbackPrices);
            getpreciolistaResponse.getGetpreciolistaResult();
            System.out.println("RESPUESTAS UPDATES" + getpreciolistaResponse.getGetpreciolistaResult());
            String prices = "[";
            prices = prices + getpreciolistaResponse.getGetpreciolistaResult() + "]";
            getpreciolistaResponse.setGetpreciolistaResult(prices);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
            ArrayList<ResponsePricingLab> responsePricingLab;
            responsePricingLab = objectMapper.readValue(getpreciolistaResponse.getGetpreciolistaResult(), objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, ResponsePricingLab.class));
            System.out.println(responsePricingLab.size());

            boolean r = false;
            for (ResponsePricingLab responsePricingLab1 : responsePricingLab) {
                if (responsePricingLab1.getError() == null) {
                    r = true;
                } else {
                    priceLogRepository.save(MapperPrices.toPriceLog(sku, responsePricingLab1));
                    System.out.println("RESPUESTAS ERROR" + responsePricingLab1.getError());
                }
            }
            if (r) {
                ResponseUpdatePricing responseUpdatePricing = setPricing(MapperPrices.toRequestPricing(responsePricingLab));
            }

        }

    }

    public ResponseSearchPricingComplete searchPricing(String productId, int pag, int num) {
        ResponseSearchPricingComplete response = new ResponseSearchPricingComplete();
        StoredProcedureQuery storedProcedureQuery = entityManager.createStoredProcedureQuery("lab_nutrition_db.get_prices");
        storedProcedureQuery.registerStoredProcedureParameter("id", String.class, ParameterMode.IN);
        storedProcedureQuery.registerStoredProcedureParameter("pag", int.class, ParameterMode.IN);
        storedProcedureQuery.registerStoredProcedureParameter("num", int.class, ParameterMode.IN);
        storedProcedureQuery.setParameter("id", productId);
        storedProcedureQuery.setParameter("pag", pag);
        storedProcedureQuery.setParameter("num", (num - 1) * pag);
        storedProcedureQuery.execute();
        List<Object[]> results = storedProcedureQuery.getResultList();
        List<ResponseSearchPricing> list = results.stream().map(result -> new ResponseSearchPricing(
                (BigDecimal) result[0],
                (String) result[1],
                (BigDecimal) result[2],
                (Date) result[3],
                (String) result[4],
                (String) result[5]
        )).collect(Collectors.toList());
        response.setData(list);
        response.setTotal(pricingRepository.count());
        System.out.println("TOTAL" + pricingRepository.count());
        System.out.println("LISTA" + list.toString());
        return response;
    }

}
