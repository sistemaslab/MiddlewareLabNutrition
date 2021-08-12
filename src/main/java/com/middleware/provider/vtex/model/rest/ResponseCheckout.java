package com.middleware.provider.vtex.model.rest;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResponseCheckout {

    private String orderFormId;     
    private BigDecimal value;     
    private PaymentData paymentData;

   
    
   
    
}
