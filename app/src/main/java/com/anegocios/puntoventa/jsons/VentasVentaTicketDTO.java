package com.anegocios.puntoventa.jsons;

import java.util.List;

public class VentasVentaTicketDTO {

    private double cantidad;
    private String producto;
    private double precioUnit;
    private double precio;
    private double iva;
    private int id;
    private int idProducto;

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private List< OpcionesPaqueteTicketDTO> opcionesPkt;

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public double getPrecioUnit() {
        return precioUnit;
    }

    public void setPrecioUnit(double precioUnit) {
        this.precioUnit = precioUnit;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public double getIva() {
        return iva;
    }

    public void setIva(double iva) {
        this.iva = iva;
    }

    public List<OpcionesPaqueteTicketDTO> getOpcionesPkt() {
        return opcionesPkt;
    }

    public void setOpcionesPkt(List<OpcionesPaqueteTicketDTO> opcionesPkt) {
        this.opcionesPkt = opcionesPkt;
    }
}
