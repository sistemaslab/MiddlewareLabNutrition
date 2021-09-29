package com.middleware.provider.vtex.model.rest;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class Slas {

    private String id;
    private String deliveryChannel;
    private String shippingEstimate;
    private BigDecimal price;
    private DeliveryWindow deliveryWindow; 

}
