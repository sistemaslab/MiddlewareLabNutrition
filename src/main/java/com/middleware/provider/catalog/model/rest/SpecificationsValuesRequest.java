package com.middleware.provider.catalog.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SpecificationsValuesRequest {
    
    @JsonProperty("FieldId") private Long fieldId;
    @JsonProperty("Name") private String name;
    @JsonProperty("Text") private String text;
    @JsonProperty("IsActive") private boolean isActive;
    @JsonProperty("Position")  private Integer position;

    @Override
    public String toString() {
        return "SpecificationsValuesRequest{" + "fieldId=" + fieldId + ", name=" + name + ", text=" + text + ", isActive=" + isActive + ", position=" + position + '}';
    }

}
