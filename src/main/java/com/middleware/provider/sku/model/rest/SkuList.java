package com.middleware.provider.sku.model.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)


public class SkuList implements Serializable{
	private Integer skus;       	
	

    public SkuList() {
    }

    public SkuList(Integer skus) {
        this.skus = skus;
    }

    public Integer getSkus() {
        return skus;
    }

    public void setSkus(Integer skus) {
        this.skus = skus;
    }

    
}
