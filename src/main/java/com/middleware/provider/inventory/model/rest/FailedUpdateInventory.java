package com.middleware.provider.inventory.model.rest;


public class FailedUpdateInventory {
	private String transactionId;   
	private String transactionType;   
	private String code; 
        private String message; 
	private ProductInventory product;   
    
    public FailedUpdateInventory() {
    }

    public FailedUpdateInventory(String transactionId, String transactionType, String code, String message, ProductInventory product) {
        this.transactionId = transactionId;
        this.transactionType = transactionType;
        this.code = code;
        this.message = message;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ProductInventory getProduct() {
        return product;
    }

    public void setProduct(ProductInventory product) {
        this.product = product;
    }
    
}
