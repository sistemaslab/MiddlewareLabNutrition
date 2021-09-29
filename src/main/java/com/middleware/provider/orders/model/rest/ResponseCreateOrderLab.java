package com.middleware.provider.orders.model.rest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResponseCreateOrderLab {

    private String status;
    private String statusname;

    public ResponseCreateOrderLab(String status, String statusname) {
        this.status = status;
        this.statusname = statusname;
    }
   
}
