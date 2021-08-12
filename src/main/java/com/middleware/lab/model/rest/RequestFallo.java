
package com.middleware.lab.model.rest;

/**
 *
 * @author Natalia Manrique
 */
public class RequestFallo {
    private String transactionId;     	
    private String transactionType;     
    private String message; 
    private Product product;    

    public RequestFallo(String transactionId, String transactionType, String message, Product product) {
        this.transactionId = transactionId;
        this.transactionType = transactionType;
        this.message = message;
        this.product = product;
    }

    public RequestFallo() {
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

}
