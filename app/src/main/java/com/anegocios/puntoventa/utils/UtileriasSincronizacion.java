package com.anegocios.puntoventa.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;

import com.anegocios.puntoventa.jsons.CajaPVDTO;
import com.anegocios.puntoventa.jsons.TicketPVDTO;
import com.anegocios.puntoventa.servicios.TicketPVService;
import com.google.gson.Gson;
import com.anegocios.puntoventa.bdlocal.CajaDTOLocal;
import com.anegocios.puntoventa.bdlocal.ClienteXYDTOLocal;
import com.anegocios.puntoventa.bdlocal.OpcionPaqueteProductoLocal;
import com.anegocios.puntoventa.bdlocal.ProductosXYDTOLocal;
import com.anegocios.puntoventa.bdlocal.TicketDTOLocal;
import com.anegocios.puntoventa.bdlocal.TicketProductoDTOLocal;
import com.anegocios.puntoventa.database.CajasDB;
import com.anegocios.puntoventa.database.ClientesDB;
import com.anegocios.puntoventa.database.ProductosDB;
import com.anegocios.puntoventa.database.UsuariosDB;
import com.anegocios.puntoventa.jsons.CajaResponseDTO;
import com.anegocios.puntoventa.jsons.CajaTicketDTO;
import com.anegocios.puntoventa.jsons.CajaXYDTO;
import com.anegocios.puntoventa.jsons.ClienteDTO;
import com.anegocios.puntoventa.jsons.ClienteXYDTO;
import com.anegocios.puntoventa.jsons.CorteDTO;
import com.anegocios.puntoventa.jsons.OpcionesVentasXYDTO;
import com.anegocios.puntoventa.jsons.ProductoDTO;
import com.anegocios.puntoventa.jsons.ProductosXYDTO;
import com.anegocios.puntoventa.jsons.TicketCajaTicketDTO;
import com.anegocios.puntoventa.jsons.TicketDTO;
import com.anegocios.puntoventa.jsons.VentasXYDTO;
import com.anegocios.puntoventa.servicios.APIClient;
import com.anegocios.puntoventa.servicios.APIInterface;
import com.anegocios.puntoventa.servicios.CajaService;
import com.anegocios.puntoventa.servicios.ClientesService;
import com.anegocios.puntoventa.servicios.ProductosService;
import com.anegocios.puntoventa.servicios.TicketService;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import retrofit2.Call;

public class UtileriasSincronizacion {


    public String sincronizarTodo(Context context, Activity activity, Realm realm, long idTiendaGlobal) {

        String error = "";
        try {

            error = sincronizarClientes(context, activity, realm);
            if (error.equals("")) {
                error = sincronizarProductos(context, activity, realm);
            } else {
                Utilerias.log(context, error, null);
            }

            CajasDB cdb = new CajasDB();
            Utilerias ut = new Utilerias();
            List<CajaDTOLocal> cajas = cdb.obtenerCajasParaEnviar(Integer.parseInt(ut.obtenerValor("idTienda", context)), realm);
            if (cajas != null && cajas.size() > 0) {
                //primero tenemos que abrir las cajas para que ya tengan idServer, no podemos mandar tickets hasta ese momento
                for (CajaDTOLocal ca : cdb.obtenerCajasParaEnviar(Integer.parseInt(ut.obtenerValor("idTienda", context)), realm)
                ) {
                    abrirCaja(ca.getFechaInicio(), ca.getMontoInicial(), context, activity, realm, ca.getIdCaja());
                }
            } else {
                if (error.equals("")) {
                    error = sincronizarCajas(context, activity, realm, idTiendaGlobal);
                } else {
                    Utilerias.log(context, error, null);
                }
            }
        } catch (Exception ex) {
            error = ex.getMessage();
            Utilerias.log(context, ex.getMessage(), ex);
        }
        return error;
    }

