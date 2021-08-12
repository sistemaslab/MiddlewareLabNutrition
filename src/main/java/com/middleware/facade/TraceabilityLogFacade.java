package com.middleware.facade;

import blacksip.vtex.client.apiStatus.ApiComponent;
import java.util.Date;

import blacksip.vtex.client.apiStatus.ApiStatus;
import blacksip.vtex.client.apiStatus.Trace;
import blacksip.vtex.client.log.TraceabilityLogFacadeInterface;
import com.middleware.lab.model.db.Product;
import com.middleware.lab.model.db.Sku;
import com.middleware.lab.model.db.TraceabilityLog;
import com.middleware.lab.repository.ProductRepository;
import com.middleware.lab.repository.SkuRepository;
import com.middleware.lab.repository.TraceabilityLogRepository;
import com.middleware.provider.security.SecurityService;
import com.middleware.provider.utils.Constants;
import com.middleware.provider.utils.Parser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


public class TraceabilityLogFacade implements TraceabilityLogFacadeInterface{

   
    @Autowired private SecurityService springAuthSession;
    @Autowired private TraceabilityLogRepository traceabilityLogDao;
    @Autowired private ProductRepository productDao;
    @Autowired private SkuRepository skuDao;
    
    private static final Logger LOGGER = LogManager.getLogger(TraceabilityLogFacade.class);
    
    @Override
    public void addTraceabilityJson(ApiComponent component, Trace trace, String objectRefId, String urlService, Object request, Date requestDate,
            Object response, Date responseDate, ApiStatus status, String detailMessage) {
        if(Constants.PROFILE.equalsIgnoreCase("production")) {
            TraceabilityLog traceabilityLog = new TraceabilityLog();
            objectRefId = homologateObjectRefIdByTrace(objectRefId, component, trace);
            traceabilityLog.setUsername(springAuthSession.findLoggedInUsername());
            traceabilityLog.setComponent(trimString(component.getName(), 100));
            traceabilityLog.setTraceName(trimString(trace.getName(), 100));
            traceabilityLog.setObjectRefId(trimString(objectRefId, 60));
            traceabilityLog.setUrlService(trimString(urlService, 1500));
            traceabilityLog.setRequestBody(Parser.objToJsonString(request));
            traceabilityLog.setRequestDate(requestDate);
            traceabilityLog.setResponseBody(Parser.objToJsonString(response));
            traceabilityLog.setResponseDate(responseDate);
            traceabilityLog.setStatusCode(trimString(status.getAppCode(), 10));
            traceabilityLog.setMessage(trimString(status.getDescription(), 40));
            traceabilityLog.setDetailedMessage(detailMessage);
            traceabilityLog.setUpdatedDate(new Date());
            traceabilityLogDao.save(traceabilityLog);
        }
    }
    
    @Override
    public void addTraceabilityXml(ApiComponent component, Trace trace, String objectRefId, String urlService, Object request, Date requestDate,
            Object response, Date responseDate, ApiStatus status, String detailMessage) {
        if(Constants.PROFILE.equalsIgnoreCase("production")) {
            TraceabilityLog traceabilityLog = new TraceabilityLog();
            objectRefId = homologateObjectRefIdByTrace(objectRefId, component, trace);
            traceabilityLog.setUsername(springAuthSession.findLoggedInUsername());
            traceabilityLog.setComponent(trimString(component.getName(), 100));
            traceabilityLog.setTraceName(trimString(trace.getName(), 100));
            traceabilityLog.setObjectRefId(trimString(objectRefId, 60));
            traceabilityLog.setUrlService(trimString(urlService, 1500));
            traceabilityLog.setRequestBody(Parser.xmlToString(request));
            traceabilityLog.setRequestDate(requestDate);
            traceabilityLog.setResponseBody(Parser.xmlToString(response));
            traceabilityLog.setResponseDate(responseDate);
            traceabilityLog.setStatusCode(trimString(status.getAppCode(), 10));
            traceabilityLog.setMessage(trimString(status.getDescription(), 40));
            traceabilityLog.setDetailedMessage(detailMessage);
            traceabilityLog.setUpdatedDate(new Date());
            traceabilityLogDao.save(traceabilityLog);
        }
    }
    
    public String trimString(String value, int maxSize){
        if(value == null){
            return null;
        }
        if(value.length() > maxSize){
            value = value.substring(0, maxSize);
        }
        return value;
    }
    
    private String homologateObjectRefIdByTrace(String objectRefId, ApiComponent component, Trace trace){
        String objectRefIdHomologated = objectRefId;
        try{
            if(null == trace){
                return objectRefId;
            }else switch (trace) {
                case PRODUCT:
                    if(component == ApiComponent.VTEX){
                        Product product = productDao.findByRefId(objectRefId);
                        if(product != null){
                            objectRefIdHomologated = product.getRefId();
                        }
                    }
                    break;
                case SKU:
                    if(component == ApiComponent.VTEX){
                        Sku sku = skuDao.findByVtexId(Integer.valueOf(objectRefId));
                        if(sku != null){
                            objectRefIdHomologated = sku.getRefId();
                        }
                    }
                    break;
                case INVENTORY:
                    if(component == ApiComponent.VTEX){
                        Sku sku = skuDao.findByVtexId(Integer.valueOf(objectRefId));
                        if(sku != null){
                            objectRefIdHomologated = sku.getRefId();
                        }
                    }
                    break;
                case SPECIFICATION:
                    if(component == ApiComponent.VTEX){
                        Product product = productDao.findByRefId(objectRefId);
                        if(product != null){
                            objectRefIdHomologated = product.getRefId();
                        }
                    }
                    break; 
                case PRICE:
                    if(component == ApiComponent.VTEX){
                        Sku sku = skuDao.findByVtexId(Integer.valueOf(objectRefId));
                        if(sku != null){
                            objectRefIdHomologated = sku.getRefId();
                        }
                    }
                    break;
                case INVOICE:
                    if(component == ApiComponent.MIDDLEWARE || component == ApiComponent.VTEX){
                        String orderId = String.valueOf(objectRefId);
                        objectRefIdHomologated = traceabilityLogDao.getSequence(orderId);
                    }
                    break;
                case CLIENT:
                    break;
                case REMITTANCE:
                    break;
                case TRACKING:
                    if(component == ApiComponent.MIDDLEWARE || component == ApiComponent.VTEX){
                        String orderId = String.valueOf(objectRefId);
                        objectRefIdHomologated = traceabilityLogDao.getSequence(orderId);
                    }
                    break;
                case ORDER:
                    String orderId = String.valueOf(objectRefId);
                    objectRefIdHomologated = traceabilityLogDao.getSequence(orderId);
                    break;
                default:
                    break;
            }
        }catch(Exception ex){
            String msg = "Problema homologando objectRefId="+objectRefId+", para Trace="+trace.toString()+", component="+component.toString();
            LOGGER.error(msg, ex);
        }
        return objectRefIdHomologated;
    }
}
