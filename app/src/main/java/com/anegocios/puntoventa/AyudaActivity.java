package com.anegocios.puntoventa;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.Toast;

import com.anegocios.puntoventa.database.ProductosDB;
import com.anegocios.puntoventa.database.UsuariosDB;
import com.anegocios.puntoventa.dtosauxiliares.ProductosXYDTOAux;
import com.anegocios.puntoventa.jsons.VersionEscritorioDTO;
import com.anegocios.puntoventa.servicios.APIClient;
import com.anegocios.puntoventa.servicios.APIInterface;
import com.anegocios.puntoventa.utils.Utilerias;

import java.net.URLEncoder;
import java.util.List;
import java.util.Set;

import io.realm.Realm;

public class AyudaActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    ImageButton btnModo;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utilerias ut = new Utilerias();
        String primera = ut.obtenerValor("primeraVez", this);
        if (primera != null && primera.equals("1")) {
            ut.guardarValor("primeraVez","0",this);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Intent i = new Intent(this, ProductosActivity.class);
            startActivity(i);
        }
        realm = ut.obtenerInstanciaBD();
        setContentView(R.layout.ayuda);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        btnModo = findViewById(R.id.btnModo);
        if (ut.obtenerModoAplicacion(this)) {
            btnModo.setImageResource(R.drawable.conconexion);

        } else {
            btnModo.setImageResource(R.drawable.sinconexion);

        }




    }


    public void btnPersonalizarClick(View view) {
        String url1 = "https://youtu.be/SJIvCL0OWMo";
        WebView yt1 = (WebView) findViewById(R.id.webView1);
        yt1.setWebChromeClient(new WebChromeClient());
        WebSettings ws = yt1.getSettings();
        ws.setBuiltInZoomControls(true);
        ws.setJavaScriptEnabled(true);
        ws.setLoadWithOverviewMode(true);
        ws.setUseWideViewPort(true);
        yt1.loadUrl(url1);
    }

    public void btnAltaProductosClick(View view) {
        String url1 = "https://youtu.be/FpaoYyllpb4";
        WebView yt1 = (WebView) findViewById(R.id.webView1);
        yt1.setWebChromeClient(new WebChromeClient());
        WebSettings ws = yt1.getSettings();
        ws.setBuiltInZoomControls(true);
        ws.setJavaScriptEnabled(true);
        ws.setLoadWithOverviewMode(true);
        ws.setUseWideViewPort(true);
        yt1.loadUrl(url1);
    }


    public void btnAltaClientesClick(View view) {
        String url1 = "https://youtu.be/MQhCUhBUxtQ";
        WebView yt1 = (WebView) findViewById(R.id.webView1);
        yt1.setWebChromeClient(new WebChromeClient());
        WebSettings ws = yt1.getSettings();
        ws.setBuiltInZoomControls(true);
        ws.setJavaScriptEnabled(true);
        ws.setLoadWithOverviewMode(true);
        ws.setUseWideViewPort(true);
        yt1.loadUrl(url1);
    }

    public void btnEmparejarImpresoraClick(View view) {
        String url1 = "https://youtu.be/hRylc6pHEvU";
        WebView yt1 = (WebView) findViewById(R.id.webView1);
        yt1.setWebChromeClient(new WebChromeClient());
        WebSettings ws = yt1.getSettings();
        ws.setBuiltInZoomControls(true);
        ws.setJavaScriptEnabled(true);
        ws.setLoadWithOverviewMode(true);
        ws.setUseWideViewPort(true);
        yt1.loadUrl(url1);
    }

    public void btnVentaClick(View view) {
        String url1 = "https://youtu.be/P1RvO3KJ9WY";
        WebView yt1 = (WebView) findViewById(R.id.webView1);
        yt1.setWebChromeClient(new WebChromeClient());
        WebSettings ws = yt1.getSettings();
        ws.setBuiltInZoomControls(true);
        ws.setJavaScriptEnabled(true);
        ws.setLoadWithOverviewMode(true);
        ws.setUseWideViewPort(true);
        yt1.loadUrl(url1);
    }

    public void btnReportesClick(View view) {

        mandarMensaje("Video filmándoce");
      /*  String url1 = "https://youtu.be/P1RvO3KJ9WY";
        WebView  yt1 = (WebView) findViewById(R.id.webView1);
        yt1.setWebChromeClient(new WebChromeClient());
        WebSettings ws = yt1.getSettings();
        ws.setBuiltInZoomControls(true);
        ws.setJavaScriptEnabled(true);
        ws.setLoadWithOverviewMode(true);
        ws.setUseWideViewPort(true);
        yt1.loadUrl(url1);*/
    }

    public void btnLogOutClick(View view) {
        cerrarRealmN(realm);
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }

    public void btnVersionEscritorioClick(View view) {

        mandarMensaje("Video filmándoce");
      /*  String url1 = "https://youtu.be/P1RvO3KJ9WY";
        WebView  yt1 = (WebView) findViewById(R.id.webView1);
        yt1.setWebChromeClient(new WebChromeClient());
        WebSettings ws = yt1.getSettings();
        ws.setBuiltInZoomControls(true);
        ws.setJavaScriptEnabled(true);
        ws.setLoadWithOverviewMode(true);
        ws.setUseWideViewPort(true);
        yt1.loadUrl(url1);*/
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

        } catch (Exception ex) {
            String error = ex.getMessage();
        }
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

    private void cerrarRealmN(Realm realm) {
        if (realm != null && !realm.isClosed()) {
            realm.close();
        }
    }


    public void mandarMensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }
}
