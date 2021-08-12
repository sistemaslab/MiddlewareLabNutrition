package com.middleware.provider.vtex.model.rest;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
        
public class Price {

    private String itemId;    
    private BigDecimal listPrice;   
    private BigDecimal basePrice;   
    private BigDecimal costPrice;   

   
   
    
}
