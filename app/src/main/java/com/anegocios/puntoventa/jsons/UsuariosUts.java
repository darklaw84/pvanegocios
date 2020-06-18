package com.anegocios.puntoventa.jsons;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class UsuariosUts extends RealmObject {

    @PrimaryKey
    private int id;
    private int idUsuario;
    private int idTienda;
    private int idUT;
    private String vigencia;
    private String tipoLic;

    public String getVigencia() {
        return vigencia;
    }

    public void setVigencia(String vigencia) {
        this.vigencia = vigencia;
    }

    public String getTipoLic() {
        return tipoLic;
    }

    public void setTipoLic(String tipoLic) {
        this.tipoLic = tipoLic;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdTienda() {
        return idTienda;
    }

    public void setIdTienda(int idTienda) {
        this.idTienda = idTienda;
    }

    public int getIdUT() {
        return idUT;
    }

    public void setIdUT(int idUT) {
        this.idUT = idUT;
    }
}
