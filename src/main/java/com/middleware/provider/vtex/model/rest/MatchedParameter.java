package com.middleware.provider.vtex.model.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Alejandro Cadena
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MatchedParameter implements java.io.Serializable {

    @JsonProperty("couponCode@Marketing")
    private String couponCodeMarketing;

    public MatchedParameter() {
    }

    public MatchedParameter(String couponCodeMarketing) {
        this.couponCodeMarketing = couponCodeMarketing;
    }

    public String getCouponCodeMarketing() {
        return couponCodeMarketing;
    }

    public void setCouponCodeMarketing(String couponCodeMarketing) {
        this.couponCodeMarketing = couponCodeMarketing;
    }

}
