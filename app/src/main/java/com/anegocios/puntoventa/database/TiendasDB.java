package com.anegocios.puntoventa.database;

import com.anegocios.puntoventa.jsons.Tienda;
import com.anegocios.puntoventa.utils.Utilerias;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class TiendasDB {

    public void actualizarBDTiendas(List<Tienda> tiendas,int idUsuario,Realm realm) {

        if(realm!=null) {
            for (Tienda ps : tiendas) {
                if (!realm.isInTransaction()) {
                    realm.beginTransaction();
                }
                Tienda tiendaGuardado = realm.where(Tienda.class).
                        equalTo("id", ps.getId()).and().equalTo("idUsuario", idUsuario).findFirst();
                if (tiendaGuardado != null && tiendaGuardado.getId() > 0) {
                    tiendaGuardado.setImagenesPV(ps.getImagenesPV());
                    tiendaGuardado.setImprimir(ps.getImprimir());
                    tiendaGuardado.setLogoURL(ps.getLogoURL());
                    tiendaGuardado.setVigencia(ps.getVigencia());
                    tiendaGuardado.setTipo(ps.getTipo());
                    tiendaGuardado.setNombre(ps.getNombre());
                    realm.insertOrUpdate(tiendaGuardado);
                    realm.commitTransaction();
                } else {

                    Number maxId = realm.where(Tienda.class).max("idReg");

                    int nextId = (maxId == null) ? 1 : maxId.intValue() + 1;
                    Tienda p = realm.createObject(Tienda.class, nextId);
                    p.setId(ps.getId());
                    p.setIdUsuario(idUsuario);
                    p.setImagenesPV(ps.getImagenesPV());
                    p.setImprimir(ps.getImprimir());
                    p.setVigencia(ps.getVigencia());
                    p.setTipo(ps.getTipo());
                    p.setLogoURL(ps.getLogoURL());
                    p.setNombre(ps.getNombre());
                    realm.insert(p);
                    realm.commitTransaction();
                }
            }
        }

    }

    public List<Tienda> obtenerListaTiendas(int idUsuario,Realm realm) {

        List<Tienda> registros = new ArrayList<Tienda>();
        if(realm!=null) {
            registros = realm.where(Tienda.class).equalTo("idUsuario", idUsuario).findAll();
        }
        return registros;
    }


    public Tienda obtenerTienda(int idTienda,Realm realm) {

        Tienda t= new Tienda();
         if(realm!=null) {
             t = realm.where(Tienda.class).equalTo("id", idTienda).findFirst();
         }

         return t;

    }
}
