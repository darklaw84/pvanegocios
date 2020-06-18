package com.anegocios.puntoventa.jsons;

public class OpcionesVentasXYDTO {

    private long idOpcPkt;
    private double cantidad;
    private double precioPaquete;
    private double comision;
    private int iva;
    private double precioCompra;

    public long getIdOpcPkt() {
        return idOpcPkt;
    }

    public void setIdOpcPkt(long idOpcPkt) {
        this.idOpcPkt = idOpcPkt;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioPaquete() {
        return precioPaquete;
    }

    public void setPrecioPaquete(double precioPaquete) {
        this.precioPaquete = precioPaquete;
    }

    public double getComision() {
        return comision;
    }

    public void setComision(double comision) {
        this.comision = comision;
    }

    public int getIva() {
        return iva;
    }

    public void setIva(int iva) {
        this.iva = iva;
    }

    public double getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(double precioCompra) {
        this.precioCompra = precioCompra;
    }
}
