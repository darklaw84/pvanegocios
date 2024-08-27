package com.anegocios.puntoventa;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;

import com.google.android.material.navigation.NavigationView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.anegocios.puntoventa.adapters.ProductosAdapter;
import com.anegocios.puntoventa.bdlocal.ProductosXYDTOLocal;
import com.anegocios.puntoventa.database.GrupoDB;
import com.anegocios.puntoventa.database.ProductosDB;
import com.anegocios.puntoventa.database.UsuariosDB;
import com.anegocios.puntoventa.dtosauxiliares.ProductosXYDTOAux;
import com.anegocios.puntoventa.jsons.GrupoVRXY;
import com.anegocios.puntoventa.jsons.ProductoDTO;
import com.anegocios.puntoventa.jsons.ProductosXYDTO;
import com.anegocios.puntoventa.jsons.Usuario;
import com.anegocios.puntoventa.servicios.APIClient;
import com.anegocios.puntoventa.servicios.APIInterface;
import com.anegocios.puntoventa.utils.ActualizacionCatalogosUtil;
import com.anegocios.puntoventa.utils.Utilerias;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import io.realm.Realm;
import retrofit2.Call;

public class ProductosActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    List<ProductosXYDTOAux> productos;
    List<GrupoVRXY> grupos;
    Uri imageUri;
    Bitmap bitmapImage;
    String pantalla;
    String accion;
    ProductosXYDTOAux prodSel;

    Usuario permisos;
    ProgressBar progress_bar;
    Realm realm;
    Context context;
    Activity activity;

    List<ProductosXYDTO> paraSubir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            context = this;
            activity = this;
            Utilerias ut = new Utilerias();
            permisos = ut.obtenerPermisosUsuario(this);
            realm = ut.obtenerInstanciaBD(this);


            if (realm != null && !realm.isClosed()) {
                realm.refresh();
            } else {
                realm = ut.obtenerInstanciaBD(this);
                if (realm != null && !realm.isClosed()) {
                    realm.refresh();
                }
            }

            mostrarListaProductos();
            GrupoDB gdb = new GrupoDB();

            grupos = gdb.obtenerListaGrupos(Integer.parseInt(ut.obtenerValor("idTienda", this)), realm);

            bitmapImage = null;
        } catch (Exception ex) {
            Utilerias.log(this, "Error: " + ex.getMessage() + " " + ex.getStackTrace(), ex);
        }

    }

    public void btnActualizarClick(View view) {

        AlertDialog.Builder alert = new AlertDialog.Builder(ProductosActivity.this);
        alert.setTitle("Actualizar Catálogo Productos ");
        alert.setMessage("Esta acción actualiza todos los productos de la tienda, puede tomar varios minutos, desea continuar?");
        alert.setPositiveButton("SI", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                siActualizar();
                dialog.dismiss();
            }
        });

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert.show();

    }

    private void siActualizar() {
        Utilerias ut = new Utilerias();
        if (ut.verificaConexion(this)) {
            ProductoDTO pr = new ProductoDTO();
            APIInterface apiInterface = APIClient.getClient(this).create(APIInterface.class);


            try {
                ActualizacionCatalogosUtil acum = new ActualizacionCatalogosUtil();
                acum.consultarGrupos(this, this);
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

    public void btnMostrarMenuClick(View view) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.openDrawer(GravityCompat.START);
    }


    public void mostrarListaProductos() {
        try {
            setContentView(R.layout.consultaproductos);
            NavigationView navigationView = findViewById(R.id.nav_view);

            navigationView.setNavigationItemSelectedListener(this);
            pantalla = "consulta";
            ProductosDB pdb = new ProductosDB();
            Utilerias ut = new Utilerias();
            if (realm != null) {
                realm.refresh();
            }
            productos = pdb.obtenerProductosCompletos(
                    Integer.parseInt(ut.obtenerValor("idTienda", this)), realm);

            TextView txtTotalRegs = (TextView) findViewById(R.id.txtTotalRegs);
            txtTotalRegs.setText("" + productos.size());
            ListView gvProductos = (ListView) findViewById(R.id.gvProductos);
            ProductosAdapter adapter = new ProductosAdapter(productos, this);
            gvProductos.setAdapter(adapter);
            gvProductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    if (permisos.getProd_U()) {


                        prodSel = productos.get(position);
                        bitmapImage = null;
                        mostrarProducto(prodSel);
                        TextView txtTitulo = (TextView) findViewById(R.id.txtTitulo);
                        txtTitulo.setText("EDITAR PRODUCTO");
                        pantalla = "producto";
                        accion = "E";

                    } else {
                        mandarMensaje("No tiene permitido editar Productos");
                    }
                }
            });
            final EditText txtBuscarProd = (EditText) findViewById(R.id.txtBuscarProd);

            TextWatcher textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    buscarProducto();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            };


            txtBuscarProd.addTextChangedListener(textWatcher);
            txtBuscarProd.setFocusableInTouchMode(true);
            //estaban comentadas pero las descomentamos para que s epueda hacer
            // lo del lector de codigos de barra
            txtBuscarProd.requestFocus();
          /*  InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(txtBuscarProd, InputMethodManager.SHOW_IMPLICIT);*/
        } catch (Exception ex) {
            Utilerias.log(this, "Error: " + ex.getMessage() + " " + ex.getStackTrace(), ex);
        }
    }

    public void btnCodCamaraClick(View view) {

        new IntentIntegrator(ProductosActivity.this).initiateScan();
    }

    public void btnBuscarClick(View view) {
        buscarProducto();
    }


    public void btnNuevoClick(View view) {

        try {
            if (permisos.getProd_C()) {

                setContentView(R.layout.product);
                cargarOnfocus();
                TextView txtTitulo = (TextView) findViewById(R.id.txtTitulo);
                TextView txtCodigoBarras = (TextView) findViewById(R.id.txtCodigoBarras);

                txtTitulo.setText("NUEVO PRODUCTO");
                EditText txtIva = (EditText) findViewById(R.id.txtIva);
                txtIva.setEnabled(false);
                pantalla = "producto";
                accion = "N";
                Spinner spGrupos = (Spinner) findViewById(R.id.spGrupos);
                List<String> gruposS = new ArrayList<String>();
                GrupoDB gdb = new GrupoDB();
                Utilerias ut = new Utilerias();

                grupos = gdb.obtenerListaGrupos(Integer.parseInt(ut.obtenerValor("idTienda", this)), realm);


                gruposS.add("Seleccione");
                for (GrupoVRXY gr : grupos
                ) {
                    gruposS.add(gr.getNombre());
                }
                ArrayAdapter aa = new ArrayAdapter(this, R.layout.combo, gruposS);
                aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


                spGrupos.setAdapter(aa);
                manejarClickIva();
                txtCodigoBarras.requestFocus();
                showSoftKeyboard(txtCodigoBarras);
            } else {
                mandarMensaje("No tienes permiso para crear productos");
            }
        } catch (Exception ex) {
            Utilerias.log(this, "Error: " + ex.getMessage() + " " + ex.getStackTrace(), ex);
        }
    }

    public void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            boolean isShowing = imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            if (!isShowing)
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }
    }

    private void cargarOnfocus() {
        final EditText txtCodigoBarras = (EditText) findViewById(R.id.txtCodigoBarras);
        final  EditText txtNombreProd = (EditText) findViewById(R.id.txtNombreProd);
        final  EditText txtPrecioCompra = (EditText) findViewById(R.id.txtPrecioCompra);
        final  EditText txtPrecioVenta = (EditText) findViewById(R.id.txtPrecioVenta);
        final  EditText txtPrecioMayoreo = (EditText) findViewById(R.id.txtPrecioMayoreo);
        final  EditText txtCantMayoreo = (EditText) findViewById(R.id.txtCantMayoreo);
        final  EditText txtComision = (EditText) findViewById(R.id.txtComision);
        final  EditText txtExistencias = (EditText) findViewById(R.id.txtExistencias);
        final  EditText txtIva = (EditText) findViewById(R.id.txtIva);
        final TextView lblCantidadMayoreo = (TextView) findViewById(R.id.lblCantidadMayoreo);
        final TextView lblCodBarra = (TextView) findViewById(R.id.lblCodBarra);
        final TextView lblComision = (TextView) findViewById(R.id.lblComision);
        final TextView lblExistencias = (TextView) findViewById(R.id.lblExistencias);
        final TextView lblIva = (TextView) findViewById(R.id.lblIva);
        final TextView lblNombre = (TextView) findViewById(R.id.lblNombre);
        final TextView lblPrecioCompra = (TextView) findViewById(R.id.lblPrecioCompra);
        final TextView lblPrecioMayoreo = (TextView) findViewById(R.id.lblPrecioMayoreo);
        final TextView lblPrecioVenta = (TextView) findViewById(R.id.lblPrecioVenta);
        final View vieCantidadMayoreo = (View) findViewById(R.id.vieCantidadMayoreo);
        final View vieCodigoBarras = (View) findViewById(R.id.vieCodigoBarras);
        final View vieComision = (View) findViewById(R.id.vieComision);
        final View vieExistencias = (View) findViewById(R.id.vieExistencias);
        final View vieIva = (View) findViewById(R.id.vieIva);
        final View vieNombre = (View) findViewById(R.id.vieNombre);
        final View viePrecioCompra = (View) findViewById(R.id.viePrecioCompra);
        final View viePrecioMayoreo = (View) findViewById(R.id.viePrecioMayoreo);
        final View viePrecioVenta = (View) findViewById(R.id.viePrecioVenta);

        txtIva.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    lblIva.setTextColor(ContextCompat.getColor(context, R.color.letraVerde));
                    vieIva.setBackgroundColor(ContextCompat.getColor(context, R.color.letraVerde));
                } else {
                    lblIva.setTextColor(ContextCompat.getColor(context, R.color.letraCobrar));
                    vieIva.setBackgroundColor(ContextCompat.getColor(context, R.color.letraCobrar));
                }
            }
        });

        txtCodigoBarras.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    lblCodBarra.setTextColor(ContextCompat.getColor(context, R.color.letraVerde));
                    vieCodigoBarras.setBackgroundColor(ContextCompat.getColor(context, R.color.letraVerde));
                    showSoftKeyboard(txtCodigoBarras);
                } else {
                    lblCodBarra.setTextColor(ContextCompat.getColor(context, R.color.letraCobrar));
                    vieCodigoBarras.setBackgroundColor(ContextCompat.getColor(context, R.color.letraCobrar));
                }
            }
        });

        txtNombreProd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    lblNombre.setTextColor(ContextCompat.getColor(context, R.color.letraVerde));
                    vieNombre.setBackgroundColor(ContextCompat.getColor(context, R.color.letraVerde));
                    showSoftKeyboard(txtNombreProd);
                } else {
                    lblNombre.setTextColor(ContextCompat.getColor(context, R.color.letraCobrar));
                    vieNombre.setBackgroundColor(ContextCompat.getColor(context, R.color.letraCobrar));
                }
            }
        });

        txtPrecioCompra.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    lblPrecioCompra.setTextColor(ContextCompat.getColor(context, R.color.letraVerde));
                    viePrecioCompra.setBackgroundColor(ContextCompat.getColor(context, R.color.letraVerde));
                    showSoftKeyboard(txtPrecioCompra);
                } else {
                    lblPrecioCompra.setTextColor(ContextCompat.getColor(context, R.color.letraCobrar));
                    viePrecioCompra.setBackgroundColor(ContextCompat.getColor(context, R.color.letraCobrar));
                }
            }
        });

        txtPrecioVenta.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    lblPrecioVenta.setTextColor(ContextCompat.getColor(context, R.color.letraVerde));
                    viePrecioVenta.setBackgroundColor(ContextCompat.getColor(context, R.color.letraVerde));
                    showSoftKeyboard(txtPrecioVenta);
                } else {
                    lblPrecioVenta.setTextColor(ContextCompat.getColor(context, R.color.letraCobrar));
                    viePrecioVenta.setBackgroundColor(ContextCompat.getColor(context, R.color.letraCobrar));
                }
            }
        });


        txtPrecioMayoreo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    lblPrecioMayoreo.setTextColor(ContextCompat.getColor(context, R.color.letraVerde));
                    viePrecioMayoreo.setBackgroundColor(ContextCompat.getColor(context, R.color.letraVerde));
                    showSoftKeyboard(txtPrecioMayoreo);
                } else {
                    lblPrecioMayoreo.setTextColor(ContextCompat.getColor(context, R.color.letraCobrar));
                    viePrecioMayoreo.setBackgroundColor(ContextCompat.getColor(context, R.color.letraCobrar));
                }
            }
        });


        txtComision.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    lblComision.setTextColor(ContextCompat.getColor(context, R.color.letraVerde));
                    vieComision.setBackgroundColor(ContextCompat.getColor(context, R.color.letraVerde));
                    showSoftKeyboard(txtComision);
                } else {
                    lblComision.setTextColor(ContextCompat.getColor(context, R.color.letraCobrar));
                    vieComision.setBackgroundColor(ContextCompat.getColor(context, R.color.letraCobrar));
                }
            }
        });

        txtExistencias.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    lblExistencias.setTextColor(ContextCompat.getColor(context, R.color.letraVerde));
                    vieExistencias.setBackgroundColor(ContextCompat.getColor(context, R.color.letraVerde));
                    showSoftKeyboard(txtExistencias);
                } else {
                    lblExistencias.setTextColor(ContextCompat.getColor(context, R.color.letraCobrar));
                    vieExistencias.setBackgroundColor(ContextCompat.getColor(context, R.color.letraCobrar));
                }
            }
        });

        txtCantMayoreo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    lblCantidadMayoreo.setTextColor(ContextCompat.getColor(context, R.color.letraVerde));
                    vieCantidadMayoreo.setBackgroundColor(ContextCompat.getColor(context, R.color.letraVerde));
                    showSoftKeyboard(txtCantMayoreo);
                } else {
                    lblCantidadMayoreo.setTextColor(ContextCompat.getColor(context, R.color.letraCobrar));
                    vieCantidadMayoreo.setBackgroundColor(ContextCompat.getColor(context, R.color.letraCobrar));
                }
            }
        });


    }


    public void btnGaleriaClick(View view) {


        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        photoPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(photoPickerIntent, 100);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 100 && resultCode == RESULT_OK) {
                Uri uri = data.getData();
                ImageView imageProd = (ImageView) findViewById(R.id.imageProd);
                try {
                    bitmapImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                } catch (Exception ex) {

                }
                imageProd.setImageURI(uri);
                imageUri = uri;
            } else if (requestCode == 200 && resultCode == RESULT_OK) {

                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        File photo = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
                        break;

                    }
                }
                Bitmap bitmap;
                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                bitmapImage = BitmapFactory.decodeFile(f.getAbsolutePath(),
                        bitmapOptions);
                ImageView imageProd = (ImageView) findViewById(R.id.imageProd);
                imageProd.setImageBitmap(bitmapImage);

            } else {
                final IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                handleResult(scanResult);
            }
        } catch (Exception ex) {
            Utilerias.log(this, "Error: " + ex.getMessage() + " " + ex.getStackTrace(), ex);
        }
    }

    private void handleResult(IntentResult scanResult) {
        if (scanResult != null) {
            updateUITextViews(scanResult.getContents(), scanResult.getFormatName());
        } else {
            Toast.makeText(this, "No se ha leído nada :(", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUITextViews(String scan_result, String scan_result_format) {

        EditText txtCodigoBarras = (EditText) findViewById(R.id.txtCodigoBarras);
        txtCodigoBarras.setText(scan_result);


    }

    private void cerrarRealmN(Realm realm) {
        if (realm != null && !realm.isClosed()) {
            realm.close();
        }
    }


    public void btnCamaraClick(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);

        } else {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

            } else {
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                Uri fileUri = getOutputMediaFileUri(); // create a file to save the image
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(cameraIntent, 200);
            }

        }
    }

    private Uri getOutputMediaFileUri() {
        return Uri.fromFile(getOutputMediaFile());
    }


    private File getOutputMediaFile() {
        File mediaFile;
        mediaFile = new File(Environment.getExternalStorageDirectory().toString() + File.separator +
                "temp.jpg");
        return mediaFile;
    }


    public void btnCancelarXClick(View view) {
        EditText txtBuscarProd = (EditText) findViewById(R.id.txtBuscarProd);
        txtBuscarProd.setText("");
        buscarProducto();
    }

    public void btnListoClick(View view) {

        try {
            EditText txtCodigoBarras = (EditText) findViewById(R.id.txtCodigoBarras);
            EditText txtNombreProd = (EditText) findViewById(R.id.txtNombreProd);
            EditText txtPrecioCompra = (EditText) findViewById(R.id.txtPrecioCompra);
            EditText txtPrecioVenta = (EditText) findViewById(R.id.txtPrecioVenta);
            EditText txtPrecioMayoreo = (EditText) findViewById(R.id.txtPrecioMayoreo);
            EditText txtCantMayoreo = (EditText) findViewById(R.id.txtCantMayoreo);
            EditText txtComision = (EditText) findViewById(R.id.txtComision);
            EditText txtExistencias = (EditText) findViewById(R.id.txtExistencias);
            EditText txtIva = (EditText) findViewById(R.id.txtIva);
            CheckBox chkIva = (CheckBox) findViewById(R.id.chkIva);


            if (txtNombreProd.getText().toString().trim().equals("")
                    || txtPrecioVenta.getText().toString().trim().equals("")
            ) {
                mandarMensaje("Los campos Nombre Producto y Precio de Venta son obligatorios");
            } else {
                boolean ivaCorrecto = false;
                if (chkIva.isChecked()) {
                    if (!txtIva.getText().toString().trim().equals("")) {
                        try {
                            Integer.parseInt(txtIva.getText().toString());
                            ivaCorrecto = true;
                        } catch (Exception e) {
                            mandarMensaje("El iva debe ser un valor númerico entero");
                        }
                    }
                } else {
                    ivaCorrecto = true;
                }
                if (!ivaCorrecto) {
                    mandarMensaje("Si seleccionas iva, debes de dar un valor válido");
                } else {
                    boolean preciosCorrectos = true;
                    Utilerias ut = new Utilerias();
                    double precioVenta = ut.parseDouble(txtPrecioVenta.getText().toString());
                    if (precioVenta == 0) {
                        preciosCorrectos = false;
                    }


                    double precioCompra = ut.parseDouble(txtPrecioCompra.getText().toString());
                    double precioMayoreo = ut.parseDouble(txtPrecioMayoreo.getText().toString());
                    double comision = ut.parseDouble(txtComision.getText().toString());
                    int cantidadMayoreo = ut.parseInt(txtCantMayoreo.getText().toString());
                    int iva = 0;
                    double existencias = ut.parseDouble(txtExistencias.getText().toString());


                    if (chkIva.isChecked()) {
                        iva = ut.parseInt(txtIva.getText().toString());
                    }


                    if (preciosCorrectos) {

                        if (ut.obtenerModoAplicacion(this)) {
                            //ya podemos mandar armar el dto
                            ProductosXYDTO p = new ProductosXYDTO();
                            if (accion.equals("E")) {
                                p.setId(prodSel.getId());
                            } else {
                                p.setId(0);
                            }
                            if (bitmapImage != null) {
                                //quiere decir que se creo una nueva imagen
                                p.setImgString(ut.BitMapToString(bitmapImage));
                            }
                            p.setCodigoBarras(txtCodigoBarras.getText().toString().trim());
                            p.setExistencias(existencias);
                            p.setPrecioVenta(precioVenta);
                            p.setPrecioCompra(precioCompra);
                            p.setPrecioMayoreo(precioMayoreo);
                            p.setCantidadMayoreo(cantidadMayoreo);
                            p.setComision(comision);
                            Spinner spGrupos = (Spinner) findViewById(R.id.spGrupos);
                            int posicion = spGrupos.getSelectedItemPosition();
                            if (posicion > 0) {
                                if (grupos != null && grupos.size() > 0) {


                                    GrupoVRXY gSel = grupos.get(posicion - 1);
                                    p.setIdVR(gSel.getId());
                                }
                            }

                            p.setIva(chkIva.isChecked());
                            p.setDescripcionCorta(txtNombreProd.getText().toString());
                            if (chkIva.isChecked()) {
                                p.setIvaCant(iva);
                            }
                            subirProducto(p);
                        } else {
                            //modo offline

                            if (accion.equals("E")) {
                                //es edicion, tenemos que saber si es edicion de la local o de la del server
                                if (prodSel.getTipoBDL().equals("L")) {
                                    ProductosXYDTOAux aux = new ProductosXYDTOAux(prodSel, prodSel.getTipoBDL());
                                    aux.setCodigoBarras(txtCodigoBarras.getText().toString());
                                    aux.setExistencias(existencias);
                                    aux.setPrecioVenta(precioVenta);
                                    aux.setPrecioCompra(precioCompra);
                                    aux.setPrecioMayoreo(precioMayoreo);
                                    aux.setCantidadMayoreo(cantidadMayoreo);
                                    aux.setComision(comision);
                                    if (bitmapImage != null) {
                                        //quiere decir que se creo una nueva imagen
                                        aux.setImgString(ut.BitMapToString(bitmapImage));
                                    }
                                    Spinner spGrupos = (Spinner) findViewById(R.id.spGrupos);
                                    int posicion = spGrupos.getSelectedItemPosition();
                                    if (posicion > 0) {
                                        if (grupos != null && grupos.size() > 0) {

                                            GrupoVRXY gSel = grupos.get(posicion - 1);
                                            aux.setIdVR(gSel.getId());
                                        }
                                    }
                                    aux.setIva(chkIva.isChecked());
                                    aux.setDescripcionCorta(txtNombreProd.getText().toString());
                                    if (chkIva.isChecked()) {
                                        aux.setIvaCant(iva);
                                    }
                                    // es local, actualizamos el local
                                    ProductosDB pdb = new ProductosDB();
                                    String error = pdb.actualizarProductoLocal(aux, realm);

                                    if (error.equals("")) {
                                        mandarMensaje("Se actualizó correctamente el producto");
                                        mostrarListaProductos();
                                    } else {
                                        mandarMensaje(error);
                                    }
                                } else {
                                    //es del server, actualizamos en el server
                                    ProductosXYDTOAux aux = new ProductosXYDTOAux(prodSel, prodSel.getTipoBDL());
                                    aux.setCodigoBarras(txtCodigoBarras.getText().toString());
                                    if (bitmapImage != null) {
                                        //quiere decir que se creo una nueva imagen
                                        aux.setImgString(ut.BitMapToString(bitmapImage));
                                    }
                                    aux.setExistencias(existencias);
                                    aux.setPrecioVenta(precioVenta);
                                    aux.setPrecioCompra(precioCompra);
                                    aux.setPrecioMayoreo(precioMayoreo);
                                    aux.setCantidadMayoreo(cantidadMayoreo);
                                    aux.setComision(comision);
                                    Spinner spGrupos = (Spinner) findViewById(R.id.spGrupos);
                                    int posicion = spGrupos.getSelectedItemPosition();
                                    if (posicion > 0) {
                                        if (grupos != null && grupos.size() > 0) {

                                            GrupoVRXY gSel = grupos.get(posicion - 1);
                                            aux.setIdVR(gSel.getId());
                                        }
                                    }
                                    aux.setIva(chkIva.isChecked());
                                    aux.setDescripcionCorta(txtNombreProd.getText().toString());
                                    if (chkIva.isChecked()) {
                                        aux.setIvaCant(iva);
                                    }


                                    ProductosDB pdb = new ProductosDB();
                                    String error = pdb.actualizarProductoServer(aux, realm);

                                    if (error.equals("")) {
                                        mandarMensaje("Se actualizó correctamente el producto");
                                        mostrarListaProductos();
                                    } else {
                                        mandarMensaje(error);
                                    }
                                }
                            } else {
                                // es nuevo, guardamos en la local
                                ProductosXYDTOLocal p = new ProductosXYDTOLocal();
                                p.setCodigoBarras(txtCodigoBarras.getText().toString());
                                if (bitmapImage != null) {
                                    //quiere decir que se creo una nueva imagen
                                    p.setImgString(ut.BitMapToString(bitmapImage));
                                }
                                p.setExistencias(existencias);
                                p.setPrecioVenta(precioVenta);
                                p.setPrecioCompra(precioCompra);
                                p.setPrecioMayoreo(precioMayoreo);
                                p.setCantidadMayoreo(cantidadMayoreo);
                                p.setComision(comision);
                                Spinner spGrupos = (Spinner) findViewById(R.id.spGrupos);
                                int posicion = spGrupos.getSelectedItemPosition();
                                if (posicion > 0) {
                                    if (grupos != null && grupos.size() > 0) {

                                        GrupoVRXY gSel = grupos.get(posicion - 1);
                                        p.setIdVR(gSel.getId());
                                    }
                                }
                                p.setIva(chkIva.isChecked());
                                p.setDescripcionCorta(txtNombreProd.getText().toString());
                                if (chkIva.isChecked()) {
                                    p.setIvaCant(iva);
                                }
                                ProductosDB pdb = new ProductosDB();
                                String error = pdb.guardarBDProductosLocal(
                                        p, Integer.parseInt(ut.obtenerValor("idTienda", this)), realm);

                                if (error.equals("")) {
                                    mandarMensaje("Se guardó correctamente el producto");
                                    mostrarListaProductos();
                                } else {
                                    mandarMensaje(error);
                                }
                            }


                        }
                    } else {
                        mandarMensaje("El precio de venta tiene que ser un valor numérico válido");
                    }
                }
            }
        } catch (Exception ex) {
            Utilerias.log(this, "Error: " + ex.getMessage() + " " + ex.getStackTrace(), ex);
        }

    }


    private void subirProducto(ProductosXYDTO p) {
        ProductoDTO pr = new ProductoDTO();
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        APIInterface apiInterface = APIClient.getClient(this).create(APIInterface.class);


        try {
            Utilerias ut = new Utilerias();
            pr.setIdAndroid(ut.obtenerSerial(this, this));
            UsuariosDB udb = new UsuariosDB();

            int idUT = udb.obtenerIdUTUsuario(Integer.parseInt(ut.obtenerValor("idUsuario", this)),
                    Integer.parseInt(ut.obtenerValor("idTienda", this)), realm).getIdUT();

            pr.setIdUT(idUT);
            pr.setAll(false);
            pr.setIdTienda(ut.obtenerValor("idTienda", this));
            List<ProductosXYDTO> productos = new ArrayList<ProductosXYDTO>();
            productos.add(p);
            pr.setProductosxy(productos);
            Call<ProductoDTO> call = apiInterface.mandarConsultarProductos(pr);
            ProductoService ls = new ProductoService(call);
            ls.execute();

        } catch (Exception ex) {
            Utilerias.log(this, "Error: " + ex.getMessage() + " " + ex.getStackTrace(), ex);
            mandarMensaje(ex.getMessage());
        }
    }

    private void mostrarProducto(ProductosXYDTOAux p) {
        try {
            setContentView(R.layout.product);
            cargarOnfocus();
            progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
            ListView gvProductos = (ListView) findViewById(R.id.gvProductos);
            EditText txtCodigoBarras = (EditText) findViewById(R.id.txtCodigoBarras);
            EditText txtNombreProd = (EditText) findViewById(R.id.txtNombreProd);
            EditText txtPrecioCompra = (EditText) findViewById(R.id.txtPrecioCompra);
            EditText txtPrecioVenta = (EditText) findViewById(R.id.txtPrecioVenta);
            EditText txtPrecioMayoreo = (EditText) findViewById(R.id.txtPrecioMayoreo);
            EditText txtCantMayoreo = (EditText) findViewById(R.id.txtCantMayoreo);
            EditText txtComision = (EditText) findViewById(R.id.txtComision);
            EditText txtExistencias = (EditText) findViewById(R.id.txtExistencias);
            EditText txtIva = (EditText) findViewById(R.id.txtIva);
            CheckBox chkIva = (CheckBox) findViewById(R.id.chkIva);
            ImageButton btnListo = (ImageButton) findViewById(R.id.btnListo);


            Spinner spGrupos = (Spinner) findViewById(R.id.spGrupos);
            List<String> gruposS = new ArrayList<String>();
            gruposS.add("Seleccione");
            for (GrupoVRXY gr : grupos
            ) {
                gruposS.add(gr.getNombre());
            }
            ArrayAdapter aa = new ArrayAdapter(this, R.layout.combo, gruposS);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spGrupos.setAdapter(aa);
            int posicion = -1;
            boolean encontrado = false;
            for (GrupoVRXY g :
                    grupos) {
                posicion++;
                if (g.getId() == p.getIdVR()) {
                    encontrado = true;
                    break;
                }
            }
            if (encontrado) {
                spGrupos.setSelection(posicion + 1);
            }

            txtExistencias.setText("" + p.getExistencias());
            txtCodigoBarras.setText(p.getCodigoBarras());
            txtNombreProd.setText(p.getDescripcionCorta());
            txtPrecioCompra.setText("" + p.getPrecioCompra());
            txtPrecioVenta.setText("" + p.getPrecioVenta());
            txtPrecioMayoreo.setText("" + p.getPrecioMayoreo());
            txtCantMayoreo.setText("" + p.getCantidadMayoreo());
            txtComision.setText("" + p.getComision());
            txtIva.setText("" + p.getIvaCant());
            if (p.isIva()) {
                chkIva.setChecked(true);
                txtIva.setEnabled(true);
            } else {
                chkIva.setChecked(false);
                txtIva.setEnabled(false);
            }

            manejarClickIva();
            Utilerias ut = new Utilerias();
            ImageView imagenProductoConsulta = (ImageView) findViewById(R.id.imageProd);
            if (p.getImagenGuardada() != null) {

                imagenProductoConsulta.setImageBitmap(ut.StringToBitMapLocal(p.getImagenGuardada()));
            } else {
                imagenProductoConsulta.setImageResource(R.drawable.sinimagen);
            }

        } catch (Exception ex) {
            Utilerias.log(this, "Error: " + ex.getMessage() + " " + ex.getStackTrace(), ex);
        }
    }


    private void manejarClickIva() {
        CheckBox chkIva = (CheckBox) findViewById(R.id.chkIva);
        chkIva.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                EditText txtIva = (EditText) findViewById(R.id.txtIva);
                if (isChecked) {
                    txtIva.setEnabled(true);
                } else {
                    txtIva.setEnabled(false);
                }
            }
        });
    }


    private void manejarEnterBuscar() {
        EditText txtBuscarProd = (EditText) findViewById(R.id.txtBuscarProd);
        txtBuscarProd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    buscarProducto();
                    handled = true;
                }
                return handled;
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (pantalla != null && pantalla.equals("producto")) {
            mostrarListaProductos();
        }
    }


    private void buscarProducto() {
        //aqui dentro lo que hayan puesto lo buscamos
        EditText txtBuscarProd = (EditText) findViewById(R.id.txtBuscarProd);
        ProductosDB pdb = new ProductosDB();
        Utilerias ut = new Utilerias();

        productos =
                pdb.obtenerProductosCompletosPatron(txtBuscarProd.getText().toString().trim(),
                        Integer.parseInt(ut.obtenerValor("idTienda", this)), realm);

        TextView txtTotalRegs = (TextView) findViewById(R.id.txtTotalRegs);
        if (productos != null) {
            txtTotalRegs.setText("" + productos.size());
        } else {
            txtTotalRegs.setText("0");
        }
        ListView gvProductos = (ListView) findViewById(R.id.gvProductos);
        ProductosAdapter adapter = new ProductosAdapter(productos, this);
        gvProductos.setAdapter(adapter);
    }


    public void mandarMensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }


    class ProductoService extends AsyncTask<Void, Void, ProductoDTO> {

        String mensajeRegresa;
        Call<ProductoDTO> call;

        public ProductoService(Call<ProductoDTO> call) {
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

                ProductoDTO res = call.execute().body();
                if (res == null) {
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            mandarMensaje("Algo salió mal guardando, por favor intente de nuevo");
                        }
                    });

                } else {
                    mensajeRegresa = res.getMsg();
                    if (res.getProductosxy() == null) {
                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                mandarMensaje(mensajeRegresa);
                            }
                        });

                    } else {
                        if (res.getProductosxy().size() > 0) {
                            ProductosDB pdb = new ProductosDB();
                            Utilerias ut = new Utilerias();
                            Realm realmaux = ut.obtenerInstanciaBD(context);
                            pdb.actualizarBDProductos(res.getProductosxy(),
                                    Integer.parseInt(ut.obtenerValor("idTienda", context)),
                                    ut.obtenerModoAplicacion(context), ut.verificaConexion(context), realmaux, context);
                            if (realmaux != null && !realmaux.isClosed()) {
                                realmaux.close();
                            }

                        }

                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                mostrarListaProductos();

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
        protected void onPostExecute(ProductoDTO res) {
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
                    mostrarListaProductos();
                }
            });
        }

    }

}
