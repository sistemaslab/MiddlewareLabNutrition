package com.middleware.provider.orders.model.rest;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter @Setter @NoArgsConstructor

public class ItemsDiscount {
	private String name;     	
	private BigDecimal discount;   
	private String measurement_unit;     	

  
    
}
