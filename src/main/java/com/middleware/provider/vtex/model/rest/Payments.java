
package com.middleware.provider.vtex.model.rest;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Payments {
        private String paymentSystem;   
        private BigDecimal referenceValue;   
        private BigDecimal value;     
        private String cardNumber;   
        private String firstDigits;   
        private String lastDigits;   
        private String tid;   


  
}
