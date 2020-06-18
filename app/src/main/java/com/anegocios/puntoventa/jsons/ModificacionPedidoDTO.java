package com.anegocios.puntoventa.jsons;

import java.util.List;

public class ModificacionPedidoDTO {

    private String androidId;
    private boolean exito;
    private int id;
    private int idUT;
    private String msg;
    private int folio;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public boolean isExito() {
        return exito;
    }

    public void setExito(boolean exito) {
        this.exito = exito;
    }


}
