package com.anegocios.puntoventa.bdlocal;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class TicketProductoDTOLocal extends RealmObject {

    @PrimaryKey
    private long id;
    private int idTicket;
    private double total;
    private String fecha;
    private int idProductoLocal;
    private int idProdcutoServer;
    private double cantidad;
    private double precioMayoreo;
    private double cantidadMayoreo;
    private double comision;
    private double precioCompra;
    private int iva;
    private double ivaTotal;
    private double precioVenta;

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioMayoreo() {
        return precioMayoreo;
    }

    public void setPrecioMayoreo(double precioMayoreo) {
        this.precioMayoreo = precioMayoreo;
    }

    public double getCantidadMayoreo() {
        return cantidadMayoreo;
    }

    public void setCantidadMayoreo(double cantidadMayoreo) {
        this.cantidadMayoreo = cantidadMayoreo;
    }

    public double getComision() {
        return comision;
    }

    public void setComision(double comision) {
        this.comision = comision;
    }

    public double getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(double precioCompra) {
        this.precioCompra = precioCompra;
    }

    public int getIva() {
        return iva;
    }

    public void setIva(int iva) {
        this.iva = iva;
    }

    public double getIvaTotal() {
        return ivaTotal;
    }

    public void setIvaTotal(double ivaTotal) {
        this.ivaTotal = ivaTotal;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public int getIdProductoLocal() {
        return idProductoLocal;
    }

    public void setIdProductoLocal(int idProductoLocal) {
        this.idProductoLocal = idProductoLocal;
    }

    public int getIdProdcutoServer() {
        return idProdcutoServer;
    }

    public void setIdProdcutoServer(int idProdcutoServer) {
        this.idProdcutoServer = idProdcutoServer;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(int idTicket) {
        this.idTicket = idTicket;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
