package com.anegocios.puntoventa.servicios;

import android.os.AsyncTask;

import com.anegocios.puntoventa.jsons.ReporteDTO;
import com.anegocios.puntoventa.jsons.ReporteTicketDetalleDTO;
import com.google.gson.Gson;
import com.anegocios.puntoventa.jsons.TicketDTO;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class DetalleTicketService extends AsyncTask<Void, Void, ReporteTicketDetalleDTO> {

    Call<ReporteTicketDetalleDTO> call;



    public DetalleTicketService(Call <ReporteTicketDetalleDTO> call) {
        this.call = call;



    }
    @Override
    protected ReporteTicketDetalleDTO doInBackground(Void... params) {
        try {
            Gson gson = new Gson();
            ReporteTicketDetalleDTO response = call.execute().body();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ReporteTicketDetalleDTO res) {



    }



}