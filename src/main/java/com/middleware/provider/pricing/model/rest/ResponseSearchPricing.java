package com.middleware.provider.pricing.model.rest;

import java.math.BigDecimal;
import java.util.Date;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor

public class ResponseSearchPricing {
	private BigDecimal base_price;
	private String is_synchronized_vtex;
	private BigDecimal list_price;	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd" )
	private Date timestamp;
	private String transaction_id;
	private String product_id;

    public ResponseSearchPricing(BigDecimal base_price, String is_synchronized_vtex, BigDecimal list_price, Date timestamp, String transaction_id, String product_id) {
        this.base_price = base_price;
        this.is_synchronized_vtex = is_synchronized_vtex;
        this.list_price = list_price;
        this.timestamp = timestamp;
        this.transaction_id = transaction_id;
        this.product_id = product_id;
    }

   

}
