package com.anegocios.puntoventa.jsons;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class DireccionesXYDTO extends RealmObject {

    @PrimaryKey
    private int id;
    private String estado;
    private String municipio;
    private String colonia;
    private String calle;
    private String numeroInt;
    private String numeroExt;
    private String cp;
    private String comentario;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

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

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
