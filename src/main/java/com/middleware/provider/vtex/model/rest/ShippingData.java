package com.middleware.provider.vtex.model.rest;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
public class ShippingData {

    private Address address;
    private List<LogisticsInfo> logisticsInfo;



}
