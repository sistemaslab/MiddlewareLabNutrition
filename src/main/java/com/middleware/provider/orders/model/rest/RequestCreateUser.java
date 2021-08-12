package com.middleware.provider.orders.model.rest;

public class RequestCreateUser {
    private Integer empresa;
    private String numDocumento;     	
    private String apellidoPaterno;     
    private String apellidoMaterno;     
    private String nombre;     
    private String razonSocial;     
    private String correo;   
    private String telefono;

    public RequestCreateUser() {
    }

    public RequestCreateUser(Integer empresa, String numDocumento, String apellidoPaterno, String apellidoMaterno, String nombre, String razonSocial, String correo, String telefono) {
        this.empresa = empresa;
        this.numDocumento = numDocumento;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.nombre = nombre;
        this.razonSocial = razonSocial;
        this.correo = correo;
        this.telefono = telefono;
    }

    public Integer getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Integer empresa) {
        this.empresa = empresa;
    }

    public String getNumDocumento() {
        return numDocumento;
    }

    public void setNumDocumento(String numDocumento) {
        this.numDocumento = numDocumento;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

   
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    
        
}
