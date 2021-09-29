package com.middleware.provider.vtex.model.rest;

import java.util.HashMap;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class RateAndBenefitsIdentifiers {

    private String description;     
    private boolean featured;     
    private String id;     
    private String name;     
    private HashMap<String, String> matchedParameters;
    //private MatchedParameter matchedParameters;
    private BenefitAdditionalInfo additionalInfo;
    
  
    
}
