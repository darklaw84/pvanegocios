package com.anegocios.puntoventa.jsons;

public class TicketDTO {

    private String idAndroid;
    private long idUT;
    private String msg;
    private boolean exito;
    private CajaTicketDTO caja;
    private long folio;
    private String urlTicket;


    public String getUrlTicket() {
        return urlTicket;
    }

    public void setUrlTicket(String urlTicket) {
        this.urlTicket = urlTicket;
    }

    public long getFolio() {
        return folio;
    }

    public void setFolio(long folio) {
        this.folio = folio;
    }

    public String getIdAndroid() {
        return idAndroid;
    }

    public void setIdAndroid(String idAndroid) {
        this.idAndroid = idAndroid;
    }

    public long getIdUT() {
        return idUT;
    }

    public void setIdUT(long idUT) {
        this.idUT = idUT;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isExito() {
        return exito;
    }

    public void setExito(boolean exito) {
        this.exito = exito;
    }

    public CajaTicketDTO getCaja() {
        return caja;
    }

    public void setCaja(CajaTicketDTO caja) {
        this.caja = caja;
    }
}
