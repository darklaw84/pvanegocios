package com.anegocios.puntoventa;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.anegocios.puntoventa.adapters.AbonosTicketAdapter;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.anegocios.puntoventa.adapters.ReporteAdapter;
import com.anegocios.puntoventa.adapters.VentasTicketAdapter;
import com.anegocios.puntoventa.bdlocal.CajaDTOLocal;
import com.anegocios.puntoventa.database.ProductosDB;
import com.anegocios.puntoventa.database.UsuariosDB;
import com.anegocios.puntoventa.dtosauxiliares.PaqueteOpcionAux;
import com.anegocios.puntoventa.jsons.ModificacionPedidoDTO;
import com.anegocios.puntoventa.jsons.OpcionesPaquete;
import com.anegocios.puntoventa.jsons.ProductosXYDTO;
import com.anegocios.puntoventa.jsons.RecibirAbonoDTO;
import com.anegocios.puntoventa.jsons.ReporteDTO;
import com.anegocios.puntoventa.jsons.ReporteDetalleDTO;
import com.anegocios.puntoventa.jsons.ReporteTicketDetalleDTO;
import com.anegocios.puntoventa.jsons.VentasDetalleTicket;
import com.anegocios.puntoventa.jsons.VentasVentaTicketDTO;
import com.anegocios.puntoventa.servicios.APIClient;
import com.anegocios.puntoventa.servicios.APIInterface;


import com.anegocios.puntoventa.servicios.DetalleTicketService;
import com.anegocios.puntoventa.servicios.EntregaProductoService;
import com.anegocios.puntoventa.servicios.RecibirAbonoService;
import com.anegocios.puntoventa.utils.Utilerias;
import com.anegocios.puntoventa.utils.UtileriasImpresion;
import com.google.gson.Gson;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import io.realm.Realm;
import retrofit2.Call;

public class ReportesGrandeActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {


    private String pantalla;

