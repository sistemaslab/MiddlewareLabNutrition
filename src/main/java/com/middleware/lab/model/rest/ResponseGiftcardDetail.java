package com.middleware.lab.model.rest;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Natalia Manrique
 */
public class ResponseGiftcardDetail {

    private String redemptionCode;
    private BigDecimal balance;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Date emissionDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")

    private Date expiringDate;

    public ResponseGiftcardDetail(String redemptionCode, BigDecimal balance, Date emissionDate, Date expiringDate) {
        this.redemptionCode = redemptionCode;
        this.balance = balance;
        this.emissionDate = emissionDate;
        this.expiringDate = expiringDate;
    }

    public ResponseGiftcardDetail() {
    }

    public String getRedemptionCode() {
        return redemptionCode;
    }

    public void setRedemptionCode(String redemptionCode) {
        this.redemptionCode = redemptionCode;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Date getEmissionDate() {
        return emissionDate;
    }

    public void setEmissionDate(Date emissionDate) {
        this.emissionDate = emissionDate;
    }

    public Date getExpiringDate() {
        return expiringDate;
    }

    public void setExpiringDate(Date expiringDate) {
        this.expiringDate = expiringDate;
    }

    @Override
    public String toString() {
        return "ResponseGiftcardDetail{" + "redemptionCode=" + redemptionCode + ", balance=" + balance + ", emissionDate=" + emissionDate + ", expiringDate=" + expiringDate + '}';
    }

}
