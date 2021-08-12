package com.middleware.provider.pricing.model.rest;

import java.math.BigDecimal;

public class RequestVtexUpdatePricing {
private BigDecimal costPrice;   
private BigDecimal basePrice;   
private BigDecimal listPrice;   
    
    public RequestVtexUpdatePricing() {
    }
    
    public RequestVtexUpdatePricing( BigDecimal costPrice, BigDecimal basePrice,BigDecimal listPrice) {
        this.costPrice = costPrice;
        this.basePrice = basePrice;
        this.listPrice = listPrice;
    }

	public BigDecimal getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(BigDecimal costPrice) {
		this.costPrice = costPrice;
	}

	public BigDecimal getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(BigDecimal basePrice) {
		this.basePrice = basePrice;
	}

	public BigDecimal getListPrice() {
		return listPrice;
	}

	public void setListPrice(BigDecimal listPrice) {
		this.listPrice = listPrice;
	}

	
}
