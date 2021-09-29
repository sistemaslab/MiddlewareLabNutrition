package com.middleware.provider.sku.model.rest;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResponseAllSkus {
	private String message;
	private List<Sku> products;

    public ResponseAllSkus(String message, List<Sku> products) {
        this.message = message;
        this.products = products;
    }

}
