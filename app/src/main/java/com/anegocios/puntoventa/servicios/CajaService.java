package com.anegocios.puntoventa.servicios;

import android.os.AsyncTask;

import com.anegocios.puntoventa.database.CajasDB;
import com.anegocios.puntoventa.jsons.CajaResponseDTO;
import com.anegocios.puntoventa.utils.Utilerias;

import java.io.IOException;

import io.realm.Realm;
import retrofit2.Call;

public class CajaService extends AsyncTask<Void, Void, CajaResponseDTO> {

    Call<CajaResponseDTO> call;
    int idCajaLocal;


    public CajaService(Call<CajaResponseDTO> call, int idCajaLocal) {
        this.call = call;

        this.idCajaLocal = idCajaLocal;

    }

    @Override
    protected CajaResponseDTO doInBackground(Void... params) {
        try {
            Utilerias ut = new Utilerias();
            CajaResponseDTO res = call.execute().body();
            Realm realm = ut.obtenerInstanciaBD();
            //actualizamos la caja con el id de la caja server
            CajasDB cdb = new CajasDB();
            if (res != null && idCajaLocal > 0) {
                if (res.isExito()) {
                    int idCajaServer = res.getIdCaja();
                    if (idCajaServer > 0) {
                        cdb.actualizarIdServerCaja(idCajaLocal, idCajaServer, realm);
                    }
                }
            }

            if (res != null && idCajaLocal == -1) {
                //es un corte
                if (res.isExito()) {
                    // segun yo aqui debería de actualizar la BD pero veamos que pasa
                    cdb.actualizarCorteCreadoCaja(res.getIdCaja(), realm);
                }

            }
            if (realm != null && !realm.isClosed()) {
                realm.close();
            }
            return res;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(CajaResponseDTO res) {


    }


}
