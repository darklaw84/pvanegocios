package com.anegocios.puntoventa.jsons;

public class DireccionDetalleTicketDTO {

    private String municipio;
    private String colonia;
    private String calle;
    private String numeroInt;
    private String numeroExt;
    private String cp;
    private String observaciones;


    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNumeroInt() {
        return numeroInt;
    }

    public void setNumeroInt(String numeroInt) {
        this.numeroInt = numeroInt;
    }

    public String getNumeroExt() {
        return numeroExt;
    }

    public void setNumeroExt(String numeroExt) {
        this.numeroExt = numeroExt;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
