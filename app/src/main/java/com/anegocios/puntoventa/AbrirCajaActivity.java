package com.anegocios.puntoventa;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.anegocios.puntoventa.utils.Utilerias;
import com.anegocios.puntoventa.utils.UtileriasSincronizacion;

import io.realm.Realm;

public class AbrirCajaActivity  extends AppCompatActivity {


    Realm realm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mostrarAbrirCaja();

    }

    private void mostrarAbrirCaja() {
        Utilerias ut = new Utilerias();
        setContentView(R.layout.abrircaja);
        realm= ut.obtenerInstanciaBD(this);

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


            if (ut.verificaConexion(this)) {
                UtileriasSincronizacion uts = new UtileriasSincronizacion(this);
                uts.sincronizarTodo(this, this, realm,
                        Long.parseLong(ut.obtenerValor("idTienda", this)));
            }

        ut.cerrarRealmN(realm);

        if(ut.esPantallaChica(this)) {
            Intent i = new Intent(this, PuntoVentaChicoActivity.class);
            startActivity(i);
        }
        else
        {
            Intent i = new Intent(this, PuntoVentaActivity.class);
            startActivity(i);
        }





    }


    public void mandarMensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }

}
