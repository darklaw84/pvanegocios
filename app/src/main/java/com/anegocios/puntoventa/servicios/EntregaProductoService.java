package com.anegocios.puntoventa.servicios;

import android.os.AsyncTask;

import com.anegocios.puntoventa.jsons.ModificacionPedidoDTO;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;

public class EntregaProductoService extends AsyncTask<Void, Void, ModificacionPedidoDTO> {

    Call<ModificacionPedidoDTO> call;



    public EntregaProductoService(Call <ModificacionPedidoDTO> call) {
        this.call = call;



    }
    @Override
    protected ModificacionPedidoDTO doInBackground(Void... params) {
        try {
            Gson gson = new Gson();
            // Response<ReporteDTO> valor = call.execute();
            ModificacionPedidoDTO response = call.execute().body();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ModificacionPedidoDTO res) {



    }



}