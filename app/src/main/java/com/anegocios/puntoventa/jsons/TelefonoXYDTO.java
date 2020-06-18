package com.anegocios.puntoventa.jsons;

import io.realm.RealmObject;

public class TelefonoXYDTO extends RealmObject {

    private String tipo;
    private String numero;

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
}
