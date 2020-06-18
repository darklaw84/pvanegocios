package com.anegocios.puntoventa.jsons;

import io.realm.RealmObject;

public class UT extends RealmObject {

    private Integer id;

    private Integer idTienda;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdTienda() {
        return idTienda;
    }

    public void setIdTienda(Integer idTienda) {
        this.idTienda = idTienda;
    }
}
