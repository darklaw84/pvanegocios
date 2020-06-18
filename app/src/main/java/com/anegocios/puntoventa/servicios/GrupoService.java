package com.anegocios.puntoventa.servicios;

import android.content.Context;
import android.os.AsyncTask;

import com.anegocios.puntoventa.database.GrupoDB;
import com.anegocios.puntoventa.jsons.GrupoDTO;
import com.anegocios.puntoventa.utils.Utilerias;

import java.io.IOException;

import io.realm.Realm;
import retrofit2.Call;

public class GrupoService extends AsyncTask<Void, Void, GrupoDTO> {

    Call<GrupoDTO> call;
    Context context;


    public GrupoService(Call<GrupoDTO> call, Context context) {
        this.call = call;

        this.context = context;

    }

    @Override
    protected GrupoDTO doInBackground(Void... params) {
        try {

            GrupoDTO res = call.execute().body();
            if (res == null) {
                System.out.println("No pudimos consultar los grupos");
            } else {
                if (res.getGruposVRxy() == null) {
                    System.out.println(res.getMsg());
                } else {
                    if (res.getGruposVRxy().size() > 0) {
                        GrupoDB pdb = new GrupoDB();
                        Utilerias ut = new Utilerias();
                        Realm realm = ut.obtenerInstanciaBD();
                        pdb.actualizarBDGrupos(res.getGruposVRxy(),
                                Integer.parseInt(ut.obtenerValor("idTienda", context)), realm);

                        if (realm != null && !realm.isClosed()) {
                            realm.close();
                        }


                    }
                }
            }
            return res;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(GrupoDTO res) {


    }


}