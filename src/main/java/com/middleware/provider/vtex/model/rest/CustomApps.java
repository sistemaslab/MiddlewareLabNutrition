package com.middleware.provider.vtex.model.rest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomApps {

    private String id;     
    private Integer major;     
    private Fields fields;     

    public CustomApps(String id, Integer major, Fields fields) {
        this.id = id;
        this.major = major;
        this.fields = fields;
    }
  
  

}
