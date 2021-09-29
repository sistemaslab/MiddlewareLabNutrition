package com.middleware.provider.catalog.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SpecificationsResponse {

    @JsonProperty("Id")
    private Long id;
    @JsonProperty("SkuId")
    private Long skuId;
    @JsonProperty("FieldId")
    private String fieldId;
    @JsonProperty("FieldValueId")
    private String fieldValueId;
    @JsonProperty("Text")
    private String text;

    @Override
    public String toString() {
        return "SpecificationsResponse{" + "id=" + id + ", skuId=" + skuId + ", fieldId=" + fieldId + ", fieldValueId=" + fieldValueId + ", text=" + text + '}';
    }

    
}
