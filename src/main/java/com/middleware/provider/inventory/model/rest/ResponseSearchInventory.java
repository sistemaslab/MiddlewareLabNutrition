package com.middleware.provider.inventory.model.rest;

import java.util.Date;



import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResponseSearchInventory {
	private String is_synchronized_vtex;
        private Integer quantity;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date timestamp;
	private String transaction_id;
	private String warehouse;
	private String product_id;

    public ResponseSearchInventory(String is_synchronized_vtex, Integer quantity, Date timestamp, String transaction_id, String warehouse, String product_id) {
        this.is_synchronized_vtex = is_synchronized_vtex;
        this.quantity = quantity;
        this.timestamp = timestamp;
        this.transaction_id = transaction_id;
        this.warehouse = warehouse;
        this.product_id = product_id;
    }

   
        
	
    
}
