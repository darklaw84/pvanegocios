package com.anegocios.puntoventa.servicios;

import android.content.Context;
import android.os.AsyncTask;

import com.anegocios.puntoventa.R;
import com.anegocios.puntoventa.database.ProductosDB;
import com.anegocios.puntoventa.jsons.ProductoDTO;
import com.anegocios.puntoventa.jsons.ProductosXYDTO;
import com.anegocios.puntoventa.utils.Utilerias;

import java.io.IOException;

import io.realm.Realm;
import retrofit2.Call;

public class ProductoAsyncService extends AsyncTask<Void, Void, ProductoDTO> {

    Call<ProductoDTO> call;
    Context con;

    public ProductoAsyncService(Call<ProductoDTO> call, Context context) {
        this.call = call;
        this.con = context;

    }


    @Override
    protected ProductoDTO doInBackground(Void... params) {
        try {
            // String json = new Gson().toJson(call.execute().body());

            ProductoDTO res = call.execute().body();
            if (res == null) {
                System.out.println("No pudimos consultar los productos");
            } else {
                if (res.getProductosxy() == null) {
                    System.out.println(res.getMsg());
                } else {
                    Utilerias ut = new Utilerias();
                    Realm realm = ut.obtenerInstanciaBD(con);
                    if (res.getProductosxy().size() > 0) {
                        ProductosDB pdb = new ProductosDB();
                        pdb.actualizarBDProductos(res.getProductosxy(),
                                Integer.parseInt(ut.obtenerValor("idTienda", con)),
                                ut.obtenerModoAplicacion(con), ut.verificaConexion(con), realm,con);
                    }
                    ProductosDB pdb = new ProductosDB();
                    for (ProductosXYDTO pro : res.getProductosxy()
                    ) {
                        if (pro.isPaquete()) {
                            pdb.borrarPaquetesProducto(pro, realm);
                            if (pro.getPaquetesxy() != null && pro.getPaquetesxy().size() > 0) {
                                pdb.guardarPaquetesProducto(pro, realm);
                            }
                        }
                    }
                    if (realm != null && !realm.isClosed()) {
                        realm.close();
                    }

                }

            }
            return res;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ProductoDTO res) {

    }

}
