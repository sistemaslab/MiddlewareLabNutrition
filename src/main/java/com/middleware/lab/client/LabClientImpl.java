package com.middleware.lab.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.middleware.provider.exception.ServiceConnectionException;
import com.middleware.provider.orders.model.rest.RequestCreateOrder;
import com.middleware.provider.orders.model.rest.RequestCreateUser;
import com.middleware.provider.orders.model.rest.ResponseCreateOrder;
import com.middleware.provider.sku.model.rest.ResponseAllSkus;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

/**
 *
 * @author Natalia Manrique
 *  Encargada de la comunicacion con la API de Coolbox
 */
@Component("labClient")
public class LabClientImpl extends LabClientAbstract implements LabClient{

    private static final Logger LOG = Logger.getLogger(LabClientImpl.class.getName());
    
    public LabClientImpl(
            @Value("${lab.maxNumberOfRetries}") int maxNumOfRetries, 
            @Value("${lab.waitTimeBetweenRetries}") int waitTimeBetweenRetries, 
            @Value("${lab.increaseTimeBetweenRetries}") int increaseTimeBetweenRetries

    ) {
        super(
                maxNumOfRetries, 
                waitTimeBetweenRetries, 
                increaseTimeBetweenRetries                      
        );
    }

    @Override
    public boolean sendFailure(Object failedUpdateInventory,String getFalloUrl) throws ServiceConnectionException,  JsonProcessingException  {
        URI uri = this.buildUri(getFalloUrl, null, null);
        
        try {
            return this.callRestServiceBool(uri,
                    HttpMethod.POST,
                    failedUpdateInventory
            );
            
        } catch (Exception ex) {
            Logger.getLogger(LabClientImpl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
     
    
    @Override
    public ResponseCreateOrder sendOrderToCoolbox(RequestCreateOrder requestCreateOrder , String url) throws ServiceConnectionException,  JsonProcessingException  {
        URI uri = this.buildUri(url, null, null);
        
        try {
            return this.callRestServiceAuth(uri,
                    HttpMethod.POST, 
                    requestCreateOrder,ResponseCreateOrder.class
            );
            
        } catch (Exception ex) {
            Logger.getLogger(LabClientImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }    
    
     @Override
    public ResponseCreateOrder sendClientToCoolbox(RequestCreateUser requestCreateUser , String url) throws ServiceConnectionException,  JsonProcessingException  {
        URI uri = this.buildUri(url, null, null);
        
        try {
            return this.callRestServiceAuth(uri,
                    HttpMethod.POST, 
                    requestCreateUser, ResponseCreateOrder.class
            );
            
        } catch (Exception ex) {
            Logger.getLogger(LabClientImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }    
    
    
     @Override
    public ResponseAllSkus getSkus(String urlPin) throws ServiceConnectionException,  JsonProcessingException  {
        URI uri = this.buildUri(urlPin, null, null);
        ResponseAllSkus responseAllSkus =new ResponseAllSkus("No logro conexión",null);
        try {
            return this.callRestService(uri,
                    HttpMethod.GET,null,ResponseAllSkus.class
            );
            
        } catch (Exception ex) {
            Logger.getLogger(LabClientImpl.class.getName()).log(Level.SEVERE, null, ex);
            return responseAllSkus;
        }
    }
    
         
}