    public String sincronizarProductos(Context context, Activity activity, Realm realm) {
        String error = "";

        try {
            //tenemos que mandar todos los productos de la local que tengan enviado = false
            Utilerias ut = new Utilerias();
            ProductosDB pdb = new ProductosDB();

            List<ProductosXYDTO> productosEditados = pdb.obtenerListaProductosServerEditados(
                    Integer.parseInt(ut.obtenerValor("idTienda", context)), realm);

            if (productosEditados != null && productosEditados.size() > 0) {
                for (ProductosXYDTO l : productosEditados
                ) {
                    ProductosXYDTO p = new ProductosXYDTO(l);
                    error += error = subirProductosEditados(p, context, activity, realm);
                }

            }

            if (error.equals("")) {

                List<ProductosXYDTOLocal> productosEnviar =
                        pdb.obtenerListaProductosLocalesEnviar(Integer.parseInt(ut.obtenerValor("idTienda", context)), realm);

                if (productosEnviar != null && productosEnviar.size() > 0) {
                    for (ProductosXYDTOLocal l : productosEnviar
                    ) {
                        ProductosXYDTO p = new ProductosXYDTO(l);
                        p.setId(0);
                        p.setIdAPP(l.getId());
                        error += subirProductoLocal(p, context, activity, l.getId(), realm);
                    }

                }
            }

        } catch (Exception ex) {
            error = ex.getMessage();
        }

        return error;
    }


    private String subirProductoLocal(ProductosXYDTO p, final Context context, Activity activity, int idLocal, Realm realm) {

        String error = "";
        ProductoDTO pr = new ProductoDTO();
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);


