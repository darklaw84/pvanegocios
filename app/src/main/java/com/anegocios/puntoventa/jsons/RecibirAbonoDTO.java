package com.anegocios.puntoventa.jsons;

public class RecibirAbonoDTO {


    private String androidId;
    private int id;
    private int idCaja;
    private int idUT;
    private String folioFactur;
    private String fechaPago;
    private float efectivo;
    private float tarjeta;
    private boolean exito;
    private int folio;
    private String msg;
    private int idUsuario;


    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
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

    public String getAndroidId() {
        return androidId;
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public String getFolioFactur() {
        return folioFactur;
    }

    public void setFolioFactur(String folioFactur) {
        this.folioFactur = folioFactur;
    }

    public String getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(String fechaPago) {
        this.fechaPago = fechaPago;
    }

    public float getEfectivo() {
        return efectivo;
    }

    public void setEfectivo(float efectivo) {
        this.efectivo = efectivo;
    }

    public float getTarjeta() {
        return tarjeta;
    }

    public void setTarjeta(float tarjeta) {
        this.tarjeta = tarjeta;
    }

    public boolean isExito() {
        return exito;
    }

    public void setExito(boolean exito) {
        this.exito = exito;
    }

    public int getFolio() {
        return folio;
    }

    public void setFolio(int folio) {
        this.folio = folio;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
