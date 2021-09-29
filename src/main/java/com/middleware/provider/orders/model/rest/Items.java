package com.middleware.provider.orders.model.rest;

import java.math.BigDecimal;

public class Items {
	private String id;     	
	private BigDecimal price;   
	private Integer quantity;   

    public Items() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    
}
