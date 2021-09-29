package com.middleware.provider.catalog.model.rest;

/**
 *
 * @author Alejandro Cadena
 */
public class ProductEspecificationRequest {
    
    private Integer idProduct;
    private String fieldName;
    private String fieldValues;

    public ProductEspecificationRequest() {
    }

    public ProductEspecificationRequest(Integer idProduct, String fieldName, String fieldValues) {
        this.idProduct = idProduct;
        this.fieldName = fieldName;
        this.fieldValues = fieldValues;
    }

    public Integer getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(Integer idProduct) {
        this.idProduct = idProduct;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldValues() {
        return fieldValues;
    }

    public void setFieldValues(String fieldValues) {
        this.fieldValues = fieldValues;
    }

    @Override
    public String toString() {
        return "ProductEspecification{" + "idProduct=" + idProduct + ", fieldName=" + fieldName + ", fieldValues=" + fieldValues + '}';
    }
    
    
}
