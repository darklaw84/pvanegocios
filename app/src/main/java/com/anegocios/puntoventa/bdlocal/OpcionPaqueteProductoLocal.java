package com.anegocios.puntoventa.bdlocal;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class OpcionPaqueteProductoLocal extends RealmObject {

    @PrimaryKey
    private long id;
    private long idProducto;
    private double cantidad;
    private double precio;
    private double total;
    private long idPaquete;
    private long idOpcion;
    private boolean mostrar;
    private String descripcion;

    public boolean isMostrar() {
        return mostrar;
    }

    public void setMostrar(boolean mostrar) {
        this.mostrar = mostrar;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public long getIdOpcion() {
        return idOpcion;
    }

    public void setIdOpcion(long idOpcion) {
        this.idOpcion = idOpcion;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(long idProducto) {
        this.idProducto = idProducto;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public long getIdPaquete() {
        return idPaquete;
    }

    public void setIdPaquete(long idPaquete) {
        this.idPaquete = idPaquete;
    }
}
