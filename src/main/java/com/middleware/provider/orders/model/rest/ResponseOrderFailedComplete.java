package com.middleware.provider.orders.model.rest;

import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResponseOrderFailedComplete {


    private List<ResponseOrderFailed> data;
    private long total;

    public ResponseOrderFailedComplete(List<ResponseOrderFailed> data, long total) {
        this.data = data;
        this.total = total;
    }

}
