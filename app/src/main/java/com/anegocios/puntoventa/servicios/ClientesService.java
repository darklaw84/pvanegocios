package com.anegocios.puntoventa.servicios;

import android.os.AsyncTask;

import com.anegocios.puntoventa.jsons.ClienteDTO;

import java.io.IOException;

import retrofit2.Call;

public class ClientesService extends AsyncTask<Void, Void, ClienteDTO> {

    Call<ClienteDTO> call;

    public ClientesService(Call <ClienteDTO> call) {
        this.call = call;
    }
    @Override
    protected ClienteDTO doInBackground(Void... params) {
        try {

            ClienteDTO response = call.execute().body();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ClienteDTO res) {
    }

}