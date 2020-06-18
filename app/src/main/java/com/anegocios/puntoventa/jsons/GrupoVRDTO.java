package com.anegocios.puntoventa.jsons;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class GrupoVRDTO extends RealmObject {

    @PrimaryKey
    private int id;
    private String nombre;
    private String color;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
