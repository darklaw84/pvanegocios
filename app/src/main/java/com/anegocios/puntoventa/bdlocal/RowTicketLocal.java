package com.anegocios.puntoventa.bdlocal;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RowTicketLocal  extends RealmObject {

    @PrimaryKey
    private int id;
    private int tamanioLetra;
    private String texto;
    private String tipo;
    private long idTienda;

    public long getIdTienda() {
        return idTienda;
    }

    public void setIdTienda(long idTienda) {
        this.idTienda = idTienda;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTamanioLetra() {
        return tamanioLetra;
    }

    public void setTamanioLetra(int tamanioLetra) {
        this.tamanioLetra = tamanioLetra;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
