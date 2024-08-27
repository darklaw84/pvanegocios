package com.anegocios.puntoventa.utils;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.anegocios.puntoventa.MainActivity;
import com.anegocios.puntoventa.ProductosActivity;
import com.anegocios.puntoventa.database.ClientesDB;
import com.anegocios.puntoventa.database.ProductosDB;
import com.anegocios.puntoventa.database.UsuariosDB;
import com.anegocios.puntoventa.jsons.ClienteDTO;
import com.anegocios.puntoventa.jsons.ClienteXYDTO;
import com.anegocios.puntoventa.jsons.GrupoDTO;
import com.anegocios.puntoventa.jsons.ProductoDTO;
import com.anegocios.puntoventa.jsons.ProductosXYDTO;
import com.anegocios.puntoventa.servicios.APIClient;
import com.anegocios.puntoventa.servicios.APIInterface;
import com.anegocios.puntoventa.servicios.ClienteAsyncService;
import com.anegocios.puntoventa.servicios.ClientesService;
import com.anegocios.puntoventa.servicios.GrupoService;
import com.anegocios.puntoventa.servicios.ProductoAsyncService;
import com.anegocios.puntoventa.servicios.ProductosService;
import com.google.gson.Gson;

import java.util.List;

import io.realm.Realm;
import retrofit2.Call;

public class ActualizacionCatalogosUtil {




    public void consultarProductos(Context context, Activity activity, boolean all) {
        ProductoDTO pr = new ProductoDTO();
        APIInterface apiInterface = APIClient.getClient(context).create(APIInterface.class);


        try {
            Utilerias ut = new Utilerias();
            pr.setIdAndroid(ut.obtenerSerial(context, activity));
            UsuariosDB udb = new UsuariosDB();
            Realm realm = ut.obtenerInstanciaBD(context);
            int idUT = udb.obtenerIdUTUsuario(Integer.parseInt(ut.obtenerValor("idUsuario", context)),
                    Integer.parseInt(ut.obtenerValor("idTienda", context)), realm).getIdUT();
            pr.setIdUT(idUT);
            pr.setAll(all);
            pr.setIdTienda(ut.obtenerValor("idTienda", context));
            Call<ProductoDTO> call = apiInterface.mandarConsultarProductos(pr);
            Gson json = new Gson();
            String gson = json.toJson(pr);
            ProductoAsyncService ls = new ProductoAsyncService(call,context);
             ls.execute();
             if(realm!=null && !realm.isClosed())
             {
                 realm.close();
             }



        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void consultarGrupos(Context context,Activity activity)
    {
        APIInterface apiInterface = APIClient.getClient(context).create(APIInterface.class);
        Utilerias ut = new Utilerias();
        GrupoDTO pro = new GrupoDTO();


        try {

            pro.setIdAndroid(ut.obtenerSerial(context, activity));
            pro.setIdUT(Long.parseLong(ut.obtenerValor("idUT", context)));
            pro.setIdTienda(ut.obtenerValor("idTienda", context));
            Call<GrupoDTO> call = apiInterface.mandarConsultarGrupos(pro);
           GrupoService ls = new GrupoService(call,context);
            ls.execute();

        } catch (Exception ex) {
            Utilerias.log(context,"Error Actualizar Catalogos: "+ ex.getMessage()+" "+ex.getStackTrace(),ex);
        }
    }




    public void consultarClientes(Context context, Activity activity) {
        ClienteDTO pr = new ClienteDTO();
        APIInterface apiInterface = APIClient.getClient(context).create(APIInterface.class);


        try {
            Utilerias ut = new Utilerias();
            Realm realm = ut.obtenerInstanciaBD(context);
            pr.setIdAndroid(ut.obtenerSerial(context, activity));
            UsuariosDB udb = new UsuariosDB();
            int idUT = udb.obtenerIdUTUsuario(Integer.parseInt(ut.obtenerValor("idUsuario", context)),
                    Integer.parseInt(ut.obtenerValor("idTienda", context)), realm).getIdUT();
            pr.setIdUT(idUT);
            Call<ClienteDTO> call = apiInterface.mandarConsultarClientes(pr);
            ClienteAsyncService ls = new ClienteAsyncService(call,context);
             ls.execute();
            if(realm!=null && !realm.isClosed())
            {
                realm.close();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
