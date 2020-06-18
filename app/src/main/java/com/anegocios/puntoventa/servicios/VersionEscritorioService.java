package com.anegocios.puntoventa.servicios;

import android.os.AsyncTask;

import com.anegocios.puntoventa.jsons.ReporteDTO;
import com.anegocios.puntoventa.jsons.VersionEscritorioDTO;
import com.google.gson.Gson;
import com.anegocios.puntoventa.jsons.TicketDTO;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class VersionEscritorioService extends AsyncTask<Void, Void, VersionEscritorioDTO> {

    Call<VersionEscritorioDTO> call;



    public VersionEscritorioService(Call <VersionEscritorioDTO> call) {
        this.call = call;



    }
    @Override
    protected VersionEscritorioDTO doInBackground(Void... params) {
        try {
            Gson gson = new Gson();
            // Response<ReporteDTO> valor = call.execute();
            VersionEscritorioDTO response = call.execute().body();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(VersionEscritorioDTO res) {



    }



}