package com.anegocios.puntoventa;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.anegocios.puntoventa.database.ProductosDB;
import com.anegocios.puntoventa.database.TiendasDB;
import com.anegocios.puntoventa.database.UsuariosDB;
import com.anegocios.puntoventa.jsons.CrearCuentaDTO;
import com.anegocios.puntoventa.jsons.CrearCuentaResponseDTO;
import com.anegocios.puntoventa.jsons.LoginDTO;
import com.anegocios.puntoventa.jsons.LoginResponseDTO;
import com.anegocios.puntoventa.jsons.ProductoDTO;
import com.anegocios.puntoventa.jsons.ProductosXYDTO;
import com.anegocios.puntoventa.jsons.UT;
import com.anegocios.puntoventa.jsons.Usuario;
import com.anegocios.puntoventa.servicios.APIClient;
import com.anegocios.puntoventa.servicios.APIInterface;

import com.anegocios.puntoventa.utils.ActualizacionCatalogosUtil;
import com.anegocios.puntoventa.utils.Utilerias;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import io.realm.Realm;
import retrofit2.Call;

public class CrearCuentaActivity extends AppCompatActivity {

    Realm realm;
    Context context;
    Activity activity;
    ProgressBar progress_bar;
    String usuarioLog;
    String passLog;
    String mensaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crearcuenta);
        context = this;
        activity = this;
        cargarOnfocus();
        Utilerias ut = new Utilerias();
        realm = ut.obtenerInstanciaBD(this);
        progress_bar = findViewById(R.id.progress_bar);
        progress_bar.setVisibility(View.INVISIBLE);
    }


    private void cargarOnfocus() {
        EditText txtUsuario = (EditText) findViewById(R.id.txtUsuario);
        EditText txtPass = (EditText) findViewById(R.id.txtPass);
        EditText txtCelular = (EditText) findViewById(R.id.txtCelular);
        EditText txtCorreo = (EditText) findViewById(R.id.txtCorreo);
        EditText txtEmpresa = (EditText) findViewById(R.id.txtEmpresa);
        EditText txtEstado = (EditText) findViewById(R.id.txtEstado);

        final TextView lblUsuario = (TextView) findViewById(R.id.lblUsuario);
        final TextView lblContrasenia = (TextView) findViewById(R.id.lblContrasenia);
        final TextView lblCelular = (TextView) findViewById(R.id.lblCelular);
        final TextView lblCorreo = (TextView) findViewById(R.id.lblCorreo);
        final TextView lblNombreNegocio = (TextView) findViewById(R.id.lblNombreNegocio);
        final TextView lblEstado = (TextView) findViewById(R.id.lblEstado);

        final View vieUsuario = (View) findViewById(R.id.vieUsuario);
        final View vieContrasenia = (View) findViewById(R.id.vieContrasenia);
        final View vieCelular = (View) findViewById(R.id.vieCelular);
        final View vieCorreo = (View) findViewById(R.id.vieCorreo);
        final View vieNombreNegocio = (View) findViewById(R.id.vieNombreNegocio);
        final View vieEstado = (View) findViewById(R.id.vieEstado);

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

        txtPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    lblContrasenia.setTextColor(ContextCompat.getColor(context, R.color.letraVerde));
                    vieContrasenia.setBackgroundColor(ContextCompat.getColor(context, R.color.letraVerde));
                } else {
                    lblContrasenia.setTextColor(ContextCompat.getColor(context, R.color.letraCobrar));
                    vieContrasenia.setBackgroundColor(ContextCompat.getColor(context, R.color.letraCobrar));
                }
            }
        });

        txtCelular.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    lblCelular.setTextColor(ContextCompat.getColor(context, R.color.letraVerde));
                    vieCelular.setBackgroundColor(ContextCompat.getColor(context, R.color.letraVerde));
                } else {
                    lblCelular.setTextColor(ContextCompat.getColor(context, R.color.letraCobrar));
                    vieCelular.setBackgroundColor(ContextCompat.getColor(context, R.color.letraCobrar));
                }
            }
        });

        txtCorreo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    lblCorreo.setTextColor(ContextCompat.getColor(context, R.color.letraVerde));
                    vieCorreo.setBackgroundColor(ContextCompat.getColor(context, R.color.letraVerde));
                } else {
                    lblCorreo.setTextColor(ContextCompat.getColor(context, R.color.letraCobrar));
                    vieCorreo.setBackgroundColor(ContextCompat.getColor(context, R.color.letraCobrar));
                }
            }
        });

        txtEmpresa.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    lblNombreNegocio.setTextColor(ContextCompat.getColor(context, R.color.letraVerde));
                    vieNombreNegocio.setBackgroundColor(ContextCompat.getColor(context, R.color.letraVerde));
                } else {
                    lblNombreNegocio.setTextColor(ContextCompat.getColor(context, R.color.letraCobrar));
                    vieNombreNegocio.setBackgroundColor(ContextCompat.getColor(context, R.color.letraCobrar));
                }
            }
        });


        txtEstado.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    lblEstado.setTextColor(ContextCompat.getColor(context, R.color.letraVerde));
                    vieEstado.setBackgroundColor(ContextCompat.getColor(context, R.color.letraVerde));
                } else {
                    lblEstado.setTextColor(ContextCompat.getColor(context, R.color.letraCobrar));
                    vieEstado.setBackgroundColor(ContextCompat.getColor(context, R.color.letraCobrar));
                }
            }
        });


    }

    public void btnCancelaCrearClick(View view) {
        //regresamos a login
        cerrarRealmN(realm);
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);

    }


    public void btnCrearCuentaClick(View view) {
        //validamos los campos
        EditText txtUsuario = (EditText) findViewById(R.id.txtUsuario);
        EditText txtPass = (EditText) findViewById(R.id.txtPass);
        EditText txtCelular = (EditText) findViewById(R.id.txtCelular);
        EditText txtCorreo = (EditText) findViewById(R.id.txtCorreo);
        EditText txtEstado = (EditText) findViewById(R.id.txtEstado);
        EditText txtEmpresa = (EditText) findViewById(R.id.txtEmpresa);
        Utilerias ut = new Utilerias();
        if (ut.verificaConexion(this)) {
            if (txtUsuario.getText().toString().trim().equals("") || txtCorreo.getText().toString().trim().equals("")
                    || txtPass.getText().toString().trim().equals("") || txtCelular.getText().toString().trim().equals("")) {
                mandarMensaje("Todos los campos son obligatorios, por favor verifique");
            } else {
                boolean celularCorrecto = true;
                try {
                    Double.parseDouble(txtCelular.getText().toString());
                } catch (Exception ex) {
                    celularCorrecto = false;
                }
                if (txtCelular.getText().length() < 10 || !celularCorrecto) {
                    mandarMensaje("El número de celular debe de ser de 10 dígitos, por favor verifique");
                } else {

                    if (ut.emailValido(txtCorreo.getText().toString())) {
                        CrearCuentaDTO cc = new CrearCuentaDTO();
                        cc.setBrand(Build.BRAND);
                        cc.setModel(Build.MODEL);
                        cc.setProduct(Build.MANUFACTURER);
                        cc.setCel(txtCelular.getText().toString());
                        cc.setCorreo(txtCorreo.getText().toString());
                        cc.setEmpresa(txtEmpresa.getText().toString());
                        cc.setEstado(txtEstado.getText().toString());
                        cc.setPais("");
                        cc.setUsuario(txtUsuario.getText().toString());
                        cc.setPass(txtPass.getText().toString());
                        cc.setProduct(ut.getDeviceName());
                        String serialNumber = ut.obtenerSerial(this, this);
                        cc.setIdAndroid(serialNumber);
                        crearCuenta(cc, txtUsuario.getText().toString(), txtPass.getText().toString());

                    } else {
                        mandarMensaje("El correo no es válido, por favor verifique");
                    }

                }
            }
        } else {
            mandarMensaje("No tiene conexión a internet, por favor verifique");
        }
    }

    private void crearCuenta(CrearCuentaDTO cc, String usuario, String pass) {
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<CrearCuentaResponseDTO> call = apiInterface.crearCuenta(cc);

        CrearCuentaService ls = new CrearCuentaService(call);
        usuarioLog = usuario;
        passLog = pass;
        ls.execute();


    }

    public void mandarMensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }


    public void login(String pass, String usuario) {
        Utilerias ut = new Utilerias();


        if (ut.verificaConexion(this)) {
            LoginDTO l = new LoginDTO();
            l.setBrand(Build.BRAND);
            l.setIdAndroid(ut.obtenerSerial(this, this));
            l.setModel(Build.MODEL);
            l.setPass(pass);
            l.setProduct(Build.PRODUCT);
            l.setUsername(usuario);
            //login(l);
            APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
            Call<LoginResponseDTO> call = apiInterface.login(l);

            LoginService ls = new LoginService(call);
            try {
                final LoginResponseDTO res = ls.execute().get();

                if (res == null) {
                    mandarMensaje("Ocurrió un problema realizando el login, por favor intente de nuevo");
                } else {

                    if (res.getUsuarios() != null && res.getUsuarios().size() > 0) {
                        UsuariosDB udb = new UsuariosDB();

                        udb.actualizarBDUsuarios(res.getUsuarios(), realm);


                        for (Usuario u : res.getUsuarios()
                        ) {
                            if (u.getUT() != null && u.getUT().size() > 0) {

                                for (UT iut : u.getUT()
                                ) {
                                    if (udb.verificarExistenciaUT(u.getId(), iut.getIdTienda(), iut.getId(), realm) == null) {
                                        udb.guardarUTUsuario(u.getId(), iut.getIdTienda(), iut.getId(),
                                                realm, iut.getVigencia(), iut.getTipoLic());
                                    }
                                }


                            }
                        }
                    }


                    if (res.getAcceso()) {
                        UsuariosDB udb = new UsuariosDB();

                        List<Usuario> usuarios = udb.obtenerListaUsuarios(realm);

                        Usuario encontrado = null;
                        for (Usuario u : usuarios) {
                            if (u.getUsername().equals(usuario)) {
                                encontrado = u;
                                break;
                            }
                        }
                        if (encontrado != null) {
                            if (res.getTiendas() != null && res.getTiendas().size() > 0) {
                                TiendasDB tdb = new TiendasDB();

                                tdb.actualizarBDTiendas(res.getTiendas(), encontrado.getId(), realm);

                            }


                            usuarioCorrecto(encontrado, res.getTiendas().get(0).getId(), res.getTiendas().get(0).getImagenesPV());
                        }

                    } else {
                        mandarMensaje(res.getMsg());
                    }
                }
            } catch (Exception ex) {
                mandarMensaje(ex.getMessage());
            }
        }
    }

    private void cerrarRealmN(Realm realm) {
        if (realm != null && !realm.isClosed()) {
            realm.close();
        }
    }

    private void siActualizar() {
        Utilerias ut = new Utilerias();
        if (ut.verificaConexion(this)) {
            ProductoDTO pr = new ProductoDTO();
            APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);


            try {

                pr.setIdAndroid(ut.obtenerSerial(this, this));
                UsuariosDB udb = new UsuariosDB();
                Realm realm = ut.obtenerInstanciaBD(this);
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
                if (realm != null && !realm.isClosed()) {
                    realm.close();
                }


            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            mandarMensaje("No tiene conexion activa a internet para actualizar los productos");
        }
    }

    public void mostrarActualizando() {
        setContentView(R.layout.actualizandoproductos);
    }

    private void irPuntoVenta() {
        Utilerias ut = new Utilerias();
        ut.validaIrPuntoVenta(this, this, realm, ut.obtenerPermisosUsuario(this), "normal");
    }

    public void usuarioCorrecto(Usuario u, int idTienda, Boolean imagenes) {

        Utilerias ut = new Utilerias();
        ut.guardarValor("idUT", "" + u.getUtInt(), this);
        ut.guardarValor("idUsuario", "" + u.getId(), this);
        ut.guardarPermisosUsuario(u, this, imagenes);

        ut.guardarValor("idTienda", "" + idTienda, this);
       siActualizar();


    }


    class CrearCuentaService extends AsyncTask<CrearCuentaService, Void, CrearCuentaResponseDTO> {

        Call<CrearCuentaResponseDTO> call;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress_bar.setVisibility(View.VISIBLE);
        }

        public CrearCuentaService(Call<CrearCuentaResponseDTO> call) {
            this.call = call;

        }

        @Override
        protected CrearCuentaResponseDTO doInBackground(CrearCuentaService... params) {
            try {

                CrearCuentaResponseDTO res = call.execute().body();
                if (res != null && res.isExito()) {

                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            mandarMensaje("Se creo con exito la cuenta");
                        }
                    });

                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            ActualizacionCatalogosUtil acum = new ActualizacionCatalogosUtil();
                            acum.consultarClientes(context, activity);
                            acum.consultarGrupos(context, activity);
                            login(passLog, usuarioLog);
                        }
                    });

                } else {
                    if (res == null) {
                        mandarMensaje("No se pudo contactar el servicio, intente de nuevo");
                    } else {
                        mensaje = res.getMsg();
                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                mandarMensaje(mensaje);
                            }
                        });

                    }
                }
                return res;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(CrearCuentaResponseDTO res) {
            progress_bar.setVisibility(View.INVISIBLE);

        }


    }


    class LoginService extends AsyncTask<MainActivity.LoginService, Void, LoginResponseDTO> {

        Call<LoginResponseDTO> call;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        public LoginService(Call<LoginResponseDTO> call) {
            this.call = call;

        }

        @Override
        protected LoginResponseDTO doInBackground(MainActivity.LoginService... params) {
            try {

                LoginResponseDTO res = call.execute().body();

                return res;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(LoginResponseDTO res) {


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
                        Realm realm = ut.obtenerInstanciaBD(context);
                        if (res.getProductosxy().size() > 0) {
                            ProductosDB pdb = new ProductosDB();
                            pdb.actualizarBDProductos(res.getProductosxy(),
                                    Integer.parseInt(ut.obtenerValor("idTienda", context)),
                                    ut.obtenerModoAplicacion(context), ut.verificaConexion(context), realm, context);
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
