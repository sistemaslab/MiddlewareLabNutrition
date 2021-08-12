package com.middleware.provider.pricing.model.rest;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.Api;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Api(value = "Employee Management System", description = "Operations pertaining to employee in Employee Management System")
@Getter @Setter @NoArgsConstructor
public class RequestUpdatePricing {
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "TX1")

	private String transactionId;     
	private List<ProductPricing> prices;   

    public RequestUpdatePricing(String transactionId, List<ProductPricing> prices) {
        this.transactionId = transactionId;
        this.prices = prices;
    }
     
    
    
}
