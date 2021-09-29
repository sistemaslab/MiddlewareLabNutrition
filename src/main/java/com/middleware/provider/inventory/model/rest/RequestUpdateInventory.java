package com.middleware.provider.inventory.model.rest;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
public class RequestUpdateInventory {
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "TX2")
	private String transactionId;     
	private List<ProductInventory> product;   

    public RequestUpdateInventory(String transactionId, List<ProductInventory> product) {
        this.transactionId = transactionId;
        this.product = product;
    }
 
}
