package com.middleware.provider.vtex.model.rest;

import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Items {

    private String id;
    private String productId;
    private String refId;
    private Integer quantity;
    private String name;
    private BigDecimal price;
    private BigDecimal sellingPrice;
    private BigDecimal listPrice;
    private BigDecimal costPrice;
    private boolean isGift;
    private String measurementUnit;
    private List<PriceTags> priceTags;
    private List<Components> components;

    public boolean isIsGift() {
        return isGift;
    }

    public void setIsGift(boolean isGift) {
        this.isGift = isGift;
    }

}
