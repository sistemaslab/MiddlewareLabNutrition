package com.middleware.provider.vtex.model.rest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
        
public class Address {

    private String addressType;     
    private String receiverName;     
    private String addressId;     
    private String postalCode;     
    private String city;     
    private String state;   
    private String country;   
    private String street;   
    private String number;   
    private String neighborhood;   
    private String complement;   
    private String [] geoCoordinates;   

    public Address(String addressType, String receiverName, String addressId, String postalCode, String city, String state, String country, String street, String number, String neighborhood, String complement, String[] geoCoordinates) {
        this.addressType = addressType;
        this.receiverName = receiverName;
        this.addressId = addressId;
        this.postalCode = postalCode;
        this.city = city;
        this.state = state;
        this.country = country;
        this.street = street;
        this.number = number;
        this.neighborhood = neighborhood;
        this.complement = complement;
        this.geoCoordinates = geoCoordinates;
    }


    
   
    
}
