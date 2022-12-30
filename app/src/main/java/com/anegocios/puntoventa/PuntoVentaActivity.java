package com.anegocios.puntoventa;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.anegocios.puntoventa.adapters.ClientesAdapter;
import com.anegocios.puntoventa.adapters.ClientesAdapterGris;
import com.anegocios.puntoventa.adapters.GruposGrandeAdapter;
import com.anegocios.puntoventa.adapters.ProductosAgregadosAdapter;
import com.anegocios.puntoventa.adapters.ProductosVentaAdapter;
import com.anegocios.puntoventa.adapters.SimpleAdapter;
import com.anegocios.puntoventa.bdlocal.CajaDTOLocal;
import com.anegocios.puntoventa.bdlocal.ProductosXYDTOLocal;
import com.anegocios.puntoventa.bdlocal.TicketDTOLocal;
import com.anegocios.puntoventa.bdlocal.TicketProductoDTOLocal;
import com.anegocios.puntoventa.database.CajasDB;
import com.anegocios.puntoventa.database.ClientesDB;
import com.anegocios.puntoventa.database.GrupoDB;
import com.anegocios.puntoventa.database.ProductosDB;
import com.anegocios.puntoventa.database.UsuariosDB;
import com.anegocios.puntoventa.dtosauxiliares.ClienteXYDTOAux;
import com.anegocios.puntoventa.dtosauxiliares.GruposVRXYAux;
import com.anegocios.puntoventa.dtosauxiliares.PaqueteOpcionAux;
import com.anegocios.puntoventa.dtosauxiliares.ProductosXYDTOAux;
import com.anegocios.puntoventa.jsons.LoginDTO;
import com.anegocios.puntoventa.jsons.LoginResponseDTO;
import com.anegocios.puntoventa.jsons.OpcionesPaquete;
import com.anegocios.puntoventa.jsons.PaquetesProducto;
import com.anegocios.puntoventa.jsons.ProductosXYDTO;
import com.anegocios.puntoventa.jsons.ReporteTicketDetalleDTO;
import com.anegocios.puntoventa.jsons.Usuario;
import com.anegocios.puntoventa.jsons.VentasVentaTicketDTO;
import com.anegocios.puntoventa.servicios.APIClient;
import com.anegocios.puntoventa.servicios.APIInterface;
import com.anegocios.puntoventa.servicios.DetalleTicketService;
import com.anegocios.puntoventa.servicios.LoginCorteService;
import com.anegocios.puntoventa.utils.ActualizacionCatalogosUtil;
import com.anegocios.puntoventa.utils.Utilerias;
import com.anegocios.puntoventa.utils.UtileriasImpresion;
import com.anegocios.puntoventa.utils.UtileriasSincronizacion;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import io.realm.Realm;
import retrofit2.Call;

