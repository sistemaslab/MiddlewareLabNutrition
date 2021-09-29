package com.middleware.provider.bs;

import java.io.IOException;

import com.middleware.provider.exception.ModelMappingException;
import com.middleware.provider.exception.ServiceConnectionException;
import com.middleware.provider.pricing.model.rest.ResponseSearchPricingComplete;

public interface PricingBs {

    public void updates() throws InterruptedException, ServiceConnectionException, IOException, ModelMappingException;
    public ResponseSearchPricingComplete searchPricing(String productId, int pag, int num) throws InterruptedException, ServiceConnectionException, IOException, ModelMappingException;

}
