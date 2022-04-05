package com.anegocios.puntoventa.jsons;

import java.util.List;

public class ClienteDetalleTicketDTO {


    private String id;
    private String nombre;
    private String apellidoP;
    private String apellidoM;
    private String zona;
    private String ruta;
    private String empresa;
    private List<TelefonosClienteDTO> telefonos;
    private List<DireccionDetalleTicketDTO> direcciones;

    public List<DireccionDetalleTicketDTO> getDirecciones() {
        return direcciones;
    }

    public void setDirecciones(List<DireccionDetalleTicketDTO> direcciones) {
        this.direcciones = direcciones;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoP() {
        return apellidoP;
    }

    public void setApellidoP(String apellidoP) {
        this.apellidoP = apellidoP;
    }

    public String getApellidoM() {
        return apellidoM;
    }

    public void setApellidoM(String apellidoM) {
        this.apellidoM = apellidoM;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public List<TelefonosClienteDTO> getTelefonos() {
        return telefonos;
    }

    public void setTelefonos(List<TelefonosClienteDTO> telefonos) {
        this.telefonos = telefonos;
    }
}
