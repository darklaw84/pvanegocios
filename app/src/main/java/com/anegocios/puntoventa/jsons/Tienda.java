package com.anegocios.puntoventa.jsons;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Tienda extends RealmObject {

    @PrimaryKey
    private int idReg;

    private int id;

    private int idUsuario;

    private String nombre;

    private Boolean imprimir;

    private String logoURL;

    private Boolean imagenesPV;

    private String vigencia;

    private String tipo;

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getVigencia() {
        return vigencia;
    }

    public void setVigencia(String vigencia) {
        this.vigencia = vigencia;
    }



    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getImprimir() {
        return imprimir;
    }

    public void setImprimir(Boolean imprimir) {
        this.imprimir = imprimir;
    }

    public String getLogoURL() {
        return logoURL;
    }

    public void setLogoURL(String logoURL) {
        this.logoURL = logoURL;
    }

    public Boolean getImagenesPV() {
        return imagenesPV;
    }

    public void setImagenesPV(Boolean imagenesPV) {
        this.imagenesPV = imagenesPV;
    }
}
