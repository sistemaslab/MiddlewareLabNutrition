package com.middleware.provider.vtex.model.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@JsonIgnoreProperties(ignoreUnknown = true)

@Getter
@Setter
@NoArgsConstructor
public class DeliveryIds {
   
    private String courierId;     
    private String courierName;     
    private String warehouseId;     
   
        
}
