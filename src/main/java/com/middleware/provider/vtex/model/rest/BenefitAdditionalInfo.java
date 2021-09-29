package com.middleware.provider.vtex.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;


public class BenefitAdditionalInfo {
    
    @JsonProperty("null")
    private String nullField;

    public BenefitAdditionalInfo() {
    }

    public BenefitAdditionalInfo(String nullField) {
        this.nullField = nullField;
    }

    public String getNullField() {
        return nullField;
    }

    public void setNullField(String nullField) {
        this.nullField = nullField;
    }

    @Override
    public String toString() {
        return "BenefitAdditionalInfo{" + "nullField=" + nullField + '}';
    }
    
}
