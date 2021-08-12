package com.middleware.provider.inventory.model.rest;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductInventory {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "49830")
    private String productId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT-5")
    private Date timestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "100")
    private Integer quantity;
    private String warehouse;

    public ProductInventory(String productId, Date timestamp, Integer quantity, String warehouse) {
        this.productId = productId;
        this.timestamp = timestamp;
        this.quantity = quantity;
        this.warehouse = warehouse;
    }

  

}
