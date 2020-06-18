package com.anegocios.puntoventa.jsons;

public class CajaResponseDTO {

    private String idAndroid;
    private long idUT;
    private String msg;
    private CajaXYDTO caja;
    private boolean exito;
    private String folio;
    private int idCaja;

    public int getIdCaja() {
        return idCaja;
    }

    public void setIdCaja(int idCaja) {
        this.idCaja = idCaja;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public boolean isExito() {
        return exito;
    }

    public void setExito(boolean exito) {
        this.exito = exito;
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

    public CajaXYDTO getCaja() {
        return caja;
    }

    public void setCaja(CajaXYDTO caja) {
        this.caja = caja;
    }
}
