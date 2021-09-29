package com.middleware.provider.pricing.model.rest;

import com.middleware.provider.pricing.model.rest.ProductPricing;

public class FailedUpdatePricing {
	private String transactionId;   
	private String transactionType;   
	private String message;   
        private String code;
	private ProductPricing product;   
    
    public FailedUpdatePricing() {
    }

    public FailedUpdatePricing(String transactionId, String transactionType, String message, String code, ProductPricing product) {
        this.transactionId = transactionId;
        this.transactionType = transactionType;
        this.message = message;
        this.code = code;
        this.product = product;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ProductPricing getProduct() {
        return product;
    }

    public void setProduct(ProductPricing product) {
        this.product = product;
    }

    
}