public class PuntoVentaActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    ProgressBar progress_bar;
    int totalProductos;
    int totalBusqueda;
    String correo;
    double tarjeta;
    double efectivo;
    Realm realm;
    boolean impresoraVerificada;
    String gruposSeleccionado;
    List<GruposVRXYAux> grupos;
    List<ProductosXYDTOAux> productosDisponibles;
    private List<ProductosXYDTOAux> productosAgregados;
    private ProductosXYDTOAux productoPaquetes;
    List<ClienteXYDTOAux> clientes;
    Context context;
    private double montoTotal;
    private double subtotal;
    private double iva;
    double propinaTotal;
    double descuentoTotal;
    private int cantidadVentas = 8;
    private int cantidadVentasG = 15;
    long idTiendaGlobal;
    String vengode;
    int idTicketGenerado;
    ListView gvProductosDisponibles;

    double efectivoCalculadoCor;
    double efectivoContadoCor;
    double tarjetacalculadoCor;
    double tarjetaContadoCor;


    private List<ProductosXYDTOAux> productosBuscar;

    private ProductosXYDTOAux prodAjuste;
    private int posicionAjuste;
    private String pantalla;
    private Usuario permisos;
    private double totalArticulos;
    private Activity activity;

    private boolean entregado;
    private boolean enviarWhatsApp;
    private double cambio;
    boolean esEditarPedido;
    boolean estatusImpresora;
    String idPedidoEditar;
    String folioPedidoEditar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {

            impresoraVerificada = false;
            productosAgregados = new ArrayList<>();
            Utilerias ut = new Utilerias();
            ut.guardarValor("hizoResenia", "SI", this);

            realm = ut.obtenerInstanciaBD(this);
            String ventaVengo = ut.obtenerValor("ventaVengo", this);

            ProductosDB pdb = new ProductosDB();
            List<ProductosXYDTOAux> total = pdb.obtenerProductosCompletos(Integer.parseInt(ut.obtenerValor("idTienda", this)), realm);
            if (total != null) {
                totalProductos = total.size();
            } else {
                totalProductos = 0;
            }
            if (ut.verificaConexion(this)) {
                ActualizacionCatalogosUtil actCat = new ActualizacionCatalogosUtil();
                actCat.consultarGrupos(this, this);
            }
            if (ventaVengo != null && ventaVengo.equals("reportes")) {
                //este camino es en caso de que sea un pedido que se esta editando
                esEditarPedido = true;
                cargarPedidoEditar(ut.obtenerValor("folioConsultar", this), ut.obtenerValor("idTicketPedido", this));
            } else {
                esEditarPedido = false;
                ut.guardarValor("comentarios", "", this);
                ut.guardarValor("descuentoPorcentaje", "", this);
                ut.guardarValor("descuentoEfectivo", "", this);
                ut.guardarValor("propinaPorcentaje", "", this);
                ut.guardarValor("propinaEfectivo", "", this);
                ut.guardarValor("idClienteAsignado", "", this);
                ut.guardarValor("correoAsignar", "", this);
                mostrarPuntoVenta();
            }
        } catch (Exception ex) {
            Utilerias.log(this, "Error: " + ex.getMessage() + " " + ex.getStackTrace(), ex);
        }

    }

    private boolean verificarImpresora() {
        boolean impresoraLista = false;
        Utilerias ut = new Utilerias();
        UtileriasImpresion uti = new UtileriasImpresion();
        if (ut.isBluetoothEnabled()) {

            String nombreImpresora = ut.obtenerValor("nombreImpresora", this);
            if (nombreImpresora != null) {

                BluetoothDevice device = ut.obtenerImpresora(nombreImpresora);
                if (device != null) {
                    // String error = uti.imprimirTicket("", device, "GENE", 1);
                    // if (error.equals("")) {
                    impresoraLista = true;
                    //}
                }
            }
        }
        return impresoraLista;
    }

    private void cargarPedidoEditar(String folio, String idTicketPedidoEditarS) {

        folioPedidoEditar = folio;
        idPedidoEditar = idTicketPedidoEditarS;

        ReporteTicketDetalleDTO r = consultarDetalle("pedidos", folio);
        // de aqui ya tengo los productos... creo, ahora hay que ver como se meten de nuevo y quitar todos los botones
        //pones los artidulos en productos agregados
        Utilerias ut = new Utilerias();
        ProductosDB pdb = new ProductosDB();

        if (r.getVentas() != null && r.getVentas().size() > 0) {
            List<VentasVentaTicketDTO> productos = r.getVentas().get(0).getVentas();
            ut.guardarValor("idClienteAsignado", "" + r.getVentas().get(0).getIdCliente(), this);
            ut.guardarValor("tipoClienteAsignado", "S", this);
            ut.guardarValor("descuentoEfectivo", "" + r.getVentas().get(0).getDescuento(), this);
            ut.guardarValor("propinaEfectivo", "" + r.getVentas().get(0).getPropina(), this);
            for (VentasVentaTicketDTO p : productos) {
                //hay que buscarlo y lo agregamos
                ProductosXYDTO prodS = pdb.obtenerproductoServer(p.getIdProducto(), realm);
                if (prodS != null) {

                    ProductosXYDTOAux prod = new ProductosXYDTOAux(prodS, "S");
                    prod.setCantidad(p.getCantidad());
                    if (prod.isPaquete()) {
                        agregarSolitoPaqueteEdicionPedido(prod, p.getOpcionesPkt().get(0).getId());
                    } else {
                        agregarProducto(prod, true);
                    }
                }
            }
        }


        mostrarPuntoVenta();

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
                mandarMensaje("Algo salió mal consultando , por favor intente de nuevo");
            } else {
                return res;
            }
        } catch (Exception ex) {
            mandarMensaje(ex.getMessage());
        }
        return null;

    }

    private void mostrarBotonesPedido() {
        Button btnCobrar = (Button) findViewById(R.id.btnCobrar);
        if (btnCobrar != null) {
            btnCobrar.setText("Guardar Pedido");
        }
        ImageView btnMenuDerecho = (ImageView) findViewById(R.id.btnMenuDerecho);
        if (btnMenuDerecho != null) {
            btnMenuDerecho.setVisibility(View.GONE);
        }

        ImageButton btnPedidoCarrito = (ImageButton) findViewById(R.id.btnPedidoCarrito);
        if (btnPedidoCarrito != null) {
            btnPedidoCarrito.setVisibility(View.GONE);
        }

        ImageButton btnCotizarPedido = (ImageButton) findViewById(R.id.btnCotizarPedido);
        if (btnCotizarPedido != null) {
            btnCotizarPedido.setVisibility(View.GONE);
        }

        ImageButton btnPagarCarrito = (ImageButton) findViewById(R.id.btnPagarCarrito);
        if (btnPagarCarrito != null) {
            btnPagarCarrito.setVisibility(View.GONE);
        }

        Button btnGuardarPedido = (Button) findViewById(R.id.btnGuardarPedido);
        if (btnGuardarPedido != null) {
            btnGuardarPedido.setVisibility(View.VISIBLE);
        }


    }

    private void ocultarBotonesPedido() {
        Button btnCobrar = (Button) findViewById(R.id.btnCobrar);
        if (btnCobrar != null) {
            btnCobrar.setText("Cobrar");
        }
        ImageView btnMenuDerecho = (ImageView) findViewById(R.id.btnMenuDerecho);
        if (btnMenuDerecho != null) {
            btnMenuDerecho.setVisibility(View.VISIBLE);
        }

        ImageButton btnPedidoCarrito = (ImageButton) findViewById(R.id.btnPedidoCarrito);
        if (btnPedidoCarrito != null) {
            btnPedidoCarrito.setVisibility(View.VISIBLE);
        }

        ImageButton btnCotizarPedido = (ImageButton) findViewById(R.id.btnCotizarPedido);
        if (btnCotizarPedido != null) {
            btnCotizarPedido.setVisibility(View.VISIBLE);
        }

        ImageButton btnPagarCarrito = (ImageButton) findViewById(R.id.btnPagarCarrito);
        if (btnPagarCarrito != null) {
            btnPagarCarrito.setVisibility(View.VISIBLE);
        }

        Button btnGuardarPedido = (Button) findViewById(R.id.btnGuardarPedido);
        if (btnGuardarPedido != null) {
            btnGuardarPedido.setVisibility(View.GONE);
        }


    }


    public void btnGuardarPedidoClick(View view) {
        guardarPedido();
    }

    private void guardarPedido() {

        generarTicket("", "0", "0", "0",
                "pedido", false, false);
    }

    private void mostrarPuntoVenta() {
        setContentView(R.layout.puntoventagrande);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        calculaMontosLista();
        actualizarBotonCarrito();
        if (!impresoraVerificada) {
            impresoraVerificada = true;
            estatusImpresora = verificarImpresora();
        }
        ImageButton botonImpresora = (ImageButton) findViewById(R.id.botonImpresora);
        final EditText txtCodigoBarras = findViewById(R.id.txtCodigoBarras);
        txtCodigoBarras.setText("");
        txtCodigoBarras.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String codigoBarras = txtCodigoBarras.getText().toString();
                if(!codigoBarras.equals("")) {
                    int cont=0;
                    boolean encontrado=false;
                    for(ProductosXYDTOAux p : productosDisponibles)
                    {
                        if(p.getCodigoBarras()!= null &&  p.getCodigoBarras().trim().equals(txtCodigoBarras.getText().toString().trim()))
                        {
                            agregarConAnimacion(cont);
                            encontrado=true;
                            break;
                        }
                        cont++;
                    }
                    if(!encontrado)
                    {
                        mandarMensaje("No se encontró el producto con el código "+txtCodigoBarras.getText().toString().trim());
                        txtCodigoBarras.setText("");
                        txtCodigoBarras.requestFocus();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        txtCodigoBarras.addTextChangedListener(textWatcher);
        txtCodigoBarras.setFocusableInTouchMode(true);
        if (estatusImpresora) {
            botonImpresora.setImageResource(R.drawable.bluetoothokazu);
        } else {
            botonImpresora.setImageResource(R.drawable.bluetoothnookazu);
        }

        if (esEditarPedido) {
            mostrarBotonesPedido();
        } else {
            ocultarBotonesPedido();
        }


        actualizarListaAgregados();


        pantalla = "puntoventa";
        vengode = "puntoventa";
        actualizarBotonCarrito();
        iniciarMenuContextual();
        if (gruposSeleccionado == null) {
            gruposSeleccionado = "0";
        }
        context = this;
        activity = this;
        Utilerias ut = new Utilerias();
        idTiendaGlobal = Long.parseLong(ut.obtenerValor("idTienda", context));

        //    UtileriasSincronizacion uts = new UtileriasSincronizacion();
        //   uts.sincronizarTodo(context, activity, realm, idTiendaGlobal);


        permisos = ut.obtenerPermisosUsuario(this);

        ProductosDB pdb = new ProductosDB();

        llenarComboGrupos();

        if (productosDisponibles == null || productosDisponibles.size() == 0) {

            productosDisponibles = pdb.obtenerProductosCompletos(
                    Integer.parseInt(ut.obtenerValor("idTienda", this)), realm);
        }
         gvProductosDisponibles = (ListView) findViewById(R.id.gvProductosDisponibles);
        if (productosDisponibles != null) {

            ProductosVentaAdapter adapter = new ProductosVentaAdapter(productosDisponibles, this,"G");
            gvProductosDisponibles.setAdapter(adapter);
            totalBusqueda = productosDisponibles.size();

        }
        gvProductosDisponibles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


            }
        });

        //actualizarTotalesProductos();
    }

    private void agregarConAnimacion(int position)
    {

        EditText txtCodigoBarras = findViewById(R.id.txtCodigoBarras);

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.movegrande);
        Animation blinkanimation = AnimationUtils.loadAnimation(context, R.anim.blinkticketbutton);
        Button btnCarrito = (Button) findViewById(R.id.btnCarrito);
               /* int posicionVisible = gvProductosDisponibles.getFirstVisiblePosition();
                View hijoNoNulo = gvProductosDisponibles.getChildAt(position - posicionVisible);
                */
        gvProductosDisponibles.startAnimation(animation);
        btnCarrito.startAnimation(blinkanimation);

        agregarProducto(productosDisponibles.get(position), false);
        actualizarListaAgregados();
        txtCodigoBarras.setText("");
        txtCodigoBarras.requestFocus();
    }


    private void actualizarListaAgregados() {
        ListView gvProductosAgregados = (ListView) findViewById(R.id.gvProductosAgregados);
        mostrarMontosTotales(montoTotal, subtotal, iva, propinaTotal, descuentoTotal);
        ProductosAgregadosAdapter adapter2 = new ProductosAgregadosAdapter(productosAgregados, this, "G");
        if(gvProductosAgregados!=null) {
            gvProductosAgregados.setAdapter(adapter2);
            gvProductosAgregados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    posicionAjuste = position;
                    prodAjuste = productosAgregados.get(position);
                    mostrarAjuste(prodAjuste);


                }
            });
        }
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.contextmenu, menu);
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.btnDescuentos:
                mostrarExtras();
                return true;
            case R.id.btnCorte:
                mostrarCorte();
                return true;
            case R.id.btnReimprimir:
                reimprimir();
                return true;
        }

        return super.onContextItemSelected(item);
    }

    public void reimprimir() {

        Utilerias ut = new Utilerias();
        try {

            if (idTicketGenerado > 0) {

                ut.imprimirTicket(this, this, idTiendaGlobal,idTicketGenerado);
            } else {
                mandarMensaje("No hay ningun ticket para reimprimir");
            }
        } catch (Exception ex) {
            Utilerias.log(this, "Error al reimprimir", ex);
        }


    }

    public void btnAceptarAjusteClick(View view) {
        try {
            EditText txtCantidadAjuste = (EditText) findViewById(R.id.txtCantidadAjuste);
            EditText txtPrecioAjuste = (EditText) findViewById(R.id.txtPrecioAjuste);
            if (txtCantidadAjuste.getText().toString().equals("") ||
                    txtPrecioAjuste.getText().toString().equals("")) {
                mandarMensaje("Se debe de ingresar un monto y una cantidad válida");
            } else {
                ProductosXYDTOAux prod = productosAgregados.get(posicionAjuste);
                double cantidadAsignada = Double.parseDouble(txtCantidadAjuste.getText().toString());
                double precioAsignado = Double.parseDouble(txtPrecioAjuste.getText().toString());
                prod.setCantidad(cantidadAsignada);
                if (prod.getCantidadMayoreo() > 0) {
                    //quiere decir que lo tenemos que evaluar
                    if (cantidadAsignada >= prod.getCantidadMayoreo()) {
                        prod.setPrecioVenta(prod.getPrecioMayoreo());
                    }
                } else {

                    prod.setPrecioVenta(precioAsignado);
                }


                mostrarPuntoVenta();


            }
        } catch (Exception ex) {
            Utilerias.log(this, "Error: " + ex.getMessage() + " " + ex.getStackTrace(), ex);
        }
    }


    public void btnCodCamaraClick(View view) {

        new IntentIntegrator(PuntoVentaActivity.this).initiateScan();
    }

    public void btnCorteClick(View view) {
        Utilerias ut = new Utilerias();
        Usuario u = ut.obtenerPermisosUsuario(this);
        if (u.getCortarCaja()) {

            mostrarCorte();
        } else {
            mandarMensaje("No tienes permiso para hacer el corte");
        }
    }

    private void mostrarCorte() {
        setContentView(R.layout.corte);
        pantalla = "corte";
    }


    private boolean validarUsuario(String usuario, String password) {
        boolean usuarioValido = false;

        Utilerias ut = new Utilerias();
        if (usuario.trim().equals("")
                || password.trim().equals("")
        ) {

            mandarMensaje("Todos los campos son obligatorios, por favor verifique");
        } else {
            if (ut.obtenerModoAplicacion(this)) {


                if (ut.verificaConexion(this)) {
                    LoginDTO l = new LoginDTO();
                    l.setBrand(Build.BRAND);
                    l.setIdAndroid(ut.obtenerSerial(this, this));
                    l.setModel(Build.MODEL);
                    l.setPass(password);
                    l.setProduct(Build.PRODUCT);
                    l.setUsername(usuario);
                    //login(l);
                    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
                    Call<LoginResponseDTO> call = apiInterface.login(l);

                    LoginCorteService ls = new LoginCorteService(call);
                    try {
                        final LoginResponseDTO res = ls.execute().get();
                        // final LoginResponseDTO res = new LoginResponseDTO();
                        if (res == null) {
                            mandarMensaje("Ocurrió un problema realizando el login, por favor intente de nuevo");
                        } else {


                            if (res.getAcceso()) {

                                //buscamos el usuario
                                for (Usuario u : res.getUsuarios()
                                ) {
                                    if (u.getUsername().equals(usuario)) {
                                        if (u.getCortarCaja()) {
                                            usuarioValido = true;
                                        }
                                    }
                                }

                                if (!usuarioValido) {
                                    mandarMensaje("No tienes permiso para hacer corte de caja");
                                }


                            } else {

                                mandarMensaje(res.getMsg());
                            }
                        }
                    } catch (Exception ex) {
                        mandarMensaje(ex.getMessage());
                        Utilerias.log(this, "Error: " + ex.getMessage() + " " + ex.getStackTrace(), ex);
                    }
                } else {
                    mandarMensaje("La aplicación esta en modo online y no se tiene ninguna red activa");

                }
            } else {
                UsuariosDB udb = new UsuariosDB();

                List<Usuario> usuarios = udb.obtenerListaUsuarios(realm);

                Usuario encontrado = null;
                for (Usuario u : usuarios) {
                    if (u.getUsername().equals(usuario)
                            && u.getPass().equals(ut.encryptPassword(password, u.getSalt()))) {
                        encontrado = u;
                        break;
                    }
                }
                if (encontrado != null) {
                    if (encontrado.getCortarCaja()) {
                        usuarioValido = true;
                    } else {
                        mandarMensaje("No tienes permiso para hacer corte de caja");
                    }
                }
            }

        }
        return usuarioValido;
    }

    public void btnHacerCorteClick(View view) {
        try {
            EditText txtUsuarioCorte = (EditText) findViewById(R.id.txtUsuarioCorte);
            EditText txtPasswordCorte = (EditText) findViewById(R.id.txtPasswordCorte);
            EditText txtEfectivoCorte = (EditText) findViewById(R.id.txtEfectivoCorte);
            EditText txtTarjetaCorte = (EditText) findViewById(R.id.txtTarjetaCorte);

            if (validarUsuario(txtUsuarioCorte.getText().toString(), txtPasswordCorte.getText().toString())) {
                //si llegamos aqui es porque usuario y pass son correctos
                //ahora hacemos el corte
                Utilerias ut = new Utilerias();
                CajasDB cdb = new CajasDB();
                CajaDTOLocal cc = new CajaDTOLocal();
                cc.setEfectivoContado(ut.parseDouble(txtEfectivoCorte.getText().toString()));
                cc.setTarjetaContado(ut.parseDouble(txtTarjetaCorte.getText().toString()));
                cc.setFechaFin(ut.obtenerFechaActualFormateada());
                double efectivoCalculado = 0;
                double tarjetaCalculado = 0;

                CajaDTOLocal cajaActual = ut.obtenerCajaActual(this, this, realm);

                if (cajaActual != null) {
                    List<TicketDTOLocal> tickets = cdb.obtenerTicketsCajaHacerCalculos(cajaActual
                            .getIdCaja(), realm);

                    for (TicketDTOLocal t : tickets
                    ) {
                        efectivoCalculado += (t.getEfectivo() - t.getCambio());
                        tarjetaCalculado += t.getTarjeta();
                    }
                    efectivoCalculado += cajaActual.getMontoInicial();
                    cc.setEfectivoCalculado(efectivoCalculado);
                    cc.setTarjetaCalculado(tarjetaCalculado);

                    cdb.hacerCorte(Integer.parseInt(ut.obtenerValor("idUsuario", this)),
                            Integer.parseInt(ut.obtenerValor("idTienda", this)), cc, realm);

                    //despues de guardar el corte
                    if (ut.obtenerModoAplicacion(this)) {
                        if (ut.verificaConexion(this)) {
                            UtileriasSincronizacion uts = new UtileriasSincronizacion();
                            uts.sincronizarTodo(this, this, realm, idTiendaGlobal);

                        }
                    }

                    mostrarResultadoCorte(efectivoCalculado, ut.parseDouble(txtEfectivoCorte.getText().toString()),
                            tarjetaCalculado, ut.parseDouble(txtTarjetaCorte.getText().toString()));
                } else {
                    mandarMensaje("No hay una caja a la cual hacerle el corte");
                }

            } else {
                mandarMensaje("El usuario y password no son válidos");
            }
        } catch (Exception ex) {
            Utilerias.log(this, "Error: " + ex.getMessage() + " " + ex.getStackTrace(), ex);
        }


    }

    public void btnAceptarCorteClick(View view) {
        Intent i = new Intent(getApplicationContext(), ProductosActivity.class);
        startActivity(i);
    }

    private void mostrarResultadoCorte(double efectivoCalculado, double efectivoContado
            , double tarjetacalculado, double tarjetaContado) {

        efectivoCalculadoCor= efectivoCalculado;
        efectivoContadoCor = efectivoContado;
        tarjetacalculadoCor = tarjetacalculado;
        tarjetaContadoCor = tarjetaContado;
        setContentView(R.layout.resultadocorte);
        TextView txtResultadoEfectivo = (TextView) findViewById(R.id.txtResultadoEfectivo);
        TextView txtResultadoTarjeta = (TextView) findViewById(R.id.txtResultadoTarjeta);
        if (efectivoCalculado == efectivoContado) {
            txtResultadoEfectivo.setText("¡Felicidades la diferencia fue de : $0 en pago con EFECTIVO! ");
            txtResultadoEfectivo.setTextColor(ContextCompat.getColor(this, R.color.textoCeros));
        } else if (efectivoContado > efectivoCalculado) {
            double diferencia = efectivoContado - efectivoCalculado;
            txtResultadoEfectivo.setText("¡Sobraron : $" + diferencia + " en EFECTIVO! ");
            txtResultadoEfectivo.setTextColor(ContextCompat.getColor(this, R.color.textoSobrante));
        } else if (efectivoContado < efectivoCalculado) {
            double diferencia = efectivoContado - efectivoCalculado;
            txtResultadoEfectivo.setText("¡Faltaron : $" + diferencia + " en EFECTIVO! ");
            txtResultadoEfectivo.setTextColor(ContextCompat.getColor(this, R.color.textoFaltante));
        }


        if (tarjetacalculado == tarjetaContado) {
            txtResultadoTarjeta.setText("¡Felicidades la diferencia fue de : $0 en pago con TARJETA! ");
            txtResultadoTarjeta.setTextColor(ContextCompat.getColor(this, R.color.textoCeros));
        } else if (tarjetaContado > tarjetacalculado) {
            double diferencia = tarjetaContado - tarjetacalculado;
            txtResultadoTarjeta.setText("¡Sobraron : $" + diferencia + " en TARJETA! ");
            txtResultadoTarjeta.setTextColor(ContextCompat.getColor(this, R.color.textoSobrante));
        } else if (tarjetaContado < tarjetacalculado) {
            double diferencia = tarjetaContado - tarjetacalculado;
            txtResultadoTarjeta.setText("¡Faltaron : $" + diferencia + " en TARJETA! ");
            txtResultadoTarjeta.setTextColor(ContextCompat.getColor(this, R.color.textoFaltante));
        }


    }

    public void btnImprimirCorteClick(View view) {
        Utilerias ut = new Utilerias();
        ut.imprimirCorte(this, efectivoContadoCor, efectivoCalculadoCor
                ,tarjetaContadoCor,tarjetacalculadoCor);
    }

    public void mostrarExtras() {
        setContentView(R.layout.extraschico);
        pantalla = "extras";
        EditText txtDescEfectivo = (EditText) findViewById(R.id.txtDescEfectivo);
        EditText txtDescPorcentaje = (EditText) findViewById(R.id.txtDescPorcentaje);
        EditText txtPropinaEfectivo = (EditText) findViewById(R.id.txtPropinaEfectivo);
        EditText txtPropinaPorcentaje = (EditText) findViewById(R.id.txtPropinaPorcentaje);
        Utilerias ut = new Utilerias();

        txtDescEfectivo.setText(ut.obtenerValor("descuentoEfectivo", this));
        txtDescPorcentaje.setText(ut.obtenerValor("descuentoPorcentaje", this));
        txtPropinaEfectivo.setText(ut.obtenerValor("propinaEfectivo", this));
        txtPropinaPorcentaje.setText(ut.obtenerValor("propinaPorcentaje", this));

        if (permisos.getCajaDesc()) {
            txtDescEfectivo.setEnabled(true);
            txtDescPorcentaje.setEnabled(true);
        } else {
            txtDescEfectivo.setEnabled(false);
            txtDescPorcentaje.setEnabled(false);
            txtDescEfectivo.setHint("Sin permisos para descuento");
            txtDescPorcentaje.setHint("Sin permisos para descuento");
        }

        if (permisos.getCajaPropina()) {
            txtPropinaEfectivo.setEnabled(true);
            txtPropinaPorcentaje.setEnabled(true);
        } else {
            txtPropinaEfectivo.setEnabled(false);
            txtPropinaPorcentaje.setEnabled(false);
            txtPropinaEfectivo.setHint("Sin permisos para propina");
            txtPropinaPorcentaje.setHint("Sin permisos para propina");
        }

    }

    public void btnGuardarExtrasClick(View view) {

        EditText txtDescEfectivo = (EditText) findViewById(R.id.txtDescEfectivo);
        EditText txtDescPorcentaje = (EditText) findViewById(R.id.txtDescPorcentaje);
        EditText txtPropinaEfectivo = (EditText) findViewById(R.id.txtPropinaEfectivo);
        EditText txtPropinaPorcentaje = (EditText) findViewById(R.id.txtPropinaPorcentaje);

        Utilerias ut = new Utilerias();

        if (!txtDescEfectivo.getText().toString().trim().equals("")) {
            //tiene valor
            double descEfectivo = ut.parseDouble(txtDescEfectivo.getText().toString());
            if (descEfectivo > 0) {
                ut.guardarValor("descuentoEfectivo", "" + descEfectivo, this);
            } else {
                ut.guardarValor("descuentoEfectivo", "", this);
            }
        }

        if (!txtDescPorcentaje.getText().toString().trim().equals("")) {
            //tiene valor
            double descPorcentaje = ut.parseDouble(txtDescPorcentaje.getText().toString());
            if (descPorcentaje > 0) {
                ut.guardarValor("descuentoPorcentaje", "" + descPorcentaje, this);
            } else {
                ut.guardarValor("descuentoPorcentaje", "", this);
            }
        }


        if (!txtPropinaEfectivo.getText().toString().trim().equals("")) {
            //tiene valor
            double propinaEfectivo = ut.parseDouble(txtPropinaEfectivo.getText().toString());
            if (propinaEfectivo > 0) {
                ut.guardarValor("propinaEfectivo", "" + propinaEfectivo, this);
            } else {
                ut.guardarValor("propinaEfectivo", "", this);
            }
        }

        if (!txtPropinaPorcentaje.getText().toString().trim().equals("")) {
            //tiene valor
            double propinaPorcentaje = ut.parseDouble(txtPropinaPorcentaje.getText().toString());
            if (propinaPorcentaje > 0) {
                ut.guardarValor("propinaPorcentaje", "" + propinaPorcentaje, this);
            } else {
                ut.guardarValor("propinaPorcentaje", "", this);
            }
        }


        mostrarPuntoVenta();


    }


    public void btnDerechoClick(View view) {
        view.showContextMenu();
    }


    private void iniciarMenuContextual() {
        ImageView btnMenuDerecho = (ImageView) findViewById(R.id.btnMenuDerecho);
        registerForContextMenu(btnMenuDerecho);
    }


    private void agregarProducto(ProductosXYDTOAux p, boolean llenandoPedido) {
        try {
            //cuando entra aqui lo agregamos a la lista de productos


            //primero vemos si ya existe, si es asi le sumamos
            if (productosAgregados == null) {
                productosAgregados = new ArrayList<ProductosXYDTOAux>();
            }
            if (p.isPaquete()) {
                //tenemos que saber si alguno de sus paquetes tiene dos o mas opciones
                ProductosDB pdb = new ProductosDB();
                boolean tieneOpcionesAlgunPaquete = false;
                Utilerias ut = new Utilerias();

                List<PaquetesProducto> paquetes = pdb.obtenerPaquetesProductos(p.getId(), realm);

                if (paquetes != null && paquetes.size() > 0) {
                    for (PaquetesProducto pa : paquetes
                    ) {

                        List<OpcionesPaquete> opciones = pdb.obtenerOpcionesPaquete(pa.getId(), realm);
                        //lo pusimos en cero porque hay paquetes que solo tienen una opcion: las fresas
                        //lo regresamos a 1 por lo de los botiquines que salian porque son paquetes, pero no tienen opciones que elegir
                        //ll debe agregar directo al ticket
                        if (opciones != null && opciones.size() > 1) {
                            tieneOpcionesAlgunPaquete = true;
                        }
                    }
                }
                if (tieneOpcionesAlgunPaquete) {
                    //se tiene que mostrar la pantalla de paquetes
                    productoPaquetes = p;
                    if (llenandoPedido) {
                        agregarSolitoPaquete(p);
                    } else {
                        mostrarPantallaPaquetes();
                    }
                } else {
                    //solo se agrega
                    agregarSolitoPaquete(p);
                }

                calculaMontosLista();

            } else {
                agregarSolito(p);

            }
        } catch (Exception ex) {
            Utilerias.log(this, "Error: " + ex.getMessage() + " " + ex.getStackTrace(), ex);
        }

    }


    private void mostrarPantallaPaquetes() {
        setContentView(R.layout.paquete);
        ProductosDB pdb = new ProductosDB();
        Utilerias ut = new Utilerias();

        List<PaquetesProducto> paquetes = pdb.obtenerPaquetesProductos(productoPaquetes.getId(), realm);

        LinearLayout linearLayout = findViewById(R.id.laySpinners);
        if (paquetes != null && paquetes.size() > 0) {
            int cont = 2;
            for (PaquetesProducto pa : paquetes
            ) {

                List<OpcionesPaquete> opciones = pdb.obtenerOpcionesPaquete(pa.getId(), realm);
                //lo pusimos en cero porque hay paquetes que solo tienen una opcion: las fresas
                //lo regresamos a 1, pero estamos haciend pruebas, veamos que pasa con las fresas
                if (opciones != null && opciones.size() > 1) {
                    // se tiene que agregar el spinner


                    final Spinner spinner = new Spinner(this);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            100);
                    params.gravity = Gravity.CENTER_HORIZONTAL;
                    spinner.setLayoutParams(params);
                    spinner.setMinimumHeight(100);

                    List<String> opcionesCombo = new ArrayList<String>();
                    for (OpcionesPaquete op : opciones
                    ) {
                        opcionesCombo.add(op.getDescripcion());
                    }
                    ArrayAdapter arrayAdapter = new SimpleAdapter(opcionesCombo, this);
                    spinner.setAdapter(arrayAdapter);
                    if (linearLayout != null) {
                        linearLayout.addView(spinner, cont);
                        cont++;
                    }
                }
            }
        }
    }


    private void agregarSolito(ProductosXYDTOAux p) {
        if (productosAgregados.size() > 0) {
            boolean existe = false;
            for (ProductosXYDTOAux pa : productosAgregados) {
                if (pa.getId() == p.getId()) {
                    //si existe, le sumamos uno a su cantidad
                    pa.setCantidad(pa.getCantidad() + 1);
                    if (pa.getCantidadMayoreo() > 1) {
                        //quiere decir que tenemos que validar la cantidadMayoreo
                        if (pa.getCantidad() >= pa.getCantidadMayoreo()) {
                            //quiere decir que tenemos que cambiar el precio
                            pa.setPrecioVenta(p.getPrecioMayoreo());
                        } else {
                            pa.setPrecioVenta(p.getPrecioVenta());
                        }
                    }
                    existe = true;
                    break;
                }
            }

            if (!existe) {
                //no existio, lo agregamos
                if (p.getCantidad() == 0) {
                    p.setCantidad(1);
                }
                //tenemos que calcular el iva y el total


                productosAgregados.add(0, p);
            }
        } else {
            if (!esEditarPedido) {
                p.setCantidad(1);
            }

            productosAgregados.add(0, p);
        }

        calculaMontosLista();

    }


    public void btnPedidoClick(View view) {
        if (permisos.getPedido()) {
            if (productosAgregados != null && productosAgregados.size() > 0) {
                pantalla = "pedido";
                mostrarPedido();
            } else {
                mandarMensaje("Debes agregar algun producto para poder generar el pedido");
            }
        } else {
            mandarMensaje("No tienes permisos para realizar pedidos");
        }
    }


    private void mostrarPedido() {
        setContentView(R.layout.pedido);
        Utilerias ut = new Utilerias();
        TextView txtTotalPago = (TextView) findViewById(R.id.txtTotalPago);

        txtTotalPago.setText("TOTAL A PAGAR:   " + ut.formatoDouble(montoTotal));
        entregado = false;
        cambiarImagenEntregado();
    }


    public void btnHacerPedidoClick(View view) {
        EditText txtTarjetaPedido = (EditText) findViewById(R.id.txtTarjetaPedido);

        EditText txtEfectivoPedido = (EditText) findViewById(R.id.txtEfectivoPedido);
        EditText txtPagoCorreo = (EditText) findViewById(R.id.txtPagoCorreo);
        String tarjetaPedido = "0";
        if (!txtTarjetaPedido.getText().toString().equals("")) {
            tarjetaPedido = txtTarjetaPedido.getText().toString();
        }

        String efectivoPedido = "0";
        if (!txtEfectivoPedido.getText().toString().equals("")) {
            efectivoPedido = txtEfectivoPedido.getText().toString();
        }


        generarTicket(txtPagoCorreo.getText().toString(), efectivoPedido, tarjetaPedido, "0",
                "pedido", entregado, enviarWhatsApp);
    }



    public void btnHacerCotizacionClick(View view) {

        EditText txtPagoCorreo = (EditText) findViewById(R.id.txtPagoCorreo);

        generarTicket(txtPagoCorreo.getText().toString(), "0", "0",
                "0", "cotizacion", false, enviarWhatsApp);
    }


    public void btnEntregadoClick(View view) {
        if (entregado) {
            entregado = false;
        } else {
            entregado = true;
        }
        cambiarImagenEntregado();
    }

    private void cambiarImagenEntregado() {
        ImageButton btnEntregado = findViewById(R.id.btnEntregado);

        if (entregado) {
            btnEntregado.setImageResource(R.drawable.entregado);
        } else {
            btnEntregado.setImageResource(R.drawable.noentregado);
        }
    }

    private void calculaMontosLista() {

        totalArticulos = 0;
        if (productosAgregados != null && productosAgregados.size() > 0) {
            double total = 0;
            subtotal = 0;
            iva = 0;
            for (ProductosXYDTOAux p : productosAgregados
            ) {
                totalArticulos += p.getCantidad();
                if (p.getOpciones() != null && p.getOpciones().size() > 0) {
                    double totalPaquete = 0;
                    for (PaqueteOpcionAux pa : p.getOpciones()
                    ) {
                        totalPaquete += pa.getCantidad() * pa.getPrecio();
                    }
                    subtotal += totalPaquete * p.getCantidad();
                    if (p.isIva()) {
                        iva += (totalPaquete * p.getIvaCant() / 100.00);
                    }
                    p.setPrecioVenta(totalPaquete);

                } else {
                    subtotal += (p.getCantidad() * p.getPrecioVenta());
                    if (p.isIva()) {
                        iva += (p.getCantidad() * p.getPrecioVenta() * p.getIvaCant() / 100.00);
                    }
                }
            }
            //ya que tenemos el subtotal y el iva, nos traemos los descuentos
            Utilerias ut = new Utilerias();
            double descEfectivo = ut.parseDouble(ut.obtenerValor("descuentoEfectivo", this));
            double descPorcentaje = ut.parseDouble(ut.obtenerValor("descuentoPorcentaje", this));
            double totalDescuentoPorcentaje = (subtotal + iva) * descPorcentaje / 100.00;

            descuentoTotal = totalDescuentoPorcentaje + descEfectivo;

            double totalTemporal = subtotal + iva - descuentoTotal;


            double propEfectivo = ut.parseDouble(ut.obtenerValor("propinaEfectivo", this));
            double propPorcentaje = ut.parseDouble(ut.obtenerValor("propinaPorcentaje", this));
            double totalPropinaPorcentaje = totalTemporal * propPorcentaje / 100.00;
            propinaTotal = totalPropinaPorcentaje + propEfectivo;

            total = subtotal + iva - descuentoTotal + propinaTotal;
            montoTotal = total;
            actualizarBotonCarrito();
        } else {
            montoTotal = 0;
            actualizarBotonCarrito();
        }
    }


    private void actualizarBotonCarrito() {
        Utilerias ut = new Utilerias();
        Button btnCarrito = (Button) findViewById(R.id.btnCarrito);
        Button btnCobrar = (Button) findViewById(R.id.btnCobrar);
        if (btnCobrar != null) {
            if (esEditarPedido) {
                btnCobrar.setText("Guardar Pedido " + ut.formatDouble(montoTotal));
            } else {
                btnCobrar.setText("Cobrar " + ut.formatDouble(montoTotal));
            }
        }
        if (btnCarrito != null) {
            btnCarrito.setText("Ticket (" + ut.formatoDouble(totalArticulos) + ")");
        }
    }

  /*  private void actualizarTotalesProductos() {
        Utilerias ut = new Utilerias();
        TextView totalProductosT = (TextView) findViewById(R.id.totalProductos);

        if (totalProductosT != null) {
            totalProductosT.setText("Total Prods: " + totalProductos);
        }

        TextView totalBusquedaT = (TextView) findViewById(R.id.totalBusqueda);

        if (totalBusquedaT != null) {
            totalBusquedaT.setText("Total Busqueda: " + totalBusqueda);
        }
    }*/

    public void btnBorrarClick(View view) {
        productosAgregados = new ArrayList<>();

        mostrarPuntoVenta();
    }

    public void btnBuscarClick(View view) {
        EditText txtBuscarProd = (EditText) findViewById(R.id.txtBuscarProd);
        Spinner spGrupos = (Spinner) findViewById(R.id.spGrupos);
        spGrupos.setVisibility(View.GONE);
        txtBuscarProd.setVisibility(View.VISIBLE);
        txtBuscarProd.setText("");


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
    }

    private void buscarProducto() {
        //aqui dentro lo que hayan puesto lo buscamos
        EditText txtBuscarProd = (EditText) findViewById(R.id.txtBuscarProd);
        ProductosDB pdb = new ProductosDB();
        Utilerias ut = new Utilerias();

        productosDisponibles =
                pdb.obtenerProductosCompletosPatron(txtBuscarProd.getText().toString(),
                        Integer.parseInt(ut.obtenerValor("idTienda", this)), realm);

        if (productosDisponibles != null) {
            ListView gvProductosDisponibles = (ListView) findViewById(R.id.gvProductosDisponibles);
            ProductosVentaAdapter adapter = new ProductosVentaAdapter(productosDisponibles, context,"G");
            gvProductosDisponibles.setAdapter(adapter);
            totalBusqueda = productosDisponibles.size();
            //  actualizarTotalesProductos();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        final IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        handleResult(scanResult);

    }

    private void handleResult(IntentResult scanResult) {
        if (scanResult != null) {
            updateUITextViews(scanResult.getContents(), scanResult.getFormatName());
        } else {
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUITextViews(String scan_result, String scan_result_format) {

        EditText txtDatoBuscar = (EditText) findViewById(R.id.txtBuscarProd);
        txtDatoBuscar.setText(scan_result);
        buscarProductoEscaneado();

    }

    private void buscarProductoEscaneado() {
        //aqui dentro lo que hayan puesto lo buscamos y si lo encontramos lo agregamos a la lista
        EditText txtDatoBuscar = (EditText) findViewById(R.id.txtBuscarProd);
        ProductosDB pdb = new ProductosDB();
        Utilerias ut = new Utilerias();

        productosBuscar =
                pdb.obtenerProductosCompletosPatron(txtDatoBuscar.getText().toString(),
                        Integer.parseInt(ut.obtenerValor("idTienda", this)), realm);


        if (productosBuscar != null) {

            if (productosBuscar.size() == 1) {
                //quiere decir que solo encontro uno
                txtDatoBuscar.setText("");
                agregarProducto(productosBuscar.get(0), false);
            } else if (productosBuscar.size() == 0) {
                //no se encontró ningun registro
                mandarMensaje("No se encontró ningun producto con el código escaneado");
            } else {
                //encontró mas de uno los mostramos en la lista para que seleccione alguno
                txtDatoBuscar.setText("");
                productosDisponibles = productosBuscar;
                if (productosDisponibles != null) {
                    ListView gvProductosDisponibles = (ListView) findViewById(R.id.gvProductosDisponibles);
                    ProductosVentaAdapter adapter = new ProductosVentaAdapter(productosDisponibles, context,"G");
                    gvProductosDisponibles.setAdapter(adapter);
                }
            }
        } else {
            //si es null  es porque no encontró nada solo mandamos el mensaje
            mandarMensaje("No se encontró ningun producto con el código escaneado");
        }

    }

    public void btnCancelClick(View view) {
        EditText txtBuscarProd = (EditText) findViewById(R.id.txtBuscarProd);

        Spinner spGrupos = (Spinner) findViewById(R.id.spGrupos);
        spGrupos.setVisibility(View.VISIBLE);
        txtBuscarProd.setVisibility(View.GONE);
    }


    public void btnEliminarProductoAjusteClick(View view) {
        restarProducto(prodAjuste);
    }

    private void restarProducto(ProductosXYDTOAux prod) {

        productosAgregados.remove(prod);


        mostrarPuntoVenta();


    }

    public void btnAsignarClick(View view) {
        setContentView(R.layout.asignarcliente);
        pantalla = "asignar";

        EditText txtAsignarCliente = (EditText) findViewById(R.id.txtAsignarCliente);
        txtAsignarCliente.setEnabled(false);
        ClientesDB cdb = new ClientesDB();
        Utilerias ut = new Utilerias();

        clientes = cdb.obtenerClientesCompletos(
                Integer.parseInt(ut.obtenerValor("idTienda", this)), realm);

        ClienteXYDTOAux cliAux = new ClienteXYDTOAux();
        cliAux.setNombre("Sin cliente");
        cliAux.setApellidoP("");
        cliAux.setApellidoM("");
        cliAux.setCorreo("");
        clientes.add(0, cliAux);
        ListView gvClientes = (ListView) findViewById(R.id.gvClientes);
        ClientesAdapterGris adapter = new ClientesAdapterGris(clientes, this);
        gvClientes.setAdapter(adapter);
        gvClientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ClienteXYDTOAux clienteSel = clientes.get(position);
                EditText txtAsignarCorreo = (EditText) findViewById(R.id.txtAsignarCorreo);
                EditText txtAsignarCliente = (EditText) findViewById(R.id.txtAsignarCliente);
                final LinearLayout datosEsconder = (LinearLayout) findViewById(R.id.datosEsconder);
                datosEsconder.setVisibility(View.VISIBLE);
                if (clienteSel.getCorreo() != null && clienteSel.getCorreo().length() > 0) {

                    txtAsignarCorreo.setText(clienteSel.getCorreo());

                } else {
                    txtAsignarCorreo.setText("");

                }
                if (position == 0) {
                    guardarClienteAsignado("", "");
                    txtAsignarCliente.setText(" ");
                } else {
                    guardarClienteAsignado("" + clienteSel.getId(), clienteSel.getTipoBDL());
                    String nombre = "";
                    String ap = "";
                    String am = "";
                    if (clienteSel.getNombre() == null) {
                        nombre = "";
                    } else {
                        nombre = clienteSel.getNombre();
                    }
                    if (clienteSel.getApellidoM() == null) {
                        am = "";
                    } else {
                        am = clienteSel.getApellidoM();
                    }
                    if (clienteSel.getApellidoP() == null) {
                        ap = "";
                    } else {
                        ap = clienteSel.getApellidoP();
                    }
                    txtAsignarCliente.setText(nombre + " " + ap + " " + am);
                    txtAsignarCliente.requestFocus();
                }
            }
        });


        final EditText txtBuscarCli = (EditText) findViewById(R.id.txtDatoBuscar);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                buscarClientes();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };


        txtBuscarCli.addTextChangedListener(textWatcher);
        final LinearLayout datosEsconder = (LinearLayout) findViewById(R.id.datosEsconder);
        txtBuscarCli.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    datosEsconder.setVisibility(View.GONE);
                } else {
                    datosEsconder.setVisibility(View.VISIBLE);
                }
            }
        });


    }


    private void buscarClientes() {
        EditText txtDatoBuscar = (EditText) findViewById(R.id.txtDatoBuscar);

        ClientesDB pdb = new ClientesDB();
        Utilerias ut = new Utilerias();

        clientes = pdb.obtenerClientesCompletosPatron(txtDatoBuscar.getText().toString(),
                Integer.parseInt(ut.obtenerValor("idTienda", this)), realm);

        ClienteXYDTOAux cliAux = new ClienteXYDTOAux();
        cliAux.setNombre("Seleccione");
        cliAux.setApellidoP("");
        cliAux.setApellidoM("");
        cliAux.setCorreo("");
        clientes.add(0, cliAux);
        ListView gvClientes = (ListView) findViewById(R.id.gvClientes);
        ClientesAdapterGris adapter = new ClientesAdapterGris(clientes, this);
        gvClientes.setAdapter(adapter);
    }

    public void btnCancelarCliXClick(View view) {
        EditText txtDatoBuscar = (EditText) findViewById(R.id.txtDatoBuscar);

        txtDatoBuscar.setText("");
        buscarClientes();
    }

    private void guardarClienteAsignado(String id, String tipo) {
        Utilerias ut = new Utilerias();
        ut.guardarValor("idClienteAsignado", id, this);
        ut.guardarValor("tipoClienteAsignado", tipo, this);
    }


    public void btnPagarClick(View view) {
        pagar();
    }


    private void pagar() {
        try {
            EditText txtPagoEfectivo = (EditText) findViewById(R.id.txtEfectivo);
            EditText txtPagoTarjeta = (EditText) findViewById(R.id.txtTarjeta);
            EditText txtPagoCorreo = (EditText) findViewById(R.id.txtPagoCorreo);
            EditText txtComentario = (EditText) findViewById(R.id.txtComentario);
            Utilerias ut = new Utilerias();
            ut.guardarValor("comentarios", txtComentario.getText().toString(), this);
            double montoEfectivo = 0;
            try {
                montoEfectivo = Double.parseDouble(txtPagoEfectivo.getText().toString());
            } catch (Exception ex) {

            }
            double montoTarjeta = 0;
            try {
                montoTarjeta = Double.parseDouble(txtPagoTarjeta.getText().toString());
            } catch (Exception ex) {

            }
            cambio = (montoEfectivo + montoTarjeta) - montoTotal;

            double juntos = montoEfectivo + montoTarjeta;
            BigDecimal bd = new BigDecimal(montoTotal).setScale(2, RoundingMode.HALF_DOWN);
            montoTotal = bd.doubleValue();

            if (juntos >= montoTotal) {
                //si llega aqui es porque todo salio bien
                if (montoTarjeta > montoTotal) {

                    mandarMensaje("El monto de la tarjeta no puede ser mayor al total");

                    regresarNormal();

                } else {
                    correo = txtPagoCorreo.getText().toString();
                    efectivo = montoEfectivo;
                    tarjeta = montoTarjeta;
                    progress_bar = findViewById(R.id.progress_bar);
                    progress_bar.setVisibility(View.VISIBLE);


                    generarTicket(correo, "" + efectivo, "" + tarjeta
                            ,
                            "" + cambio, "venta", true
                            , enviarWhatsApp);


                }
            } else {

                mandarMensaje("El monto ingresado: " + (juntos) + " es menor al monto total: " + montoTotal);
                regresarNormal();


            }
        } catch (Exception ex) {
            Utilerias.log(this, "Error: " + ex.getMessage() + " " + ex.getStackTrace().toString(), ex);
        }


    }

    public void btnCobrarClick(View view) {

        if (esEditarPedido) {
            guardarPedido();
        } else {

            if (productosAgregados != null && productosAgregados.size() > 0) {
                Utilerias ut = new Utilerias();
                setContentView(R.layout.pantallapagargrande);
                pantalla = "pantallapagar";
                progress_bar = findViewById(R.id.progress_bar);
                progress_bar.setVisibility(View.INVISIBLE);
                TextView txtTotalPagar = (TextView) findViewById(R.id.txtTotalPagar);
                TextView txtEfectivo = (TextView) findViewById(R.id.txtEfectivo);
                TextView txtTarjeta = (TextView) findViewById(R.id.txtTarjeta);
                txtTotalPagar.setText(ut.formatoDouble(montoTotal));
                txtEfectivo.setText(ut.formatoDouble(montoTotal));


                TextWatcher textWatcher = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        calcularMontosCampos();
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                };
                txtTarjeta.addTextChangedListener(textWatcher);


            } else {
                mandarMensaje("Debes agregar algun producto para poder generar la venta");
            }
        }

    }

    private void calcularMontosCampos() {
        TextView txtEfectivo = (TextView) findViewById(R.id.txtEfectivo);
        TextView txtTarjeta = (TextView) findViewById(R.id.txtTarjeta);


        double montoTarjeta = 0;
        try {
            montoTarjeta = Double.parseDouble(txtTarjeta.getText().toString());
        } catch (Exception ex) {

        }

        txtEfectivo.setText("" + (montoTotal - montoTarjeta));


    }

    public void btnEnviarWhatsClick(View view) {
        if (enviarWhatsApp) {
            enviarWhatsApp = false;
        } else {
            enviarWhatsApp = true;
        }
        cambiarImagenWhats();
    }


    public void btn20Click(View view) {
        TextView txtEfectivo = (TextView) findViewById(R.id.txtEfectivo);
        txtEfectivo.setText("20");
        ImageButton btn20 = (ImageButton) findViewById(R.id.btn20);
        btn20.setImageResource(R.drawable.btn20v);
        pagar();
    }

    public void btn50Click(View view) {
        TextView txtEfectivo = (TextView) findViewById(R.id.txtEfectivo);
        txtEfectivo.setText("50");
        ImageButton btn = (ImageButton) findViewById(R.id.btn50);
        btn.setImageResource(R.drawable.btn50v);
        pagar();
    }

    public void btn100Click(View view) {
        TextView txtEfectivo = (TextView) findViewById(R.id.txtEfectivo);
        txtEfectivo.setText("100");
        ImageButton btn = (ImageButton) findViewById(R.id.btn100);
        btn.setImageResource(R.drawable.btn100v);
        pagar();
    }

    public void btn200Click(View view) {
        TextView txtEfectivo = (TextView) findViewById(R.id.txtEfectivo);
        txtEfectivo.setText("200");
        ImageButton btn = (ImageButton) findViewById(R.id.btn200);
        btn.setImageResource(R.drawable.btn200v);
        pagar();

    }

    public void btn500Click(View view) {
        TextView txtEfectivo = (TextView) findViewById(R.id.txtEfectivo);
        txtEfectivo.setText("500");
        ImageButton btn = (ImageButton) findViewById(R.id.btn500);
        btn.setImageResource(R.drawable.btn500v);
        pagar();
    }

    private void regresarNormal() {
        ImageButton btn500 = (ImageButton) findViewById(R.id.btn500);
        if (btn500 != null)
            btn500.setImageResource(R.drawable.btn500);

        ImageButton btn200 = (ImageButton) findViewById(R.id.btn200);
        if (btn200 != null)
            btn200.setImageResource(R.drawable.btn200);
        ImageButton btn100 = (ImageButton) findViewById(R.id.btn100);
        if (btn100 != null)
            btn100.setImageResource(R.drawable.btn100);
        ImageButton btn50 = (ImageButton) findViewById(R.id.btn50);
        if (btn50 != null)
            btn50.setImageResource(R.drawable.btn50);
        ImageButton btn20 = (ImageButton) findViewById(R.id.btn20);
        if (btn20 != null)
            btn20.setImageResource(R.drawable.btn20);
        progress_bar = findViewById(R.id.progress_bar);
        if (progress_bar != null)
            progress_bar.setVisibility(View.INVISIBLE);

    }

    private void cambiarImagenWhats() {
        ImageButton btnWhatss = findViewById(R.id.btnWhatss);

        if (enviarWhatsApp) {
            btnWhatss.setImageResource(R.drawable.wsi);
        } else {
            btnWhatss.setImageResource(R.drawable.wno);
        }
    }

    @Override
    public void onBackPressed() {
        if (pantalla != null && pantalla.equals("extras")) {
            mostrarPuntoVenta();
        } else if (pantalla != null && pantalla.equals("ajusteventa")) {
            mostrarPuntoVenta();
        } else if (pantalla != null && pantalla.equals("configurar")) {
            mostrarPuntoVenta();
        } else if (pantalla != null && pantalla.equals("comentarios")) {
            mostrarPuntoVenta();
        } else if (pantalla != null && pantalla.equals("corte")) {
            mostrarPuntoVenta();
        } else if (pantalla != null && pantalla.equals("pago")) {
            mostrarPuntoVenta();
        } else if (pantalla != null && pantalla.equals("asignar")) {
            mostrarPuntoVenta();
        } else if (pantalla != null && pantalla.equals("buscarProducto")) {
            mostrarPuntoVenta();
        } else if (pantalla != null && pantalla.equals("pedido")) {
            mostrarPuntoVenta();
        } else if (pantalla != null && pantalla.equals("cotizacion")) {
            mostrarPuntoVenta();
        } else if (pantalla != null && pantalla.equals("configuracionticket")) {
            mostrarPuntoVenta();
        } else if (pantalla != null && pantalla.equals("pantallapagar")) {
            mostrarPuntoVenta();
        } else if (pantalla != null && pantalla.equals("carritocompras")) {
            mostrarPuntoVenta();
        }

    }

    private void mostrarMontosTotales(double total, double subtotal, double iva, double propina, double descuento) {
        Utilerias ut = new Utilerias();
        TextView txtSubtotal = (TextView) findViewById(R.id.txtSubtotal);
        TextView txtIva = (TextView) findViewById(R.id.txtIva);
        TextView txtDescuento = (TextView) findViewById(R.id.txtDescuento);
        TextView txtPropina = (TextView) findViewById(R.id.txtPropina);
        TextView txtTotal = (TextView) findViewById(R.id.txtTotal);
        if(txtDescuento!=null) {
            txtDescuento.setText(ut.formatoDouble(descuento));
            txtSubtotal.setText(ut.formatoDouble(subtotal));
            txtTotal.setText(ut.formatoDouble(total));
            txtIva.setText(ut.formatoDouble(iva));
            txtPropina.setText(ut.formatoDouble(propina));
        }
        actualizarBotonCarrito();
    }


    public void btnGuardarPaquetesClick(View view) {


        LinearLayout linearLayout = findViewById(R.id.laySpinners);

        List<PaqueteOpcionAux> opcionesSeleccionadas = new ArrayList<>();
        int hijos = linearLayout.getChildCount();
        int contHijo = 0;
        ProductosDB pdb = new ProductosDB();
        Utilerias ut = new Utilerias();

        List<PaquetesProducto> paquetes = pdb.obtenerPaquetesProductos(productoPaquetes.getId(), realm);

        List<PaquetesProducto> paquetesSinOpciones = new ArrayList<>();
        List<PaquetesProducto> paquetesConOpciones = new ArrayList<>();
        for (PaquetesProducto pa : paquetes
        ) {
            realm = ut.obtenerInstanciaBD(this);
            //lo pusimos en cero porque hay paquetes que solo tienen una opcion: las fresas
            //tambien aqui lo tuvimos que quitar, veamo sque pasa
            if (pdb.obtenerOpcionesPaquete(pa.getId(), realm).size() > 1) {
                paquetesConOpciones.add(pa);
            } else {
                paquetesSinOpciones.add(pa);
            }
        }
        for (int i = 0; i < hijos; i++) {
            View hijo = linearLayout.getChildAt(i);
            try {
                Spinner sp = (Spinner) hijo;
                int posicion = sp.getSelectedItemPosition();

                List<OpcionesPaquete> opcionesPaquete = pdb.obtenerOpcionesPaquete(paquetesConOpciones.get(contHijo).getId(), realm);

                OpcionesPaquete op = opcionesPaquete.get(posicion);
                //hay que agregar
                opcionesSeleccionadas.add(new PaqueteOpcionAux(paquetesConOpciones.get(contHijo).getId(), op.getId()));
                contHijo++;

            } catch (Exception e) {

            }
        }
        ///llegando aqui ya tenemos todas los datos
        agregarProductoPaquete(paquetesSinOpciones, opcionesSeleccionadas);


        mostrarPuntoVenta();
    }

    public void btnRegresarClick(View view) {
        mostrarPuntoVenta();
    }

    public void btnDespuesClick(View view) {
        mostrarPuntoVenta();
    }

    private void generarTicket(String correo, String efectivo, String tarjeta,
                               String cambio, String tipo, boolean productoEntregado, boolean compartirWhatssApp) {
        Utilerias ut = new Utilerias();
        Realm realm4 = ut.obtenerInstanciaBD(this);

        String descuentoEfectivo = ut.obtenerValor("descuentoEfectivo", this);
        if (descuentoEfectivo == null || descuentoEfectivo.equals("")) {
            descuentoEfectivo = "0";
        }
        String descuentoProcentaje = ut.obtenerValor("descuentoPorcentaje", this);
        if (descuentoProcentaje == null || descuentoProcentaje.equals("")) {
            descuentoProcentaje = "0";
        }

        String propinaProcentaje = ut.obtenerValor("propinaPorcentaje", this);
        if (propinaProcentaje == null || propinaProcentaje.equals("")) {
            propinaProcentaje = "0";
        }

        String propinaEfectivo = ut.obtenerValor("propinaEfectivo", this);
        if (propinaEfectivo == null || propinaEfectivo.equals("")) {
            propinaEfectivo = "0";
        }

        CajaDTOLocal cajaActual = ut.obtenerCajaActual(this, this, realm4);

        //guardamos el ticket
        CajasDB cdb = new CajasDB();
        TicketDTOLocal ti = new TicketDTOLocal();
        ti.setTotal(montoTotal);
        ti.setTarjeta(ut.parseDouble(tarjeta));
        ti.setSubtotal(subtotal);
        ti.setIva(iva);
        ti.setTipo(tipo);
        if (tipo.equals("pedido")) {
            try {
                ti.setIdEdit(Integer.parseInt(idPedidoEditar));
            } catch (Exception ex) {

            }
        }
        ti.setProdEntregado(productoEntregado);
        ti.setCompartirWhatsApp(compartirWhatssApp);
        ti.setIdCliente(ut.obtenerValor("idClienteAsignado", this));
        ti.setTipoCliente(ut.obtenerValor("tipoClienteAsignado", this));
        ti.setIdCaja(cajaActual.getIdCaja());
        ti.setFecha(ut.obtenerFechaActualFormateada());
        ti.setEfectivo(ut.parseDouble(efectivo));
        ti.setDescuentoPorcentual(ut.parseDouble(descuentoProcentaje));
        ti.setDescuentoEfectivo(ut.parseDouble(descuentoEfectivo));
        ti.setCorreoTicket(correo);
        ti.setComentario(ut.obtenerValor("comentarios", this));
        ti.setCambio(ut.parseDouble(cambio));
        ti.setPropinaEfectivo(Double.parseDouble(propinaEfectivo));
        ti.setPropinaPorcentual(Double.parseDouble(propinaProcentaje));
        ti.setPropinaTotal(propinaTotal);
        ti.setDescuentoTotal(descuentoTotal);

        TicketDTOLocal til = cdb.crearTicket(ti, realm4);


        ProductosDB pdb = new ProductosDB();
        //despues de crear el ticket creamos los productos del ticket

        for (ProductosXYDTOAux p : productosAgregados
        ) {
            TicketProductoDTOLocal tp = new TicketProductoDTOLocal();
            tp.setTotal(p.getPrecioVenta() * p.getCantidad());
            tp.setPrecioVenta(p.getPrecioVenta());
            tp.setPrecioMayoreo(p.getPrecioMayoreo());
            tp.setPrecioCompra(p.getPrecioCompra());
            tp.setFecha(ut.obtenerFechaActualFormateada());
            if (p.getTipoBDL().equals("S")) {
                tp.setIdProdcutoServer(p.getId());
                //descontamos existencias en Server
                if (tipo.equals("venta")) {
                    pdb.actualizarExistenciaServer(p.getId(), p.getCantidad(), realm4, p.isPaquete(), p.getOpciones());
                }
                if (tipo.equals("pedido") && productoEntregado) {
                    pdb.actualizarExistenciaServer(p.getId(), p.getCantidad(), realm4, p.isPaquete(), p.getOpciones());
                }
            } else {
                tp.setIdProductoLocal(p.getId());
                tp.setIdProdcutoServer(0);
                if (tipo.equals("venta")) {
                    pdb.actualizarExistenciaLocal(p.getId(), p.getCantidad(), realm4);
                }
                if (tipo.equals("pedido") && productoEntregado) {
                    pdb.actualizarExistenciaLocal(p.getId(), p.getCantidad(), realm4);
                }
            }

            tp.setComision(p.getComision());
            tp.setCantidadMayoreo(p.getCantidadMayoreo());
            tp.setCantidad(p.getCantidad());

            if (p.isIva()) {
                double ivaCal = p.getPrecioVenta() * (double) p.getIvaCant() / 100.00;
                tp.setIvaTotal(ivaCal);
                tp.setIva(p.getIvaCant());
            } else {
                tp.setIvaTotal(0);
                tp.setIva(0);
            }
            idTicketGenerado = til.getIdTicket();
            TicketProductoDTOLocal prodCreado = cdb.crearProducto(tp, til.getIdTicket(), realm4);


            if (p.getOpciones() != null && p.getOpciones().size() > 0) {
                for (PaqueteOpcionAux pa : p.getOpciones()
                ) {

                    cdb.crearOpcionProducto(pa, prodCreado.getId(), realm4);

                }
            }

        }


        ut.guardarValor("comentarios", "", this);
        ut.guardarValor("descuentoPorcentaje", "", this);
        ut.guardarValor("descuentoEfectivo", "", this);
        ut.guardarValor("propinaPorcentaje", "", this);
        ut.guardarValor("propinaEfectivo", "", this);
        ut.guardarValor("idClienteAsignado", "", this);
        ut.guardarValor("tipoClienteAsignado", "", this);

        ut.guardarValor("correoAsignar", "", this);

     /*   if (tipo.equals("cotizacion")) {
            productosAgregados = new ArrayList<ProductosXYDTOAux>();
            montoTotal = 0;
            totalArticulos = 0;

            mostrarPuntoVenta();


        } else {
*/
            mostrarVentaExitosa("" + idTicketGenerado, tipo);


  //      }


        if (ut.verificaConexion(context)) {


            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    Utilerias ut = new Utilerias();
                    ut.imprimirTicket(context, activity, idTiendaGlobal,idTicketGenerado);
                }
            });


            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    Utilerias ut = new Utilerias();
                    Realm realm3 = ut.obtenerInstanciaBD(context);
                    UtileriasSincronizacion uts = new UtileriasSincronizacion();
                    uts.sincronizarTodo(context, activity, realm3, idTiendaGlobal);
                    if (realm3 != null && !realm3.isClosed()) {
                        realm3.close();
                    }
                }
            });


        } else {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    Utilerias ut = new Utilerias();
                    ut.imprimirTicket(context, activity, idTiendaGlobal,idTicketGenerado);
                }
            });

        }


        if (tipo.equals("venta")) {
            sumarVenta();

            validarResenia();


        }

        cerrarRealmN(realm4);

    }

    public void btnCotizarClick(View view) {
        if (permisos.getCotizar()) {
            if (productosAgregados != null && productosAgregados.size() > 0) {
                pantalla = "cotizacion";

                mostrarConfirmaCotizacion();
            } else {
                mandarMensaje("Debes agregar algun producto para poder generar la venta");
            }
        } else {
            mandarMensaje("No tienes permisos para cotizar");
        }
    }

    private void mostrarConfirmaCotizacion()
    {
        setContentView(R.layout.cotizacion);
        Utilerias ut = new Utilerias();
        TextView txtTotalPago = (TextView) findViewById(R.id.txtTotalPago);

        txtTotalPago.setText("TOTAL COTIZADO:   " + ut.formatoDouble(montoTotal));

    }

    private void mostrarVentaExitosa(String folioTicket, String tipo) {
        Utilerias ut = new Utilerias();
        UtileriasImpresion uti = new UtileriasImpresion();

        setContentView(R.layout.ventaexitosa);


        TextView txtCambio = (TextView) findViewById(R.id.txtCambio);
        TextView txtTotalPagar = (TextView) findViewById(R.id.txtTotalPagar);
        TextView txtFolioApp = (TextView) findViewById(R.id.txtFolioApp);
        TextView txtTituloVentaPedido = (TextView) findViewById(R.id.txtTituloVentaPedido);
        if (tipo.equals("pedido")) {
            txtTituloVentaPedido.setText("Pedido Exitoso");
        } else if (tipo.equals("venta")) {
            txtTituloVentaPedido.setText("Venta Exitosa");
        } else {
            txtTituloVentaPedido.setText("Cotización Exitosa");
        }

        ImageButton imgImpresora = (ImageButton) findViewById(R.id.imgImpresora);
        boolean impresoraLista = false;


         /*   if (ut.isBluetoothEnabled()) {

                String nombreImpresora = ut.obtenerValor("nombreImpresora", this);
                if (nombreImpresora != null) {

                    BluetoothDevice device = ut.obtenerImpresora(nombreImpresora);
                    if (device != null) {
                        String error = uti.imprimirTicket("", device, "GENE", 1);
                        if (error.equals("")) {
                            impresoraLista = true;
                        } else {
                            mandarMensaje("La impresora esta apagada o requiere volverla a emparejar");
                        }
                    } else {
                        mandarMensaje("No se encontró la impresora configurada");
                    }
                } else {
                    mandarMensaje("No tiene configurada ninguna impresora");
                }
            } else {
                mandarMensaje("El bluetooth esta apagado");
            }
        }*/
        if (estatusImpresora) {
            imgImpresora.setImageResource(R.drawable.impresoraok);
        } else {
            imgImpresora.setImageResource(R.drawable.impresoranook);
        }


        txtCambio.setText("Cambio: " + ut.formatoDouble(cambio));
        txtFolioApp.setText("Folio App: " + folioTicket);
        txtTotalPagar.setText("Total: " + ut.formatoDouble(montoTotal));

        productosAgregados = new ArrayList<ProductosXYDTOAux>();
        montoTotal = 0;
        totalArticulos = 0;
        idPedidoEditar = "0";

    }

    public void btnImprimirClick(View view) {
        reimprimir();
    }

    public void btnAceptarVentaExitosa(View view) {
        if (esEditarPedido) {
            irReportes();
        } else {
            esEditarPedido = false;
            mostrarPuntoVenta();
        }


    }

    private void irReportes() {
        Utilerias ut = new Utilerias();
        if (ut.esPantallaChica(this)) {
            Intent i = new Intent(getApplicationContext(), ReportesActivity.class);
            startActivity(i);
        } else {
            Intent i = new Intent(getApplicationContext(), ReportesGrandeActivity.class);
            startActivity(i);
        }
    }

    private void validarResenia() {
        Utilerias ut = new Utilerias();

        int numeroVenta = obtenerNumeroVenta();
        if (!hizoResenia()) {
            mostrarHacerResenia();
        } else {
            if (numeroVenta == cantidadVentas) {
                mostrarHacerResenia();
            } else if (numeroVenta == cantidadVentasG) {
                mostrarHacerResenia();
            }
        }


    }


    private int obtenerNumeroVenta() {
        Utilerias ut = new Utilerias();

        int numeroVenta = 0;
        try {
            numeroVenta = Integer.parseInt(ut.obtenerValor("numVenta", this));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return numeroVenta;
    }

    private boolean hizoResenia() {
        Utilerias ut = new Utilerias();

        boolean hizoRes = false;
        try {
            String hizo = ut.obtenerValor("hizoResenia", this);
            if (hizo != null) {
                hizoRes = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return hizoRes;
    }


    private void mostrarHacerResenia() {
        setContentView(R.layout.haceressenia);
    }

    public void btnReseniaSIClick(View view) {
        Utilerias ut = new Utilerias();
        ut.guardarValor("hizoResenia", "SI", this);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(
                "https://play.google.com/store/apps/details?id=com.anegocios.puntoventa"));
        startActivity(intent);
        mostrarPuntoVenta();
    }


    private void sumarVenta() {
        Utilerias ut = new Utilerias();
        String numVenta = ut.obtenerValor("numVenta", this);
        int numeVenta = 0;
        if (numVenta == null) {
            numeVenta = 1;
        } else {
            try {
                numeVenta = 1 + Integer.parseInt(numVenta);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        ut.guardarValor("numVenta", "" + numeVenta, this);
    }


    private void mostrarAjuste(ProductosXYDTOAux prod) {
        setContentView(R.layout.ajusteproducto);
        pantalla = "ajusteventa";
        EditText txtCantidadAjuste = (EditText) findViewById(R.id.txtCantidadAjuste);
        EditText txtPrecioAjuste = (EditText) findViewById(R.id.txtPrecioAjuste);
        txtCantidadAjuste.setText("" + prod.getCantidad());
        ProductosDB pdb = new ProductosDB();
        Utilerias ut = new Utilerias();
        if (prod.getTipoBDL().equals("S")) {

            ProductosXYDTO productoOriginal = pdb.obtenerproductoServer(prod.getId(), realm);


            txtPrecioAjuste.setText("" + productoOriginal.getPrecioVenta());
            prod.setPrecioVenta(productoOriginal.getPrecioVenta());

        } else {

            ProductosXYDTOLocal original = pdb.obtenerproductoLocal(prod.getId(), realm);

            txtPrecioAjuste.setText("" + original.getPrecioVenta());
            prod.setPrecioVenta(original.getPrecioVenta());
        }
        TextView lblPermisoPrecio = (TextView) findViewById(R.id.lblPermisoPrecio);
        if (permisos.getEditPrecio()) {
            txtPrecioAjuste.setEnabled(true);
            lblPermisoPrecio.setText("");
        } else {
            txtPrecioAjuste.setEnabled(false);
            lblPermisoPrecio.setText("Sin permiso para ajustar precio");
        }

    }


    public void btnMasPrecioClick(View view) {

        EditText txtPrecioAjuste = (EditText) findViewById(R.id.txtPrecioAjuste);
        Utilerias ut = new Utilerias();
        double precio = 0;
        try {
            precio = Double.parseDouble(txtPrecioAjuste.getText().toString());
        } catch (Exception ex) {

        }

        precio = precio + 1;
        txtPrecioAjuste.setText(ut.formatoDouble(precio));

    }

    public void btnMenosPrecioClick(View view) {
        EditText txtPrecioAjuste = (EditText) findViewById(R.id.txtPrecioAjuste);
        Utilerias ut = new Utilerias();
        double precio = 0;
        try {
            precio = Double.parseDouble(txtPrecioAjuste.getText().toString());
        } catch (Exception ex) {

        }
        if (precio > 1) {
            precio = precio - 1;
            txtPrecioAjuste.setText(ut.formatoDouble(precio));
        } else {
            txtPrecioAjuste.setText("0");
        }
    }

    public void btnMasCantidadClick(View view) {
        EditText txtCantidadAjuste = (EditText) findViewById(R.id.txtCantidadAjuste);

        Utilerias ut = new Utilerias();
        double precio = 0;
        try {
            precio = Double.parseDouble(txtCantidadAjuste.getText().toString());
        } catch (Exception ex) {

        }

        precio = precio + 1;
        txtCantidadAjuste.setText(ut.formatoDouble(precio));

    }

    public void btnMenosCantidadClick(View view) {
        EditText txtCantidadAjuste = (EditText) findViewById(R.id.txtCantidadAjuste);
        Utilerias ut = new Utilerias();
        double precio = 0;
        try {
            precio = Double.parseDouble(txtCantidadAjuste.getText().toString());
        } catch (Exception ex) {

        }
        if (precio > 1) {
            precio = precio - 1;
            txtCantidadAjuste.setText(ut.formatoDouble(precio));
        } else {
            txtCantidadAjuste.setText("0");
        }
    }


    private void agregarSolitoPaquete(ProductosXYDTOAux p) {
        productoPaquetes = p;
        ProductosDB pdb = new ProductosDB();
        Utilerias ut = new Utilerias();

        List<PaquetesProducto> paquetesSinOpciones = pdb.obtenerPaquetesProductos(p.getId(), realm);

        List<PaqueteOpcionAux> opcionesSeleccionadas = new ArrayList<>();
        agregarProductoPaquete(paquetesSinOpciones, opcionesSeleccionadas);
    }


    private void agregarSolitoPaqueteEdicionPedido(ProductosXYDTOAux p, String idOpcion) {
        productoPaquetes = p;
        ProductosDB pdb = new ProductosDB();
        Utilerias ut = new Utilerias();

        List<PaquetesProducto> paquetesSinOpciones = new ArrayList<>();

        List<PaqueteOpcionAux> opcionesSeleccionadas = new ArrayList<>();
        PaqueteOpcionAux pq = new PaqueteOpcionAux();
        pq.setIdOpcion(Long.parseLong(idOpcion));
        opcionesSeleccionadas.add(pq);
        agregarProductoPaquete(paquetesSinOpciones, opcionesSeleccionadas);
    }


    private void agregarProductoPaquete(List<PaquetesProducto> paquetesSinOpciones,
                                        List<PaqueteOpcionAux> opcionesSeleccionadas) {
        ProductosDB pdb = new ProductosDB();
        Utilerias ut = new Utilerias();
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


        for (PaquetesProducto pa : paquetesSinOpciones
        ) {

            List<OpcionesPaquete> opcionS = pdb.obtenerOpcionesPaquete(pa.getId(), realm);

            if (opcionS != null && opcionS.size() > 0) {

                OpcionesPaquete opcion = pdb.obtenerOpcionPaquete(opcionS.get(0).getId(), realm);

                PaqueteOpcionAux po = new PaqueteOpcionAux();
                po.setCantidad(opcion.getCantidad());
                po.setDescripion(opcion.getDescripcion());
                po.setIdProducto(opcion.getIdProducto());
                po.setIdOpcion(opcion.getId());
                po.setPrecio(opcion.getPrecio());
                po.setIdPaquete(opcion.getIdPaquete());
                po.setMostrar(opcion.isMostrar());
                opcionesSeleccionadas.add(po);
            }

        }

        //legando aqui solo armamos el producto Agregado y lo insertamos
        ProductosXYDTOAux p = new ProductosXYDTOAux(productoPaquetes, "S");

        p.setOpciones(opcionesSeleccionadas);
        p.setCantidad(1);
        productosAgregados.add(0, p);


    }

    public void btnAceptarAsignarClick(View view) {
        Utilerias ut = new Utilerias();
        EditText txtAsignarCorreo = (EditText) findViewById(R.id.txtAsignarCorreo);
        ut.guardarValor("correoAsignado", txtAsignarCorreo.getText().toString(), this);
        mostrarPuntoVenta();
    }


    public void btnTarjetaClick(View view) {
        EditText txtPagoEfectivo = (EditText) findViewById(R.id.txtEfectivo);
        EditText txtPagoTarjeta = (EditText) findViewById(R.id.txtTarjeta);
        Utilerias ut = new Utilerias();
        txtPagoEfectivo.setText("");
        txtPagoTarjeta.setText(ut.formatoDouble(montoTotal));

    }

    public void btnEfectivoClick(View view) {
        EditText txtPagoEfectivo = (EditText) findViewById(R.id.txtEfectivo);
        EditText txtPagoTarjeta = (EditText) findViewById(R.id.txtTarjeta);
        Utilerias ut = new Utilerias();
        txtPagoTarjeta.setText("");
        txtPagoEfectivo.setText(ut.formatoDouble(montoTotal));
    }

    private void llenarComboGrupos() {
        GrupoDB gdb = new GrupoDB();
        Utilerias ut = new Utilerias();
        grupos = gdb.obtenerListaGruposAuxiliar(Integer.parseInt(ut.obtenerValor("idTienda", this)), realm);
        ProductosDB pdb = new ProductosDB();
        List<ProductosXYDTOAux> disponibles = pdb.obtenerProductosCompletos(
                Integer.parseInt(ut.obtenerValor("idTienda", this)), realm);

        List<String> gruposAux = new ArrayList<String>();
        gruposAux.add("TODOS LOS PRODUCTOS " + disponibles.size());
        for (GruposVRXYAux g : grupos) {

            List<ProductosXYDTOAux> disponiblesg = pdb.obtenerProductosCompletosGrupo(g.getId(),
                    Integer.parseInt(ut.obtenerValor("idTienda", context)), realm);

            gruposAux.add(g.getNombre() + " " + disponiblesg.size());
        }

        Spinner spGrupos = (Spinner) findViewById(R.id.spGrupos);

        GruposGrandeAdapter aa = new GruposGrandeAdapter(gruposAux, this);
        aa.setDropDownViewResource(R.layout.gruposazul);

        spGrupos.setAdapter(aa);
        spGrupos.setSelection(Integer.parseInt(gruposSeleccionado));


        spGrupos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Utilerias ut = new Utilerias();
                ProductosDB pdb = new ProductosDB();
                gruposSeleccionado = "" + position;
                if (position == 0) {
                    productosDisponibles = pdb.obtenerProductosCompletos(
                            Integer.parseInt(ut.obtenerValor("idTienda", context)), realm);
                } else {
                    position--;
                    int idGrupo = grupos.get(position).getId();


                    productosDisponibles = pdb.obtenerProductosCompletosGrupo(idGrupo,
                            Integer.parseInt(ut.obtenerValor("idTienda", context)), realm);


                }

                if (productosDisponibles != null) {
                    ListView gvProductosDisponibles = (ListView) findViewById(R.id.gvProductosDisponibles);
                    ProductosVentaAdapter adapter = new ProductosVentaAdapter(productosDisponibles, context,"G");
                    gvProductosDisponibles.setAdapter(adapter);
                    totalBusqueda = productosDisponibles.size();
                    // actualizarTotalesProductos();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
        ut.guardarValor("idUsuario","",this);
        cerrarRealmN(realm);
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }

    public void btnMostrarMenuClick(View view) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.openDrawer(GravityCompat.START);
    }


    public void mandarMensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }

    private void cerrarRealmN(Realm realm) {
        if (realm != null && !realm.isClosed()) {
            realm.close();
        }
    }
}
