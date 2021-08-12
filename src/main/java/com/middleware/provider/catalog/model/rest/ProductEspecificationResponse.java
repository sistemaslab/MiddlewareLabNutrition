package com.middleware.provider.catalog.model.rest;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Natalia Manrique
 */
@Getter @Setter
public class ProductEspecificationResponse {
    
    private Integer idProduct;
    private String fieldName;
    private String fieldValues;
    private Boolean success;
    private String message;

    public ProductEspecificationResponse() {
    }

    public ProductEspecificationResponse(Integer idProduct, String fieldName, String fieldValues, Boolean success, String message) {
        this.idProduct = idProduct;
        this.fieldName = fieldName;
        this.fieldValues = fieldValues;
        this.success = success;
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
    
    @Override
    public String toString() {
        return "ProductEspecification{" + "idProduct=" + idProduct + ", fieldName=" + fieldName + ", fieldValues=" + fieldValues + '}';
    }
    
    
}
