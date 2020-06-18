package com.anegocios.puntoventa.jsons;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

public class PaqueteXYDTO extends RealmObject {

    private long id;
    private String opciones;
    private RealmList<OpcionesPaqueteXY> opcionesPaquetexy;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOpciones() {
        return opciones;
    }

    public void setOpciones(String opciones) {
        this.opciones = opciones;
    }

    public List<OpcionesPaqueteXY> getOpcionesPaquetexy() {
        return opcionesPaquetexy;
    }

    public void setOpcionesPaquetexy(RealmList<OpcionesPaqueteXY> opcionesPaquetexy) {
        this.opcionesPaquetexy = opcionesPaquetexy;
    }
}
