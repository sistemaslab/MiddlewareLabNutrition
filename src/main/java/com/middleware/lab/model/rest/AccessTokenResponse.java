package com.middleware.lab.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Alejandro Cadena
 */
public class AccessTokenResponse {
    

    private String token;
   

    public AccessTokenResponse() {
    }

    public AccessTokenResponse(String token) {
        this.token = token;
    }

    
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    

   
}
