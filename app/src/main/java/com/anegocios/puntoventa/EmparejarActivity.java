package com.anegocios.puntoventa;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.anegocios.puntoventa.adapters.ImpresorasAdapter;
import com.anegocios.puntoventa.utils.Utilerias;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import io.realm.Realm;

public class EmparejarActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    List<BluetoothDevice> devices;
    Realm realm ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.configurarimpresora);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Utilerias ut = new Utilerias();
        Set<BluetoothDevice> dispositivos = ut.obtenerDispositivos();
        ListView gvImpresoras = (ListView) findViewById(R.id.gvImpresoras);
        devices = new ArrayList<>();
        for (BluetoothDevice ba : dispositivos
        ) {
            devices.add(ba);
        }
        ImpresorasAdapter adapter = new ImpresorasAdapter(devices, this);
        gvImpresoras.setAdapter(adapter);

        gvImpresoras.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


                String macadress = devices.get(position).getAddress();
                guardarNombreImpresora(devices.get(position).getName());
                guardarMacImpresora(macadress);
                mandarMensaje("Impresora: "+devices.get(position).getName()+" configurada con éxito");
               irPuntoVenta();

            }
        });


    }

    private void  irPuntoVenta()
    {
        Utilerias ut = new Utilerias();
        ut.validaIrPuntoVenta(this, this, realm, ut.obtenerPermisosUsuario(this), "normal");
    }

    private void guardarMacImpresora(String macadress) {
        Utilerias ut = new Utilerias();
        ut.guardarValor("macImpresora", macadress, this);

    }

    private void guardarNombreImpresora(String nombre) {
        Utilerias ut = new Utilerias();
        ut.guardarValor("nombreImpresora", nombre, this);

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

    @Override
    public void onBackPressed() {

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
            ut.validaIrPuntoVenta(this, this, ut.obtenerInstanciaBD(), ut.obtenerPermisosUsuario(this), "normal");
        } else if (id == R.id.nav_productos) {
            Intent i = new Intent(getApplicationContext(), ProductosActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_clientes) {
            Utilerias ut = new Utilerias();
            if (ut.esPantallaChica(this)) {
                Intent i = new Intent(getApplicationContext(), ClientesActivity.class);
                startActivity(i);
            } else {
                Intent i = new Intent(getApplicationContext(), ClientesGrandeActivity.class);
                startActivity(i);
            }
        } else if (id == R.id.nav_reportes) {
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


    public void btnMostrarMenuClick(View view) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.openDrawer(GravityCompat.START);
    }


    private void cerrarRealmN(Realm realm) {
        if (realm != null && !realm.isClosed()) {
            realm.close();
        }
    }


    public void mandarMensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }
}
