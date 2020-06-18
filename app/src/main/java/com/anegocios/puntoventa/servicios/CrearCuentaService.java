package com.anegocios.puntoventa.servicios;

import android.os.AsyncTask;

import com.anegocios.puntoventa.jsons.CrearCuentaResponseDTO;

import java.io.IOException;

import retrofit2.Call;

public class CrearCuentaService extends AsyncTask<String, Void, CrearCuentaResponseDTO> {

    Call<CrearCuentaResponseDTO> call;

    public CrearCuentaService(Call <CrearCuentaResponseDTO> call) {
        this.call = call;

    }
    @Override
    protected CrearCuentaResponseDTO doInBackground(String... params) {
        try {

            CrearCuentaResponseDTO response = call.execute().body();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(CrearCuentaResponseDTO res) {

    }



}

