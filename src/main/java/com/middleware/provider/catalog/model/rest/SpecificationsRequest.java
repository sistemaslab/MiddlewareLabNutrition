package com.middleware.provider.catalog.model.rest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SpecificationsRequest {
    
    private Long Id;
    private Long fieldId;
    private Long fieldValueId;

    @Override
    public String toString() {
        return "SpecificationsRequest{" + "Id=" + Id + ", fieldId=" + fieldId + ", fieldValueId=" + fieldValueId + '}';
    }
}
