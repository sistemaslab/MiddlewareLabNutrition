package com.middleware.provider.orders.model.rest;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
public class RequestTicketLab {
    private String trackingNumber;   
    private String trackingUrl;   
    private String invoiceNumber;   
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")    
    private Date issuanceDate;     
    
  
}
