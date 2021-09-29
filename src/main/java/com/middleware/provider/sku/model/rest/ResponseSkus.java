package com.middleware.provider.sku.model.rest;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResponseSkus {
	private String sku_id;     	
	private String product_id;   
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date update_date;   	

    public ResponseSkus(String sku_id, String product_id, Date update_date) {
        this.sku_id = sku_id;
        this.product_id = product_id;
        this.update_date = update_date;
    }

   

}
