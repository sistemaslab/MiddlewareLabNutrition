/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.middleware.provider.orders.model.rest;

import java.math.BigDecimal;

/**
 *
 * @author Natalia Manrique
 */
public class Detalle {
    private String codProducto;
    private Integer cantidad;     
    private BigDecimal precioUnitario;
    private BigDecimal precioTotal;

    public Detalle() {
    }

    public Detalle(String codProducto, Integer cantidad, BigDecimal precioUnitario, BigDecimal precioTotal) {
        this.codProducto = codProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.precioTotal = precioTotal;
    }

    public String getCodProducto() {
        return codProducto;
    }

    public void setCodProducto(String codProducto) {
        this.codProducto = codProducto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(BigDecimal precioTotal) {
        this.precioTotal = precioTotal;
    }

    @Override
    public String toString() {
        return "Detalle{" + "codProducto=" + codProducto + ", cantidad=" + cantidad + ", precioUnitario=" + precioUnitario + ", precioTotal=" + precioTotal + '}';
    }
     
    
}
