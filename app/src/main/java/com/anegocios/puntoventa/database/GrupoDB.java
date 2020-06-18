package com.anegocios.puntoventa.database;

import com.anegocios.puntoventa.dtosauxiliares.GruposVRXYAux;
import com.anegocios.puntoventa.jsons.GrupoVRXY;
import com.anegocios.puntoventa.utils.Utilerias;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class GrupoDB {

    public void actualizarBDGrupos(List<GrupoVRXY> grupos,int idTienda,Realm realm) {

        if(realm!=null) {

            //borramos todos los grupos porque hay unos que probablemente ya no existen
            if (!realm.isInTransaction()) {
                realm.beginTransaction();
            }
            RealmResults<GrupoVRXY> gruposAnteriores = realm.where(GrupoVRXY.class).equalTo("idTienda", idTienda).findAll();

            gruposAnteriores.deleteAllFromRealm();

            realm.commitTransaction();

            for (GrupoVRXY ps : grupos) {

                if (!realm.isInTransaction()) {
                    realm.beginTransaction();
                }
                GrupoVRXY regGuardado = realm.where(GrupoVRXY.class).
                        equalTo("id", ps.getId()).findFirst();
                if (regGuardado != null && regGuardado.getId() > 0) {
                    regGuardado.deleteFromRealm();
                    GrupoVRXY p = realm.createObject(GrupoVRXY.class, ps.getId());
                    p.setColor(ps.getColor());
                    p.setNombre(ps.getNombre());


                    p.setIdTienda(idTienda);

                    realm.insert(p);
                    realm.commitTransaction();
                } else {
                    GrupoVRXY p = realm.createObject(GrupoVRXY.class, ps.getId());
                    p.setColor(ps.getColor());
                    p.setNombre(ps.getNombre());
                    p.setIdTienda(idTienda);
                    realm.insert(p);
                    realm.commitTransaction();
                }

            }
        }

    }


    public List<GruposVRXYAux> obtenerListaGruposAuxiliar(int idTienda,Realm realm) {
       List<GrupoVRXY> grupos = obtenerListaGrupos(idTienda,realm);
       List<GruposVRXYAux> auxl= new ArrayList<GruposVRXYAux>();
        for (GrupoVRXY g: grupos
             ) {
            if(g.getNombre()!=null) {
                auxl.add(new GruposVRXYAux(g));
            }
        }
       return auxl;
    }


    public List<GrupoVRXY> obtenerListaGrupos(int idTienda,Realm realm) {

        List<GrupoVRXY> regs = new ArrayList<GrupoVRXY>();
        List<GrupoVRXY> aux = new ArrayList<GrupoVRXY>();
        if(realm!=null) {
            aux = realm.where(GrupoVRXY.class)
                    .equalTo("idTienda", idTienda).sort("nombre").findAll();
        }
        for (GrupoVRXY g : aux
             ) {
            if(g.getNombre()!=null)
            {
                regs.add(g);
            }

        }
        return regs;
    }




}
