/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.middleware.provider.bs;

import com.middleware.provider.exception.ModelMappingException;
import com.middleware.provider.exception.ServiceConnectionException;
import com.middleware.provider.inventory.model.rest.RequestUpdateInventory;
import com.middleware.provider.pricing.model.rest.RequestUpdatePricing;
import java.io.IOException;

/**
 *
 * @author Natalia Manrique
 */
public interface AsynchronousTasks {
    
     public void UpdateSkus() throws InterruptedException, ServiceConnectionException, IOException, ModelMappingException ;
     public void UpdatePricing(RequestUpdatePricing requestUpdatePricing) throws InterruptedException, ServiceConnectionException, IOException, ModelMappingException ;
     public void UpdateInventory(RequestUpdateInventory requestUpdateInventory) throws InterruptedException, ServiceConnectionException, IOException, ModelMappingException ;

}
