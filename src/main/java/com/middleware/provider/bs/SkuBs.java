package com.middleware.provider.bs;

import java.io.IOException;

import com.middleware.provider.exception.ModelMappingException;
import com.middleware.provider.exception.ServiceConnectionException;
import com.middleware.provider.sku.model.rest.RequestSkus;
import com.middleware.provider.sku.model.rest.ResponseSkus;
import com.middleware.provider.sku.model.rest.Sku;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface SkuBs {
	public ResponseEntity<String> detailSku (String IdProduct) throws InterruptedException, ServiceConnectionException, IOException, ModelMappingException ;
	public List<ResponseSkus> getSkus () throws InterruptedException, ServiceConnectionException, IOException, ModelMappingException ;
	public boolean requestToSku (RequestSkus requestSkus, String idSku) throws InterruptedException, ServiceConnectionException, IOException, ModelMappingException ;
	public void searchWarehouse () throws InterruptedException, ServiceConnectionException, IOException, ModelMappingException ;
	public boolean setBrand (Integer brand , String name ) throws InterruptedException, ServiceConnectionException, IOException, ModelMappingException ;
	public boolean setCategory (Integer category , String name , Integer department , String department_name) throws InterruptedException, ServiceConnectionException, IOException, ModelMappingException ;
	public void updateCatalog () throws InterruptedException, ServiceConnectionException, IOException, ModelMappingException ;
	public boolean syncProduct (String idProduct) throws InterruptedException, ServiceConnectionException, IOException, ModelMappingException ;

}        

