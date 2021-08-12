package com.middleware.provider.pricing.model.rest;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor

public class ResponsePricingLab {
    	private String Error;
	private String codf;
	private BigDecimal preu;	

    public ResponsePricingLab(String Error, String codf, BigDecimal preu) {
        this.Error = Error;
        this.codf = codf;
        this.preu = preu;
    }

  

}
