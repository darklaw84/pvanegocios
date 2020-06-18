package com.anegocios.puntoventa.servicios;

import android.os.AsyncTask;

import com.anegocios.puntoventa.R;
import com.anegocios.puntoventa.jsons.ProductoDTO;

import java.io.IOException;

import retrofit2.Call;

public class ProductosService extends AsyncTask<Void, Void, ProductoDTO> {

    Call <ProductoDTO> call;

    public ProductosService(Call <ProductoDTO> call) {
        this.call = call;

    }



    @Override
    protected ProductoDTO doInBackground(Void... params) {
        try {
           // String json = new Gson().toJson(call.execute().body());

            ProductoDTO response = call.execute().body();

            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ProductoDTO res) {

    }

}
