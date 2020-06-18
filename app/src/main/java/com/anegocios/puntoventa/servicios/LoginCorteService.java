package com.anegocios.puntoventa.servicios;

import android.os.AsyncTask;

import com.anegocios.puntoventa.jsons.LoginResponseDTO;

import retrofit2.Call;

public class LoginCorteService extends AsyncTask<Void, Void, LoginResponseDTO> {

    Call<LoginResponseDTO> call;


    public LoginCorteService(Call<LoginResponseDTO> call) {
        this.call = call;

    }

    @Override
    protected LoginResponseDTO doInBackground(Void... voids) {
        try {
            return call.execute().body();
        }
        catch (Exception ex)
        {

        }
        return  null;
    }

    @Override
    protected void onPostExecute(LoginResponseDTO res) {


    }
}