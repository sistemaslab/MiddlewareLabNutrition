package com.middleware.provider.pricing.model.rest;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor

public class ResponseSearchPricingComplete {
	private List<ResponseSearchPricing> data;
	private long total;

    public ResponseSearchPricingComplete(List<ResponseSearchPricing> data, long total) {
        this.data = data;
        this.total = total;
    }
	

   
   

}
