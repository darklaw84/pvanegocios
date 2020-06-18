package com.anegocios.puntoventa.jsons;

import android.widget.ListView;

import java.util.List;

public class TicketPVDTO {

    private String androId;
    private int idUtEdit;
    private String id;
    private boolean online;
    private boolean exito;
    private String tipo;
    private String folioApp;
    private int idCaja;
    private int idUT;
    private String idUsuario;
    private String idTienda;
    private String idCliente;
    private String idVendedor;
    private String idMesa;
    private String mandarTicket;
    private boolean compartirWhatsApp;
    private String fecha;
    private String comment;
    private String descuentoPorcentual;
    private String descuentoEfectivo;
    private String propinaEfectivo;
    private String propinaPorcentual;
    private boolean prodEntregado;
    private String subTotal;
    private String total;
    private String efectivo;
    private String tarjeta;
    private List<VentasXYDTO> ventasxy;
    private int folio;
    private String urlTicket;
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getIdUtEdit() {
        return idUtEdit;
    }

    public void setIdUtEdit(int idUtEdit) {
        this.idUtEdit = idUtEdit;
    }

    public int getIdCaja() {
        return idCaja;
    }

    public void setIdCaja(int idCaja) {
        this.idCaja = idCaja;
    }

    public int getIdUT() {
        return idUT;
    }

    public void setIdUT(int idUT) {
        this.idUT = idUT;
    }

    public String getUrlTicket() {
        return urlTicket;
    }

    public void setUrlTicket(String urlTicket) {
        this.urlTicket = urlTicket;
    }


    public int getFolio() {
        return folio;
    }

    public void setFolio(int folio) {
        this.folio = folio;
    }

    public String getAndroId() {
        return androId;
    }

    public void setAndroId(String androId) {
        this.androId = androId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public boolean isExito() {
        return exito;
    }

    public void setExito(boolean exito) {
        this.exito = exito;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFolioApp() {
        return folioApp;
    }

    public void setFolioApp(String folioApp) {
        this.folioApp = folioApp;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdTienda() {
        return idTienda;
    }

    public void setIdTienda(String idTienda) {
        this.idTienda = idTienda;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(String idVendedor) {
        this.idVendedor = idVendedor;
    }

    public String getIdMesa() {
        return idMesa;
    }

    public void setIdMesa(String idMesa) {
        this.idMesa = idMesa;
    }

    public String getMandarTicket() {
        return mandarTicket;
    }

    public void setMandarTicket(String mandarTicket) {
        this.mandarTicket = mandarTicket;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDescuentoPorcentual() {
        return descuentoPorcentual;
    }

    public void setDescuentoPorcentual(String descuentoPorcentual) {
        this.descuentoPorcentual = descuentoPorcentual;
    }

    public String getDescuentoEfectivo() {
        return descuentoEfectivo;
    }

    public void setDescuentoEfectivo(String descuentoEfectivo) {
        this.descuentoEfectivo = descuentoEfectivo;
    }

    public String getPropinaEfectivo() {
        return propinaEfectivo;
    }

    public void setPropinaEfectivo(String propinaEfectivo) {
        this.propinaEfectivo = propinaEfectivo;
    }

    public String getPropinaPorcentual() {
        return propinaPorcentual;
    }

    public void setPropinaPorcentual(String propinaPorcentual) {
        this.propinaPorcentual = propinaPorcentual;
    }

    public boolean isProdEntregado() {
        return prodEntregado;
    }

    public void setProdEntregado(boolean prodEntregado) {
        this.prodEntregado = prodEntregado;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getEfectivo() {
        return efectivo;
    }

    public void setEfectivo(String efectivo) {
        this.efectivo = efectivo;
    }

    public String getTarjeta() {
        return tarjeta;
    }

    public void setTarjeta(String tarjeta) {
        this.tarjeta = tarjeta;
    }

    public List<VentasXYDTO> getVentasxy() {
        return ventasxy;
    }

    public void setVentasxy(List<VentasXYDTO> ventasxy) {
        this.ventasxy = ventasxy;
    }
}
