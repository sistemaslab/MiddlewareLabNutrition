package com.middleware.provider.vtex.model.rest;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter @Setter @NoArgsConstructor
public class LogisticsInfo {

    private BigDecimal price;
    private BigDecimal listPrice;
    private BigDecimal sellingPrice;
    private String selectedSla;
    private String deliveryCompany;
    private String shippingEstimate;
    private Date shippingEstimateDate;
    private DeliveryWindow deliveryWindow;
    private List<DeliveryIds> deliveryIds;
    
   
}
