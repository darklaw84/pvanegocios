package com.anegocios.puntoventa.database;

import android.graphics.Bitmap;

import com.anegocios.puntoventa.bdlocal.ImagenTicketDTOLocal;
import com.anegocios.puntoventa.bdlocal.RowTicketLocal;
import com.anegocios.puntoventa.utils.Utilerias;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class TicketDB {


    public List<RowTicketLocal> obtenerRows(String tipo, long idTienda,Realm realm) {


        List<RowTicketLocal> registros = new ArrayList<RowTicketLocal>();
        if (realm != null) {
            registros = realm.where(RowTicketLocal.class)
                    .equalTo("tipo", tipo)
                    .equalTo("idTienda", idTienda)
                    .sort("id").findAll();
        }
        return registros;
    }


    public void guardarRow(String texto, int tamanioLetra, String tipo, long idTienda,Realm realm) {


        if (realm != null) {
            if (!realm.isInTransaction()) {
                realm.beginTransaction();
            }
            Number maxId = realm.where(RowTicketLocal.class).max("id");

            int nextId = (maxId == null) ? 1 : maxId.intValue() + 1;
            RowTicketLocal p = realm.createObject(RowTicketLocal.class, nextId);
            p.setTamanioLetra(tamanioLetra);
            p.setTexto(texto);

            p.setTipo(tipo);
            p.setIdTienda(idTienda);

            realm.insert(p);
            realm.commitTransaction();
        }

    }


    public void actualizarRow(String texto, int tamanioLetra, String tipo, int id,long idTienda,Realm realm) {


        if (realm != null) {
            if (!realm.isInTransaction()) {
                realm.beginTransaction();
            }
            RowTicketLocal p = realm.where(RowTicketLocal.class).
                    equalTo("id", id).findFirst();
            p.setTamanioLetra(tamanioLetra);
            p.setTexto(texto);
            p.setTipo(tipo);
            p.setIdTienda(idTienda);
            realm.insertOrUpdate(p);
            realm.commitTransaction();
        }

    }


    public void borrarRow(int id,Realm realm) {


        if (realm != null) {
            if (!realm.isInTransaction()) {
                realm.beginTransaction();
            }
            RowTicketLocal p = realm.where(RowTicketLocal.class).
                    equalTo("id", id).findFirst();
            p.deleteFromRealm();
            realm.commitTransaction();
        }

    }


    public ImagenTicketDTOLocal obtenerImagen(long idTienda,Realm realm) {


        ImagenTicketDTOLocal registros = new ImagenTicketDTOLocal();
        if (realm != null) {
            registros = realm.where(ImagenTicketDTOLocal.class)
                    .equalTo("idTienda", idTienda)
                    .findFirst();
        }
        return registros;
    }


    public void borrarImagen(long idTienda,Realm realm) {


        if (realm != null) {
            if (!realm.isInTransaction()) {
                realm.beginTransaction();
            }
            ImagenTicketDTOLocal p = realm.where(ImagenTicketDTOLocal.class)
                    .equalTo("idTienda", idTienda)
                    .findFirst();
            if (p != null) {
                p.deleteFromRealm();
                realm.commitTransaction();
            }
        }

    }


    public void guardarImagen(byte[] bitmap, byte[] view,long idTienda,Realm realm) {

        if (realm != null) {
            if (!realm.isInTransaction()) {
                realm.beginTransaction();
            }
            Number maxId = realm.where(RowTicketLocal.class).max("id");

            int nextId = (maxId == null) ? 1 : maxId.intValue() + 1;
            ImagenTicketDTOLocal p = realm.createObject(ImagenTicketDTOLocal.class, nextId);
            p.setImagen(bitmap);
            p.setImagenView(view);
            p.setIdTienda(idTienda);

            realm.insert(p);
            realm.commitTransaction();
        }

    }


}