        try {
            Utilerias ut = new Utilerias();
            pr.setIdAndroid(ut.obtenerSerial(context, activity));
            UsuariosDB udb = new UsuariosDB();
            int idUT = udb.obtenerIdUTUsuario(Integer.parseInt(ut.obtenerValor("idUsuario", context)),
                    Integer.parseInt(ut.obtenerValor("idTienda", context)), realm).getIdUT();
            pr.setIdUT(idUT);
            pr.setAll(false);
            pr.setIdTienda(ut.obtenerValor("idTienda", context));
            List<ProductosXYDTO> productos = new ArrayList<ProductosXYDTO>();
            productos.add(p);
            pr.setProductosxy(productos);
            Gson gson = new Gson();
            String json = gson.toJson(pr);
            Call<ProductoDTO> call = apiInterface.mandarConsultarProductos(pr);
            ProductosService ls = new ProductosService(call);
            ProductoDTO res = ls.execute().get();
            if (res == null) {
                error = "No se obtuvo respuesta del servicio";
            } else {
                if (res.getProductosxy() == null) {
                    error = res.getMsg();
                } else {
                    if (res.getProductosxy().size() > 0) {

                        paraSubir = res.getProductosxy();
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {

                                ProductosDB pdb = new ProductosDB();
                                Utilerias ut = new Utilerias();
                                Realm realmAux = ut.obtenerInstanciaBD();
                                pdb.actualizarBDProductos(paraSubir,
                                        Integer.parseInt(ut.obtenerValor("idTienda", context)),
                                        ut.obtenerModoAplicacion(context), ut.verificaConexion(context), realmAux,context);
                                if (realmAux != null && !realmAux.isClosed()) {
                                    realmAux.close();
                                }
                            }
                        });
                        Thread.sleep(3000);
                        realm.refresh();
                        ProductosDB pdb = new ProductosDB();

                        for (ProductosXYDTO pro : res.getProductosxy()
                        ) {
                            if (pro.getIdAPP() > 0) {
                                //quiere decir que es el que acabamos de mandar
                                //actualizamos su id de Server en el registro local
                                pdb.actualizarIdProductoLocalPorIdProductoServer(pro.getId(), idLocal, realm);
                            }
                        }

                    }


                }
            }
        } catch (Exception ex) {
            error = ex.getMessage();
        }
        return error;
    }

    List<ProductosXYDTO> paraSubir;

    private String subirProductosEditados(ProductosXYDTO p, final Context context, Activity activity, Realm realm) {

        String error = "";
        ProductoDTO pr = new ProductoDTO();
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);


        try {
            Utilerias ut = new Utilerias();
            pr.setIdAndroid(ut.obtenerSerial(context, activity));
            UsuariosDB udb = new UsuariosDB();
            int idUT = udb.obtenerIdUTUsuario(Integer.parseInt(ut.obtenerValor("idUsuario", context)),
                    Integer.parseInt(ut.obtenerValor("idTienda", context)), realm).getIdUT();
            pr.setIdUT(idUT);
            pr.setAll(false);
            pr.setIdTienda(ut.obtenerValor("idTienda", context));
            List<ProductosXYDTO> productos = new ArrayList<ProductosXYDTO>();
            productos.add(p);
            pr.setProductosxy(productos);
            Call<ProductoDTO> call = apiInterface.mandarConsultarProductos(pr);
            ProductosService ls = new ProductosService(call);
            ProductoDTO res = ls.execute().get();
            if (res == null) {
                error = "No se obtuvo respuesta del servicio";
            } else {
                if (res.getProductosxy() == null) {
                    error = res.getMsg();
                } else {
                    if (res.getProductosxy().size() > 0) {
                        ProductosDB pdb = new ProductosDB();
                        paraSubir = res.getProductosxy();

                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                ProductosDB pdb = new ProductosDB();
                                Utilerias ut = new Utilerias();
                                Realm realmaux = ut.obtenerInstanciaBD();
                                pdb.actualizarBDProductos(paraSubir,
                                        Integer.parseInt(ut.obtenerValor("idTienda", context)),
                                        ut.obtenerModoAplicacion(context), ut.verificaConexion(context), realmaux,context);
                                if (realmaux != null && !realmaux.isClosed()) {
                                    realmaux.close();
                                }

                            }
                        });


                        for (ProductosXYDTO pro : res.getProductosxy()
                        ) {
                            pdb.actualizarProductoServerEditadoFalse(pro.getId(), realm);
                        }

                    }


                }
            }
        } catch (Exception ex) {
            error = ex.getMessage();
        }
        return error;
    }


    public String sincronizarClientes(Context context, Activity activity, Realm realm) {
        String error = "";

        try {
            //tenemos que mandar todos los clientes de la local que tengan enviado = false

            ClientesDB pdb = new ClientesDB();
            Utilerias ut = new Utilerias();
            List<ClienteXYDTO> regsEditados = pdb.obtenerListaClientesServerEditados(
                    Integer.parseInt(ut.obtenerValor("idTienda", context)), realm);

            if (regsEditados != null && regsEditados.size() > 0) {
                for (ClienteXYDTO l : regsEditados
                ) {
                    ClienteXYDTO p = new ClienteXYDTO(l);
                    error += error = subirClientesEditados(p, context, activity, realm);
                }

            }


            if (error.equals("")) {

                List<ClienteXYDTOLocal> regsEnviar = pdb.obtenerListaClientesLocalesEnviar(
                        Integer.parseInt(ut.obtenerValor("idTienda", context)), realm);

                if (regsEnviar != null && regsEnviar.size() > 0) {
                    for (ClienteXYDTOLocal l : regsEnviar
                    ) {
                        ClienteXYDTO p = new ClienteXYDTO(l);
                        p.setId(0);

                        error += subirClienteLocal(p, context, activity, l.getId(), realm);
                    }

                }
            }

        } catch (Exception ex) {
            error = ex.getMessage();
        }

        return error;
    }


    private String subirClienteLocal(ClienteXYDTO p, Context context, Activity activity, int idLocal, Realm realm) {

        String error = "";
        ClienteDTO pr = new ClienteDTO();
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);


        try {
            Utilerias ut = new Utilerias();
            pr.setIdAndroid(ut.obtenerSerial(context, activity));
            UsuariosDB udb = new UsuariosDB();
            int idUT = udb.obtenerIdUTUsuario(Integer.parseInt(ut.obtenerValor("idUsuario", context)),
                    Integer.parseInt(ut.obtenerValor("idTienda", context)), realm).getIdUT();
            pr.setIdUT(idUT);


            List<ClienteXYDTO> regs = new ArrayList<ClienteXYDTO>();
            regs.add(p);
            pr.setClientesxy(regs);

            Call<ClienteDTO> call = apiInterface.mandarConsultarClientes(pr);
            ClientesService ls = new ClientesService(call);
            ClienteDTO res = ls.execute().get();
            if (res == null) {
                error = "Algo salió mal guardando, por favor intente de nuevo";
            } else {
                if (res.getClientesxy() == null) {
                    error = res.getMsg();
                } else {
                    if (res.getClientesxy().size() > 0) {
                        ClientesDB pdb = new ClientesDB();
                        pdb.actualizarBDClientes(res.getClientesxy(),
                                Integer.parseInt(ut.obtenerValor("idTienda", context)), realm);
                        pdb.actualizarClienteLocalEnviado(idLocal, realm);
                    }
                }
            }
        } catch (Exception ex) {
            error = ex.getMessage();
        }
        return error;
    }


    private String subirClientesEditados(ClienteXYDTO p, Context context, Activity activity, Realm realm) {

        String error = "";
        ClienteDTO pr = new ClienteDTO();
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);


        try {
            Utilerias ut = new Utilerias();
            pr.setIdAndroid(ut.obtenerSerial(context, activity));
            UsuariosDB udb = new UsuariosDB();
            int idUT = udb.obtenerIdUTUsuario(Integer.parseInt(ut.obtenerValor("idUsuario", context)),
                    Integer.parseInt(ut.obtenerValor("idTienda", context)), realm).getIdUT();
            pr.setIdUT(idUT);


            List<ClienteXYDTO> regs = new ArrayList<ClienteXYDTO>();
            regs.add(p);
            pr.setClientesxy(regs);

            Call<ClienteDTO> call = apiInterface.mandarConsultarClientes(pr);
            ClientesService ls = new ClientesService(call);
            ClienteDTO res = ls.execute().get();
            if (res == null) {
                error = "Algo salió mal guardando, por favor intente de nuevo";
            } else {
                if (res.getClientesxy() == null) {
                    error = res.getMsg();
                } else {
                    if (res.getClientesxy().size() > 0) {
                        ClientesDB pdb = new ClientesDB();
                        pdb.actualizarBDClientes(res.getClientesxy(),
                                Integer.parseInt(ut.obtenerValor("idTienda", context)), realm);
                        pdb.actualizarClienteServerEditadoFalse(p.getId(), realm);
                    }
                }
            }
        } catch (Exception ex) {
            error = ex.getMessage();
        }
        return error;
    }


    public String sincronizarCajas(Context c, Activity a, Realm realm, long idTiendaGlobal) {
        String error = "";

        //lo primero que hay que hacer es ir por todas las cajas
        //que no se han enviado
        Utilerias ut = new Utilerias();
        CajasDB cdb = new CajasDB();
      /*  for (CajaDTOLocal ca : cdb.obtenerCajasParaEnviar(Integer.parseInt(ut.obtenerValor("idTienda", c)), realm)
        ) {
            //por cada caja primero la enviamos(creamos en el server)
            int idCajaServer = abrirCaja(ca.getFechaInicio(), ca.getMontoInicial(), c, a, realm);
            if (idCajaServer > 0) {
                //actualizamos la caja con el id de la caja server
                cdb.actualizarIdServerCaja(ca.getIdCaja(), idCajaServer, realm);
                //despues vamos por los tickets
                error = mandarTicketsCaja(ca, c, a, realm, idTiendaGlobal);
                if (!error.equals("")) {
                    break;
                } else {
                    //verificamos que no tenga ya corte
                    if (ca.isTieneCorteLocal()) {
                        //si tiene corte se manda hacer el corte al server
                        hacerCorteCaja(c, a, ca, realm);
                    }
                }
            } else {
                error = "Ocurrió un problema arbiendo una caja";
                break;
            }
        }*/
        // como puede ser que se quede alguna caja abierta y solo este mandando tickets
        for (CajaDTOLocal ca : cdb.obtenerCajasAbiertasParaRevisarEnviar(
                Integer.parseInt(ut.obtenerValor("idTienda", c)), realm)
        ) {
            //despues vamos por los tickets
            error = mandarTicketsCaja(ca, c, a, realm, idTiendaGlobal);
            if (!error.equals("")) {
                break;
            } else {
                //verificamos que no tenga ya corte
                if (ca.isTieneCorteLocal()) {
                    //si tiene corte se manda hacer el corte al server
                    hacerCorteCaja(c, a, ca, realm);
                }
            }
        }
        return error;

    }

    private String mandarTicketsCaja(CajaDTOLocal caja, Context c, Activity a, Realm realm, long idTiendaGlobal) {
        ProductosDB pdb = new ProductosDB();
        String error = "";
        //tenemos que ir por los tickets de la caja que no se hayan mandado al server
        CajasDB cdb = new CajasDB();
        List<TicketDTOLocal> tickets = cdb.obtenerTicketsCaja(caja.getIdCaja(), realm);
        if (tickets != null) {
            Utilerias.log(c, "intentando subir: " + tickets.size() + " Tickets", null);
        } else {
            Utilerias.log(c, "No hay Tickets que subir", null);
        }
        for (TicketDTOLocal t : tickets
        ) {
            //de cada ticket obtenemos sus productos
            List<TicketProductoDTOLocal> productosTicket = cdb.obtenerProductosTicket(t.getIdTicket(), realm);


            //ya que todos los productos tienen su idServer
            //ahori si mandamos el Ticket

            //AQUI ES LA LINEA ENTRE LOS WS ANTERIORES Y LOS NUEVOS
            //TODO
           // generarTicket(c, a, caja, t, productosTicket, a, realm, idTiendaGlobal);
             error=generarTicketPV(c, a, caja, t, productosTicket, a, realm, idTiendaGlobal);

        }

        return error;
    }

    private int abrirCaja(String fechaInicio, double montoInicial, Context context, Activity activity, Realm realm, int idCajaLocal) {
        int idCajaServer = 0;
        Utilerias ut = new Utilerias();
        CajaResponseDTO cr = new CajaResponseDTO();
        cr.setIdAndroid(ut.obtenerSerial(context, activity));
        UsuariosDB udb = new UsuariosDB();
        int idUT = udb.obtenerIdUTUsuario(Integer.parseInt(ut.obtenerValor("idUsuario", context)),
                Integer.parseInt(ut.obtenerValor("idTienda", context)), realm).getIdUT();
        cr.setIdUT(idUT);
        CajaXYDTO caja = new CajaXYDTO();
        caja.setId(0);

        caja.setIdUT(idUT);
        caja.setFechaInicio(fechaInicio);
        caja.setMontoInicial(montoInicial);
        cr.setCaja(caja);
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<CajaResponseDTO> call = apiInterface.mandarCaja(cr);
        Gson gson = new Gson();
        String json = gson.toJson(cr);
        CajaService ls = new CajaService(call, idCajaLocal);
        try {
            ls.execute();

        } catch (Exception ex) {
            Utilerias.log(context, ex.getMessage(), ex);
            ex.getMessage();
        }
        return idCajaServer;
    }


    private String hacerCorteCaja(Context c, Activity a, CajaDTOLocal ca, Realm realm) {
        String error = "";
        Utilerias ut = new Utilerias();
        CajaResponseDTO cr = new CajaResponseDTO();
        cr.setIdAndroid(ut.obtenerSerial(c, a));
        UsuariosDB udb = new UsuariosDB();
        int idUT = udb.obtenerIdUTUsuario(Integer.parseInt(ut.obtenerValor("idUsuario", c)),
                Integer.parseInt(ut.obtenerValor("idTienda", c)), realm).getIdUT();
        cr.setIdUT(idUT);

        CajaXYDTO caja = new CajaXYDTO();
        caja.setFechaInicio(ca.getFechaInicio());
        caja.setFechaFin(ca.getFechaFin());
        caja.setMontoInicial(ca.getMontoInicial());
        caja.setId(ca.getIdCajaServer());

        caja.setIdUT(idUT);
        CorteDTO corte = new CorteDTO();
        corte.setEfectivoCal(ca.getEfectivoCalculado());
        corte.setEfectivoCont(ca.getEfectivoContado());
        corte.setIdUserCorte(ca.getIdUsuarioCorte());
        corte.setTarjetaCal(ca.getTarjetaCalculado());
        corte.setTarjetaCont(ca.getTarjetaContado());

        caja.setCorte(corte);
        cr.setCaja(caja);

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<CajaResponseDTO> call = apiInterface.mandarCaja(cr);
        CajaService ls = new CajaService(call, -1);
        try {
            ls.execute();

        } catch (Exception ex) {
            error = ex.getMessage();
        }
        return error;
    }


    public String generarTicket(Context c, Activity a, CajaDTOLocal ca,
                                TicketDTOLocal ti, List<TicketProductoDTOLocal> productos, Activity activity, Realm realm, long idTiendaGlobal) {
        CajasDB cdb = new CajasDB();
        String error = "";
        Utilerias ut = new Utilerias();
        TicketDTO cr = new TicketDTO();
        cr.setIdAndroid(ut.obtenerSerial(c, a));
        UsuariosDB udb = new UsuariosDB();
        int idUT = udb.obtenerIdUTUsuario(Integer.parseInt(ut.obtenerValor("idUsuario", c)),
                Integer.parseInt(ut.obtenerValor("idTienda", c)), realm).getIdUT();
        cr.setIdUT(idUT);
        ProductosDB pdb = new ProductosDB();
        CajaTicketDTO caja = new CajaTicketDTO();
        caja.setFechaInicio(ca.getFechaInicio());
        caja.setId(ca.getIdCajaServer());


        caja.setIdUT(idUT);
        TicketCajaTicketDTO ticket = new TicketCajaTicketDTO();
        ticket.setFolioApp(ti.getIdTicket());
        ticket.setComment(ti.getComentario());
        ticket.setIdUsuario(ca.getIdUsuario());
        ticket.setIdTienda(ca.getIdTienda());
        ticket.setIdCliente(ti.getIdCliente());
        ticket.setCorreoTicket(ti.getCorreoTicket());
        ticket.setCompartirWhatsApp(ti.isCompartirWhatsApp());
        ticket.setTotal(ti.getTotal());
        ticket.setFecha(ti.getFecha());
        ticket.setCambio(ti.getCambio());
        ticket.setEfectivo(ti.getEfectivo());
        ticket.setTarjeta(ti.getTarjeta());
        ticket.setIva(ti.getIva());
        ticket.setTipo(ti.getTipo());
        ticket.setProdEntregado(ti.isProdEntregado());
        ticket.setSubTotal(ti.getSubtotal());
        ticket.setDescuentoEfectivo(ti.getDescuentoEfectivo());
        ticket.setDescuentoPorcentual(ti.getDescuentoPorcentual());
        ticket.setPropinaPorcentual(ti.getPropinaPorcentual());
        ticket.setPropinaEfectivo(ti.getPropinaEfectivo());
        List<VentasXYDTO> ventas = new ArrayList<VentasXYDTO>();
        for (TicketProductoDTOLocal p : productos
        ) {
            VentasXYDTO v = new VentasXYDTO();
            v.setIdProducto(p.getIdProdcutoServer());
            v.setFecha(p.getFecha());
            v.setCantidad(p.getCantidad());
            v.setCantMayoreo(p.getCantidadMayoreo());
            v.setPrecioMayoreo(p.getPrecioMayoreo());
            v.setComision(p.getComision());
            v.setPrecioCompra(p.getPrecioCompra());
            v.setIva(p.getIva());
            v.setIvaTotal(p.getIvaTotal());
            v.setPrecioVenta(p.getPrecioVenta());

            v.setTotal(p.getPrecioVenta() * p.getCantidad());

            List<OpcionPaqueteProductoLocal> opciones = cdb.obtenerOpcionesPaquetesProductos(p.getId(), realm);
            List<OpcionesVentasXYDTO> opcionesVentas = new ArrayList<>();

            if (opciones != null && opciones.size() > 0) {
                for (OpcionPaqueteProductoLocal opa : opciones
                ) {
                    OpcionesVentasXYDTO ven = new OpcionesVentasXYDTO();
                    ven.setCantidad(opa.getCantidad());
                    TicketProductoDTOLocal prodLocal = cdb.obtenerProductosTicketLocal(opa.getIdProducto(), realm);
                    ProductosXYDTO prod = pdb.obtenerproductoServer(prodLocal.getIdProdcutoServer(), realm);
                    ven.setComision(prod.getComision());
                    ven.setIva(prod.getIvaCant());
                    ven.setIdOpcPkt(opa.getIdOpcion());
                    ven.setPrecioCompra(prod.getPrecioCompra());
                    ven.setPrecioPaquete(opa.getPrecio());
                }
            }
            v.setOpcionesVentasxy(opcionesVentas);
            ventas.add(v);
        }
        ticket.setVentasxy(ventas);
        caja.setTicket(ticket);
        cr.setCaja(caja);


        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Gson gson = new Gson();
        String json = gson.toJson(cr);
        Call<TicketDTO> call = apiInterface.mandarTicket(cr);
        TicketService ls = new TicketService(call, ti.getIdTicket(), activity, c, idTiendaGlobal);

        try {
            ls.execute();

        } catch (Exception ex) {
            error = ex.getMessage();
            Utilerias.log(c, "Error: " + ex.getMessage() + " " + ex.getStackTrace(), ex);
        }
        return error;
    }


    public String generarTicketPV(Context c, Activity a, CajaDTOLocal ca,
                                  TicketDTOLocal ti, List<TicketProductoDTOLocal> productos, Activity activity, Realm realm, long idTiendaGlobal) {
        CajasDB cdb = new CajasDB();
        String error = "";
        Utilerias ut = new Utilerias();
        TicketPVDTO cr = new TicketPVDTO();
        cr.setAndroId(ut.obtenerSerial(c, a));
        UsuariosDB udb = new UsuariosDB();
        int idUT = udb.obtenerIdUTUsuario(Integer.parseInt(ut.obtenerValor("idUsuario", c)),
                Integer.parseInt(ut.obtenerValor("idTienda", c)), realm).getIdUT();
        CajaDTOLocal cajaLocalDelTicket = cdb.obtenerCajaPorIdLocal(ti.getIdCaja(), realm);
        cr.setIdCaja(cajaLocalDelTicket.getIdCajaServer());
        cr.setIdUT(idUT);
        cr.setCompartirWhatsApp(ti.isCompartirWhatsApp());
        cr.setComment(ti.getComentario());
        cr.setDescuentoEfectivo("" + ti.getDescuentoEfectivo());
        cr.setDescuentoPorcentual("" + ti.getDescuentoPorcentual());
        cr.setEfectivo("" + ti.getEfectivo());
        cr.setFecha(ti.getFecha());
        cr.setFolioApp("" + ti.getIdTicket());
        cr.setIdCliente(ti.getIdCliente());
        cr.setIdVendedor("");
        cr.setIdMesa("");
        cr.setMandarTicket(ti.getCorreoTicket());
        cr.setIdTienda("" + ca.getIdTienda());
        cr.setIdUsuario("" + ca.getIdUsuario());
        cr.setPropinaEfectivo("" + ti.getPropinaEfectivo());
        cr.setPropinaPorcentual("" + ti.getPropinaPorcentual());
        cr.setSubTotal("" + ti.getSubtotal());
        cr.setTotal("" + ti.getTotal());
        cr.setTarjeta("" + ti.getTarjeta());
        cr.setTipo(ti.getTipo());
        cr.setOnline(false);
        cr.setProdEntregado(ti.isProdEntregado());
        if (ti.getIdEdit() > 0) {
            cr.setIdUtEdit(idUT);
            cr.setId(""+ti.getIdEdit());
        }

        ProductosDB pdb = new ProductosDB();

        List<VentasXYDTO> ventas = new ArrayList<VentasXYDTO>();
        for (TicketProductoDTOLocal p : productos
        ) {
            VentasXYDTO v = new VentasXYDTO();
            v.setId("");
            v.setIdProducto(p.getIdProdcutoServer());
            v.setFecha(p.getFecha());
            v.setCantidad(p.getCantidad());
            v.setCantMayoreo(p.getCantidadMayoreo());
            v.setPrecioMayoreo(p.getPrecioMayoreo());
            v.setComision(p.getComision());
            v.setPrecioCompra(p.getPrecioCompra());
            v.setIva(p.getIva());
            v.setIvaTotal(p.getIvaTotal());
            v.setPrecioVenta(p.getPrecioVenta());

            v.setTotal(p.getPrecioVenta() * p.getCantidad());

            List<OpcionPaqueteProductoLocal> opciones = cdb.obtenerOpcionesPaquetesProductos(p.getId(), realm);
            List<OpcionesVentasXYDTO> opcionesVentas = new ArrayList<>();

            if (opciones != null && opciones.size() > 0) {
                for (OpcionPaqueteProductoLocal opa : opciones
                ) {
                    OpcionesVentasXYDTO ven = new OpcionesVentasXYDTO();
                    ven.setCantidad(opa.getCantidad());
                    TicketProductoDTOLocal prodLocal = cdb.obtenerProductosTicketLocal(opa.getIdProducto(), realm);
                    ProductosXYDTO prod = pdb.obtenerproductoServer(prodLocal.getIdProdcutoServer(), realm);
                    ven.setComision(prod.getComision());
                    ven.setIva(prod.getIvaCant());
                    ven.setIdOpcPkt(opa.getIdOpcion());
                    ven.setPrecioCompra(prod.getPrecioCompra());
                    ven.setPrecioPaquete(opa.getPrecio());
                    opcionesVentas.add(ven);
                }
            }
            v.setOpcionesVentasxy(opcionesVentas);
            ventas.add(v);
        }
        cr.setVentasxy(ventas);

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Gson gson = new Gson();
        String json = gson.toJson(cr);
        if (ti.getIdEdit() > 0) {
            Call<TicketPVDTO> call = apiInterface.editarPedido(cr);
            TicketPVService ls = new TicketPVService(call, ti.getIdTicket(), activity, c, idTiendaGlobal);

            try {
                ls.execute();

            } catch (Exception ex) {
                error = ex.getMessage();
                Utilerias.log(c,"No se subió el ticket",ex);
            }
        }
        else {

            Call<TicketPVDTO> call = apiInterface.mandarTicketPV(cr);
            TicketPVService ls = new TicketPVService(call, ti.getIdTicket(), activity, c, idTiendaGlobal);

            try {
                ls.execute();


            } catch (Exception ex) {
                error = ex.getMessage();
                Utilerias.log(c,"No se subió el ticket",ex);
            }
        }
        return error;
    }


}
