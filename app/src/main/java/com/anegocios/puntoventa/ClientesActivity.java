package com.anegocios.puntoventa;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.anegocios.puntoventa.adapters.ClientesAdapter;
import com.anegocios.puntoventa.bdlocal.ClienteXYDTOLocal;
import com.anegocios.puntoventa.database.ClientesDB;
import com.anegocios.puntoventa.dtosauxiliares.ClienteXYDTOAux;
import com.anegocios.puntoventa.jsons.ClienteDTO;
import com.anegocios.puntoventa.jsons.ClienteXYDTO;
import com.anegocios.puntoventa.jsons.CorreosXYDTO;
import com.anegocios.puntoventa.jsons.DireccionesXYDTO;
import com.anegocios.puntoventa.jsons.TelefonoXYDTO;
import com.anegocios.puntoventa.jsons.Usuario;
import com.anegocios.puntoventa.servicios.APIClient;
import com.anegocios.puntoventa.servicios.APIInterface;
import com.anegocios.puntoventa.servicios.ClientesService;
import com.anegocios.puntoventa.utils.ActualizacionCatalogosUtil;
import com.anegocios.puntoventa.utils.Utilerias;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmList;
import retrofit2.Call;

public class ClientesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    List<ClienteXYDTOAux> clientes;
    String pantalla;
    String accion;
    ClienteXYDTOAux pSel;
    Usuario permisos;
    Realm realm;
    Context ctx;
    Activity act;
    double latitud;
    double longitud;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Utilerias ut = new Utilerias();
        realm = ut.obtenerInstanciaBD(this);
        permisos = ut.obtenerPermisosUsuario(this);
        ctx = this;
        act = this;

        if (ut.verificaConexion(this)) {
            ActualizacionCatalogosUtil acut = new ActualizacionCatalogosUtil();
            acut.consultarClientes(ctx, act);
        }

        if (realm != null && !realm.isClosed()) {
            realm.refresh();
        } else {
            realm = ut.obtenerInstanciaBD(this);
            if (realm != null && !realm.isClosed()) {
                realm.refresh();
            }
        }

        mostrarListaClientes();


    }


    private void cargarOnfocus() {
        EditText txtNombre = (EditText) findViewById(R.id.txtNombre);
        EditText txtPaterno = (EditText) findViewById(R.id.txtPaterno);
        EditText txtMaterno = (EditText) findViewById(R.id.txtMaterno);
        EditText txtCorreo = (EditText) findViewById(R.id.txtCorreo);
        EditText txtTelefono = (EditText) findViewById(R.id.txtTelefono);
        EditText txtCalle = (EditText) findViewById(R.id.txtCalle);
        EditText txtNumeroExt = (EditText) findViewById(R.id.txtNumeroExt);
        EditText txtNumeroInt = (EditText) findViewById(R.id.txtNumeroInt);
        EditText txtColonia = (EditText) findViewById(R.id.txtColonia);
        EditText txtMunicipio = (EditText) findViewById(R.id.txtMunicipio);
        EditText txtEstado = (EditText) findViewById(R.id.txtEstado);
        EditText txtCodigoPostal = (EditText) findViewById(R.id.txtCodigoPostal);
        EditText txtComentario = (EditText) findViewById(R.id.txtComentario);
        final TextView lblNombre = (TextView) findViewById(R.id.lblNombre);
        final TextView lblAppPaterno = (TextView) findViewById(R.id.lblAppPaterno);
        final TextView lblAppMaterno = (TextView) findViewById(R.id.lblAppMaterno);
        final TextView lblCorreo = (TextView) findViewById(R.id.lblCorreo);
        final TextView lblTelefono = (TextView) findViewById(R.id.lblTelefono);
        final TextView lblCalle = (TextView) findViewById(R.id.lblCalle);
        final TextView lblExterior = (TextView) findViewById(R.id.lblExterior);
        final TextView lblInterior = (TextView) findViewById(R.id.lblInterior);
        final TextView lblColonia = (TextView) findViewById(R.id.lblColonia);
        final TextView lblMunicipio = (TextView) findViewById(R.id.lblMunicipio);
        final TextView lblEstado = (TextView) findViewById(R.id.lblEstado);
        final TextView lblCodigoPostal = (TextView) findViewById(R.id.lblCodigoPostal);
        final TextView lblComentario = (TextView) findViewById(R.id.lblComentario);
        final View vieNombre = (View) findViewById(R.id.vieNombre);
        final View vieAppPaterno = (View) findViewById(R.id.vieAppPaterno);
        final View vieAppMaterno = (View) findViewById(R.id.vieAppMaterno);
        final View vieCorreo = (View) findViewById(R.id.vieCorreo);
        final View vieTelefono = (View) findViewById(R.id.vieTelefono);
        final View vieCalle = (View) findViewById(R.id.vieCalle);
        final View vieExterior = (View) findViewById(R.id.vieExterior);
        final View vieInterior = (View) findViewById(R.id.vieInterior);
        final View vieColonia = (View) findViewById(R.id.vieColonia);
        final View vieMunicipio = (View) findViewById(R.id.vieMunicipio);
        final View vieEstado = (View) findViewById(R.id.vieEstado);
        final View vieCodigoPostal = (View) findViewById(R.id.vieCodigoPostal);
        final View vieComentario = (View) findViewById(R.id.vieComentario);

        txtCalle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    lblCalle.setTextColor(ContextCompat.getColor(ctx, R.color.letraVerde));
                    vieCalle.setBackgroundColor(ContextCompat.getColor(ctx, R.color.letraVerde));
                } else {
                    lblCalle.setTextColor(ContextCompat.getColor(ctx, R.color.letraCobrar));
                    vieCalle.setBackgroundColor(ContextCompat.getColor(ctx, R.color.letraCobrar));
                }
            }
        });

        txtCodigoPostal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    lblCodigoPostal.setTextColor(ContextCompat.getColor(ctx, R.color.letraVerde));
                    vieCodigoPostal.setBackgroundColor(ContextCompat.getColor(ctx, R.color.letraVerde));
                } else {
                    lblCodigoPostal.setTextColor(ContextCompat.getColor(ctx, R.color.letraCobrar));
                    vieCodigoPostal.setBackgroundColor(ContextCompat.getColor(ctx, R.color.letraCobrar));
                }
            }
        });

        txtColonia.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    lblColonia.setTextColor(ContextCompat.getColor(ctx, R.color.letraVerde));
                    vieColonia.setBackgroundColor(ContextCompat.getColor(ctx, R.color.letraVerde));
                } else {
                    lblColonia.setTextColor(ContextCompat.getColor(ctx, R.color.letraCobrar));
                    vieColonia.setBackgroundColor(ContextCompat.getColor(ctx, R.color.letraCobrar));
                }
            }
        });

        txtComentario.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    lblComentario.setTextColor(ContextCompat.getColor(ctx, R.color.letraVerde));
                    vieComentario.setBackgroundColor(ContextCompat.getColor(ctx, R.color.letraVerde));
                } else {
                    lblComentario.setTextColor(ContextCompat.getColor(ctx, R.color.letraCobrar));
                    vieComentario.setBackgroundColor(ContextCompat.getColor(ctx, R.color.letraCobrar));
                }
            }
        });

        txtCorreo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    lblCorreo.setTextColor(ContextCompat.getColor(ctx, R.color.letraVerde));
                    vieCorreo.setBackgroundColor(ContextCompat.getColor(ctx, R.color.letraVerde));
                } else {
                    lblCorreo.setTextColor(ContextCompat.getColor(ctx, R.color.letraCobrar));
                    vieCorreo.setBackgroundColor(ContextCompat.getColor(ctx, R.color.letraCobrar));
                }
            }
        });


        txtEstado.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    lblEstado.setTextColor(ContextCompat.getColor(ctx, R.color.letraVerde));
                    vieEstado.setBackgroundColor(ContextCompat.getColor(ctx, R.color.letraVerde));
                } else {
                    lblEstado.setTextColor(ContextCompat.getColor(ctx, R.color.letraCobrar));
                    vieEstado.setBackgroundColor(ContextCompat.getColor(ctx, R.color.letraCobrar));
                }
            }
        });


        txtMaterno.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    lblAppMaterno.setTextColor(ContextCompat.getColor(ctx, R.color.letraVerde));
                    vieAppMaterno.setBackgroundColor(ContextCompat.getColor(ctx, R.color.letraVerde));
                } else {
                    lblAppMaterno.setTextColor(ContextCompat.getColor(ctx, R.color.letraCobrar));
                    vieAppMaterno.setBackgroundColor(ContextCompat.getColor(ctx, R.color.letraCobrar));
                }
            }
        });

        txtMunicipio.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    lblMunicipio.setTextColor(ContextCompat.getColor(ctx, R.color.letraVerde));
                    vieMunicipio.setBackgroundColor(ContextCompat.getColor(ctx, R.color.letraVerde));
                } else {
                    lblMunicipio.setTextColor(ContextCompat.getColor(ctx, R.color.letraCobrar));
                    vieMunicipio.setBackgroundColor(ContextCompat.getColor(ctx, R.color.letraCobrar));
                }
            }
        });

        txtNombre.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    lblNombre.setTextColor(ContextCompat.getColor(ctx, R.color.letraVerde));
                    vieNombre.setBackgroundColor(ContextCompat.getColor(ctx, R.color.letraVerde));
                } else {
                    lblNombre.setTextColor(ContextCompat.getColor(ctx, R.color.letraCobrar));
                    vieNombre.setBackgroundColor(ContextCompat.getColor(ctx, R.color.letraCobrar));
                }
            }
        });


        txtNumeroExt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    lblExterior.setTextColor(ContextCompat.getColor(ctx, R.color.letraVerde));
                    vieExterior.setBackgroundColor(ContextCompat.getColor(ctx, R.color.letraVerde));
                } else {
                    lblExterior.setTextColor(ContextCompat.getColor(ctx, R.color.letraCobrar));
                    vieExterior.setBackgroundColor(ContextCompat.getColor(ctx, R.color.letraCobrar));
                }
            }
        });


        txtNumeroInt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    lblInterior.setTextColor(ContextCompat.getColor(ctx, R.color.letraVerde));
                    vieInterior.setBackgroundColor(ContextCompat.getColor(ctx, R.color.letraVerde));
                } else {
                    lblInterior.setTextColor(ContextCompat.getColor(ctx, R.color.letraCobrar));
                    vieInterior.setBackgroundColor(ContextCompat.getColor(ctx, R.color.letraCobrar));
                }
            }
        });


        txtPaterno.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    lblAppPaterno.setTextColor(ContextCompat.getColor(ctx, R.color.letraVerde));
                    vieAppPaterno.setBackgroundColor(ContextCompat.getColor(ctx, R.color.letraVerde));
                } else {
                    lblAppPaterno.setTextColor(ContextCompat.getColor(ctx, R.color.letraCobrar));
                    vieAppPaterno.setBackgroundColor(ContextCompat.getColor(ctx, R.color.letraCobrar));
                }
            }
        });


        txtTelefono.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    lblTelefono.setTextColor(ContextCompat.getColor(ctx, R.color.letraVerde));
                    vieTelefono.setBackgroundColor(ContextCompat.getColor(ctx, R.color.letraVerde));
                } else {
                    lblTelefono.setTextColor(ContextCompat.getColor(ctx, R.color.letraCobrar));
                    vieTelefono.setBackgroundColor(ContextCompat.getColor(ctx, R.color.letraCobrar));
                }
            }
        });


    }


    public void btnMostrarMenuClick(View view) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.openDrawer(GravityCompat.START);
    }


    private void mostrarListaClientes() {
        Utilerias ut = new Utilerias();
        setContentView(R.layout.consultaclientes);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        pantalla = "consulta";
        ClientesDB pdb = new ClientesDB();

        clientes = pdb.obtenerClientesCompletos(Integer.parseInt(ut.obtenerValor("idTienda", this)), realm);

        TextView txtTotalRegs = (TextView) findViewById(R.id.txtTotalRegs);
        txtTotalRegs.setText("" + clientes.size());
        ListView gvClientes = (ListView) findViewById(R.id.gvClientes);
        ClientesAdapter adapter = new ClientesAdapter(clientes, this, "C");
        gvClientes.setAdapter(adapter);
        gvClientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                if (permisos.getClt_U()) {


                    pSel = clientes.get(position);
                    mostrarCliente(pSel);
                    TextView txtTitulo = (TextView) findViewById(R.id.txtTitulo);
                    txtTitulo.setText("EDITAR CLIENTE");
                    pantalla = "cliente";
                    accion = "E";
                } else {
                    mandarMensaje("No tiene permitido editar Clientes");
                }
            }
        });

        final EditText txtBuscarCli = (EditText) findViewById(R.id.txtBuscarCli);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                buscarCliente();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };


        txtBuscarCli.addTextChangedListener(textWatcher);
        txtBuscarCli.setFocusableInTouchMode(true);
      /* txtBuscarCli.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(txtBuscarCli, InputMethodManager.SHOW_IMPLICIT);*/

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_ventas) {
            Utilerias ut = new Utilerias();
            ut.validaIrPuntoVenta(this, this, realm, ut.obtenerPermisosUsuario(this), "normal");
        } else if (id == R.id.nav_productos) {
            cerrarRealmN(realm);
            Intent i = new Intent(getApplicationContext(), ProductosActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_clientes) {
            cerrarRealmN(realm);
            Utilerias ut = new Utilerias();
            if (ut.esPantallaChica(this)) {
                Intent i = new Intent(getApplicationContext(), ClientesActivity.class);
                startActivity(i);
            } else {
                Intent i = new Intent(getApplicationContext(), ClientesGrandeActivity.class);
                startActivity(i);
            }
        } else if (id == R.id.nav_reportes) {
            cerrarRealmN(realm);
            Utilerias ut = new Utilerias();
            ut.guardarValor("mostrarPedidos", "NO", this);
            if (ut.esPantallaChica(this)) {
                Intent i = new Intent(getApplicationContext(), ReportesActivity.class);
                startActivity(i);
            } else {
                Intent i = new Intent(getApplicationContext(), ReportesGrandeActivity.class);
                startActivity(i);
            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void btnLogOutClick(View view) {
        Utilerias ut = new Utilerias();
        ut.guardarValor("idUsuario", "", this);
        cerrarRealmN(realm);
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }


    public void btnEmparejarClick(View view) {
        Utilerias ut = new Utilerias();
        if (ut.isBluetoothEnabled()) {
            Set<BluetoothDevice> dispositivos = ut.obtenerDispositivos();
            if (dispositivos != null && dispositivos.size() > 0) {
                cerrarRealmN(realm);
                Intent i = new Intent(getApplicationContext(), EmparejarActivity.class);
                startActivity(i);
            } else {
                mandarMensaje("No hay ninguna impresora vinculada para configurar," +
                        " debe vincular primero la impresora con el dispositivo ");
            }
        } else {
            mandarMensaje("El Bluetooth esta apagado, por favor verifique");
        }
    }


    public void btnConfigurarTicketClick(View view) {
        cerrarRealmN(realm);
        Intent i = new Intent(getApplicationContext(), ConfigurarTicketActivity.class);
        startActivity(i);
    }

    public void btnConfigurarClick(View view) {
        cerrarRealmN(realm);
        Intent i = new Intent(getApplicationContext(), ConfiguracionActivity.class);
        startActivity(i);
    }

    public void btnAyudaClick(View view) {
        cerrarRealmN(realm);
        Intent i = new Intent(getApplicationContext(), AyudaActivity.class);
        startActivity(i);
    }

    public void btnBuscarClick(View view) {
        buscarCliente();
    }

    public void btnCancelarXClick(View view) {
        EditText txtBuscarCli = (EditText) findViewById(R.id.txtBuscarCli);
        txtBuscarCli.setText("");
        buscarCliente();
    }

    public void btnNuevoClick(View view) {
        if (permisos.getClt_C()) {

            Utilerias ut = new Utilerias();
            if (ut.esPantallaChica(this)) {
                setContentView(R.layout.cliente);
            } else {
                setContentView(R.layout.clientegrande);
            }
            cargarOnfocus();
            ImageButton btnMaps = (ImageButton) findViewById(R.id.btnMaps);
            ImageButton btnWaze = (ImageButton) findViewById(R.id.btnWaze);
            btnWaze.setVisibility(View.GONE);
            btnMaps.setVisibility(View.GONE);
            TextView txtTitulo = (TextView) findViewById(R.id.txtTitulo);
            txtTitulo.setText("NUEVO CLIENTE");
            accion = "N";
            pantalla = "cliente";
        } else {
            mandarMensaje("No tienes permisos para crear clientes");
        }
    }


    public void btnListoClick(View view) {

        EditText txtNombre = (EditText) findViewById(R.id.txtNombre);
        EditText txtPaterno = (EditText) findViewById(R.id.txtPaterno);
        EditText txtMaterno = (EditText) findViewById(R.id.txtMaterno);
        EditText txtCorreo = (EditText) findViewById(R.id.txtCorreo);
        EditText txtTelefono = (EditText) findViewById(R.id.txtTelefono);

        EditText txtCalle = (EditText) findViewById(R.id.txtCalle);
        EditText txtNumeroExt = (EditText) findViewById(R.id.txtNumeroExt);
        EditText txtNumeroInt = (EditText) findViewById(R.id.txtNumeroInt);
        EditText txtColonia = (EditText) findViewById(R.id.txtColonia);
        EditText txtMunicipio = (EditText) findViewById(R.id.txtMunicipio);
        EditText txtEstado = (EditText) findViewById(R.id.txtEstado);
        EditText txtCodigoPostal = (EditText) findViewById(R.id.txtCodigoPostal);
        EditText txtComentario = (EditText) findViewById(R.id.txtComentario);
        EditText txtLatitud = (EditText) findViewById(R.id.txtLatitud);
        EditText txtLongitud = (EditText) findViewById(R.id.txtLongitud);


        if (txtNombre.getText().toString().trim().equals("")


        ) {
            mandarMensaje("El nombre del cliente es obligatorio");
        } else {

            Utilerias ut = new Utilerias();
            if (ut.obtenerModoAplicacion(this)) {
                ClienteXYDTO p = new ClienteXYDTO();
                p.setApellidoP(txtPaterno.getText().toString());
                p.setApellidoM(txtMaterno.getText().toString());
                p.setNombre(txtNombre.getText().toString());

                RealmList<TelefonoXYDTO> telefono = new RealmList<TelefonoXYDTO>();
                TelefonoXYDTO t = new TelefonoXYDTO();
                t.setTipo("celular");
                t.setNumero(txtTelefono.getText().toString());
                telefono.add(t);
                p.setTelefonosxy(telefono);


                RealmList<CorreosXYDTO> correos = new RealmList<CorreosXYDTO>();
                CorreosXYDTO c = new CorreosXYDTO();
                c.setCorreo(txtCorreo.getText().toString());

                correos.add(c);
                p.setCorreosxy(correos);

                RealmList<DireccionesXYDTO> direcciones = new RealmList<DireccionesXYDTO>();
                DireccionesXYDTO d = new DireccionesXYDTO();
                d.setCalle(txtCalle.getText().toString());
                d.setNumeroExt(txtNumeroExt.getText().toString());
                d.setNumeroInt(txtNumeroInt.getText().toString());
                d.setColonia(txtColonia.getText().toString());
                d.setMunicipio(txtMunicipio.getText().toString());
                d.setEstado(txtEstado.getText().toString());
                d.setCp(txtCodigoPostal.getText().toString());
                String lati = txtLatitud.getText().toString();
                String longi = txtLongitud.getText().toString();
                if (!lati.equals("") && !longi.equals("")) {
                    d.setComentario(txtComentario.getText().toString() + "[#" + lati + "," + longi + "#]");
                } else {
                    d.setComentario(txtComentario.getText().toString());
                }
                direcciones.add(d);
                p.setDireccionesxy(direcciones);

                if (accion.equals("E")) {
                    p.setId(pSel.getId());
                } else {
                    p.setId(0);
                }

                subirCliente(p);
            } else {
                if (accion.equals("E")) {
                    //es edicion, tenemos que saber si es edicion de la local o de la del server
                    if (pSel.getTipoBDL().equals("L")) {
                        ClienteXYDTOAux aux = new ClienteXYDTOAux(pSel, pSel.getTipoBDL());
                        aux.setApellidoM(txtMaterno.getText().toString());
                        aux.setApellidoP(txtPaterno.getText().toString());
                        aux.setCorreo(txtCorreo.getText().toString());
                        aux.setNombre(txtNombre.getText().toString());
                        aux.setTelefono(txtTelefono.getText().toString());
                        aux.setCalle(txtCalle.getText().toString());
                        aux.setNumeroExt(txtNumeroExt.getText().toString());
                        aux.setNumeroInt(txtNumeroInt.getText().toString());
                        aux.setColonia(txtColonia.getText().toString());
                        aux.setMunicipio(txtMunicipio.getText().toString());
                        aux.setEstado(txtEstado.getText().toString());
                        aux.setCp(txtCodigoPostal.getText().toString());
                        String lati = txtLatitud.getText().toString();
                        String longi = txtLongitud.getText().toString();
                        if (!lati.equals("") && !longi.equals("")) {
                            aux.setComentario(txtComentario.getText().toString() + "[#" + lati + "," + longi + "#]");
                        } else {
                            aux.setComentario(txtComentario.getText().toString());
                        }

                        // es local, actualizamos el local
                        ClientesDB pdb = new ClientesDB();

                        String error = pdb.actualizarClienteLocal(aux, Integer.parseInt(ut.obtenerValor("idTienda", this)), realm);

                        if (error.equals("")) {
                            mandarMensaje("Se actualizó correctamente el cliente");
                            mostrarListaClientes();
                        } else {
                            mandarMensaje(error);
                        }
                    } else {
                        //es del server, actualizamos en el server
                        ClienteXYDTOAux aux = new ClienteXYDTOAux(pSel, pSel.getTipoBDL());
                        aux.setApellidoM(txtMaterno.getText().toString());
                        aux.setApellidoP(txtPaterno.getText().toString());
                        aux.setCorreo(txtCorreo.getText().toString());
                        aux.setNombre(txtNombre.getText().toString());
                        aux.setTelefono(txtTelefono.getText().toString());
                        aux.setCalle(txtCalle.getText().toString());
                        aux.setNumeroExt(txtNumeroExt.getText().toString());
                        aux.setNumeroInt(txtNumeroInt.getText().toString());
                        aux.setColonia(txtColonia.getText().toString());
                        aux.setMunicipio(txtMunicipio.getText().toString());
                        aux.setEstado(txtEstado.getText().toString());
                        aux.setCp(txtCodigoPostal.getText().toString());
                        String lati = txtLatitud.getText().toString();
                        String longi = txtLongitud.getText().toString();
                        if (!lati.equals("") && !longi.equals("")) {
                            aux.setComentario(txtComentario.getText().toString() + "[#" + lati + "," + longi + "#]");
                        } else {
                            aux.setComentario(txtComentario.getText().toString());
                        }


                        ClientesDB pdb = new ClientesDB();

                        String error = pdb.actualizarClienteServer(aux, Integer.parseInt(ut.obtenerValor("idTienda", this)), realm);

                        if (error.equals("")) {
                            mandarMensaje("Se actualizó correctamente el cliente");
                            mostrarListaClientes();
                        } else {
                            mandarMensaje(error);
                        }
                    }
                } else {
                    // es nuevo, guardamos en la local
                    ClienteXYDTOLocal p = new ClienteXYDTOLocal();
                    p.setApellidoM(txtMaterno.getText().toString());
                    p.setApellidoP(txtPaterno.getText().toString());
                    p.setCorreo(txtCorreo.getText().toString());
                    p.setNombre(txtNombre.getText().toString());
                    p.setTelefono(txtTelefono.getText().toString());
                    p.setCalle(txtCalle.getText().toString());
                    p.setNumeroExt(txtNumeroExt.getText().toString());
                    p.setNumeroInt(txtNumeroInt.getText().toString());
                    p.setColonia(txtColonia.getText().toString());
                    p.setMunicipio(txtMunicipio.getText().toString());
                    p.setEstado(txtEstado.getText().toString());
                    p.setCp(txtCodigoPostal.getText().toString());
                    String lati = txtLatitud.getText().toString();
                    String longi = txtLongitud.getText().toString();
                    if (!lati.equals("") && !longi.equals("")) {
                        p.setComentario(txtComentario.getText().toString() + "[#" + lati + "," + longi + "#]");
                    } else {
                        p.setComentario(txtComentario.getText().toString());
                    }

                    ClientesDB pdb = new ClientesDB();

                    String error = pdb.guardarBDClienteLocal(p, Integer.parseInt(ut.obtenerValor("idTienda", this)), realm);


                    if (error.equals("")) {
                        mandarMensaje("Se guardó correctamente el cliente");
                        mostrarListaClientes();
                    } else {
                        mandarMensaje(error);
                    }
                }
            }


        }

    }

    public void btnGPS(View view) {
        Location location = getLastKnownLocation();
        if (location == null) {
            latitud = 0.00;
            longitud = 0.00;
        } else {
            latitud = location.getLatitude();
            longitud = location.getLongitude();
        }
        EditText txtLongitud = (EditText) findViewById(R.id.txtLongitud);
        EditText txtLatitud = (EditText) findViewById(R.id.txtLatitud);
        txtLongitud.setText("" + longitud);
        txtLatitud.setText("" + latitud);
    }


    LocationManager mLocationManager;


    private Location getLastKnownLocation() {
        mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;

        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return null;
            }
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }


    public void btnMaps(View view) {
        if (latitud != 0) {
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?daddr=" + latitud + "," + longitud + ""));
            startActivity(intent);
        } else {
            mandarMensaje("No se tiene la dirección del cliente");
        }
    }

    public void btnWaze(View view) {
        if (latitud != 0) {
            try {
                String uri = "waze://?ll=" + latitud + "," + longitud + "&z=10";
                startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri)));
            }
            catch (Exception ex)
            {
                mandarMensaje("No tienes instalado Waze");
            }
        } else {
            mandarMensaje("No se tiene la dirección del cliente");
        }
    }


    private void subirCliente(ClienteXYDTO p) {
        ClienteDTO pr = new ClienteDTO();
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);


        try {
            Utilerias ut = new Utilerias();
            pr.setIdAndroid(ut.obtenerSerial(this, this));
            pr.setIdUT(Long.parseLong(ut.obtenerValor("idUT", this)));

            List<ClienteXYDTO> clientes = new ArrayList<ClienteXYDTO>();
            clientes.add(p);
            pr.setClientesxy(clientes);
            Gson gson = new Gson();
            String json = gson.toJson(pr);
            Call<ClienteDTO> call = apiInterface.mandarConsultarClientes(pr);
            ClientesService ls = new ClientesService(call);
            ClienteDTO res = ls.execute().get();
            if (res == null) {
                mandarMensaje("Algo salió mal guardando, por favor intente de nuevo");
            } else {
                if (res.getClientesxy() == null) {
                    mandarMensaje(res.getMsg());
                } else {
                    if (res.getClientesxy().size() > 0) {
                        ClientesDB pdb = new ClientesDB();

                        pdb.actualizarBDClientes(res.getClientesxy(),
                                Integer.parseInt(ut.obtenerValor("idTienda", this)), realm);

                    }
                    //después de guardar en la BD volvemos a mostrar  la pantalla de clientes
                    // con el agregado
                    mandarMensaje("Se guardó correctamente el cliente");
                    mostrarListaClientes();

                }
            }
        } catch (Exception ex) {
            mandarMensaje(ex.getMessage());
        }
    }

    private void mostrarCliente(ClienteXYDTOAux p) {
        Utilerias ut = new Utilerias();
        if (ut.esPantallaChica(this)) {
            setContentView(R.layout.cliente);
        } else {
            setContentView(R.layout.clientegrande);
        }
        cargarOnfocus();

        EditText txtNombre = (EditText) findViewById(R.id.txtNombre);
        EditText txtPaterno = (EditText) findViewById(R.id.txtPaterno);

        EditText txtMaterno = (EditText) findViewById(R.id.txtMaterno);
        EditText txtCorreo = (EditText) findViewById(R.id.txtCorreo);
        EditText txtTelefono = (EditText) findViewById(R.id.txtTelefono);

        EditText txtCalle = (EditText) findViewById(R.id.txtCalle);
        EditText txtNumeroExt = (EditText) findViewById(R.id.txtNumeroExt);
        EditText txtNumeroInt = (EditText) findViewById(R.id.txtNumeroInt);
        EditText txtColonia = (EditText) findViewById(R.id.txtColonia);
        EditText txtMunicipio = (EditText) findViewById(R.id.txtMunicipio);
        EditText txtEstado = (EditText) findViewById(R.id.txtEstado);
        EditText txtCodigoPostal = (EditText) findViewById(R.id.txtCodigoPostal);
        EditText txtComentario = (EditText) findViewById(R.id.txtComentario);
        EditText txtLatitud = (EditText) findViewById(R.id.txtLatitud);
        EditText txtLongitud = (EditText) findViewById(R.id.txtLongitud);
        ImageButton btnListo = (ImageButton) findViewById(R.id.btnListo);
        ImageButton btnMaps = (ImageButton) findViewById(R.id.btnMaps);
        ImageButton btnWaze = (ImageButton) findViewById(R.id.btnWaze);


        if (p.getNombre() == null) {
            txtNombre.setText("");
        } else {
            txtNombre.setText(p.getNombre());
        }

        if (p.getApellidoM() == null) {
            txtMaterno.setText("");
        } else {
            txtMaterno.setText("" + p.getApellidoM());
        }
        if (p.getApellidoP() == null) {
            txtPaterno.setText("");
        } else {
            txtPaterno.setText(p.getApellidoP());
        }
        txtCalle.setText(p.getCalle());
        txtNumeroExt.setText(p.getNumeroExt());
        txtNumeroInt.setText(p.getNumeroInt());
        txtColonia.setText(p.getColonia());
        txtMunicipio.setText(p.getMunicipio());
        txtEstado.setText(p.getEstado());

        txtCodigoPostal.setText(p.getCp());

        if (p.getComentario().contains("[#")) {
            btnMaps.setVisibility(View.VISIBLE);
            btnWaze.setVisibility(View.VISIBLE);
            int inicia = p.getComentario().indexOf("[");
            int termina = p.getComentario().indexOf("]");
            String ubi = p.getComentario().substring(inicia + 2, termina - 1);
            String comentario = p.getComentario().substring(0, inicia);
            txtComentario.setText(comentario);
            String[] datos = ubi.split(",");
            if (datos.length == 2) {
                latitud = Double.parseDouble(datos[0]);
                longitud = Double.parseDouble(datos[1]);
                txtLatitud.setText(datos[0]);
                txtLongitud.setText(datos[1]);
            }
        } else {
            txtComentario.setText(p.getComentario());
            btnMaps.setVisibility(View.GONE);
            btnWaze.setVisibility(View.GONE);
        }


        String correo = "" + p.getCorreo();
        if (correo == null) {
            correo = "";
        }
        txtCorreo.setText(correo);


        String tel = "" + p.getTelefono();
        if (tel == null) {
            tel = "";
        }
        txtTelefono.setText(tel);


    }


    private void manejarEnterBuscar() {
        EditText txtBuscarCli = (EditText) findViewById(R.id.txtBuscarCli);
        txtBuscarCli.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    buscarCliente();
                    handled = true;
                }
                return handled;
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (pantalla != null && pantalla.equals("cliente")) {
            mostrarListaClientes();
        }
    }

    private void buscarCliente() {
        //aqui dentro lo que hayan puesto lo buscamos
        Utilerias ut = new Utilerias();
        EditText txtBuscarCli = (EditText) findViewById(R.id.txtBuscarCli);
        ClientesDB pdb = new ClientesDB();

        clientes =
                pdb.obtenerClientesCompletosPatron(txtBuscarCli.getText().toString(),
                        Integer.parseInt(ut.obtenerValor("idTienda", this)), realm);

        TextView txtTotalRegs = (TextView) findViewById(R.id.txtTotalRegs);
        txtTotalRegs.setText("" + clientes.size());
        ListView gvClientes = (ListView) findViewById(R.id.gvClientes);
        ClientesAdapter adapter = new ClientesAdapter(clientes, this, "C");
        gvClientes.setAdapter(adapter);
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


}
