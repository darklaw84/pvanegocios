package com.anegocios.puntoventa.jsons;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class OpcionesPaquete extends RealmObject {

    @PrimaryKey
    private long id;
    private long idPaquete;
    private double cantidad;
    private double precio;
    private boolean mostrar;
    private int idProducto;
    private boolean iva;
    private int ivaCant;
    private String descripcion;


    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public boolean isIva() {
        return iva;
    }

    public void setIva(boolean iva) {
        this.iva = iva;
    }

    public int getIvaCant() {
        return ivaCant;
    }

    public void setIvaCant(int ivaCant) {
        this.ivaCant = ivaCant;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdPaquete() {
        return idPaquete;
    }

    public void setIdPaquete(long idPaquete) {
        this.idPaquete = idPaquete;
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

    public boolean isMostrar() {
        return mostrar;
    }

    public void setMostrar(boolean mostrar) {
        this.mostrar = mostrar;
    }
}
