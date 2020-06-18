package com.anegocios.puntoventa.jsons;

import io.realm.RealmObject;

public class CorreosXYDTO  extends RealmObject {

    private String correo;

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}
