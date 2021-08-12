package com.middleware.provider.vtex.model.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@JsonIgnoreProperties(ignoreUnknown = true)

@Getter
@Setter
@NoArgsConstructor
public class DeliveryWindow {
   
    private Date startDateUtc;     
    private Date endDateUtc;     
   
        
}
