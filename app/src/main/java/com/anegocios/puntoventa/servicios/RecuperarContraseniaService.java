package com.anegocios.puntoventa.servicios;

import android.os.AsyncTask;

import com.anegocios.puntoventa.R;
import com.anegocios.puntoventa.jsons.RecuperarContraseniaResponseDTO;

import java.io.IOException;

import retrofit2.Call;

public class RecuperarContraseniaService extends AsyncTask<Void, Void, RecuperarContraseniaResponseDTO> {

    Call <RecuperarContraseniaResponseDTO> call;



    public RecuperarContraseniaService(Call <RecuperarContraseniaResponseDTO> call) {
        this.call = call;



    }
    @Override
    protected RecuperarContraseniaResponseDTO doInBackground(Void... params) {
        try {

            RecuperarContraseniaResponseDTO response = call.execute().body();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(RecuperarContraseniaResponseDTO res) {
    }



}
