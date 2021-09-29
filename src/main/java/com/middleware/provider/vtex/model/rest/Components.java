package com.middleware.provider.vtex.model.rest;

import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Components {

    private String uniqueId;
    private String id;
    private String productId;
    private String name;
    private String refId;
    private String measurementUnit;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal sellingPrice;
    private List<PriceTags> priceTags;


}
