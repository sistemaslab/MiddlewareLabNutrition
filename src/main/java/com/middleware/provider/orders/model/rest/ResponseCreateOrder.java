package com.middleware.provider.orders.model.rest;

public class ResponseCreateOrder {
    private boolean insertado;     	
    private String mensaje;     
   
    public ResponseCreateOrder() {
    }

    public ResponseCreateOrder(boolean insertado, String mensaje) {
        this.insertado = insertado;
        this.mensaje = mensaje;
    }

    public boolean isInsertado() {
        return insertado;
    }

    public void setInsertado(boolean insertado) {
        this.insertado = insertado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }


    @Override
    public String toString() {
        return "ResponseCreateOrder{" + "insertado=" + insertado + ", mensaje=" + mensaje + '}';
    }

    
        
}
