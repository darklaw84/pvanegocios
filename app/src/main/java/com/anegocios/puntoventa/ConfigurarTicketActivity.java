package com.anegocios.puntoventa;

import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.anegocios.puntoventa.adapters.RowsAdapter;
import com.anegocios.puntoventa.bdlocal.ImagenTicketDTOLocal;
import com.anegocios.puntoventa.bdlocal.RowTicketLocal;
import com.anegocios.puntoventa.database.TicketDB;
import com.anegocios.puntoventa.dtosauxiliares.BytesImagenResultadoDTO;
import com.anegocios.puntoventa.utils.Utilerias;
import com.anegocios.puntoventa.utils.UtilsImagenes;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.List;
import java.util.Set;

import io.realm.Realm;

public class ConfigurarTicketActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private List<RowTicketLocal> headers;
    private List<RowTicketLocal> footers;
    private RowTicketLocal rowSel;
    long idTiendaGlobal;
    private String tipoRow;
    Realm realm;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mostrarConfigurarTicket();

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
            ut.validaIrPuntoVenta(this, this, realm, ut.obtenerPermisosUsuario(this),"normal");
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
            ut.guardarValor("mostrarPedidos","NO",this);
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

    public void btnLogOutClick(View view)
    {
        cerrarRealmN(realm);
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }

    private void cerrarRealmN(Realm realm) {
        if (realm != null && !realm.isClosed()) {
            realm.close();
        }
    }

    private void mostrarConfigurarTicket()
    {
        setContentView(R.layout.configuracionticket);
        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);
        Utilerias ut = new Utilerias();
        idTiendaGlobal = Long.parseLong(ut.obtenerValor("idTienda", this));
        realm = ut.obtenerInstanciaBD();

        //tenemos que cargar los header is los footer que haya

        TicketDB tdb = new TicketDB();

        ImagenTicketDTOLocal imagen = tdb.obtenerImagen(idTiendaGlobal,realm);

        if (imagen != null && imagen.getImagenView() != null) {
            Bitmap bitmap = ut.ByteToBitMap(imagen.getImagenView());
            ImageView imageTicket = (ImageView) findViewById(R.id.imageTicket);
            imageTicket.setImageBitmap(bitmap);
        }

        headers = tdb.obtenerRows("H", idTiendaGlobal,realm);


        footers = tdb.obtenerRows("F", idTiendaGlobal,realm);

        ListView gvHeader = (ListView) findViewById(R.id.gvHeader);
        ListView gvFooter = (ListView) findViewById(R.id.gvFooter);
        RowsAdapter adapter = new RowsAdapter(headers, this);
        gvHeader.setAdapter(adapter);


        RowsAdapter adapterf = new RowsAdapter(footers, this);
        gvFooter.setAdapter(adapterf);
        gvHeader.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                rowSel = headers.get(position);
                tipoRow = rowSel.getTipo();
                mostrarRow();
            }
        });

        gvFooter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                rowSel = footers.get(position);
                tipoRow = rowSel.getTipo();
                mostrarRow();
            }
        });
    }

    private void mostrarRow() {
        setContentView(R.layout.rowticket);
        RadioButton chica = (RadioButton) findViewById(R.id.chica);
        RadioButton media = (RadioButton) findViewById(R.id.media);
        RadioButton grande = (RadioButton) findViewById(R.id.grande);
        chica.setChecked(false);
        media.setChecked(false);
        grande.setChecked(false);
        // TamaniosLetraAdapter adapter = new TamaniosLetraAdapter(obtenerTamanios(), this);
        if (rowSel.getTamanioLetra() == 1) {
            chica.setChecked(true);
        } else if (rowSel.getTamanioLetra() == 2) {
            media.setChecked(true);
        } else if (rowSel.getTamanioLetra() == 3) {
            grande.setChecked(true);
        }


        EditText txtTextoRow = (EditText) findViewById(R.id.txtTextoRow);

        txtTextoRow.setText(rowSel.getTexto());

    }

    public void btnBorrarRowClick(View view) {
        //si llega aqui es porque se tiene que borrar de la BD

        AlertDialog.Builder alert = new AlertDialog.Builder(ConfigurarTicketActivity.this);
        alert.setTitle("Borrar ");
        alert.setMessage("Estas seguro que deseas borrar el registro?");
        alert.setPositiveButton("SI", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                borrarRow();
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

    public void borrarRow() {
        TicketDB tdb = new TicketDB();
        Utilerias ut = new Utilerias();

        tdb.borrarRow(rowSel.getId(),realm);

        mostrarConfigurarTicket();
    }


    public void btnAceptarRowClick(View view) {
        // cuando lega aqui solo evaluamos que tenga valor y
        Utilerias ut = new Utilerias();
        TicketDB tdb = new TicketDB();
        EditText txtTextoRow = (EditText) findViewById(R.id.txtTextoRow);
        RadioButton chica = (RadioButton) findViewById(R.id.chica);
        RadioButton media = (RadioButton) findViewById(R.id.media);
        RadioButton grande = (RadioButton) findViewById(R.id.grande);
        if (!txtTextoRow.getText().toString().trim().equals("")) {
            if (chica.isChecked() || media.isChecked() || grande.isChecked()) {
                int tamanio = 0;
                if (chica.isChecked()) {
                    tamanio = 1;
                } else if (media.isChecked()) {
                    tamanio = 2;
                } else if (grande.isChecked()) {
                    tamanio = 3;
                }

                if (rowSel.getId() > 0) {
                    //es edicion

                    tdb.actualizarRow(txtTextoRow.getText().toString(), tamanio, tipoRow, rowSel.getId(), idTiendaGlobal,realm);

                } else {
                    // es nuevo

                    tdb.guardarRow(txtTextoRow.getText().toString(), tamanio, tipoRow, idTiendaGlobal,realm);

                }
                mostrarConfigurarTicket();
            } else {
                mandarMensaje("Debes seleccionar un tamaño de letra");
            }
        } else {
            if (tipoRow.equals("H")) {
                mandarMensaje("Debes incluir un texto valido al Header");
            } else {
                mandarMensaje("Debes incluir un texto valido al Footer");
            }
        }
    }

    @Override
    public void onBackPressed() {
        mostrarConfigurarTicket();
    }

    public void btnNuevoHeaderClick(View view) {

        setContentView(R.layout.rowticket);

        RadioButton chica = (RadioButton) findViewById(R.id.chica);
        RadioButton media = (RadioButton) findViewById(R.id.media);
        RadioButton grande = (RadioButton) findViewById(R.id.grande);
        ImageButton btnBorrarRow = (ImageButton) findViewById(R.id.btnBorrarRow);
        btnBorrarRow.setVisibility(View.INVISIBLE);
        chica.setChecked(false);
        media.setChecked(false);
        grande.setChecked(false);
        TextView txtTipoRow = (TextView) findViewById(R.id.txtTipoRow);
        TextView txtTextoRow = (TextView) findViewById(R.id.txtTextoRow);
        txtTextoRow.setHint("Nombre de tu negocio, calle, colonia, telefono");
        rowSel = new RowTicketLocal();
        tipoRow = "H";
        txtTipoRow.setText("CABECERA TICKET");

    }

    public void btnGaleriaClick(View view) {


        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        photoPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(photoPickerIntent, 400);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ImageView imageTicket = (ImageView) findViewById(R.id.imageTicket);
        if (requestCode == 400 && resultCode == RESULT_OK) {
            Uri uri = data.getData();

            try {
                Bitmap bitmapImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                Bitmap resized = Bitmap.createScaledBitmap(bitmapImage, 250, 100, false);
                TicketDB tdb = new TicketDB();
                Utilerias ut = new Utilerias();

                tdb.borrarImagen(idTiendaGlobal,realm);


                BytesImagenResultadoDTO resultado = UtilsImagenes.decodeBitmap(resized);
                if (resultado.getError().equals("")) {
                    imageTicket.setImageURI(uri);

                    tdb.guardarImagen(resultado.getImagen(),
                            ut.BitMapToBytes(resized), idTiendaGlobal,realm);

                } else {
                    mandarMensaje(resultado.getError());
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }


        }


    }

    public void btnMostrarMenuClick(View view)
    {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.openDrawer(GravityCompat.START);
    }


    public void btnNuevoFooterClick(View view) {

        setContentView(R.layout.rowticket);

        RadioButton chica = (RadioButton) findViewById(R.id.chica);
        RadioButton media = (RadioButton) findViewById(R.id.media);
        RadioButton grande = (RadioButton) findViewById(R.id.grande);
        ImageButton btnBorrarRow = (ImageButton) findViewById(R.id.btnBorrarRow);
        btnBorrarRow.setVisibility(View.INVISIBLE);
        chica.setChecked(false);
        media.setChecked(false);
        grande.setChecked(false);
        TextView txtTipoRow = (TextView) findViewById(R.id.txtTipoRow);

        rowSel = new RowTicketLocal();
        tipoRow = "F";
        txtTipoRow.setText("PIE DE PÁGINA TICKET");
        TextView txtTextoRow = (TextView) findViewById(R.id.txtTextoRow);
        txtTextoRow.setHint("Gracias por su preferencia");
    }

    public void mandarMensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }

}
