package com.anegocios.puntoventa.servicios;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.anegocios.puntoventa.database.CajasDB;
import com.anegocios.puntoventa.jsons.TicketPVDTO;
import com.anegocios.puntoventa.utils.Utilerias;
import com.google.gson.Gson;
import com.anegocios.puntoventa.jsons.TicketDTO;

import java.io.IOException;

import io.realm.Realm;
import retrofit2.Call;

public class TicketPVService extends AsyncTask<Void, Void, TicketPVDTO> {

    Call<TicketPVDTO> call;
    int idTicketLocal;
    Activity act;
    Context con;
    long idTGlobal;


    public TicketPVService(Call<TicketPVDTO> call, int idTicketL, Activity activity, Context context, long idTiendaGlobal) {
        this.call = call;
        this.idTicketLocal = idTicketL;
        this.act = activity;
        this.con = context;
        this.idTGlobal = idTiendaGlobal;
    }

    @Override
    protected TicketPVDTO doInBackground(Void... params) {
        try {
            Gson gson = new Gson();
            //  String json = gson.toJson(call.execute().body());
            TicketPVDTO res = call.execute().body();
            Utilerias ut = new Utilerias();

            if (res != null) {
                if (res.isExito()) {
                    CajasDB cdb = new CajasDB();
                    if (res.getFolio() > 0) {
                        Realm realm = ut.obtenerInstanciaBD();
                        cdb.actualizarIdFolioTicketServer(idTicketLocal, res.getFolio(), realm);
                        cdb.actualizarTicketEnviado(idTicketLocal, realm);
                        if (realm != null && !realm.isClosed()) {
                            realm.close();
                        }
                        ut.imprimirTicket(con, act, idTGlobal);


                    }
                    if (res.getUrlTicket() != null && res.getUrlTicket().length() > 0) {

                        //enviarTicketWhatssap("https://bit.ly/2JwSNWK",activity);
                        ut.enviarTicketWhatssap(res.getUrlTicket(), act);
                    }
                }

            } else {
                Utilerias.log(con, "El servicio esta abajo", null);
            }
            return res;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(TicketPVDTO res) {


    }

    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            System.out.println(msg.toString());
        }
    };
}