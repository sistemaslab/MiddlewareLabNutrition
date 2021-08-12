package com.middleware.provider.pricing.model.rest;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor

public class ResponsePricingLabAll {
	   List<ResponsePricingLab> getpreciolistaResult;

    public ResponsePricingLabAll(List<ResponsePricingLab> getpreciolistaResult) {
        this.getpreciolistaResult = getpreciolistaResult;
    }

   

}
