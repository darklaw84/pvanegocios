package com.anegocios.puntoventa.dtosauxiliares;

public class PaqueteOpcionAux {

    public PaqueteOpcionAux()
    {

    }

    public PaqueteOpcionAux(int idPaquete,long idOpcion)
    {
        this.idOpcion=idOpcion;
        this.idPaquete=idPaquete;
    }

    private long idPaquete;
    private long idOpcion;
    private String descripion;
    private double precio;
    private double cantidad;
    private int idProducto;
    private boolean mostrar;

    public boolean isMostrar() {
        return mostrar;
    }

    public void setMostrar(boolean mostrar) {
        this.mostrar = mostrar;
    }

    public String getDescripion() {
        return descripion;
    }

    public void setDescripion(String descripion) {
        this.descripion = descripion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public long getIdPaquete() {
        return idPaquete;
    }

    public void setIdPaquete(long idPaquete) {
        this.idPaquete = idPaquete;
    }

    public long getIdOpcion() {
        return idOpcion;
    }

    public void setIdOpcion(long idOpcion) {
        this.idOpcion = idOpcion;
    }
}
