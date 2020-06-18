package com.anegocios.puntoventa;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.anegocios.puntoventa.database.GrupoDB;
import com.anegocios.puntoventa.database.ProductosDB;
import com.anegocios.puntoventa.database.TiendasDB;
import com.anegocios.puntoventa.database.UsuariosDB;
import com.anegocios.puntoventa.dtosauxiliares.ProductosXYDTOAux;
import com.anegocios.puntoventa.jsons.GrupoDTO;
import com.anegocios.puntoventa.jsons.LoginDTO;
import com.anegocios.puntoventa.jsons.LoginResponseDTO;
import com.anegocios.puntoventa.jsons.ProductoDTO;
import com.anegocios.puntoventa.jsons.ProductosXYDTO;
import com.anegocios.puntoventa.jsons.RecuperarContraseniaDTO;
import com.anegocios.puntoventa.jsons.RecuperarContraseniaResponseDTO;
import com.anegocios.puntoventa.jsons.Tienda;
import com.anegocios.puntoventa.jsons.UT;
import com.anegocios.puntoventa.jsons.Usuario;
import com.anegocios.puntoventa.jsons.UsuariosUts;
import com.anegocios.puntoventa.servicios.APIClient;
import com.anegocios.puntoventa.servicios.APIInterface;

import com.anegocios.puntoventa.servicios.RecuperarContraseniaService;
import com.anegocios.puntoventa.utils.ActualizacionCatalogosUtil;
import com.anegocios.puntoventa.utils.Utilerias;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import retrofit2.Call;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {


    List<UsuariosUts> tiendasUsuario;
    ProgressBar progress_bar;
    EditText txtUsuario;
    Context context;
    Activity activity;
    LoginResponseDTO resLogin;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utilerias ut = new Utilerias();
        String wizardVisto = ut.obtenerValor("wizardVisto", this);
        context = this;
        activity = this;


        if (wizardVisto != null && wizardVisto.toUpperCase().equals("TRUE")) {

            setContentView(R.layout.login);
            progress_bar = findViewById(R.id.progress_bar);
            progress_bar.setVisibility(View.INVISIBLE);
            txtUsuario = findViewById(R.id.txtUsuario);
            manejarEnterPass();
            if (!ut.tienePermisos(this, this)) {
                mandarMensaje("Por favor permite los permisos correspondientes" +
                        " a la app, para su funcionamiento correcto");
            }

            Realm.init(this);
       /* RealmConfiguration config = new RealmConfiguration.Builder()
                .schemaVersion(1) // Must be bumped when the schema changes
                .migration(new MigracionRealm()) // Migration to run instead of throwing an exception
                .build();*/

            RealmConfiguration config = new RealmConfiguration.Builder()
                    .deleteRealmIfMigrationNeeded()
                    .build();

            Realm.setDefaultConfiguration(config);
            realm = ut.obtenerInstanciaBD();
            ImageButton btnModo = findViewById(R.id.btnModo);
            if (ut.obtenerModoAplicacion(this)) {
                btnModo.setImageResource(R.drawable.conconexionbla);
            } else {
                btnModo.setImageResource(R.drawable.sinconexionbla);
            }
        } else {
            Intent i = new Intent(getApplicationContext(), SliderActivity.class);
            startActivity(i);
        }
    }


    public void btnRecuperarClick(View view) {
        setContentView(R.layout.recuperarcontrasenia);
        EditText txtUsuario = (EditText) findViewById(R.id.txtUsuario);
        final TextView lblUsuario = (TextView) findViewById(R.id.lblUsuario);
        final View vieUsuario = (View) findViewById(R.id.vieUsuario);
        txtUsuario.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    lblUsuario.setTextColor(ContextCompat.getColor(context, R.color.letraVerde));
                    vieUsuario.setBackgroundColor(ContextCompat.getColor(context, R.color.letraVerde));
                } else {
                    lblUsuario.setTextColor(ContextCompat.getColor(context, R.color.letraCobrar));
                    vieUsuario.setBackgroundColor(ContextCompat.getColor(context, R.color.letraCobrar));
                }
            }
        });

    }


    public void btnCrearClick(View view) {
        cerrarRealmN(realm);
        Intent i = new Intent(getApplicationContext(), CrearCuentaActivity.class);
        startActivity(i);

    }

    public void btnEntrarClick(View view) {

        login();

    }


    public void preguntarActualizar() {

        android.support.v7.app.AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle("Actualizar Catálogos ");
        alert.setMessage("Esta acción actualiza todos los catálogos, puede tomar varios minutos, desea continuar?");
        alert.setPositiveButton("Actualizar Catálogos", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                siActualizar();
                dialog.dismiss();
            }
        });

        alert.setNegativeButton("Ir Punto de Venta", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                irPuntoVenta();
                dialog.dismiss();
            }
        });

        alert.show();

    }


    private void siActualizar()
    {
        Utilerias ut = new Utilerias();
        if (ut.verificaConexion(this)) {
            ProductoDTO pr = new ProductoDTO();
            APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);


            try {

                pr.setIdAndroid(ut.obtenerSerial(this, this));
                UsuariosDB udb = new UsuariosDB();
                Realm realm = ut.obtenerInstanciaBD();
                int idUT = udb.obtenerIdUTUsuario(Integer.parseInt(ut.obtenerValor("idUsuario", this)),
                        Integer.parseInt(ut.obtenerValor("idTienda", this)), realm).getIdUT();
                pr.setIdUT(idUT);
                pr.setAll(true);
                pr.setIdTienda(ut.obtenerValor("idTienda", this));
                Call<ProductoDTO> call = apiInterface.mandarConsultarProductos(pr);
                Gson json = new Gson();
                String gson = json.toJson(pr);

                ProductoAsyncService ls = new ProductoAsyncService(call);
                ls.execute();
                if(realm!=null && !realm.isClosed())
                {
                    realm.close();
                }



            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            mandarMensaje("No tiene conexion activa a internet para actualizar los productos");
        }
    }

    public void btnModoClick(View view) {

        Utilerias ut = new Utilerias();
        String modo = ut.obtenerValor("modoApp", this);
        ImageButton btnModo = findViewById(R.id.btnModo);
        if (modo == null) {
            ut.guardarValor("modoApp", "on", this);
            btnModo.setImageResource(R.drawable.conconexionbla);
        } else {
            if (modo.equals("on")) {
                //estaba en on lo cambiamos a off
                ut.guardarValor("modoApp", "off", this);
                btnModo.setImageResource(R.drawable.sinconexionbla);
            } else {
                ut.guardarValor("modoApp", "on", this);
                btnModo.setImageResource(R.drawable.conconexionbla);
            }
        }
    }

    public void btnCancelaRecuperarClick(View view) {
        //regresamos al login
        regresarLogin();

    }

    private void manejarEnterPass() {
        EditText txtPass = (EditText) findViewById(R.id.txtPass);
        txtPass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    login();

                    handled = true;
                }
                return handled;
            }
        });
    }


    public void btnMandarCalificar(View view) {
        cerrarRealmN(realm);
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.anegocios.puntoventa")));
    }

    public void btnRecuperarContraseniaClick(View view) {
        //validamos el campo
        EditText txtUsuario = (EditText) findViewById(R.id.txtUsuario);
        if (txtUsuario.getText().toString().trim().equals("")
        ) {
            mandarMensaje("El usuario o correo es obligatorio, por favor verifique");
        } else {
            RecuperarContraseniaDTO rc = new RecuperarContraseniaDTO();
            rc.setUsername(txtUsuario.getText().toString());
            APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
            Call<RecuperarContraseniaResponseDTO> call = apiInterface.recuperarContrasenia(rc);

            RecuperarContraseniaService ls = new RecuperarContraseniaService(call);
            try {
                final RecuperarContraseniaResponseDTO res = ls.execute().get();
                if (res == null) {
                    mandarMensaje("Ocurrió un problema, por favor intente de nuevo");
                } else {
                    mandarMensaje(res.getMsg());
                }
            } catch (Exception ex) {
                mandarMensaje(ex.getMessage());
            }

        }
    }

    public void login() {

        try {
            Utilerias ut = new Utilerias();

            if (ut.tienePermisos(this, this)) {
                Utilerias.log(this, "entro aplicacion", null);

                EditText txtUsuario = (EditText) findViewById(R.id.txtUsuario);
                EditText txtPass = (EditText) findViewById(R.id.txtPass);
                if (txtUsuario.getText().toString().trim().equals("")
                        || txtPass.getText().toString().trim().equals("")
                ) {
                    mandarMensaje("Todos los campos son obligatorios, por favor verifique");
                } else {
                    String modo = ut.obtenerValor("modoApp", this);
                    if (modo == null) {
                        modo = "off";
                    }

                    if (ut.verificaConexion(this) && modo.equals("on")) {
                        LoginDTO l = new LoginDTO();
                        l.setBrand(Build.BRAND);
                        l.setIdAndroid(ut.obtenerSerial(this, this));
                        l.setModel(Build.MODEL);
                        l.setPass(txtPass.getText().toString());
                        l.setProduct(Build.PRODUCT);
                        l.setUsername(txtUsuario.getText().toString());
                        //login(l);
                        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
                        Gson gson = new Gson();
                        String json = gson.toJson(l);
                        Call<LoginResponseDTO> call = apiInterface.login(l);
                        LoginService ls = new LoginService(call);
                        ls.execute();
                    } else {
                        //verificamos el acceso con lo que tenemos guardado
                        UsuariosDB udb = new UsuariosDB();
                        if (realm == null || realm.isClosed()) {
                            realm = ut.obtenerInstanciaBD();
                        }
                        List<Usuario> usuarios = udb.obtenerListaUsuarios(realm);

                        Usuario encontrado = null;
                        String mensaje = "";
                        for (Usuario u : usuarios) {
                            if (u.getUsername().toUpperCase().equals(txtUsuario.getText().toString().toUpperCase())) {
                                if (u.getPass().equals(ut.encryptPassword(txtPass.getText().toString(), u.getSalt()))) {
                                    encontrado = u;
                                    break;
                                } else {
                                    mensaje = "La contraseña no coincide";
                                }

                            }
                        }
                        if (encontrado != null) {
                            usuarioCorrecto(encontrado, realm, true);
                        } else {
                            if (mensaje.equals("")) {
                                mensaje = "No existe conexión y no se encontró" +
                                        "ningún usuario que concuerde en el dispositivo";
                            }
                            mandarMensaje(mensaje);
                        }
                    }
                }
            } else {
                mandarMensaje("Debes agregar los permisos a la aplicacion para que funcione correctamente");
                ut.pedirPermisos(this, this);
            }
        } catch (Exception ex) {
            Utilerias.log(this, "Error Login: " + ex.getMessage() + " " + ex.getStackTrace(), ex);
        }
    }

    @Override
    public void onBackPressed() {
        regresarLogin();
    }

    private void regresarLogin() {
        cerrarRealmN(realm);
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }


    public void mostrarTiendas() {
        setContentView(R.layout.tiendas);
        Spinner spTiendas = (Spinner) findViewById(R.id.spTiendas);
        List<String> gruposS = new ArrayList<String>();
        TiendasDB tdb = new TiendasDB();
        UsuariosDB udb = new UsuariosDB();
        Utilerias ut = new Utilerias();

        List<UsuariosUts> tiendas = udb.obtenerTiendasUT(Integer.parseInt(
                ut.obtenerValor("idUsuario", this)), realm);
        for (UsuariosUts gr : tiendas
        ) {
            Tienda t = tdb.obtenerTienda(gr.getIdTienda(), realm);
            if (t != null) {
                gruposS.add(t.getNombre());
            }
        }

        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, gruposS);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spTiendas.setAdapter(aa);
    }

    public void mandarMensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }

    private void cerrarRealmN(Realm realm) {
        try {
            if (realm != null && !realm.isClosed()) {
                realm.close();
            }
        } catch (Exception ef) {

        }
    }

    public void usuarioCorrecto(Usuario u, Realm realm2, Boolean imagenes) {

        Utilerias ut = new Utilerias();
        ut.guardarValor("idUT", "" + u.getUtInt(), this);
        ut.guardarValor("idUsuario", "" + u.getId(), this);

        ut.guardarPermisosUsuario(u, this, imagenes);
        UsuariosDB udb = new UsuariosDB();

        tiendasUsuario = udb.obtenerTiendasUT(u.getId(), realm2);


        //antes de entrar tenemos que verificar si tiene productos
        //si no los tiene es porque se reinstalo y los traemos todos de nuevo


        if (tiendasUsuario != null && tiendasUsuario.size() > 1) {

            activity.runOnUiThread(new Runnable() {
                public void run() {
                    mostrarTiendas();
                }
            });
        } else {
            if (tiendasUsuario != null && tiendasUsuario.size() > 0) {
                ProductosDB pdb = new ProductosDB();

                List<ProductosXYDTOAux> productos = pdb.obtenerProductosCompletos(tiendasUsuario.get(0).getIdTienda(), realm2);
                ut.guardarValor("vigencia", tiendasUsuario.get(0).getVigencia(), this);
                ut.guardarValor("tipoUsuario", tiendasUsuario.get(0).getTipoLic(), this);
                if (productos != null && productos.size() > 0) {
                    ut.guardarValor("idTienda", "" + tiendasUsuario.get(0).getIdTienda(), this);
                    ActualizacionCatalogosUtil acut = new ActualizacionCatalogosUtil();
                    acut.consultarGrupos(context,activity);
                    acut.consultarClientes(context, activity);
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                           preguntarActualizar();
                        }
                    });


                } else {


                    ut.guardarValor("idTienda", "" + tiendasUsuario.get(0).getIdTienda(), this);
                    cerrarRealmN(realm2);

                    ActualizacionCatalogosUtil acut = new ActualizacionCatalogosUtil();

                    acut.consultarGrupos(context, activity);
                    acut.consultarClientes(context, activity);

                    ut.guardarValor("modoApp", "on", this);
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            cerrarRealmN(realm);
                        }
                    });

                    siActualizar();
                }
            } else {
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        mandarMensaje("No tienes ninguna tienda asignada " +
                                "por favor verifica con el administrador");
                    }
                });

            }

        }
    }

    public void mostrarActualizando()
    {
        setContentView(R.layout.actualizandoproductos);
    }

    public void btnSelTiendaClick(View view) {
        try {
            UsuariosDB udb = new UsuariosDB();
            Utilerias ut = new Utilerias();

            List<UsuariosUts> tiendas = udb.obtenerTiendasUT(Integer.parseInt(
                    ut.obtenerValor("idUsuario", this)), realm);

            if (tiendas != null && tiendas.size() > 0) {
                Spinner spTiendas = (Spinner) findViewById(R.id.spTiendas);
                int posicion = spTiendas.getSelectedItemPosition();
                UsuariosUts gSel = tiendas.get(posicion);


                ProductosDB pdb = new ProductosDB();

                List<ProductosXYDTOAux> productos = pdb.obtenerProductosCompletos(gSel.getIdTienda(), realm);

                TiendasDB tbd = new TiendasDB();
                Tienda tienda= tbd.obtenerTienda(gSel.getIdTienda(),realm);
                String vigencia=tienda.getVigencia();
                String tipoUsuario=tienda.getTipo();
                ut.guardarValor("vigencia", vigencia, this);
                ut.guardarValor("tipoUsuario", tipoUsuario, this);
                ut.guardarValor("idTienda", "" + gSel.getIdTienda(), this);
                if (productos != null && productos.size() > 0) {
                    preguntarActualizar();

                } else {
                    ActualizacionCatalogosUtil acum= new ActualizacionCatalogosUtil();
                    acum.consultarClientes(this,this);
                    acum.consultarGrupos(this,this);
                    siActualizar();
                }

                ActualizacionCatalogosUtil acut = new ActualizacionCatalogosUtil();
                acut.consultarClientes(context, activity);

            }
        } catch (Exception ex) {
            Utilerias.log(this, "Error Login: " + ex.getMessage() + " " + ex.getStackTrace(), ex);
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
            Utilerias.log(this, "Error Actualizar Catalogos: " + ex.getMessage() + " " + ex.getStackTrace(), ex);
        }


        ProductoDTO pr = new ProductoDTO();


        try {

            pr.setIdAndroid(ut.obtenerSerial(this, this));
            UsuariosDB udb = new UsuariosDB();
            Realm realm7 = ut.obtenerInstanciaBD();
            int idUT = udb.obtenerIdUTUsuario(Integer.parseInt(ut.obtenerValor("idUsuario", this)),
                    Integer.parseInt(ut.obtenerValor("idTienda", this)), realm7).getIdUT();

            pr.setIdUT(idUT);
            pr.setAll(true);
            pr.setIdTienda(ut.obtenerValor("idTienda", this));
            Call<ProductoDTO> call = apiInterface.mandarConsultarProductos(pr);
            ProductosService ls = new ProductosService(call);
            ls.execute();
            if (realm7 != null && !realm7.isClosed()) {
                realm7.close();
            }

        } catch (Exception ex) {
            Utilerias.log(this, "Error Actualizar Catalogos: " + ex.getMessage() + " " + ex.getStackTrace(), ex);
        }


    }

    private void  irPuntoVenta()
    {
        Utilerias ut = new Utilerias();
        ut.validaIrPuntoVenta(this, this, realm, ut.obtenerPermisosUsuario(this), "normal");
    }


    class ProductosService extends AsyncTask<Void, Void, ProductoDTO> {

        Call<ProductoDTO> call;

        public ProductosService(Call<ProductoDTO> call) {
            this.call = call;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

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
                            Realm realm = ut.obtenerInstanciaBD();
                            pdb.actualizarBDProductos(res.getProductosxy(),
                                    Integer.parseInt(ut.obtenerValor("idTienda", context)),
                                    ut.obtenerModoAplicacion(context), ut.verificaConexion(context), realm, context);

                            if (realm != null && !realm.isClosed()) {
                                realm.close();
                            }

                            for (ProductosXYDTO pro : res.getProductosxy()
                            ) {
                                if (pro.isPaquete()) {
                                    realm = ut.obtenerInstanciaBD();
                                    pdb.borrarPaquetesProducto(pro, realm);
                                    if (realm != null && !realm.isClosed()) {
                                        realm.close();
                                    }
                                    if (pro.getPaquetesxy() != null && pro.getPaquetesxy().size() > 0) {
                                        realm = ut.obtenerInstanciaBD();
                                        pdb.guardarPaquetesProducto(pro, realm);
                                        if (realm != null && !realm.isClosed()) {
                                            realm.close();
                                        }
                                    }
                                }
                            }

                        }
                    }
                }
                return res;
            } catch (IOException ex) {
                Utilerias.log(context, "Error Login: " + ex.getMessage() + " " + ex.getStackTrace(), ex);
            }
            return null;
        }

        @Override
        protected void onPostExecute(ProductoDTO res) {

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
                            Realm realm = ut.obtenerInstanciaBD();
                            pdb.actualizarBDGrupos(res.getGruposVRxy(),
                                    Integer.parseInt(ut.obtenerValor("idTienda", context)), realm);

                            if (realm != null && !realm.isClosed()) {
                                realm.close();
                            }


                        }
                    }
                }
                return res;
            } catch (IOException ex) {
                Utilerias.log(context, "Error Login: " + ex.getMessage() + " " + ex.getStackTrace(), ex);
            }
            return null;
        }

        @Override
        protected void onPostExecute(GrupoDTO res) {

        }


    }

    class LoginService extends AsyncTask<LoginService, Void, LoginResponseDTO> {

        Call<LoginResponseDTO> call;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress_bar.setVisibility(View.VISIBLE);
        }

        public LoginService(Call<LoginResponseDTO> call) {
            this.call = call;

        }

        @Override
        protected LoginResponseDTO doInBackground(LoginService... params) {
            try {
                Utilerias ut = new Utilerias();

                resLogin = call.execute().body();
                try {

                    if (resLogin == null) {
                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                mandarMensaje("Ocurrió un problema realizando el login, por favor intente de nuevo");
                            }
                        });

                    } else {

                        if (resLogin.getUsuarios() != null && resLogin.getUsuarios().size() > 0) {
                            UsuariosDB udb = new UsuariosDB();
                            Realm realm3 = ut.obtenerInstanciaBD();
                            udb.actualizarBDUsuarios(resLogin.getUsuarios(), realm3);

                            for (Usuario u : resLogin.getUsuarios()
                            ) {
                                if (u.getUT() != null && u.getUT().size() > 0) {
                                    for (UT iut : u.getUT()
                                    ) {
                                        if (udb.verificarExistenciaUT(u.getId(), iut.getIdTienda(), iut.getId(), realm3) == null) {
                                            udb.guardarUTUsuario(u.getId(), iut.getIdTienda(),
                                                    iut.getId(), realm3, iut.getVigencia(), iut.getTipoLic());
                                        }
                                    }

                                }
                            }
                            if (realm3 != null && !realm3.isClosed()) {
                                realm3.close();
                            }
                        }


                        if (resLogin.getAcceso()) {
                            UsuariosDB udb = new UsuariosDB();
                            Realm realm4 = ut.obtenerInstanciaBD();
                            List<Usuario> usuarios = udb.obtenerListaUsuarios(realm4);

                            Usuario encontrado = null;
                            for (Usuario u : usuarios) {
                                if (u.getUsername().trim().toUpperCase().equals(txtUsuario.getText().toString().trim().toUpperCase())) {
                                    encontrado = u;
                                    break;
                                }
                            }

                            if (encontrado != null) {
                                if (resLogin.getTiendas() != null && resLogin.getTiendas().size() > 0) {
                                    TiendasDB tdb = new TiendasDB();

                                    tdb.actualizarBDTiendas(resLogin.getTiendas(), encontrado.getId(), realm4);

                                }


                                usuarioCorrecto(encontrado, realm4, resLogin.getTiendas().get(0).getImagenesPV());
                            } else {
                                activity.runOnUiThread(new Runnable() {
                                    public void run() {
                                        mandarMensaje("Esta escribiendo de forma incorrecta su usuario");
                                    }
                                });
                            }


                        } else {
                            activity.runOnUiThread(new Runnable() {
                                public void run() {
                                    mandarMensaje(resLogin.getMsg());
                                }
                            });

                        }
                    }
                } catch (Exception ex) {
                    Utilerias.log(context, "Error Login: " + ex.getMessage() + " " + ex.getStackTrace(), ex);
                }
                return resLogin;
            } catch (IOException ex) {
                Utilerias.log(context, "Error Login: " + ex.getMessage() + " " + ex.getStackTrace(), ex);
            }
            return null;
        }

        @Override
        protected void onPostExecute(LoginResponseDTO res) {

            progress_bar.setVisibility(View.INVISIBLE);

        }


    }


    class ProductoAsyncService extends AsyncTask<Void, Void, ProductoDTO> {

        Call<ProductoDTO> call;


        public ProductoAsyncService(Call<ProductoDTO> call) {
            this.call = call;

            activity.runOnUiThread(new Runnable() {
                public void run() {
                    mostrarActualizando();
                }
            });
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


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
                        Realm realm = ut.obtenerInstanciaBD();
                        if (res.getProductosxy().size() > 0) {
                            ProductosDB pdb = new ProductosDB();
                            pdb.actualizarBDProductos(res.getProductosxy(),
                                    Integer.parseInt(ut.obtenerValor("idTienda", context)),
                                    ut.obtenerModoAplicacion(context), ut.verificaConexion(context), realm,context);
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
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    irPuntoVenta();
                }
            });
        }

    }
}
