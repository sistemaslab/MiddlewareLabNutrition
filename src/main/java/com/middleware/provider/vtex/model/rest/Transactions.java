package com.middleware.provider.vtex.model.rest;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Transactions {
    private String transactionId;
    private List<PaymentsOrder> payments; 

    
}
