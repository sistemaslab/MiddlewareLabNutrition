package com.middleware.provider.pricing.model.rest;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ProductPricing {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT-5")
    private Date timestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "49830")
    private String productId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "100.2")
    private BigDecimal basePrice;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "100")
    private BigDecimal listPrice;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "PEN")
    private String currency;

    public ProductPricing() {
    }

    public ProductPricing(Date timestamp, String productId, BigDecimal basePrice, BigDecimal listPrice, String currency) {
        this.timestamp = timestamp;
        this.productId = productId;
        this.basePrice = basePrice;
        this.listPrice = listPrice;
        this.currency = currency;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

}
