
package com.middleware.provider.vtex.model.rest;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GiftCards {

    private String redemptionCode;
    private String provider;
    private String id;
    private BigDecimal value;
    private BigDecimal balance;

    @Override
    public String toString() {
        return "GiftCards{" + "redemptionCode=" + redemptionCode + ", provider=" + provider + ", id=" + id + ", value=" + value + ", balance=" + balance + '}';
    }

}
