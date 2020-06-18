package com.anegocios.puntoventa.jsons;

import java.util.List;

public class TicketCajaTicketDTO {

    private int idUsuario;
    private int idTienda;
    private String idCliente;
    private String correoTicket;
    private boolean compartirWhatsApp;
    private double total;
    private String fecha;
    private double cambio;
    private double efectivo;
    private double tarjeta;
    private double iva;
    private double subTotal;
    private double descuentoEfectivo;
    private String comment;
    private double descuentoPorcentual;
    private List<VentasXYDTO> ventasxy;
    private String tipo;
    private boolean prodEntregado;
    private double propinaEfectivo;
    private double propinaPorcentual;
    private int folioApp;

    public int getFolioApp() {
        return folioApp;
    }

    public void setFolioApp(int folioApp) {
        this.folioApp = folioApp;
    }

    public double getPropinaEfectivo() {
        return propinaEfectivo;
    }

    public void setPropinaEfectivo(double propinaEfectivo) {
        this.propinaEfectivo = propinaEfectivo;
    }

    public double getPropinaPorcentual() {
        return propinaPorcentual;
    }

    public void setPropinaPorcentual(double propinaPorcentual) {
        this.propinaPorcentual = propinaPorcentual;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean isProdEntregado() {
        return prodEntregado;
    }

    public void setProdEntregado(boolean prodEntregado) {
        this.prodEntregado = prodEntregado;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getCambio() {
        return cambio;
    }

    public void setCambio(double cambio) {
        this.cambio = cambio;
    }

    public double getEfectivo() {
        return efectivo;
    }

    public void setEfectivo(double efectivo) {
        this.efectivo = efectivo;
    }

    public double getTarjeta() {
        return tarjeta;
    }

    public void setTarjeta(double tarjeta) {
        this.tarjeta = tarjeta;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdTienda() {
        return idTienda;
    }

    public void setIdTienda(int idTienda) {
        this.idTienda = idTienda;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getCorreoTicket() {
        return correoTicket;
    }

    public void setCorreoTicket(String correoTicket) {
        this.correoTicket = correoTicket;
    }

    public boolean isCompartirWhatsApp() {
        return compartirWhatsApp;
    }

    public void setCompartirWhatsApp(boolean compartirWhatsApp) {
        this.compartirWhatsApp = compartirWhatsApp;
    }


    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public double getIva() {
        return iva;
    }

    public void setIva(double iva) {
        this.iva = iva;
    }

    public double getDescuentoEfectivo() {
        return descuentoEfectivo;
    }

    public void setDescuentoEfectivo(double descuentoEfectivo) {
        this.descuentoEfectivo = descuentoEfectivo;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public double getDescuentoPorcentual() {
        return descuentoPorcentual;
    }

    public void setDescuentoPorcentual(double descuentoPorcentual) {
        this.descuentoPorcentual = descuentoPorcentual;
    }

    public List<VentasXYDTO> getVentasxy() {
        return ventasxy;
    }

    public void setVentasxy(List<VentasXYDTO> ventasxy) {
        this.ventasxy = ventasxy;
    }
}
