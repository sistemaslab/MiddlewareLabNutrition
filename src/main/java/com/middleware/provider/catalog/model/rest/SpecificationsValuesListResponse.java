package com.middleware.provider.catalog.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SpecificationsValuesListResponse {

    @JsonProperty("FieldValueId")
    private Long fieldValueId;
    @JsonProperty("Value")
    private String value;
    @JsonProperty("IsActive")
    private boolean isActive;
    @JsonProperty("Position")
    private Integer position;

    @Override
    public String toString() {
        return "SpecificationsValuesListResponse{" + "FieldValueId=" + fieldValueId + ", value=" + value + ", isActive=" + isActive + ", position=" + position + '}';
    }

    
}
