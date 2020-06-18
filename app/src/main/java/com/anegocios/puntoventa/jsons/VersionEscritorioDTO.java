package com.anegocios.puntoventa.jsons;

public class VersionEscritorioDTO {

    private String idAndroid;
    private String idUT;
    private boolean exito;

    public String getIdAndroid() {
        return idAndroid;
    }

    public void setIdAndroid(String idAndroid) {
        this.idAndroid = idAndroid;
    }

    public String getIdUT() {
        return idUT;
    }

    public void setIdUT(String idUT) {
        this.idUT = idUT;
    }

    public boolean isExito() {
        return exito;
    }

    public void setExito(boolean exito) {
        this.exito = exito;
    }
}
