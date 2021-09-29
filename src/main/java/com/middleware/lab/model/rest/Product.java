/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.middleware.lab.model.rest;

/**
 *
 * @author Natalia Manrique
 */
public class Product {

    private String productId;
    private String timestamp;

    public Product(String productId, String timestamp) {
        this.productId = productId;
        this.timestamp = timestamp;
    }

    public Product() {
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    
}
