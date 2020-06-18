package com.anegocios.puntoventa;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.anegocios.puntoventa.utils.Utilerias;

import java.util.Set;

import io.realm.Realm;

public class ConfiguracionActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utilerias ut = new Utilerias();
        realm = ut.obtenerInstanciaBD();
        mostrarConfiguracionUsuario();
    }

    private void  mostrarConfiguracionUsuario()
    {
        setContentView(R.layout.configuracionusuario);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        TextView txtVigencia = (TextView) findViewById(R.id.txtVigencia);
        TextView txtTipoUsuario = (TextView) findViewById(R.id.txtTipoUsuario);

        Utilerias ut = new Utilerias();
        String vigencia=ut.obtenerValor("vigencia",this);
        String tipoUsuario=ut.obtenerValor("tipoUsuario",this);
        txtVigencia.setText(vigencia);
        txtTipoUsuario.setText(tipoUsuario);



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

    public void btnMostrarMenuClick(View view)
    {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.openDrawer(GravityCompat.START);
    }





    public void mandarMensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }
}
