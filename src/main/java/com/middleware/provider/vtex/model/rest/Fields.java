package com.middleware.provider.vtex.model.rest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Fields {

    private String firstName;     
    private String lastName;     
    private String document;     
    private String typeDocument;     
    private String fiscalAddress;     

    public Fields(String firstName, String lastName, String document, String typeDocument, String fiscalAddress) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.document = document;
        this.typeDocument = typeDocument;
        this.fiscalAddress = fiscalAddress;
    }

    
  

}
