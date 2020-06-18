package com.anegocios.puntoventa.jsons;

import io.realm.RealmObject;

public class OpcionesPaqueteXY extends RealmObject {
    private long id;
    private double cantidad;
    private boolean mostrar;
    private double precioPaquete;
    private ProductoOpcionXYDTO idProducto;

    public ProductoOpcionXYDTO getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(ProductoOpcionXYDTO idProducto) {
        this.idProducto = idProducto;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public boolean isMostrar() {
        return mostrar;
    }

    public void setMostrar(boolean mostrar) {
        this.mostrar = mostrar;
    }

    public double getPrecioPaquete() {
        return precioPaquete;
    }

    public void setPrecioPaquete(double precioPaquete) {
        this.precioPaquete = precioPaquete;
    }


}
