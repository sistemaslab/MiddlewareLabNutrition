package com.middleware.provider.orders.model.rest;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class ResponseOrderStatus {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
	private Date date;     	
	private String orderId;     
	private String code;  

    public ResponseOrderStatus(Date date, String orderId, String code) {
        this.date = date;
        this.orderId = orderId;
        this.code = code;
    }    
}
