package com.anegocios.puntoventa.jsons;

import java.util.List;

public class VentasDetalleTicket {

    private String folio;
    private String folioApp;
    private String estatus;
    private double subtotal;
    private double iva;
    private double descuento;
    private double propina;
    private double total;
    private double pago;
    private double saldo;
    private String comentario;
    private String fecha;
    private boolean prodEntregado;
    private String cliente;
    private String idCliente;
    private String vendedor;
    private String mesa;
    private List<VentasVentaTicketDTO> ventas;

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getFolioApp() {
        return folioApp;
    }

    public void setFolioApp(String folioApp) {
        this.folioApp = folioApp;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getIva() {
        return iva;
    }

    public void setIva(double iva) {
        this.iva = iva;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public double getPropina() {
        return propina;
    }

    public void setPropina(double propina) {
        this.propina = propina;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getPago() {
        return pago;
    }

    public void setPago(double pago) {
        this.pago = pago;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public boolean isProdEntregado() {
        return prodEntregado;
    }

    public void setProdEntregado(boolean prodEntregado) {
        this.prodEntregado = prodEntregado;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }

    public String getMesa() {
        return mesa;
    }

    public void setMesa(String mesa) {
        this.mesa = mesa;
    }

    public List<VentasVentaTicketDTO> getVentas() {
        return ventas;
    }

    public void setVentas(List<VentasVentaTicketDTO> ventas) {
        this.ventas = ventas;
    }
}
