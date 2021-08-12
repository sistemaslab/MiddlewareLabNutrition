package com.middleware.provider.orders.model.rest;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResponseOrder {

    private String numPedido;
    private String codCliente;
    private String ubigeo;
    private String direccion;
    private String refdireccion;
    private String estadoPedido;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date fechaPedido;
    private String nomPersonaEntregar;
    private String numDocumento;
    private String razonSocial;
    private String direccionFiscal;
    private BigDecimal montoaPagar;
    private String telefono;
    private String tiendaRecojo;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date fechaEntrega;
    private String rangoEntrega;
    private List<Items> items;



    public ResponseOrder(String numPedido, String codCliente, String ubigeo, String direccion, String refdireccion, String estadoPedido, Date fechaPedido, String nomPersonaEntregar, String numDocumento, String razonSocial, String direccionFiscal, BigDecimal montoaPagar, String telefono, String tiendaRecojo, Date fechaEntrega, String rangoEntrega, List<Items> items) {
        this.numPedido = numPedido;
        this.codCliente = codCliente;
        this.ubigeo = ubigeo;
        this.direccion = direccion;
        this.refdireccion = refdireccion;
        this.estadoPedido = estadoPedido;
        this.fechaPedido = fechaPedido;
        this.nomPersonaEntregar = nomPersonaEntregar;
        this.numDocumento = numDocumento;
        this.razonSocial = razonSocial;
        this.direccionFiscal = direccionFiscal;
        this.montoaPagar = montoaPagar;
        this.telefono = telefono;
        this.tiendaRecojo = tiendaRecojo;
        this.fechaEntrega = fechaEntrega;
        this.rangoEntrega = rangoEntrega;
        this.items = items;
    }


}
