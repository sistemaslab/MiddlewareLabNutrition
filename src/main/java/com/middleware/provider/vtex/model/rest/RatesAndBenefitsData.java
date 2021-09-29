package com.middleware.provider.vtex.model.rest;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter @Setter @NoArgsConstructor
public class RatesAndBenefitsData {

    private String id;     
    private List<RateAndBenefitsIdentifiers> rateAndBenefitsIdentifiers;

    public RatesAndBenefitsData(String id, List<RateAndBenefitsIdentifiers> rateAndBenefitsIdentifiers) {
        this.id = id;
        this.rateAndBenefitsIdentifiers = rateAndBenefitsIdentifiers;
    }
    
    
    
}
