package com.middleware.provider.orders.model.rest;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResponseOrderSearch {

    private String order_num;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT-5")
    private Date created_date;
    private String is_corporate;
    private String is_synchronized_coolbox;
    private String order_status;
    private BigDecimal total_value;

    public ResponseOrderSearch(String order_num, Date created_date, String is_corporate, String is_synchronized_coolbox, String order_status, BigDecimal total_value) {
        this.order_num = order_num;
        this.created_date = created_date;
        this.is_corporate = is_corporate;
        this.is_synchronized_coolbox = is_synchronized_coolbox;
        this.order_status = order_status;
        this.total_value = total_value;
    }

}
