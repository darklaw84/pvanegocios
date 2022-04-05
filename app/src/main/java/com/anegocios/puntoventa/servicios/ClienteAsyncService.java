package com.anegocios.puntoventa.servicios;

import android.content.Context;
import android.os.AsyncTask;

import com.anegocios.puntoventa.database.ClientesDB;
import com.anegocios.puntoventa.jsons.ClienteDTO;
import com.anegocios.puntoventa.utils.Utilerias;

import java.io.IOException;

import io.realm.Realm;
import retrofit2.Call;

public class ClienteAsyncService extends AsyncTask<Void, Void, ClienteDTO> {

    Call<ClienteDTO> call;
    Context con;

    public ClienteAsyncService(Call<ClienteDTO> call, Context context) {

        this.con=context;
        this.call = call;
    }

    @Override
    protected ClienteDTO doInBackground(Void... params) {
        try {

            ClienteDTO res = call.execute().body();


            if (res == null) {
                System.out.println("No pudimos consultar los clientes");
            } else {
                if (res.getClientesxy() == null) {
                    System.out.println(res.getMsg());
                } else {

                    if (res.getClientesxy().size() > 0) {


                        ClientesDB pdb = new ClientesDB();
                        Utilerias ut = new Utilerias();
                        Realm realm3 = ut.obtenerInstanciaBD(con);
                        pdb.actualizarBDClientes(res.getClientesxy(),
                                Integer.parseInt(ut.obtenerValor("idTienda", con)), realm3);
                        if (realm3 != null && !realm3.isClosed()) {
                            realm3.close();
                        }
                    }

                }
            }


            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ClienteDTO res) {
    }

}