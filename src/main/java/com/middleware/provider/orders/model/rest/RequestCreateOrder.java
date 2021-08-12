package com.middleware.provider.orders.model.rest;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
public class RequestCreateOrder {
    private Integer empresa;
    private String numPedido;     	
    private String codCliente;     
    private String ubigeo;     
    private String direccion;     
    private String refdireccion;     
    private Integer estadoPedido;   
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm", timezone = "GMT-5")
    private Date fechaPedido;
    private BigDecimal montoTotal;    
    private String tiempoEstimado;
    private String docPersonaEntregar;
    private String nomPersonaEntregar;
    private String tipoDoc;
    private String numDocumento;
    private String razonSocial;
    private String direccionFiscal;
    private Integer tipoPago;
    private Integer medioPago;
    private String numGifCard;
    private BigDecimal montoGifCard;
    private BigDecimal montBaseImponible;
    private BigDecimal montoIGV;
    private BigDecimal montoExonerado;
    private BigDecimal montoComision;
    private BigDecimal montoMakeaWish;
    private BigDecimal montoaPagar;
    private String telefono;
    private String celular;
    private String campo1;
    private String campo2;
    private String campo3;
    private String tiendaRecojo;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "GMT-5")
    private Date fechaEntrega;
    private String rangoEntrega;
    private int ventaVerde;
    private int ventaIvr;
    private int ventaBenef;
    private Integer web;
    private List<Detalle> detalle;

    @Override
    public String toString() {
        return "RequestCreateOrder{" + "empresa=\"" + empresa + "\",\n "
                + "numPedido=\"" + numPedido + "\",\n "
                + "codCliente=\"" + codCliente + "\",\n "
                + "ubigeo=\"" + ubigeo + "\",\n "
                + "direccion=\"" + direccion + "\",\n "
                + "refdireccion=\"" + refdireccion + "\",\n "
                + "estadoPedido=\"" + estadoPedido + "\",\n "
                + "fechaPedido=\"" + fechaPedido + "\",\n "
                + "montoTotal=\"" + montoTotal + "\",\n "
                + "tiempoEstimado=\"" + tiempoEstimado + "\",\n "
                + "docPersonaEntregar=\"" + docPersonaEntregar + "\", \n"
                + "nomPersonaEntregar=\"" + nomPersonaEntregar + "\",\n "
                + "tipoDoc=\"" + tipoDoc + "\", \n"
                + "numDocumento=\"" + numDocumento + "\", \n"
                + "razonSocial=\"" + razonSocial + "\", \n"
                + "direccionFiscal=\"" + direccionFiscal + "\",\n "
                + "tipoPago=\"" + tipoPago + "\", \n"
                + "medioPago=\"" + medioPago + "\",\n"
                + " numGifCard=\"" + numGifCard + "\", \n"
                + "montoGifCard=\"" + montoGifCard + "\",\n "
                + "montBaseImponible=\"" + montBaseImponible + "\",\n"
                + " montoIGV=\"" + montoIGV + "\",\n "
                + "montoExonerado=\"" + montoExonerado + "\", \n"
                + "montoComision=\"" + montoComision + "\",\n "
                + "montoMakeaWish=\"" + montoMakeaWish + "\", \n"
                + "montoaPagar=\"" + montoaPagar + "\", \n"
                + "telefono=\"" + telefono + "\", \n"
                + "celular=\"" + celular + "\",\n "
                + "campo1=\"" + campo1 + "\", \n"
                + "campo2=\"" + campo2 + "\", \n"
                + "campo3=\"" + campo3 + "\", \n"
                + "tiendaRecojo=\"" + tiendaRecojo + "\", \n"
                + "fechaEntrega=\"" + fechaEntrega + "\", \n"
                + "rangoEntrega=\"" + rangoEntrega + "\", \n"
                + "ventaVerde=\"" + ventaVerde + "\", \n"
                + "ventaIvr=\"" + ventaIvr + "\", \n"
                + "ventaBenef=\"" + ventaBenef + "\",\n "
                + "web=\"" + web + "\", \n"
                + "detalle=\"" + detalle + '}';
    }

   
        
}
