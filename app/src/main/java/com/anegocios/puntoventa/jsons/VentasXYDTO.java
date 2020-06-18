package com.anegocios.puntoventa.jsons;

import java.util.List;

public class VentasXYDTO {

    private int idProducto;
    private double cantidad;
    private double cantMayoreo;
    private double precioMayoreo;
    private double comision;
    private double precioCompra;
    private int iva;
    private double precioVenta;
    private double newPrice;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private double total;
    private double importe;
    private String fecha;
    private double ivaTotal;


    private List<OpcionesVentasXYDTO> opcionesVentasxy;


    public double getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(double newPrice) {
        this.newPrice = newPrice;
    }

    public List<OpcionesVentasXYDTO> getOpcionesVentasxy() {
        return opcionesVentasxy;
    }

    public void setOpcionesVentasxy(List<OpcionesVentasXYDTO> opcionesVentasxy) {
        this.opcionesVentasxy = opcionesVentasxy;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public double getCantMayoreo() {
        return cantMayoreo;
    }

    public void setCantMayoreo(double cantMayoreo) {
        this.cantMayoreo = cantMayoreo;
    }

    public double getPrecioMayoreo() {
        return precioMayoreo;
    }

    public void setPrecioMayoreo(double precioMayoreo) {
        this.precioMayoreo = precioMayoreo;
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

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }
}
