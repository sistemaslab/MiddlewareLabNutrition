package com.middleware.provider.vtex.model.rest;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter @Setter @NoArgsConstructor
public class PriceTags {
    
    private String name;
    private String identifier;
    private BigDecimal value;

    @Override
    public String toString() {
        return "PriceTags{" + "name=" + name + ", identifier=" + identifier + ", value=" + value + '}';
    }
   
}
