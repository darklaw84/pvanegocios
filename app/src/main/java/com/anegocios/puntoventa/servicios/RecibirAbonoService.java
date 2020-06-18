package com.anegocios.puntoventa.servicios;

import android.os.AsyncTask;

import com.anegocios.puntoventa.jsons.LoginResponseDTO;
import com.anegocios.puntoventa.jsons.RecibirAbonoDTO;

import retrofit2.Call;

public class RecibirAbonoService extends AsyncTask<Void, Void, RecibirAbonoDTO> {

    Call<RecibirAbonoDTO> call;


    public RecibirAbonoService(Call<RecibirAbonoDTO> call) {
        this.call = call;

    }

    @Override
    protected RecibirAbonoDTO doInBackground(Void... voids) {
        try {
            return call.execute().body();
        }
        catch (Exception ex)
        {

        }
        return  null;
    }

    @Override
    protected void onPostExecute(RecibirAbonoDTO res) {


    }
}