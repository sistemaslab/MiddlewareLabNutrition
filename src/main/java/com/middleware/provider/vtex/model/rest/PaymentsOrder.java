package com.middleware.provider.vtex.model.rest;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Natalia Manrique
 */
@Getter
@Setter
@NoArgsConstructor
public class PaymentsOrder {
    private String id;    
    private String paymentSystem;
    private String paymentSystemName;
    private BigDecimal value;
    private String cardNumber;
    private String firstDigits;   
    private String lastDigits;   
    private String giftCardId;
    private String redemptionCode;
    private String tid;

   
}
