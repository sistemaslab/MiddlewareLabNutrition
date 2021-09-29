package com.middleware.provider.orders.model.rest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class ResponseOrderStatusLab {
    private String status;
    private String statusname;   
    private String codfac;   
    private String nrofac;   
    private String fecfac;   
    private String statusdespacho;   
    private String urltracking;   

    public ResponseOrderStatusLab(String status, String statusname, String codfac, String nrofac, String fecfac, String statusdespacho, String urltracking) {
        this.status = status;
        this.statusname = statusname;
        this.codfac = codfac;
        this.nrofac = nrofac;
        this.fecfac = fecfac;
        this.statusdespacho = statusdespacho;
        this.urltracking = urltracking;
    }

    
    
    
}
