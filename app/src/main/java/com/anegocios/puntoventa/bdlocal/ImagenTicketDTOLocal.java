package com.anegocios.puntoventa.bdlocal;


import android.graphics.Bitmap;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ImagenTicketDTOLocal extends RealmObject {

    @PrimaryKey
    private int id;
    private byte[] imagen;
    private byte[] imagenView;
    private long idTienda;

    public long getIdTienda() {
        return idTienda;
    }

    public void setIdTienda(long idTienda) {
        this.idTienda = idTienda;
    }

    public byte[] getImagenView() {
        return imagenView;
    }

    public void setImagenView(byte[] imagenView) {
        this.imagenView = imagenView;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }
}
