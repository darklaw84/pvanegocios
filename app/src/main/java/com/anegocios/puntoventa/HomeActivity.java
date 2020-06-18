package com.anegocios.puntoventa;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.anegocios.puntoventa.bdlocal.CajaDTOLocal;
import com.anegocios.puntoventa.database.ClientesDB;
import com.anegocios.puntoventa.database.GrupoDB;
import com.anegocios.puntoventa.database.ProductosDB;
import com.anegocios.puntoventa.database.UsuariosDB;
import com.anegocios.puntoventa.dtosauxiliares.ProductosXYDTOAux;
import com.anegocios.puntoventa.jsons.ClienteDTO;
import com.anegocios.puntoventa.jsons.GrupoDTO;
import com.anegocios.puntoventa.jsons.ProductoDTO;
import com.anegocios.puntoventa.jsons.ProductosXYDTO;
import com.anegocios.puntoventa.jsons.ReporteDTO;
import com.anegocios.puntoventa.jsons.Usuario;
import com.anegocios.puntoventa.jsons.VersionEscritorioDTO;
import com.anegocios.puntoventa.servicios.APIClient;
import com.anegocios.puntoventa.servicios.APIInterface;


import com.anegocios.puntoventa.servicios.ReporteService;
import com.anegocios.puntoventa.servicios.VersionEscritorioService;
import com.anegocios.puntoventa.utils.Utilerias;
import com.anegocios.puntoventa.utils.UtileriasSincronizacion;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import io.realm.Realm;
import retrofit2.Call;

public class HomeActivity extends AppCompatActivity {


    private String pantalla;
    private Usuario permisos;
    ProgressBar progress_bar;
    Context context;
    Activity activity;
    String boton = "";
    ImageButton btnClientes;
    ImageButton btnProductos;
    ImageButton btnPuntoVenta;
    ImageButton btnModo;
    Realm realm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        pantalla = "home";
        Utilerias ut = new Utilerias();
        context = this;
        activity = this;
        realm = ut.obtenerInstanciaBD();
        btnClientes = findViewById(R.id.btnClientes);
        btnPuntoVenta = findViewById(R.id.btnPuntoVenta);
        btnProductos = findViewById(R.id.btnProductos);
        btnModo = findViewById(R.id.btnModo);
        if (ut.obtenerModoAplicacion(this)) {
            btnModo.setImageResource(R.drawable.conconexion);

        } else {
            btnModo.setImageResource(R.drawable.sinconexion);

        }