    ProgressBar pb;
    Context context;
    Activity activity;
    List<ReporteDetalleDTO> datos;
    List<ReporteDetalleDTO> datosBuscar;
    List<ReporteDetalleDTO> datosAux;
    private String tipo;
    String folioConsultar;
    EditText txtFechaFin, txtFechaIni, txtFechaPago;
    private int mYear, mMonth, mDay;
    double total;
    double abonado;
    double saldo;
    Realm realm;
    ReporteTicketDetalleDTO r;
    double restante;
    int idTicket;
    String fechaIni,fechaFin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utilerias ut = new Utilerias();
        realm = ut.obtenerInstanciaBD(this);
        activity = this;
        context = this;
        String mostrarPedidos=ut.obtenerValor("mostrarPedidos",this);
        if(mostrarPedidos==null || mostrarPedidos.equals("NO"))
        {
            mostrarReportes();
        }
        else
        {
            irAPedidos();
        }



    }

    private void mostrarReportes() {
        setContentView(R.layout.homereportesgrande);
        pb = (ProgressBar) findViewById(R.id.progress_bar);
        pb.setVisibility(View.INVISIBLE);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        pantalla = "homereportes";

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

    public void btnLogOutClick(View view) {
        Utilerias ut = new Utilerias();
        ut.guardarValor("idUsuario","",this);
        cerrarRealmN(realm);
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }


    public void btnMostrarMenuClick(View view) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.openDrawer(GravityCompat.START);
    }


    public void btnVentasClick(View view) {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        fechaIni = sd.format(new Date());
        fechaFin = sd.format(new Date());
        pantalla = "reporteVista";
        setContentView(R.layout.reportevistagrande);
        pb = (ProgressBar) findViewById(R.id.progress_bar);
        pb.setVisibility(View.INVISIBLE);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        txtFechaIni = (EditText) findViewById(R.id.txtFechaIni);
        txtFechaFin = (EditText) findViewById(R.id.txtFechaFin);
        txtFechaFin.setText(fechaFin);
        txtFechaIni.setText(fechaIni);
        txtFechaFin.setOnClickListener(this);
        txtFechaIni.setOnClickListener(this);
        tipo = "ventas";
        TextView txtHEaderReporte = (TextView) findViewById(R.id.txtHEaderReporte);
        txtHEaderReporte.setText("REPORTE DE " + tipo.toUpperCase());
        consultarReporte("ventas", fechaIni, fechaFin);

    }

    public void btnCotizacionesClick(View view) {
        pb = (ProgressBar) findViewById(R.id.progress_bar);
        pb.setVisibility(View.VISIBLE);

        tipo = "cotizaciones";

        consultarReporte("cotizaciones", "", "");

    }

    public void btnPedidosClick(View view) {
        irAPedidos();
    }

    private void irAPedidos()
    {
        setContentView(R.layout.homereportesgrande);
        pb = (ProgressBar) findViewById(R.id.progress_bar);
        pb.setVisibility(View.VISIBLE);

        tipo = "pedidos";

        consultarReporte("pedidos", "", "");
    }


    @Override
    public void onClick(View view) {

        if (view == txtFechaFin) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            String dia = "";
                            if (dayOfMonth < 10) {
                                dia = "0" + dayOfMonth;
                            } else {
                                dia = "" + dayOfMonth;
                            }


                            String mes = "";
                            int mesReal = monthOfYear + 1;
                            if (mesReal < 10) {
                                mes = "0" + mesReal;
                            } else {
                                mes = "" + mesReal;
                            }
                            String fechaSeleccionada=year + "-" + mes + "-" + dia;
                            fechaFin=fechaSeleccionada;
                            txtFechaFin.setText(fechaSeleccionada);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        } else if (view == txtFechaIni) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            String dia = "";
                            if (dayOfMonth < 10) {
                                dia = "0" + dayOfMonth;
                            } else {
                                dia = "" + dayOfMonth;
                            }


                            String mes = "";
                            int mesReal = monthOfYear + 1;
                            if (mesReal < 10) {
                                mes = "0" + mesReal;
                            } else {
                                mes = "" + mesReal;
                            }
                            String fechaSeleccionada=year + "-" + mes + "-" + dia;
                            fechaIni=fechaSeleccionada;
                            txtFechaIni.setText(fechaSeleccionada);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        } else if (view == txtFechaPago) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            String dia = "";
                            if (dayOfMonth < 10) {
                                dia = "0" + dayOfMonth;
                            } else {
                                dia = "" + dayOfMonth;
                            }


                            String mes = "";
                            int mesReal = monthOfYear + 1;
                            if (mesReal < 10) {
                                mes = "0" + mesReal;
                            } else {
                                mes = "" + mesReal;
                            }
                            txtFechaPago.setText(year + "-" + mes + "-" + dia);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }

    }


    public void btnConsultarReporteClick(View view) {

        txtFechaIni = (EditText) findViewById(R.id.txtFechaIni);
        txtFechaFin = (EditText) findViewById(R.id.txtFechaFin);
        tipo = "ventas";
        pb = (ProgressBar) findViewById(R.id.progress_bar);
        pb.setVisibility(View.VISIBLE);
        consultarReporte("ventas", txtFechaIni.getText().toString(),
                txtFechaFin.getText().toString());

    }

    private void calcularTotales() {
        total = 0;
        abonado = 0;
        saldo = 0;
        if (datos != null && datos.size() > 0) {
            for (ReporteDetalleDTO r : datos) {
                total += r.getTotal();
                abonado += r.getAbonado();
                saldo += r.getSaldo();
            }
        } else {
            total = 0;
            saldo = 0;
            abonado = 0;
        }
    }

    private void mostrarReporte() {
        setContentView(R.layout.reportevistagrande);
        pb = (ProgressBar) findViewById(R.id.progress_bar);
        pb.setVisibility(View.INVISIBLE);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        calcularTotales();
        TextView txtRepAbonado = (TextView) findViewById(R.id.txtRepAbonado);
        TextView txtRepSaldo = (TextView) findViewById(R.id.txtRepSaldo);
        TextView txtRepTotal = (TextView) findViewById(R.id.txtRepTotal);
        Utilerias ut = new Utilerias();
        txtRepAbonado.setText("$ " + ut.formatDouble(abonado));
        txtRepSaldo.setText("$ " + ut.formatDouble(saldo));
        txtRepTotal.setText("$ " + ut.formatDouble(total));
        TextView txtCabAbonado = (TextView) findViewById(R.id.txtCabAbonado);
        TextView txtCabSaldo = (TextView) findViewById(R.id.txtCabSaldo);
        if (tipo.equals("cotizaciones")) {
            txtRepAbonado.setText("");
            txtRepSaldo.setText("");
            txtCabSaldo.setText("");
            txtCabAbonado.setText("");
        } else {
            txtCabSaldo.setText("Saldo");
            txtCabAbonado.setText("Abonado");
        }


        txtFechaIni = (EditText) findViewById(R.id.txtFechaIni);
        txtFechaFin = (EditText) findViewById(R.id.txtFechaFin);
        LinearLayout layFechas = (LinearLayout) findViewById(R.id.layFechas);
        if (tipo.equals("ventas")) {
            layFechas.setVisibility(View.VISIBLE);
            txtFechaFin.setOnClickListener(this);
            txtFechaIni.setOnClickListener(this);
            txtFechaIni.setText(fechaIni);
            txtFechaFin.setText(fechaFin);
        } else {
            layFechas.setVisibility(View.GONE);
        }

        pantalla = "reporteVista";

        TextView txtHEaderReporte = (TextView) findViewById(R.id.txtHEaderReporte);
        txtHEaderReporte.setText("REPORTE DE " + tipo.toUpperCase());
        ListView gvRegistros = (ListView) findViewById(R.id.gvRegistros);
        ReporteAdapter adapter = new ReporteAdapter(datos, this, tipo,"G");
        gvRegistros.setAdapter(adapter);

        gvRegistros.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ReporteDetalleDTO rowSel = datos.get(position);
                mostrarDetalleReporte(rowSel);

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
        imm.showSoftInput(txtBuscarCli, InputMethodManager.SHOW_IMPLICIT);
*/

    }

    private void buscarCliente() {
        EditText txtBuscarCli = (EditText) findViewById(R.id.txtBuscarCli);
        ListView gvRegistros = (ListView) findViewById(R.id.gvRegistros);
        datosBuscar = new ArrayList<>();
        if (datosAux != null) {
            for (ReporteDetalleDTO d : datosAux
            ) {
                if (d.getCliente().contains(txtBuscarCli.getText().toString())) {
                    datosBuscar.add(d);
                }
            }
            datos = datosBuscar;
            calcularTotales();
            TextView txtRepAbonado = (TextView) findViewById(R.id.txtRepAbonado);
            TextView txtRepSaldo = (TextView) findViewById(R.id.txtRepSaldo);
            TextView txtRepTotal = (TextView) findViewById(R.id.txtRepTotal);
            Utilerias ut = new Utilerias();
            txtRepAbonado.setText("$ " + ut.formatDouble(abonado));
            txtRepSaldo.setText("$ " + ut.formatDouble(saldo));
            txtRepTotal.setText("$ " + ut.formatDouble(total));
            ReporteAdapter adapter = new ReporteAdapter(datos, this, tipo,"G");
            gvRegistros.setAdapter(adapter);
        }
    }

    public void btnCancelarXClick(View view) {
        EditText txtBuscarCli = (EditText) findViewById(R.id.txtBuscarCli);
        txtBuscarCli.setText("");
        buscarCliente();
    }

    private void mostrarDetalleReporte(ReporteDetalleDTO sel) {
        r = consultarDetalle(tipo, sel.getFolio());
        idTicket = sel.getId();
        folioConsultar = sel.getFolio();
        if (r != null) {
            setContentView(R.layout.detalleticketgrande);
            iniciarMenuContextual();
            pantalla = "detalleticket";
            TextView txtTituloDetalle = (TextView) findViewById(R.id.txtTituloDetalle);
            ImageButton btnTresPuntos = (ImageButton) findViewById(R.id.btnTresPuntos);
            if (tipo.equals("pedidos")) {
                txtTituloDetalle.setText("Detalle Pedido");
                btnTresPuntos.setVisibility(View.VISIBLE);
            } else {
                txtTituloDetalle.setText("Detalle Ticket");
                btnTresPuntos.setVisibility(View.INVISIBLE);
            }
            TextView txtFolio = (TextView) findViewById(R.id.txtFolio);
            TextView txtEstatus = (TextView) findViewById(R.id.txtEstatus);
            TextView txtSubtotal = (TextView) findViewById(R.id.txtSubtotal);
            TextView txtIva = (TextView) findViewById(R.id.txtIva);
            TextView txtDescuento = (TextView) findViewById(R.id.txtDescuento);
            TextView txtPropina = (TextView) findViewById(R.id.txtPropina);
            TextView txtTotal = (TextView) findViewById(R.id.txtTotal);
            TextView txtPago = (TextView) findViewById(R.id.txtPago);
            TextView txtSaldo = (TextView) findViewById(R.id.txtSaldo);
            TextView txtComentario = (TextView) findViewById(R.id.txtComentario);

            TextView txtFecha = (TextView) findViewById(R.id.txtFecha);
            TextView txtProdEntregado = (TextView) findViewById(R.id.txtProdEntregado);
            TextView txtCliente = (TextView) findViewById(R.id.txtCliente);

            if (r.getVentas() != null && r.getVentas().size() > 0) {
                VentasDetalleTicket v = r.getVentas().get(0);
                txtFolio.setText(v.getFolio());
                txtEstatus.setText(v.getEstatus());
                txtSubtotal.setText("" + v.getSubTotal());
                txtIva.setText("" + v.getIva());
                txtDescuento.setText("" + v.getDescuento());
                txtPropina.setText("" + v.getPropina());
                txtTotal.setText("" + v.getTotal());
                txtPago.setText("" + v.getPago());
                txtSaldo.setText("" + v.getSaldo());
                txtComentario.setText(v.getComentario());
                txtFecha.setText(v.getFecha());
                if (v.isProdEntregado()) {
                    txtProdEntregado.setText("SI");
                } else {
                    txtProdEntregado.setText("NO");
                }
                txtCliente.setText(v.getCliente().getNombre()+" "+v.getCliente().getApellidoP()+" "+v.getCliente().getApellidoM());
                ListView gvVentas = (ListView) findViewById(R.id.gvVentas);
                if (r.getVentas().get(0).getVentas() != null) {
                    VentasTicketAdapter adapter = new VentasTicketAdapter(r.getVentas().get(0).getVentas(), this,"G");
                    gvVentas.setAdapter(adapter);

                }



                LinearLayout layoutTituloAbonos = (LinearLayout) findViewById(R.id.layoutTituloAbonos);
                LinearLayout layoutHeadersAbonos = (LinearLayout) findViewById(R.id.layoutHeadersAbonos);
                ListView gvAbonos = (ListView) findViewById(R.id.gvAbonos);


                if(r.getVentas().get(0).getPagosDiferidos()!=null && r.getVentas().get(0).getPagosDiferidos().size()>0)
                {
                    layoutTituloAbonos.setVisibility(View.VISIBLE);
                    layoutHeadersAbonos.setVisibility(View.VISIBLE);
                    gvAbonos.setVisibility(View.VISIBLE);
                    AbonosTicketAdapter adapter = new AbonosTicketAdapter(r.getVentas().get(0).getPagosDiferidos(), this, "C");
                    gvAbonos.setAdapter(adapter);

                }
                else
                {
                    layoutTituloAbonos.setVisibility(View.GONE);
                    layoutHeadersAbonos.setVisibility(View.GONE);
                    gvAbonos.setVisibility(View.GONE);
                }
            }
        }
    }


    public void btnReimprimirTicket(View view) {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                reimprimirTicketActual();
            }
        });

    }

    private void reimprimirTicketActual()
    {
        Utilerias ut = new Utilerias();
        long idTiendaGlobal = Long.parseLong(ut.obtenerValor("idTienda", context));
        ut.reImprimirTicket(this,this,idTiendaGlobal,r);
    }

    private void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {

            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {

            totalHeight += 37;
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
        ;
        listView.setLayoutParams(params);
        listView.requestLayout();

    }

    @Override
    public void onBackPressed() {

        if (pantalla != null && pantalla.equals("reporteVista")) {
            cerrarRealmN(realm);
            Intent i = new Intent(getApplicationContext(), ReportesActivity.class);
            startActivity(i);
        } else if (pantalla != null && pantalla.equals("detalleticket")) {
            setContentView(R.layout.reportevistagrande);
            mostrarReporte();
        }

    }


    public void btnDerechoClick(View view) {
        view.showContextMenu();
    }


    private void iniciarMenuContextual() {
        ImageView btnMenuDerecho = (ImageView) findViewById(R.id.btnTresPuntos);
        registerForContextMenu(btnMenuDerecho);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.contextmenupedido, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.btnEntregar:
                entregarProducto();
                return true;
            case R.id.btnEditar:
                editarPedido();
                return true;
            case R.id.btnAbonar:
                abonar();
                return true;
        }

        return super.onContextItemSelected(item);
    }

    private void entregarProducto() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Delete");
        alert.setMessage("¿Esta seguro de realizar la entrega?");
        alert.setPositiveButton("Si", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                siEntregarProducto();

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

    private void siEntregarProducto() {
        ModificacionPedidoDTO pr = new ModificacionPedidoDTO();
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        try {
            Utilerias ut = new Utilerias();
            pr.setAndroidId(ut.obtenerSerial(this, this));
            UsuariosDB udb = new UsuariosDB();
            int idUT = udb.obtenerIdUTUsuario(Integer.parseInt(ut.obtenerValor("idUsuario", this)),
                    Integer.parseInt(ut.obtenerValor("idTienda", this)), realm).getIdUT();
            pr.setIdUT(idUT);
            pr.setId(idTicket);
            Gson gson = new Gson();
            String json = gson.toJson(pr);
            Call<ModificacionPedidoDTO> call = apiInterface.entregarProducto(pr);
            EntregaProductoService ls = new EntregaProductoService(call);
            ModificacionPedidoDTO res = ls.execute().get();
            if (res == null) {
                mandarMensaje("Ocurrió un problema con e servicio y no pudimos entregar el pedido");
            } else {
                if (!res.isExito()) {
                    mandarMensaje(res.getMsg());
                } else {
                    mandarMensaje(res.getMsg());
                    mostrarReportes();
                    ReporteTicketDetalleDTO detalle = consultarDetalle(tipo, "" + folioConsultar);
                    ProductosDB pdb = new ProductosDB();
                    if (detalle != null && detalle.getVentas() != null && detalle.getVentas().size() > 0) {
                        for (VentasVentaTicketDTO p : detalle.getVentas().get(0).getVentas()
                        ) {
                            ProductosXYDTO pro = pdb.obtenerproductoServer(p.getIdProducto(), realm);
                            if (pro.isPaquete()) {
                                List<PaqueteOpcionAux> opcionesSeleccionadas = new ArrayList<>();
                                PaqueteOpcionAux pq = new PaqueteOpcionAux();
                                pq.setIdOpcion(Long.parseLong(p.getOpcionesPkt().get(0).getId()));
                                opcionesSeleccionadas.add(pq);
                                for (PaqueteOpcionAux po : opcionesSeleccionadas
                                ) {

                                    OpcionesPaquete opcion = pdb.obtenerOpcionPaquete(po.getIdOpcion(), realm);

                                    po.setCantidad(opcion.getCantidad());
                                    po.setDescripion(opcion.getDescripcion());
                                    po.setIdProducto(opcion.getIdProducto());
                                    po.setPrecio(opcion.getPrecio());
                                    po.setIdPaquete(opcion.getIdPaquete());
                                    po.setMostrar(opcion.isMostrar());
                                }
                                pdb.actualizarExistenciaServer(pro.getId(), p.getCantidad(), realm, pro.isPaquete(), opcionesSeleccionadas);
                            } else {
                                List<PaqueteOpcionAux> opciones = new ArrayList<>();
                                pdb.actualizarExistenciaServer(pro.getId(), p.getCantidad(), realm, pro.isPaquete(), opciones);
                            }
                        }
                    }


                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    private void abonar() {
        Utilerias ut = new Utilerias();
        VentasDetalleTicket v = r.getVentas().get(0);
        if (v.getSaldo() > 0) {
            CajaDTOLocal caja = ut.obtenerCajaActual(this, this, realm);

            if (caja != null) {
                //mostramos la pantalla para pedir datos de abonar
                mostrarAbonar();
            } else {
                mandarMensaje("Necesitas abrir una caja desde el punto de venta para poder abonar a un pedido");
            }
        } else {
            mandarMensaje("El pedido no tiene saldo para abonar");
        }
    }

    private void mostrarAbonar() {
        setContentView(R.layout.abonar);
        VentasDetalleTicket v = r.getVentas().get(0);
        TextView txtTotalPago = (TextView) findViewById(R.id.txtTotalPago);
        txtFechaPago = (EditText) findViewById(R.id.txtFechaPago);
        txtFechaPago.setOnClickListener(this);
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        String fecha = sd.format(new Date());
        txtFechaPago.setText(fecha);
        txtTotalPago.setText("Adeudo: " + v.getSaldo());
        restante = v.getSaldo();

    }

    public void btnPedidoAbonarClick(View view) {
        RecibirAbonoDTO pr = new RecibirAbonoDTO();
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);


        EditText txtFechaPag = (EditText) findViewById(R.id.txtFechaPago);
        EditText txtFactura = (EditText) findViewById(R.id.txtFactura);
        EditText txtComentario = (EditText) findViewById(R.id.txtComentario);
        EditText txtTarjetaPedido = (EditText) findViewById(R.id.txtTarjetaPedido);
        EditText txtEfectivoPedido = (EditText) findViewById(R.id.txtEfectivoPedido);

        float efectivo = 0;
        try {
            efectivo = Float.parseFloat(txtEfectivoPedido.getText().toString());
        } catch (Exception ex) {

        }

        float tarjeta = 0;
        try {
            tarjeta = Float.parseFloat(txtTarjetaPedido.getText().toString());
        } catch (Exception ex) {

        }


        if (efectivo + tarjeta <= 0) {
            mandarMensaje("Debe proporcionar un valor válido para el abono");
        } else {
            BigDecimal bd = new BigDecimal(restante).setScale(2, RoundingMode.HALF_DOWN);
            restante = bd.doubleValue();
            double juntos = efectivo + tarjeta;
            BigDecimal bd2 = new BigDecimal(juntos).setScale(2, RoundingMode.HALF_DOWN);
            juntos = bd2.doubleValue();
            if (juntos > restante) {
                mandarMensaje("El monto  a abonar: " + juntos + " no puede ser mayor al adeudo: " + restante);
            } else {

                try {
                    Utilerias ut = new Utilerias();
                    pr.setAndroidId(ut.obtenerSerial(this, this));
                    UsuariosDB udb = new UsuariosDB();
                    int idUT = udb.obtenerIdUTUsuario(Integer.parseInt(ut.obtenerValor("idUsuario", this)),
                            Integer.parseInt(ut.obtenerValor("idTienda", this)), realm).getIdUT();
                    pr.setIdUT(idUT);
                    pr.setIdUsuario(Integer.parseInt(ut.obtenerValor("idUsuario", this)));
                    pr.setFechaPago(txtFechaPag.getText().toString() + " 00:00:00");
                    pr.setEfectivo(efectivo);
                    pr.setTarjeta(tarjeta);
                    pr.setFolioFactur(txtFactura.getText().toString());
                    pr.setId(idTicket);
                    CajaDTOLocal caja = ut.obtenerCajaActual(this, this, realm);
                    pr.setIdCaja(caja.getIdCajaServer());
                    Call<RecibirAbonoDTO> call = apiInterface.recibirAbono(pr);
                    RecibirAbonoService ls = new RecibirAbonoService(call);
                    RecibirAbonoDTO res = ls.execute().get();
                    if (res == null) {
                        mandarMensaje("No pudimos abonar al pedido");
                    } else {
                        if (!res.isExito()) {
                            mandarMensaje("No se pudo realizar el abono, por favor intente de nuevo");
                        } else {
                            mandarMensaje("Se realizó con éxito el abono ");
                            r = consultarDetalle(tipo, folioConsultar);
                            AsyncTask.execute(new Runnable() {
                                @Override
                                public void run() {
                                    reimprimirTicketActual();
                                }
                            });
                            mostrarReportes();
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private void editarPedido() {
        Utilerias ut = new Utilerias();
        ut.guardarValor("ventaVengo", "reportes", this);
        ut.guardarValor("folioConsultar", "" + folioConsultar, this);
        ut.guardarValor("idTicketPedido", "" + idTicket, this);

        ut.guardarValor("mostrarPedidos","SI",this);
        ut.validaIrPuntoVenta(this, this, realm, ut.obtenerPermisosUsuario(this), "reportes");
    }

    private void consultarReporte(String tipo, String fechaIni, String fechaFin) {
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        ReporteDTO pr = new ReporteDTO();

        try {
            Utilerias ut = new Utilerias();
            pr.setIdAndroid(ut.obtenerSerial(this, this));
            UsuariosDB udb = new UsuariosDB();

            int idUT = udb.obtenerIdUTUsuario(Integer.parseInt(ut.obtenerValor("idUsuario", this)),
                    Integer.parseInt(ut.obtenerValor("idTienda", this)), realm).getIdUT();

            pr.setIdUT(idUT);
            pr.setFI(fechaIni);
            pr.setFF(fechaFin);
            pr.setReporte(tipo);
            Gson gson = new Gson();
            String json = gson.toJson(pr);
            Call<ReporteDTO> call = apiInterface.consultarReporte(pr);
            ReporteServiceAsync ls = new ReporteServiceAsync(call);
            ls.execute();

        } catch (Exception ex) {
            mandarMensaje(ex.getMessage());
        }

    }


    private ReporteTicketDetalleDTO consultarDetalle(String tipo, String folio) {
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        ReporteTicketDetalleDTO pr = new ReporteTicketDetalleDTO();

        try {
            Utilerias ut = new Utilerias();
            pr.setIdAndroid(ut.obtenerSerial(this, this));
            UsuariosDB udb = new UsuariosDB();

            int idUT = udb.obtenerIdUTUsuario(Integer.parseInt(ut.obtenerValor("idUsuario", this)),
                    Integer.parseInt(ut.obtenerValor("idTienda", this)), realm).getIdUT();

            pr.setIdUT("" + idUT);

            pr.setReporte(tipo);

            pr.setFolio(folio);
            Gson gson = new Gson();
            String json = gson.toJson(pr);
            Call<ReporteTicketDetalleDTO> call = apiInterface.consultaDetalleTicket(pr);
            DetalleTicketService ls = new DetalleTicketService(call);

            ReporteTicketDetalleDTO res = ls.execute().get();
            if (res == null) {
                mandarMensaje("Algo salió mal consultando el detalle , por favor intente de nuevo");
            } else {
                return res;
            }
        } catch (Exception ex) {
            mandarMensaje(ex.getMessage());
        }
        return null;

    }

    private void cerrarRealmN(Realm realm) {
        if (realm != null && !realm.isClosed()) {
            realm.close();
        }
    }

    public void btnImprimirReporteClick(View view) {
        imprimirReporte();
    }

    private void imprimirReporte() {
        String tipoImpresora = "GENE";
        if (datos != null && datos.size() > 0) {
            if (isBluetoothEnabled()) {
                Utilerias ut = new Utilerias();
                String nombreImpresora = ut.obtenerValor("nombreImpresora", this);
                if (nombreImpresora != null) {
                    BluetoothDevice device = obtenerImpresora(nombreImpresora);
                    if (device != null) {

                        UtileriasImpresion uim = new UtileriasImpresion();
                        uim.imprimirTicket(("REPORTE DE " + tipo.toUpperCase() + " TOTALES \n"),
                                device, tipoImpresora, 200);
                        uim.imprimirTicket(("Folio     Importe     Saldo\n"),
                                device, tipoImpresora, 200);

                        for (ReporteDetalleDTO r : datos) {
                            String row = ut.formatDoubleTicket(Double.parseDouble(r.getFolio()), 7, " ") +
                                    ut.formatDoubleTicket(r.getTotal(), 10, "$") +
                                    ut.formatDoubleTicket(r.getSaldo(), 10, "$") +
                                    "\n";
                            uim.imprimirTicket(row,
                                    device, tipoImpresora, 200);
                        }
                        String totales = "Total: " + ut.formatDoubleTicket(total, 10, "$") +
                                ut.formatDoubleTicket(saldo, 10, "$") +
                                "\n\n\n\n";
                        uim.imprimirTicket(totales,
                                device, tipoImpresora, 200);
                    } else {
                        mandarMensaje("No se encontró la impresora configurada");
                    }
                } else {
                    mandarMensaje("Por favor configure primero una impresora");
                }
            } else {
                mandarMensaje("El Bluetooth esta apagado, por favor verifique");
            }
        } else {
            mandarMensaje("No hay información para imprimir");
        }
    }


    private BluetoothDevice obtenerImpresora(String nombreImpresora) {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();


        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {

                // RPP300 is the name of the bluetooth printer device
                // we got this name from the list of paired devices

                if (device.getName().equals(nombreImpresora)) {

                    int bondstate = device.getBondState();
                    if (bondstate == BluetoothDevice.BOND_NONE) {
                        if (device.createBond()) {
                            return device;
                        } else {
                            return null;
                        }
                    } else {
                        return device;
                    }
                }
            }
        }

        return null;
    }

    public boolean isBluetoothEnabled() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return mBluetoothAdapter.isEnabled();

    }

    public void mandarMensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }


    class ReporteServiceAsync extends AsyncTask<Void, Void, ReporteDTO> {

        Call<ReporteDTO> call;


        public ReporteServiceAsync(Call<ReporteDTO> call) {
            this.call = call;


        }

        @Override
        protected ReporteDTO doInBackground(Void... params) {
            try {
                Gson gson = new Gson();
                // Response<ReporteDTO> valor = call.execute();
                ReporteDTO res = call.execute().body();


                if (res == null) {
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            mandarMensaje("Algo salió mal consultado el detalle, por favor intente de nuevo");
                        }
                    });

                } else {
                    if (res.getCotizaciones() == null && res.getPedidos() == null && res.getVentas() == null) {
                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                mandarMensaje("No se obtuvieron resultados");
                            }
                        });

                    } else {
                        if (res.getVentas() != null) {
                            if (res.getVentas().size() == 0) {
                                activity.runOnUiThread(new Runnable() {
                                    public void run() {
                                        mandarMensaje("No hay ventas  registradas");
                                    }
                                });
                            } else {
                                datosAux = res.getVentas();
                                datos = res.getVentas();
                                activity.runOnUiThread(new Runnable() {
                                    public void run() {
                                        mostrarReporte();
                                    }
                                });
                            }
                        } else if (res.getCotizaciones() != null) {
                            if (res.getCotizaciones().size() == 0) {
                                activity.runOnUiThread(new Runnable() {
                                    public void run() {
                                        mandarMensaje("No hay cotizaciones registradas");
                                    }
                                });
                            } else {
                                datos = res.getCotizaciones();
                                datosAux = res.getCotizaciones();
                                activity.runOnUiThread(new Runnable() {
                                    public void run() {
                                        mostrarReporte();
                                    }
                                });
                            }
                        } else if (res.getPedidos() != null) {
                            if (res.getPedidos().size() == 0) {
                                activity.runOnUiThread(new Runnable() {
                                    public void run() {
                                        mandarMensaje("No hay pedidos registrados");
                                    }
                                });
                            } else {
                                datos = res.getPedidos();
                                datosAux = res.getPedidos();
                                activity.runOnUiThread(new Runnable() {
                                    public void run() {
                                        mostrarReporte();
                                    }
                                });
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
        protected void onPostExecute(ReporteDTO res) {

            if (pb != null) {
                pb.setVisibility(View.INVISIBLE);
            }


        }


    }
}


