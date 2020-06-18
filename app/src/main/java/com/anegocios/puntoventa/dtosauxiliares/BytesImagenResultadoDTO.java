package com.anegocios.puntoventa.dtosauxiliares;

public class BytesImagenResultadoDTO {

    private byte[] imagen;
    private String error;

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
