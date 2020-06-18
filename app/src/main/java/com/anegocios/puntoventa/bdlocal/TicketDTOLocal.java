package com.anegocios.puntoventa.bdlocal;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class TicketDTOLocal extends RealmObject {

    @PrimaryKey
    private int idTicket;
    private int idCaja;
    private String comentario;
    private String idCliente;
    private String correoTicket;
    private double total;
    private String fecha;
    private double cambio;
    private double efectivo;
    private double tarjeta;
    private double iva;
    private double subtotal;
    private double descuentoEfectivo;
    private double descuentoPorcentual;
    private boolean enviadoServer;
    private String tipo;
    private boolean prodEntregado;
    private double propinaEfectivo;
    private double propinaPorcentual;
    private boolean compartirWhatsApp;
    private double descuentoTotal;
    private double propinaTotal;
    private long idFolioServer;
    private String tipoCliente;
    private int idEdit;

    public int getIdEdit() {
        return idEdit;
    }

    public void setIdEdit(int idEdit) {
        this.idEdit = idEdit;
    }

    public String getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(String tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public long getIdFolioServer() {
        return idFolioServer;
    }

    public void setIdFolioServer(long idFolioServer) {
        this.idFolioServer = idFolioServer;
    }

    public double getDescuentoTotal() {
        return descuentoTotal;
    }

    public void setDescuentoTotal(double descuentoTotal) {
        this.descuentoTotal = descuentoTotal;
    }

    public double getPropinaTotal() {
        return propinaTotal;
    }

    public void setPropinaTotal(double propinaTotal) {
        this.propinaTotal = propinaTotal;
    }

    public boolean isCompartirWhatsApp() {
        return compartirWhatsApp;
    }

    public void setCompartirWhatsApp(boolean compartirWhatsApp) {
        this.compartirWhatsApp = compartirWhatsApp;
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

    public boolean isEnviadoServer() {
        return enviadoServer;
    }

    public void setEnviadoServer(boolean enviadoServer) {
        this.enviadoServer = enviadoServer;
    }

    public int getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(int idTicket) {
        this.idTicket = idTicket;
    }

    public int getIdCaja() {
        return idCaja;
    }

    public void setIdCaja(int idCaja) {
        this.idCaja = idCaja;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
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

    public double getIva() {
        return iva;
    }

    public void setIva(double iva) {
        this.iva = iva;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getDescuentoEfectivo() {
        return descuentoEfectivo;
    }

    public void setDescuentoEfectivo(double descuentoEfectivo) {
        this.descuentoEfectivo = descuentoEfectivo;
    }

    public double getDescuentoPorcentual() {
        return descuentoPorcentual;
    }

    public void setDescuentoPorcentual(double descuentoPorcentual) {
        this.descuentoPorcentual = descuentoPorcentual;
    }
}
