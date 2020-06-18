package com.anegocios.puntoventa.jsons;

public class CrearCuentaResponseDTO {

    private boolean exito;
    private String msg;
    private String idUsuario;



    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }


    public boolean isExito() {
        return exito;
    }

    public void setExito(boolean exito) {
        this.exito = exito;
    }
}
