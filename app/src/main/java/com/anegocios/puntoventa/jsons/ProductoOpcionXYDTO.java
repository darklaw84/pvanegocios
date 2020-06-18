package com.anegocios.puntoventa.jsons;

import io.realm.RealmObject;

public class ProductoOpcionXYDTO extends RealmObject {

    private int id;
    private String descripcionCorta;
    private int ivaCant;
    private boolean iva;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcionCorta() {
        return descripcionCorta;
    }

    public void setDescripcionCorta(String descripcionCorta) {
        this.descripcionCorta = descripcionCorta;
    }

    public int getIvaCant() {
        return ivaCant;
    }

    public void setIvaCant(int ivaCant) {
        this.ivaCant = ivaCant;
    }

    public boolean isIva() {
        return iva;
    }

    public void setIva(boolean iva) {
        this.iva = iva;
    }
}
