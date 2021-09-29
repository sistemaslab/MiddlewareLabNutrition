package com.middleware.provider.orders.model.rest;

import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResponseOrderFailed {

    private String order_id;
    private Date created_date;
    private String detail_message;

    public ResponseOrderFailed(String order_id, Date created_date, String detail_message) {
        this.order_id = order_id;
        this.created_date = created_date;
        this.detail_message = detail_message;
    }
}
