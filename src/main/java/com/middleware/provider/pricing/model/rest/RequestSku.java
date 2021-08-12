package com.middleware.provider.pricing.model.rest;

import io.swagger.annotations.Api;

@Api(value = "Employee Management System", description = "Operations pertaining to employee in Employee Management System")

public class RequestSku {
	private String transactionId;     
	private ProductPricing [] prices;   
    
    public RequestSku() {
    }
    
    public RequestSku( ProductPricing [] prices,String transactionId) {
        this.transactionId = transactionId;
        this.prices = prices;
    }

	public ProductPricing [] getProduct() {
		return prices;
	}

	public void setProduct(ProductPricing [] prices) {
		this.prices = prices;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

    
}
