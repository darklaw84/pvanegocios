package com.anegocios.puntoventa.database;

import com.anegocios.puntoventa.bdlocal.CajaDTOLocal;
import com.anegocios.puntoventa.bdlocal.OpcionPaqueteProductoLocal;
import com.anegocios.puntoventa.bdlocal.TicketDTOLocal;
import com.anegocios.puntoventa.bdlocal.TicketProductoDTOLocal;
import com.anegocios.puntoventa.dtosauxiliares.PaqueteOpcionAux;
import com.anegocios.puntoventa.utils.Utilerias;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class CajasDB {


    public List<OpcionPaqueteProductoLocal> obtenerOpcionesPaquetesProductos(long idProducto, Realm realm) {

        Utilerias ut = new Utilerias();

        List<OpcionPaqueteProductoLocal> opciones = new ArrayList<>();
        if (realm != null) {
            opciones = realm.where(OpcionPaqueteProductoLocal.class).equalTo("idProducto", idProducto)
                    .findAll();
        }
        return opciones;

    }

    public List<TicketDTOLocal> obtenerTicketsCaja(int idCaja, Realm realm) {

        Utilerias ut = new Utilerias();

        List<TicketDTOLocal> tickets = new ArrayList<>();
        if (realm != null) {
            tickets = realm.where(TicketDTOLocal.class)
                    .equalTo("idCaja", idCaja)
                    .and()
                    .equalTo("enviadoServer", false)
                    .sort("idTicket").findAll();
        }
        return tickets;
    }

    public List<TicketDTOLocal> obtenerTicketsCajaLocales(int idCaja, Realm realm) {

        Utilerias ut = new Utilerias();

        List<TicketDTOLocal> ticketsCaja = new ArrayList<>();
        if (realm != null) {
            ticketsCaja = realm.where(TicketDTOLocal.class)
                    .equalTo("idCaja", idCaja)
                    .sort("idTicket").findAll();
        }
        return ticketsCaja;
    }




    public TicketDTOLocal obtenerTicketCaja(long idTicket, Realm realm) {

        Utilerias ut = new Utilerias();

        TicketDTOLocal ticket = new TicketDTOLocal();
        if (realm != null) {
            ticket = realm.where(TicketDTOLocal.class)
                    .equalTo("idTicket", idTicket)
                    .findFirst();
        }
        return ticket;

    }

    public List<TicketDTOLocal> obtenerTicketsCajaHacerCalculos(int idCaja, Realm realm) {

        Utilerias ut = new Utilerias();

        List<TicketDTOLocal> ticketsCaja = new ArrayList<>();

        if (realm != null) {
            ticketsCaja = realm.where(TicketDTOLocal.class)
                    .equalTo("idCaja", idCaja)
                    .findAll();
        }
        return ticketsCaja;
    }

    public List<TicketProductoDTOLocal> obtenerProductosTicket(int idTicket, Realm realm) {

        Utilerias ut = new Utilerias();

        List<TicketProductoDTOLocal> productosTicket = new ArrayList<>();
        if (realm != null) {
            productosTicket = realm.where(TicketProductoDTOLocal.class)
                    .equalTo("idTicket", idTicket).findAll();
            ProductosDB pdb = new ProductosDB();
            for (TicketProductoDTOLocal pro : productosTicket
            ) {
                if (pro.getIdProdcutoServer() == 0) {
                    if (!realm.isInTransaction()) {
                        realm.beginTransaction();
                    }

                    pro.setIdProdcutoServer(pdb.obtenerproductoLocal(pro.getIdProductoLocal(), realm).getIdServer());

                    realm.commitTransaction();

                }
            }
        }

        return productosTicket;
    }


    public TicketProductoDTOLocal obtenerProductosTicketLocal(long idProducto, Realm realm) {

        Utilerias ut = new Utilerias();

        TicketProductoDTOLocal ticket = new TicketProductoDTOLocal();
        if (realm != null) {
            ticket = realm.where(TicketProductoDTOLocal.class)
                    .equalTo("id", idProducto).findFirst();
        }
        return ticket;
    }

    public CajaDTOLocal obtenerCajaActual(int idUsuario, int idTienda, Realm realm) {
        //este metodo tiene que revisar si hay alguna caja ya abierta para el usuario, tienda
        // si es asi, devolverla
        //si no devuelve null
        Utilerias ut = new Utilerias();

        CajaDTOLocal caja = new CajaDTOLocal();
        if (realm != null) {
            return caja = realm.where(CajaDTOLocal.class).equalTo("idUsuario", idUsuario)
                    .and().equalTo("idTienda", idTienda)
                    .and().equalTo("tieneCorteLocal", false)
                    .and().equalTo("creadoCorte", false).findFirst();
        } else {
            return null;
        }

    }

    public CajaDTOLocal abrirNuevaCaja(double montoInicial, String fechaInicio,
                                       int idUsuario, int idTienda, Realm realm) {


        Utilerias ut = new Utilerias();

        CajaDTOLocal p = new CajaDTOLocal();
        if (realm != null) {
            if (!realm.isInTransaction()) {
                realm.beginTransaction();
            }

            Number maxId = realm.where(CajaDTOLocal.class).max("idCaja");

            int nextId = (maxId == null) ? 1 : maxId.intValue() + 1;
            p = realm.createObject(CajaDTOLocal.class, nextId);

            p.setFechaInicio(fechaInicio);
            p.setMontoInicial(montoInicial);
            p.setIdUsuario(idUsuario);
            p.setIdTienda(idTienda);
            p.setCreadaServer(false);
            p.setCreadoCorte(false);
            p.setTieneCorteLocal(false);
            realm.insert(p);
            realm.commitTransaction();


        }
        return p;
    }


    public TicketDTOLocal crearTicket(TicketDTOLocal ti, Realm realm) {


        Utilerias ut = new Utilerias();

        TicketDTOLocal p = new TicketDTOLocal();
        if (realm != null) {
            if (!realm.isInTransaction()) {
                realm.beginTransaction();
            }

            Number maxId = realm.where(TicketDTOLocal.class).max("idTicket");

            int nextId = (maxId == null) ? 1 : maxId.intValue() + 1;
            p = realm.createObject(TicketDTOLocal.class, nextId);

            p.setCambio(ti.getCambio());
            p.setComentario(ti.getComentario());
            p.setCorreoTicket(ti.getCorreoTicket());
            p.setDescuentoEfectivo(ti.getDescuentoEfectivo());
            p.setDescuentoPorcentual(ti.getDescuentoPorcentual());
            p.setEfectivo(ti.getEfectivo());
            p.setIdEdit(ti.getIdEdit());
            p.setFecha(ti.getFecha());
            p.setIdCaja(ti.getIdCaja());
            p.setIdCliente(ti.getIdCliente());
            p.setIva(ti.getIva());
            p.setCompartirWhatsApp(ti.isCompartirWhatsApp());
            p.setSubtotal(ti.getSubtotal());
            p.setTarjeta(ti.getTarjeta());
            p.setTipo(ti.getTipo());
            p.setProdEntregado(ti.isProdEntregado());
            p.setTotal(ti.getTotal());
            p.setEnviadoServer(false);
            p.setPropinaPorcentual(ti.getPropinaPorcentual());
            p.setPropinaEfectivo(ti.getPropinaEfectivo());
            p.setDescuentoTotal(ti.getDescuentoTotal());
            p.setPropinaTotal(ti.getPropinaTotal());
            p.setTipoCliente(ti.getTipoCliente());

            realm.insert(p);
            realm.commitTransaction();
        }

        return p;
    }


    public TicketProductoDTOLocal crearProducto(TicketProductoDTOLocal ti, int idTicket, Realm realm) {


        Utilerias ut = new Utilerias();

        TicketProductoDTOLocal p = new TicketProductoDTOLocal();
        if (realm != null) {
            if (!realm.isInTransaction()) {
                realm.beginTransaction();
            }

            Number maxId = realm.where(TicketProductoDTOLocal.class).max("id");

            int nextId = (maxId == null) ? 1 : maxId.intValue() + 1;
            p = realm.createObject(TicketProductoDTOLocal.class, nextId);

            p.setCantidad(ti.getCantidad());
            p.setCantidadMayoreo(ti.getCantidadMayoreo());
            p.setComision(ti.getComision());
            p.setFecha(ti.getFecha());
            p.setIdProdcutoServer(ti.getIdProdcutoServer());
            p.setIdProductoLocal(ti.getIdProductoLocal());
            p.setIva(ti.getIva());
            p.setIdTicket(idTicket);
            p.setIvaTotal(ti.getIvaTotal());
            p.setPrecioCompra(ti.getPrecioCompra());
            p.setPrecioMayoreo(ti.getPrecioMayoreo());
            p.setPrecioVenta(ti.getPrecioVenta());
            p.setTotal(ti.getTotal());

            realm.insert(p);
            realm.commitTransaction();
        }

        return p;
    }


    public void crearOpcionProducto(PaqueteOpcionAux po, long idProducto, Realm realm) {


        Utilerias ut = new Utilerias();

        if (realm != null) {
            if (!realm.isInTransaction()) {
                realm.beginTransaction();
            }

            Number maxId = realm.where(OpcionPaqueteProductoLocal.class).max("id");

            int nextId = (maxId == null) ? 1 : maxId.intValue() + 1;
            OpcionPaqueteProductoLocal p = realm.createObject(OpcionPaqueteProductoLocal.class, nextId);

            p.setCantidad(po.getCantidad());
            p.setIdPaquete(po.getIdPaquete());
            p.setIdOpcion(po.getIdOpcion());
            p.setPrecio(po.getPrecio());
            p.setTotal(po.getCantidad() * po.getPrecio());
            p.setIdProducto(idProducto);
            p.setDescripcion(po.getDescripion());
            p.setMostrar(po.isMostrar());

            realm.insert(p);
            realm.commitTransaction();
        }

    }


    public void hacerCorte(int idUsuario, int idTienda, CajaDTOLocal cajaCorte, Realm realm) {

        Utilerias ut = new Utilerias();

        if (realm != null) {
            if (!realm.isInTransaction()) {
                realm.beginTransaction();
            }
            CajaDTOLocal cajaActual = obtenerCajaActual(idUsuario, idTienda, realm);
            cajaActual.setFechaFin(cajaCorte.getFechaFin());
            cajaActual.setEfectivoCalculado(cajaCorte.getEfectivoCalculado());
            cajaActual.setTarjetaCalculado(cajaCorte.getTarjetaCalculado());
            cajaActual.setEfectivoContado(cajaCorte.getEfectivoContado());
            cajaActual.setTarjetaContado(cajaCorte.getTarjetaContado());
            cajaActual.setIdUsuarioCorte(idUsuario);
            cajaActual.setTieneCorteLocal(true);
            realm.insertOrUpdate(cajaActual);
            realm.commitTransaction();
        }
    }


    public List<CajaDTOLocal> obtenerCajasParaEnviar(int idTienda, Realm realm) {

        Utilerias ut = new Utilerias();

        List<CajaDTOLocal> cajas = new ArrayList<>();
        if (realm != null) {
            cajas = realm.where(CajaDTOLocal.class).equalTo("creadaServer", false)
                    .and()
                    .equalTo("idTienda", idTienda)
                    .findAll();
        }
        return cajas;

    }


    public CajaDTOLocal obtenerCajaPorIdLocal(int idCaja, Realm realm) {

        Utilerias ut = new Utilerias();

        CajaDTOLocal cajas = null;
        if (realm != null) {
            cajas = realm.where(CajaDTOLocal.class)
                    .equalTo("idCaja", idCaja)
                    .findFirst();
        }
        return cajas;

    }

    public List<CajaDTOLocal> obtenerCajasAbiertasParaRevisarEnviar(int idTienda, Realm realm) {

        Utilerias ut = new Utilerias();

        List<CajaDTOLocal> cajas = new ArrayList<>();
        if (realm != null) {
            cajas = realm.where(CajaDTOLocal.class).equalTo("creadaServer", true)
                    .and()
                    .equalTo("creadoCorte", false)
                    .and()
                    .equalTo("idTienda", idTienda)
                    .findAll();
        }
        return cajas;

    }


    public void actualizarIdServerCaja(int idCaja, int idCajaServer, Realm realm) {

        Utilerias ut = new Utilerias();

        if (realm != null) {
            if (!realm.isInTransaction()) {
                realm.beginTransaction();
            }
            CajaDTOLocal caja = realm.where(CajaDTOLocal.class).equalTo("idCaja", idCaja)
                    .findFirst();
            caja.setIdCajaServer(idCajaServer);
            caja.setCreadaServer(true);
            realm.insertOrUpdate(caja);
            realm.commitTransaction();
        }

    }


    public void actualizarCorteCreadoCaja( int idCajaServer, Realm realm) {

        Utilerias ut = new Utilerias();

        if (realm != null) {
            if (!realm.isInTransaction()) {
                realm.beginTransaction();
            }
            CajaDTOLocal caja = realm.where(CajaDTOLocal.class).equalTo("idCajaServer", idCajaServer)
                    .findFirst();
            caja.setCreadoCorte(true);
            realm.insertOrUpdate(caja);
            realm.commitTransaction();
        }

    }


    public void actualizarTicketEnviado(int idTicket, Realm realm) {

        Utilerias ut = new Utilerias();

        if (realm != null) {
            if (!realm.isInTransaction()) {
                realm.beginTransaction();
            }
            TicketDTOLocal ticket = realm.where(TicketDTOLocal.class)
                    .equalTo("idTicket", idTicket)
                    .findFirst();
            ticket.setEnviadoServer(true);
            realm.insertOrUpdate(ticket);
            realm.commitTransaction();
        }


    }

    public void actualizarIdFolioTicketServer(long idTicket, long folio, Realm realm) {

        Utilerias ut = new Utilerias();

        if (realm != null) {
            if (!realm.isInTransaction()) {
                realm.beginTransaction();
            }
            TicketDTOLocal p = realm.where(TicketDTOLocal.class).equalTo("idTicket", idTicket).findFirst();
            p.setIdFolioServer(folio);
            realm.insertOrUpdate(p);
            realm.commitTransaction();
        }

    }

}
