package com.anegocios.puntoventa.jsons;

import java.util.List;

public class GrupoDTO {

    private String idAndroid;
    private long idUT;
    private String msg;
    private String idTienda;
    private boolean exito;
    private List<GrupoVRXY> gruposVRxy;

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

    public String getIdTienda() {
        return idTienda;
    }

    public void setIdTienda(String idTienda) {
        this.idTienda = idTienda;
    }

    public boolean isExito() {
        return exito;
    }

    public void setExito(boolean exito) {
        this.exito = exito;
    }

    public List<GrupoVRXY> getGruposVRxy() {
        return gruposVRxy;
    }

    public void setGruposVRxy(List<GrupoVRXY> gruposVRxy) {
        this.gruposVRxy = gruposVRxy;
    }
}
