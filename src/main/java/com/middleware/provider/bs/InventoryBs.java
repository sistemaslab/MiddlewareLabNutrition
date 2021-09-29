package com.middleware.provider.bs;

import com.middleware.lab.model.db.SkuHomologation;
import java.io.IOException;

import com.middleware.provider.exception.ModelMappingException;
import com.middleware.provider.exception.ServiceConnectionException;
import com.middleware.provider.inventory.model.rest.RequestUpdateInventory;
import com.middleware.provider.inventory.model.rest.ResponseSearchInventoryComplete;
import com.middleware.provider.inventory.model.rest.ResponseUpdateInventory;
import java.util.List;

public interface InventoryBs {
	public ResponseUpdateInventory setInventory(RequestUpdateInventory requestUpdateInventory) throws InterruptedException, ServiceConnectionException, IOException, ModelMappingException ;
	public ResponseSearchInventoryComplete searchInventory(String numPedido , String warehouse , int pag, int num) throws InterruptedException, ServiceConnectionException, IOException, ModelMappingException ;
        public void updates() throws InterruptedException, ServiceConnectionException, IOException, ModelMappingException;
        public void updatesInvoices(List<SkuHomologation> skus, String job) throws InterruptedException, ServiceConnectionException, IOException, ModelMappingException;
    
}
