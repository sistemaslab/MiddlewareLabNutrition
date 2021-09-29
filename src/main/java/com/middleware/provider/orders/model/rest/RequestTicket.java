package com.middleware.provider.orders.model.rest;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter @NoArgsConstructor
public class RequestTicket {
	private String invoiceNumber;     	
	private BigDecimal invoiceValue;     
	private Date issuanceDate;     
	private List<Items> items;     

    public RequestTicket(String invoiceNumber, BigDecimal invoiceValue, Date issuanceDate, List<Items> items) {
        this.invoiceNumber = invoiceNumber;
        this.invoiceValue = invoiceValue;
        this.issuanceDate = issuanceDate;
        this.items = items;
    }


   
}
