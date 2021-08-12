package com.middleware.provider.vtex.model.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@JsonIgnoreProperties(ignoreUnknown = true)

@Getter
@Setter
@NoArgsConstructor
public class ResponseOrderVtex {
   
    private String orderId;     
    private String sequence;     
    private String status;     
    private String statusDescription;  
    private BigDecimal value;     
    private Date creationDate;     
    private Date lastChange;  
    private ArrayList<Totals> totals;
    private ArrayList<Items> items;
    private ClientProfileData clientProfileData;
    private RatesAndBenefitsData ratesAndBenefitsData;
    private ShippingData shippingData ;
    private PaymentOrder paymentData;
    private CustomData customData;

    public ResponseOrderVtex(String orderId, String sequence, String status, String statusDescription, BigDecimal value, Date creationDate, Date lastChange, ArrayList<Totals> totals, ArrayList<Items> items, ClientProfileData clientProfileData, RatesAndBenefitsData ratesAndBenefitsData, ShippingData shippingData, PaymentOrder paymentData, CustomData customData) {
        this.orderId = orderId;
        this.sequence = sequence;
        this.status = status;
        this.statusDescription = statusDescription;
        this.value = value;
        this.creationDate = creationDate;
        this.lastChange = lastChange;
        this.totals = totals;
        this.items = items;
        this.clientProfileData = clientProfileData;
        this.ratesAndBenefitsData = ratesAndBenefitsData;
        this.shippingData = shippingData;
        this.paymentData = paymentData;
        this.customData = customData;
    }

    
    
        
}
