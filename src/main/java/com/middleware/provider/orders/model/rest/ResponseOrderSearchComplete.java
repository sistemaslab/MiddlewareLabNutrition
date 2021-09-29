package com.middleware.provider.orders.model.rest;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResponseOrderSearchComplete {

    private List<ResponseOrderSearch> data;
    private long total;

    public ResponseOrderSearchComplete(List<ResponseOrderSearch> data, long total) {
        this.data = data;
        this.total = total;
    }

  

}
