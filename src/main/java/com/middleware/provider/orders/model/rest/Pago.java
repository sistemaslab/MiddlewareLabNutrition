package com.middleware.provider.orders.model.rest;

import java.math.BigDecimal;

public class Pago {
	private Integer TipoPago;
	private Integer MedioPago;
	private BigDecimal MontoaPagar;
	private String Campo1;     	
	private String Campo2;     
	private String Campo3;     
	

    public Pago() {
    }

    public Integer getTipoPago() {
        return TipoPago;
    }

    public void setTipoPago(Integer TipoPago) {
        this.TipoPago = TipoPago;
    }

    public Integer getMedioPago() {
        return MedioPago;
    }

    public void setMedioPago(Integer MedioPago) {
        this.MedioPago = MedioPago;
    }

    public BigDecimal getMontoaPagar() {
        return MontoaPagar;
    }

    public void setMontoaPagar(BigDecimal MontoaPagar) {
        this.MontoaPagar = MontoaPagar;
    }

    public String getCampo1() {
        return Campo1;
    }

    public void setCampo1(String Campo1) {
        this.Campo1 = Campo1;
    }

    public String getCampo2() {
        return Campo2;
    }

    public void setCampo2(String Campo2) {
        this.Campo2 = Campo2;
    }

    public String getCampo3() {
        return Campo3;
    }

    public void setCampo3(String Campo3) {
        this.Campo3 = Campo3;
    }
    
    
}
