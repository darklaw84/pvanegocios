package com.anegocios.puntoventa.servicios;

import android.os.AsyncTask;

import com.anegocios.puntoventa.jsons.ReporteDTO;
import com.google.gson.Gson;
import com.anegocios.puntoventa.jsons.TicketDTO;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class ReporteService extends AsyncTask<Void, Void, ReporteDTO> {

    Call<ReporteDTO> call;



    public ReporteService(Call <ReporteDTO> call) {
        this.call = call;



    }
    @Override
    protected ReporteDTO doInBackground(Void... params) {
        try {
            Gson gson = new Gson();
           // Response<ReporteDTO> valor = call.execute();
            ReporteDTO response = call.execute().body();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ReporteDTO res) {



    }



}