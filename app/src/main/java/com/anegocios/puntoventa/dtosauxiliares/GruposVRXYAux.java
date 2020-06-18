package com.anegocios.puntoventa.dtosauxiliares;

import com.anegocios.puntoventa.jsons.GrupoVRXY;

public class GruposVRXYAux {

    public GruposVRXYAux() {

    }

    public GruposVRXYAux(GrupoVRXY g)
    {
        this.id=g.getId();
        this.color=g.getColor();
        this.idTienda=g.getIdTienda();
        this.nombre=g.getNombre();
    }

    private int id;
    private String nombre;
    private String color;
    private int idTienda;

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

    public int getIdTienda() {
        return idTienda;
    }

    public void setIdTienda(int idTienda) {
        this.idTienda = idTienda;
    }
}