        permisos = ut.obtenerPermisosUsuario(this);
        progress_bar = findViewById(R.id.progress_bar);
        progress_bar.setVisibility(View.INVISIBLE);


    }

    public void btnWebClick(View view) {

        try {
            setContentView(R.layout.webview);
            APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
            Utilerias ut = new Utilerias();
            VersionEscritorioDTO ve = new VersionEscritorioDTO();


            String idAndroid = ut.obtenerSerial(this, this);
            UsuariosDB udb = new UsuariosDB();
            WebView webView = (WebView) findViewById(R.id.webView1);
            int idUT = udb.obtenerIdUTUsuario(Integer.parseInt(ut.obtenerValor("idUsuario", this)),
                    Integer.parseInt(ut.obtenerValor("idTienda", this)), realm).getIdUT();


            String url = "http://www.anegocios.com/WebService/ws_VersionEscritorio_V3";

            webView.setWebViewClient(new WebViewClient());


            String postData = "idUT=" + URLEncoder.encode("" + idUT, "UTF-8")
                    + "&idAndroid=" + URLEncoder.encode("" + ut.obtenerSerial(this, this), "UTF-8");
            webView.postUrl(url, postData.getBytes());

            //webView.loadUrl(url);

        }
        catch(Exception ex)
        {
            String error = ex.getMessage();
        }
    }

    public void btnModoClick(View view) {

        Utilerias ut = new Utilerias();
        String modo = ut.obtenerValor("modoApp", this);
        btnModo = findViewById(R.id.btnModo);
        if (modo == null) {
            ut.guardarValor("modoApp", "on", this);
            btnModo.setImageResource(R.drawable.conconexion);
        } else {
            if (modo.equals("on")) {
                //estaba en on lo cambiamos a off
                ut.guardarValor("modoApp", "off", this);
                btnModo.setImageResource(R.drawable.sinconexion);
            } else {
                ut.guardarValor("modoApp", "on", this);
                btnModo.setImageResource(R.drawable.conconexion);
            }
        }
    }

    public void btnPuntoVentaClick(View view) {
        boton = "puntoVenta";
        if (permisos.getUsarCaja()) {


            Utilerias ut = new Utilerias();

            boolean seguir = true;
            if (ut.obtenerModoAplicacion(this)) {
                if (ut.verificaConexion(this)) {

                    actualizarClientes();
                    actualizarCatalogos();

                } else {
                    seguir = false;
                    mandarMensaje("No puedes ingresar en modo online, ya que no tienes una conexón a la red");
                }
            } else {

                int screenSize = getResources().getConfiguration().screenLayout &
                        Configuration.SCREENLAYOUT_SIZE_MASK;

                String toastMsg;
                switch(screenSize) {
                    case Configuration.SCREENLAYOUT_SIZE_LARGE:
                        toastMsg = "Large screen";
                        break;
                    case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                        toastMsg = "Normal screen";
                        break;
                    case Configuration.SCREENLAYOUT_SIZE_SMALL:
                        toastMsg = "Small screen";
                        break;
                    case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                        toastMsg = "Small screen";
                        break;
                    default:
                        toastMsg = "Screen size is neither large, normal or small";
                }




                CajaDTOLocal caja = ut.obtenerCajaActual(context, activity, realm);

                if (caja != null) {
                    cerrarRealmN(realm);
                    Intent i = new Intent(getApplicationContext(), PuntoVentaActivity.class);
                    startActivity(i);
                } else {
                    mostrarAbrirCaja();
                }
            }


        } else {
            mandarMensaje("No tienes permiso parausar el punto de venta");
        }
    }


    private void mostrarAbrirCaja() {
        setContentView(R.layout.abrircaja);
        pantalla = "abrirCaja";
    }

    @Override
    public void onBackPressed() {
        if (pantalla != null) {
            cerrarRealmN(realm);
            if (pantalla.equals("abrirCaja")) {
                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(i);
            } else if (pantalla.equals("webview")) {
                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(i);
            } else {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        }
    }

    public void btnClientesClick(View view) {

        if (permisos.getClt_R()) {


            boolean seguir = true;
            Utilerias ut = new Utilerias();
            if (ut.obtenerModoAplicacion(this)) {
                if (ut.verificaConexion(this)) {
                    UtileriasSincronizacion uts = new UtileriasSincronizacion();

                    String error = uts.sincronizarTodo(this, this, realm,
                            Long.parseLong(ut.obtenerValor("idTienda", context)));

                    if (!error.equals("")) {
                        seguir = false;
                        mandarMensaje("No se pudo sincronizar la info, " +
                                "por favor intentelo de nuevo o pase a modo offline:" + error);
                    } else {
                        actualizarClientes();

                    }
                } else {
                    seguir = false;
                    mandarMensaje("No puedes ingresar en modo online, ya que no tienes una conexón a la red");
                }
            } else {
                cerrarRealmN(realm);

                if (ut.esPantallaChica(this)) {
                    Intent i = new Intent(getApplicationContext(), ClientesActivity.class);
                    startActivity(i);
                } else {
                    Intent i = new Intent(getApplicationContext(), ClientesGrandeActivity.class);
                    startActivity(i);
                }
            }


        } else {
            mandarMensaje("No tienes permiso para consultar los clientes");
        }
    }

    public void btnAbrirCajaClick(View view) {

        EditText txtEfectivo = (EditText) findViewById(R.id.txtMontoInicial);
        if (txtEfectivo.getText().toString().trim().equals("")) {
            mandarMensaje("Debes de dar un monto válido para Abrir la Caja");
        } else {
            boolean efectivoValido = true;
            double efectivo = 0;
            try {
                efectivo = Double.parseDouble(txtEfectivo.getText().toString());
            } catch (Exception ex) {
                efectivoValido = false;
            }


            if (efectivoValido) {
                abrirCaja();

            } else {
                mandarMensaje("Debes de dar un monto válido para Abrir la Caja");
            }
        }

    }

    private void abrirCaja() {
        Utilerias ut = new Utilerias();
        EditText txtMontoInicial = findViewById(R.id.txtMontoInicial);
        double montoInicial = 0;
        if (!txtMontoInicial.getText().toString().equals("")) {
            montoInicial = ut.parseDouble(txtMontoInicial.getText().toString());
        }

        // En cajas siempre es modo offline, porque siempre guardamos en local

        ut.abrirCaja(this, this, montoInicial, realm);

        if (ut.obtenerModoAplicacion(this)) {
            if (ut.verificaConexion(this)) {
                UtileriasSincronizacion uts = new UtileriasSincronizacion();
                uts.sincronizarTodo(this, this, realm,
                        Long.parseLong(ut.obtenerValor("idTienda", context)));
            }
        }
        cerrarRealmN(realm);
        Intent i = new Intent(getApplicationContext(), PuntoVentaActivity.class);
        startActivity(i);


    }

    public void btnWhatssClick(View view) {
        goToUrl("https://bit.ly/2JwSNWK");
    }


    private void goToUrl(String url) {
        Uri uriUrl = Uri.parse(url);
        cerrarRealmN(realm);
        Intent WebView = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(WebView);
    }

    public void btnReportesClick(View view) {
        cerrarRealmN(realm);
        Intent i = new Intent(getApplicationContext(), ReportesActivity.class);
        startActivity(i);
    }


    public void btnProductosClick(View view) {
        boton = "productos";
        if (permisos.getProd_R()) {

            boolean seguir = true;
            Utilerias ut = new Utilerias();
            if (ut.obtenerModoAplicacion(this)) {
                if (ut.verificaConexion(this)) {
                    UtileriasSincronizacion uts = new UtileriasSincronizacion();



                     uts.sincronizarTodo(this, this, realm,
                            Long.parseLong(ut.obtenerValor("idTienda", context)));


                        actualizarCatalogos();

                } else {
                    seguir = false;
                    mandarMensaje("No puedes ingresar en modo online, ya que no tienes una conexón a la red");
                }
            } else {
                cerrarRealmN(realm);
                Intent i = new Intent(getApplicationContext(), ProductosActivity.class);
                startActivity(i);
            }


        } else {
            mandarMensaje("No tienes permiso para consultar los productos");
        }
    }

    public void mandarMensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }

    private void cerrarRealmN(Realm realm) {
        if (realm != null && !realm.isClosed()) {
            realm.close();
        }
    }

    private void actualizarCatalogos() {
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Utilerias ut = new Utilerias();
        GrupoDTO pro = new GrupoDTO();


        try {

            pro.setIdAndroid(ut.obtenerSerial(context, this));
            pro.setIdUT(Long.parseLong(ut.obtenerValor("idUT", context)));
            pro.setIdTienda(ut.obtenerValor("idTienda", context));
            Call<GrupoDTO> call = apiInterface.mandarConsultarGrupos(pro);
            GrupoService ls = new GrupoService(call);
            ls.execute();

        } catch (Exception ex) {
            ex.printStackTrace();
        }


        ProductoDTO pr = new ProductoDTO();


        try {

            pr.setIdAndroid(ut.obtenerSerial(this, this));
            UsuariosDB udb = new UsuariosDB();

            int idUT = udb.obtenerIdUTUsuario(Integer.parseInt(ut.obtenerValor("idUsuario", this)),
                    Integer.parseInt(ut.obtenerValor("idTienda", this)), realm).getIdUT();

            pr.setIdUT(idUT);
            pr.setAll(false);
            pr.setIdTienda(ut.obtenerValor("idTienda", this));
            Call<ProductoDTO> call = apiInterface.mandarConsultarProductos(pr);
            ProductosService ls = new ProductosService(call);
            ls.execute();

        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }


    class ProductosService extends AsyncTask<Void, Void, ProductoDTO> {

        Call<ProductoDTO> call;

        public ProductosService(Call<ProductoDTO> call) {
            this.call = call;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress_bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ProductoDTO doInBackground(Void... params) {
            try {
                // String json = new Gson().toJson(call.execute().body());
                Utilerias ut = new Utilerias();
                ProductoDTO res = call.execute().body();
                if (res == null) {
                    System.out.println("No pudimos consultar los productos");
                } else {
                    if (res.getProductosxy() == null) {
                        System.out.println(res.getMsg());
                    } else {
                        if (res.getProductosxy().size() > 0) {
                            ProductosDB pdb = new ProductosDB();
                            Realm realm2 = ut.obtenerInstanciaBD();
                            pdb.actualizarBDProductos(res.getProductosxy(),
                                    Integer.parseInt(ut.obtenerValor("idTienda", context)),
                                    ut.obtenerModoAplicacion(context), ut.verificaConexion(context), realm2,context);

                            for (ProductosXYDTO pro : res.getProductosxy()
                            ) {
                                if (pro.isPaquete()) {

                                    pdb.borrarPaquetesProducto(pro, realm2);

                                    if (pro.getPaquetesxy() != null && pro.getPaquetesxy().size() > 0) {

                                        pdb.guardarPaquetesProducto(pro, realm2);

                                    }
                                }
                            }
                            if (realm2 != null && !realm2.isClosed()) {
                                realm2.close();
                            }
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

            if (boton.equals("productos")) {
                cerrarRealmN(realm);
                Intent i = new Intent(getApplicationContext(), ProductosActivity.class);
                startActivity(i);
            } else {
                Utilerias ut = new Utilerias();
                ProductosDB pdb = new ProductosDB();

                List<ProductosXYDTOAux> productos = pdb.obtenerProductosCompletos(
                        Integer.parseInt(ut.obtenerValor("idTienda", context)), realm);

                if (productos != null && productos.size() > 0) {

                    UtileriasSincronizacion uts = new UtileriasSincronizacion();

                    String error = uts.sincronizarCajas(context, activity, realm,
                            Long.parseLong(ut.obtenerValor("idTienda", context)));


                    CajaDTOLocal caja = ut.obtenerCajaActual(context, activity, realm);

                    if (caja != null) {
                        cerrarRealmN(realm);
                        Intent i = new Intent(getApplicationContext(), PuntoVentaActivity.class);
                        startActivity(i);
                    } else {
                        mostrarAbrirCaja();
                    }

                } else {
                    progress_bar.setVisibility(View.INVISIBLE);
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            mandarMensaje("Necesitas tener dados de alta productos" +
                                    " para poder ingresar al punto de venta");
                        }
                    });

                }

            }
        }

    }


    class GrupoService extends AsyncTask<Void, Void, GrupoDTO> {

        Call<GrupoDTO> call;


        public GrupoService(Call<GrupoDTO> call) {
            this.call = call;


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress_bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected GrupoDTO doInBackground(Void... params) {
            Utilerias ut = new Utilerias();
            try {

                GrupoDTO res = call.execute().body();
                if (res == null) {
                    System.out.println("No pudimos consultar los grupos");
                } else {
                    if (res.getGruposVRxy() == null) {
                        System.out.println(res.getMsg());
                    } else {
                        if (res.getGruposVRxy().size() > 0) {
                            GrupoDB pdb = new GrupoDB();
                            Realm realm1 = ut.obtenerInstanciaBD();
                            pdb.actualizarBDGrupos(res.getGruposVRxy(),
                                    Integer.parseInt(ut.obtenerValor("idTienda", context)), realm1);
                            cerrarRealmN(realm1);
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
        protected void onPostExecute(GrupoDTO res) {

        }


    }

    private void actualizarClientes() {
        ClienteDTO pr = new ClienteDTO();
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);


        try {
            Utilerias ut = new Utilerias();
            pr.setIdAndroid(ut.obtenerSerial(context, this));
            UsuariosDB udb = new UsuariosDB();

            int idUT = udb.obtenerIdUTUsuario(Integer.parseInt(ut.obtenerValor("idUsuario", context)),
                    Integer.parseInt(ut.obtenerValor("idTienda", context)), realm).getIdUT();

            pr.setIdUT(idUT);
            Call<ClienteDTO> call = apiInterface.mandarConsultarClientes(pr);
            ClientesService ls = new ClientesService(call);

            ls.execute();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    class ClientesService extends AsyncTask<Void, Void, ClienteDTO> {

        Call<ClienteDTO> call;

        public ClientesService(Call<ClienteDTO> call) {
            this.call = call;
        }

        @Override
        protected ClienteDTO doInBackground(Void... params) {
            try {
                Utilerias ut = new Utilerias();
                ClienteDTO res = call.execute().body();

                if (res == null) {
                    System.out.println("No pudimos consultar los clientes");
                } else {
                    if (res.getClientesxy() == null) {
                        System.out.println(res.getMsg());
                    } else {
                        if (res.getClientesxy().size() > 0) {
                            ClientesDB pdb = new ClientesDB();
                            Realm auxrealm = ut.obtenerInstanciaBD();
                            pdb.actualizarBDClientes(res.getClientesxy(),
                                    Integer.parseInt(ut.obtenerValor("idTienda", context)), auxrealm);
                            auxrealm.close();
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
        protected void onPreExecute() {
            super.onPreExecute();

            progress_bar.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(ClienteDTO res) {
            if (!boton.equals("puntoVenta")) {
                cerrarRealmN(realm);
                Intent i = new Intent(getApplicationContext(), ClientesActivity.class);
                startActivity(i);
            }
        }


    }


}
