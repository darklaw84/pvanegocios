package com.anegocios.puntoventa.jsons;

import java.util.List;

public class LoginResponseDTO {
    private Boolean acceso;

    private Integer idUser;

    private String msg;

    private List<Usuario> Usuarios = null;

    private List<Tienda> Tiendas = null;



    public Boolean getAcceso() {
        return acceso;
    }

    public void setAcceso(Boolean acceso) {
        this.acceso = acceso;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }



    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Usuario> getUsuarios() {
        return Usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        Usuarios = usuarios;
    }

    public List<Tienda> getTiendas() {
        return Tiendas;
    }

    public void setTiendas(List<Tienda> tiendas) {
        Tiendas = tiendas;
    }
}
