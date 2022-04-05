package com.anegocios.puntoventa.database;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.anegocios.puntoventa.bdlocal.ProductosXYDTOLocal;
import com.anegocios.puntoventa.dtosauxiliares.PaqueteOpcionAux;
import com.anegocios.puntoventa.dtosauxiliares.ProductosXYDTOAux;
import com.anegocios.puntoventa.jsons.OpcionesPaquete;
import com.anegocios.puntoventa.jsons.OpcionesPaqueteXY;
import com.anegocios.puntoventa.jsons.PaqueteXYDTO;
import com.anegocios.puntoventa.jsons.PaquetesProducto;
import com.anegocios.puntoventa.jsons.ProductosXYDTO;
import com.anegocios.puntoventa.utils.ImageSeek;
import com.anegocios.puntoventa.utils.Utilerias;

import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;

public class ProductosDB {

    public void actualizarBDProductos(List<ProductosXYDTO> productos, int idTienda,
                                      boolean aplicacionOnline, boolean hayconexion, Realm realm, Context context) {

        Utilerias ut = new Utilerias();
        try {
            if (realm != null) {

                for (ProductosXYDTO ps : productos) {
                    if(ps.getDescripcionCorta().contains("v"))
                    {
                        System.out.println(ps);
                    }
                    if (ps.isActivo()) {

                        if (!realm.isInTransaction()) {
                            realm.beginTransaction();
                        }
                        ProductosXYDTO productoGuardado = realm.where(ProductosXYDTO.class).
                                equalTo("id", ps.getId()).findFirst();
                        if (productoGuardado != null && productoGuardado.getId() > 0) {
                            //productoGuardado.deleteFromRealm();
                            // ProductosXYDTO p = realm.createObject(ProductosXYDTO.class, ps.getId());
                            ProductosXYDTO p = productoGuardado;
                            p.setCodigoBarras(ps.getCodigoBarras());
                            p.setActivo(ps.isActivo());
                            p.setIvaCant(ps.getIvaCant());
                            p.setPaquete(ps.isPaquete());
                            p.setIva(ps.isIva());
                            p.setExistencias(ps.getExistencias());
                            p.setComision(ps.getComision());
                            p.setIdTienda(idTienda);
                            p.setCantidadMayoreo(ps.getCantidadMayoreo());
                            p.setPrecioMayoreo(ps.getPrecioMayoreo());
                            p.setPrecioCompra(ps.getPrecioCompra());
                            p.setDescripcionCorta(ps.getDescripcionCorta());
                            p.setPrecioVenta(ps.getPrecioVenta());
                            p.setImgProdURL(ps.getImgProdURL());
                            if (ps.getIdGrupoVR() != null && ps.getIdGrupoVR().getNombre() != null
                                    && ps.getIdGrupoVR().getColor() != null) {
                                p.setNombreGrupo(ps.getIdGrupoVR().getNombre());
                                p.setColorGrupo(ps.getIdGrupoVR().getColor());
                                p.setIdVR(ps.getIdGrupoVR().getId());
                            }
                            if (hayconexion && p.getImgProdURL() != null
                                    && p.getImgProdURL().length() > 0) {
                                try {
                                    Bitmap imagen = obtenerImagen(p.getImgProdURL());
                                    if (imagen != null) {
                                        String imagenS = ut.BitMapToStringLocal(imagen);
                                        if (imagenS != null && imagenS.length() > 0) {
                                            p.setImagenGuardada(imagenS);
                                        }
                                    }
                                } catch (Exception ex) {
                                    Utilerias.log(context, "error actualizando imagen", ex);
                                }
                            }


                            realm.insertOrUpdate(p);
                            realm.commitTransaction();
                        } else {
                            ProductosXYDTO p = realm.createObject(ProductosXYDTO.class, ps.getId());
                            p.setCodigoBarras(ps.getCodigoBarras());
                            p.setActivo(ps.isActivo());
                            p.setIvaCant(ps.getIvaCant());
                            p.setIva(ps.isIva());
                            p.setComision(ps.getComision());
                            p.setCantidadMayoreo(ps.getCantidadMayoreo());
                            p.setPrecioMayoreo(ps.getPrecioMayoreo());
                            p.setPrecioCompra(ps.getPrecioCompra());
                            p.setExistencias(ps.getExistencias());
                            p.setIdTienda(idTienda);
                            p.setDescripcionCorta(ps.getDescripcionCorta());
                            p.setPrecioVenta(ps.getPrecioVenta());
                            p.setImgProdURL(ps.getImgProdURL());
                            p.setPaquete(ps.isPaquete());
                            if (ps.getIdGrupoVR() != null && ps.getIdGrupoVR().getNombre() != null
                                    && ps.getIdGrupoVR().getColor() != null) {
                                p.setNombreGrupo(ps.getIdGrupoVR().getNombre());
                                p.setColorGrupo(ps.getIdGrupoVR().getColor());
                                p.setIdVR(ps.getIdGrupoVR().getId());
                            }
                            if (hayconexion && p.getImgProdURL() != null && p.getImgProdURL().length() > 0) {
                                try {
                                    Bitmap imagen = obtenerImagen(p.getImgProdURL());
                                    if (imagen != null) {
                                        p.setImagenGuardada(ut.BitMapToStringLocal(imagen));
                                    }
                                } catch (Exception ex) {
                                    String datos = ex.getMessage();
                                }
                            }
                            realm.insert(p);
                            realm.commitTransaction();
                        }


                    } else {
                        if (!realm.isInTransaction()) {
                            realm.beginTransaction();
                        }
                        ProductosXYDTO productoGuardado = realm.where(ProductosXYDTO.class).
                                equalTo("id", ps.getId()).findFirst();
                        if (productoGuardado != null) {
                            productoGuardado.deleteFromRealm();
                        }
                        realm.commitTransaction();
                    }


                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }

    }


    public void actualizarExistenciaServer(int idProdServer, double cantidad, Realm realm, boolean isPaquete, List<PaqueteOpcionAux> opciones) {

        if (realm != null) {
            if (!isPaquete) {
                if (!realm.isInTransaction()) {
                    realm.beginTransaction();
                }
                ProductosXYDTO pg = realm.where(ProductosXYDTO.class).
                        equalTo("id", idProdServer).findFirst();
                if (pg != null && pg.getId() > 0) {
                    pg.setExistencias(pg.getExistencias() - cantidad);
                    realm.insertOrUpdate(pg);
                    realm.commitTransaction();
                }
            } else {
                //si es paquete
                if (opciones != null && opciones.size() > 0) {
                    for (PaqueteOpcionAux o : opciones) {
                        actualizarExistenciaServer(o.getIdProducto(), cantidad * o.getCantidad(), realm, false, null);
                    }
                }
            }
        }
    }

    public void actualizarExistenciaLocal(int idProdLocal, double cantidad, Realm realm) {

        if (realm != null) {
            if (!realm.isInTransaction()) {
                realm.beginTransaction();
            }
            ProductosXYDTOLocal pg = realm.where(ProductosXYDTOLocal.class).
                    equalTo("id", idProdLocal).findFirst();
            if (pg != null && pg.getId() > 0) {
                pg.setExistencias(pg.getExistencias() - cantidad);
                realm.insertOrUpdate(pg);
                realm.commitTransaction();
            }
        }
    }





    public static Bitmap obtenerImagen(String src) {
        try {

            src= src.replace("http://anegocios.com","http://www.anegocios.com");
            src= src.replace("http","https");
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();

            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }




    public List<ProductosXYDTOAux> obtenerProductosCompletos(int idTienda, Realm realm) {
        List<ProductosXYDTOAux> completos = new ArrayList<ProductosXYDTOAux>();
        List<ProductosXYDTO> productosServer = obtenerListaProductos(idTienda, realm);

        for (ProductosXYDTO p : productosServer
        ) {
            completos.add(new ProductosXYDTOAux(p, "S"));
        }

        List<ProductosXYDTOLocal> productosLocal = obtenerListaProductosLocales(idTienda, realm);

        for (ProductosXYDTOLocal p : productosLocal
        ) {
            completos.add(new ProductosXYDTOAux(p, "L"));
        }
        if (completos != null && completos.size() > 0) {
            Collections.sort(completos, new Comparator<ProductosXYDTOAux>() {
                public int compare(ProductosXYDTOAux obj1, ProductosXYDTOAux obj2) {
                    // ## Ascending order
                    return obj1.getDescripcionCorta().compareToIgnoreCase(obj2.getDescripcionCorta()); // To compare string values
                    // return Integer.valueOf(obj1.empId).compareTo(Integer.valueOf(obj2.empId)); // To compare integer values

                    // ## Descending order
                    // return obj2.firstName.compareToIgnoreCase(obj1.firstName); // To compare string values
                    // return Integer.valueOf(obj2.empId).compareTo(Integer.valueOf(obj1.empId)); // To compare integer values
                }
            });
        }
        return completos;
    }


    public List<ProductosXYDTOAux> obtenerProductosCompletosGrupo(int idGrupo, int idTienda, Realm realm) {
        List<ProductosXYDTOAux> completos = new ArrayList<ProductosXYDTOAux>();
        List<ProductosXYDTO> productosServer = obtenerListaProductosGrupoServer(idGrupo, idTienda, realm);

        for (ProductosXYDTO p : productosServer
        ) {
            completos.add(new ProductosXYDTOAux(p, "S"));
        }

        List<ProductosXYDTOLocal> productosLocal = obtenerListaProductosGrupoLocal(idGrupo, idTienda, realm);

        for (ProductosXYDTOLocal p : productosLocal
        ) {
            completos.add(new ProductosXYDTOAux(p, "L"));
        }
        if (completos != null && completos.size() > 0) {
            Collections.sort(completos, new Comparator<ProductosXYDTOAux>() {
                public int compare(ProductosXYDTOAux obj1, ProductosXYDTOAux obj2) {
                    // ## Ascending order
                    return obj1.getDescripcionCorta().compareToIgnoreCase(obj2.getDescripcionCorta()); // To compare string values
                    // return Integer.valueOf(obj1.empId).compareTo(Integer.valueOf(obj2.empId)); // To compare integer values

                    // ## Descending order
                    // return obj2.firstName.compareToIgnoreCase(obj1.firstName); // To compare string values
                    // return Integer.valueOf(obj2.empId).compareTo(Integer.valueOf(obj1.empId)); // To compare integer values
                }
            });
        }

        return completos;
    }


    public List<ProductosXYDTOAux> obtenerProductosCompletosPatron(String patron, int idTienda, Realm realm) {
        List<ProductosXYDTOAux> completos = new ArrayList<ProductosXYDTOAux>();
        List<ProductosXYDTO> productosServer = obtenerListaProductosServer(patron, idTienda, realm);

        for (ProductosXYDTO p : productosServer
        ) {
            completos.add(new ProductosXYDTOAux(p, "S"));
        }

        List<ProductosXYDTOLocal> productosLocal = obtenerListaProductosLocal(patron, idTienda, realm);

        for (ProductosXYDTOLocal p : productosLocal
        ) {
            completos.add(new ProductosXYDTOAux(p, "L"));
        }
        if (completos != null && completos.size() > 0) {
            Collections.sort(completos, new Comparator<ProductosXYDTOAux>() {
                public int compare(ProductosXYDTOAux obj1, ProductosXYDTOAux obj2) {
                    // ## Ascending order
                    return obj1.getDescripcionCorta().compareToIgnoreCase(obj2.getDescripcionCorta()); // To compare string values
                    // return Integer.valueOf(obj1.empId).compareTo(Integer.valueOf(obj2.empId)); // To compare integer values

                    // ## Descending order
                    // return obj2.firstName.compareToIgnoreCase(obj1.firstName); // To compare string values
                    // return Integer.valueOf(obj2.empId).compareTo(Integer.valueOf(obj1.empId)); // To compare integer values
                }
            });
        }

        return completos;
    }


    public List<ProductosXYDTO> obtenerListaProductos(int idTienda, Realm realm) {


        List<ProductosXYDTO> productos = new ArrayList<ProductosXYDTO>();
        if (realm != null) {
            productos = realm.where(ProductosXYDTO.class)
                    .equalTo("idTienda", idTienda)
                    .and()
                    .equalTo("activo", true)
                    .sort("descripcionCorta").findAll();
        }

        //si son server puede que alguno sea paquete, hay que verfificarlo y actualizar su existencia
        for (ProductosXYDTO p : productos) {
            try {
                if (p.isPaquete()) {
                    double existencia = obtenerExistenciaPaquete(p, realm);
                    if (!realm.isInTransaction()) {
                        realm.beginTransaction();
                    }
                    p.setExistencias(existencia);
                    realm.insertOrUpdate(p);
                    realm.commitTransaction();
                }
            } catch (Exception e) {

            }
        }
        return productos;
    }


    public List<ProductosXYDTOLocal> obtenerListaProductosLocales(int idTienda, Realm realm) {


        List<ProductosXYDTOLocal> productos = new ArrayList<ProductosXYDTOLocal>();
        if (realm != null) {
            productos = realm.where(ProductosXYDTOLocal.class)
                    .equalTo("enviado", false)
                    .and()
                    .equalTo("idTienda", idTienda)
                    .sort("descripcionCorta").findAll();
        }
        return productos;
    }


    public List<ProductosXYDTOLocal> obtenerListaProductosLocalesEnviar(int idTienda, Realm realm) {


        List<ProductosXYDTOLocal> productos = new ArrayList<ProductosXYDTOLocal>();
        if (realm != null) {
            productos = realm.where(ProductosXYDTOLocal.class).equalTo("enviado", false)
                    .and()
                    .equalTo("idTienda", idTienda).findAll();
        }
        return productos;
    }

    public List<ProductosXYDTO> obtenerListaProductosServerEditados(int idTienda, Realm realm) {


        List<ProductosXYDTO> productos = new ArrayList<ProductosXYDTO>();
        if (realm != null) {
            productos = realm.where(ProductosXYDTO.class).equalTo("editado", true)
                    .and()
                    .equalTo("idTienda", idTienda).findAll();
        }
        return productos;
    }


    public List<ProductosXYDTO> obtenerListaProductosGrupoServer(int idGrupo, int idTienda, Realm realm) {

        List<ProductosXYDTO> productos = new ArrayList<ProductosXYDTO>();
        try {
            if (realm != null) {
                productos = realm.where(ProductosXYDTO.class)
                        .equalTo("idTienda", idTienda)
                        .and()
                        .equalTo("idVR", idGrupo)
                        .and()
                        .equalTo("activo", true)
                        .sort("descripcionCorta").findAll();
            }


            for (ProductosXYDTO p : productos) {
                try {
                    if (p.isPaquete()) {
                        double existencia = obtenerExistenciaPaquete(p, realm);
                        if (!realm.isInTransaction()) {
                            realm.beginTransaction();
                        }
                        p.setExistencias(existencia);
                        realm.insertOrUpdate(p);
                        realm.commitTransaction();
                    }
                } catch (Exception e) {

                }
            }
        } catch (Exception ex) {
            System.out.println("obtenerListaProductosGrupoServer: " + ex.getMessage());
        }
        return productos;
    }

    public List<ProductosXYDTOLocal> obtenerListaProductosGrupoLocal(int idGrupo, int idTienda, Realm realm) {

        List<ProductosXYDTOLocal> productos = new ArrayList<ProductosXYDTOLocal>();
        try {
            if (realm != null) {
                productos = realm.where(ProductosXYDTOLocal.class)
                        .equalTo("idTienda", idTienda)
                        .and()
                        .equalTo("idVR", idGrupo)
                        .and().equalTo("enviado", false)
                        .sort("descripcionCorta").findAll();
            }
        } catch (Exception ex) {
            System.out.println("obtenerListaProductosGrupoLocal: " + ex.getMessage());
        }
        return productos;
    }


    public void actualizarIdProductoLocalPorIdProductoServer(int idServer, int idLocal, Realm realm) {

        if (realm != null) {
            if (!realm.isInTransaction()) {
                realm.beginTransaction();
            }
            ProductosXYDTOLocal p = realm.where(ProductosXYDTOLocal.class).equalTo("id", idLocal).findFirst();
            p.setEnviado(true);
            p.setIdServer(idServer);
            realm.insertOrUpdate(p);
            realm.commitTransaction();
        }

    }


    public void actualizarProductoServerEditadoFalse(int idServer, Realm realm) {

        if (realm != null) {
            if (!realm.isInTransaction()) {
                realm.beginTransaction();
            }
            ProductosXYDTO p = realm.where(ProductosXYDTO.class).equalTo("id", idServer).findFirst();
            p.setEditado(false);
            realm.insertOrUpdate(p);
            realm.commitTransaction();
        }

    }


    public List<ProductosXYDTO> obtenerListaProductosServer(String patron, int idTienda, Realm realm) {

        List<ProductosXYDTO> regs = new ArrayList<ProductosXYDTO>();
        try {
            if (realm != null) {
                regs = realm.where(ProductosXYDTO.class)
                        .equalTo("idTienda", idTienda)
                        .and()
                        .equalTo("activo", true)
                        .and()
                        .beginGroup()
                        .contains("codigoBarras", patron, Case.INSENSITIVE).or()
                        .contains("descripcionCorta", patron, Case.INSENSITIVE)
                        .endGroup().sort("descripcionCorta").findAll();
            }

            //si son server puede que alguno sea paquete, hay que verfificarlo y actualizar su existencia
            for (ProductosXYDTO p : regs) {
                try {
                    if (p.isPaquete()) {
                        double existencia = obtenerExistenciaPaquete(p, realm);
                        if (!realm.isInTransaction()) {
                            realm.beginTransaction();
                        }
                        p.setExistencias(existencia);
                        realm.insertOrUpdate(p);
                        realm.commitTransaction();
                    }
                } catch (Exception e) {

                }
            }
        } catch (Exception ex) {
            System.out.println("obtenerListaProductosServer: " + ex.getMessage());
        }
        return regs;
    }

    public double obtenerExistenciaPaquete(ProductosXYDTO p, Realm realm) {
        double existencia = 0;
        boolean primero = true;

        List<PaquetesProducto> paquetes = obtenerPaquetesProductos(p.getId(), realm);
        if (paquetes != null && paquetes.size() > 0) {
            for (PaquetesProducto pa : paquetes) {
                List<OpcionesPaquete> opciones = obtenerOpcionesPaquete(pa.getId(), realm);
                for (OpcionesPaquete o : opciones) {
                    if (o.getCantidad() > 0) {
                        ProductosXYDTO producto = obtenerproductoServer(o.getIdProducto(), realm);
                        if (producto != null) {
                            if (primero) {

                                existencia = producto.getExistencias() / o.getCantidad();
                                primero = false;

                            } else {
                                double posibles = producto.getExistencias() / o.getCantidad();

                                if (existencia > posibles) {
                                    existencia = posibles;
                                }
                            }
                        }
                    }
                }
            }
        }


        return existencia;

    }

    public List<ProductosXYDTOLocal> obtenerListaProductosLocal(String patron, int idTienda, Realm realm) {

        List<ProductosXYDTOLocal> regs = new ArrayList<ProductosXYDTOLocal>();
        try {
            if (realm != null) {
                regs = realm.where(ProductosXYDTOLocal.class)
                        .beginGroup()
                        .contains("codigoBarras", patron, Case.INSENSITIVE).or()
                        .contains("descripcionCorta", patron, Case.INSENSITIVE).endGroup()
                        .and().equalTo("enviado", false)
                        .and()
                        .equalTo("idTienda", idTienda)
                        .sort("descripcionCorta").findAll();
            }
        } catch (Exception ex) {
            System.out.println("obtenerListaProductosLocal: " + ex.getMessage());
        }
        return regs;
    }

    public ProductosXYDTOLocal obtenerproductoLocal(int id, Realm realm) {

        ProductosXYDTOLocal regs = new ProductosXYDTOLocal();
        if (realm != null) {
            regs = realm.where(ProductosXYDTOLocal.class)
                    .equalTo("id", id)
                    .findFirst();
        }
        return regs;

    }


    public ProductosXYDTO obtenerproductoServer(long id, Realm realm) {

        ProductosXYDTO pro = new ProductosXYDTO();
        if (realm != null) {
            pro = realm.where(ProductosXYDTO.class)
                    .equalTo("id", id)
                    .findFirst();
        }
        return pro;

    }

    public String actualizarProductoLocal(ProductosXYDTOAux p, Realm realm) {

        String error = "";

        if (realm != null) {
            if (!realm.isInTransaction()) {
                realm.beginTransaction();
            }

            boolean exito = true;

            ProductosXYDTOLocal pe = realm.where(ProductosXYDTOLocal.class)
                    .equalTo("id", p.getId()).findFirst();
            if (pe == null) {
                error = "Ya no existe el producto";
            } else {
                pe.setIdVR(p.getIdVR());
                pe.setEnviado(false);
                pe.setCantidadMayoreo(p.getCantidadMayoreo());
                pe.setCodigoBarras(p.getCodigoBarras());
                pe.setComision(p.getComision());
                pe.setDescripcionCorta(p.getDescripcionCorta());
                pe.setIva(p.isIva());
                pe.setIvaCant(p.getIvaCant());
                pe.setPrecioCompra(p.getPrecioCompra());
                pe.setPrecioMayoreo(p.getPrecioMayoreo());
                pe.setPrecioVenta(p.getPrecioVenta());
                pe.setExistencias(p.getExistencias());
                pe.setImgString(p.getImgString());
                realm.insertOrUpdate(pe);
                realm.commitTransaction();
            }
        } else {
            error = "No se pudo obtener la instancia de la BD";
        }

        return error;
    }


    public String actualizarProductoServer(ProductosXYDTOAux p, Realm realm) {

        String error = "";

        if (realm != null) {
            if (!realm.isInTransaction()) {
                realm.beginTransaction();
            }

            boolean exito = true;

            ProductosXYDTO pe = realm.where(ProductosXYDTO.class).equalTo("id", p.getId()).findFirst();
            if (pe == null) {
                error = "Ya no existe el producto";
            } else {
                pe.setIdVR(p.getIdVR());
                pe.setEditado(true);
                pe.setCantidadMayoreo(p.getCantidadMayoreo());
                pe.setCodigoBarras(p.getCodigoBarras());
                pe.setComision(p.getComision());
                pe.setDescripcionCorta(p.getDescripcionCorta());
                pe.setIva(p.isIva());
                pe.setIvaCant(p.getIvaCant());
                pe.setPrecioCompra(p.getPrecioCompra());
                pe.setPrecioMayoreo(p.getPrecioMayoreo());
                pe.setPrecioVenta(p.getPrecioVenta());
                pe.setExistencias(p.getExistencias());
                pe.setImgString(p.getImgString());
                realm.insertOrUpdate(pe);
                realm.commitTransaction();
            }
        } else {
            error = "No se pudo obtener la instancia de la BD";
        }

        return error;
    }


    public String guardarBDProductosLocal(ProductosXYDTOLocal ps, int idTienda, Realm realm) {

        String error = "";

        if (realm != null) {
            if (!realm.isInTransaction()) {
                realm.beginTransaction();
            }

            //tenemos que checar que no exista ya el codigoDeBarras en el server y en local
       /* ProductosXYDTO productoGuardado = realm.where(ProductosXYDTO.class).
                equalTo("codigoBarras", ps.getCodigoBarras())
                .and()
                .notEqualTo("codigoBarras", "").findFirst();
        if (productoGuardado != null && productoGuardado.getId() > 0) {
            //quiere decir que si existe en el server por lo tanto madamos mensaje
            error += "Ya existe un producto con ese código de Barras: " + ps.getCodigoBarras();
        } else {
            ProductosXYDTOLocal productoGuardadoLocal = realm.where(ProductosXYDTOLocal.class).
                    equalTo("codigoBarras", ps.getCodigoBarras())
                    .and()
                    .notEqualTo("codigoBarras", "").findFirst();
            if (productoGuardadoLocal != null && productoGuardadoLocal.getId() > 0) {
                //quiere decir que si existe en el server por lo tanto madamos mensaje
                error += "Ya existe un producto con ese código de Barras: " + ps.getCodigoBarras();
            } else {*/
            Number maxId = realm.where(ProductosXYDTOLocal.class).max("id");

            int nextId = (maxId == null) ? 1 : maxId.intValue() + 1;


            ProductosXYDTOLocal p = realm.createObject(ProductosXYDTOLocal.class, nextId);

            p.setCodigoBarras(ps.getCodigoBarras());
            p.setActivo(ps.isActivo());
            p.setIvaCant(ps.getIvaCant());
            p.setIva(ps.isIva());
            p.setComision(ps.getComision());
            p.setCantidadMayoreo(ps.getCantidadMayoreo());
            p.setPrecioMayoreo(ps.getPrecioMayoreo());
            p.setPrecioCompra(ps.getPrecioCompra());
            p.setDescripcionCorta(ps.getDescripcionCorta());
            p.setPrecioVenta(ps.getPrecioVenta());
            p.setImgProdURL(ps.getImgProdURL());
            p.setIdTienda(idTienda);
            p.setNombreGrupo(ps.getNombreGrupo());
            p.setColorGrupo(ps.getColorGrupo());
            p.setIdVR(ps.getIdVR());
            p.setExistencias(ps.getExistencias());
            p.setEnviado(false);
            p.setImgString(ps.getImgString());
            realm.insert(p);
            realm.commitTransaction();
            //  }
            // }
        } else {
            error = "No se pudo obtener la instancia de la BD";
        }

        return error;
    }


    public void borrarPaquetesProducto(ProductosXYDTO producto, Realm realm) {

        if (realm != null) {
            //buscamos los paquetes del producto
            RealmResults<PaquetesProducto> paquetes = realm.where(PaquetesProducto.class)
                    .equalTo("idProducto", producto.getId()).findAll();

            if (paquetes != null && paquetes.size() > 0) {
                for (PaquetesProducto pa : paquetes
                ) {
                    //por cada paquete vamos por sus opciones
                    borrarOpcionesPaquete(pa, realm);
                }
                if (!realm.isInTransaction()) {
                    realm.beginTransaction();
                }
                paquetes.deleteAllFromRealm();
                realm.commitTransaction();
            }
        }


    }


    public void borrarOpcionesPaquete(PaquetesProducto pack, Realm realm) {

        if (realm != null) {

            if (!realm.isInTransaction()) {
                realm.beginTransaction();
            }

            //buscamos los paquetes del producto
            RealmResults<OpcionesPaquete> opciones = realm.where(OpcionesPaquete.class)
                    .equalTo("idPaquete", pack.getId()).findAll();

            if (opciones != null && opciones.size() > 0) {
                opciones.deleteAllFromRealm();
            }
            realm.commitTransaction();
        }

    }


    public void guardarPaquetesProducto(ProductosXYDTO ps, Realm realm) {

        for (PaqueteXYDTO pack : ps.getPaquetesxy()
        ) {
            guardarPaqueteProducto(pack, ps.getId(), realm);
        }

    }

    private void guardarPaqueteProducto(PaqueteXYDTO pack, int idProducto, Realm realm) {

        if (realm != null) {
            if (!realm.isInTransaction()) {
                realm.beginTransaction();
            }
            PaquetesProducto p = realm.createObject(PaquetesProducto.class, pack.getId());
            p.setIdProducto(idProducto);
            realm.insert(p);
            realm.commitTransaction();

            for (OpcionesPaqueteXY op : pack.getOpcionesPaquetexy()
            ) {
                guardarOpcionPaquete(op, pack.getId(), realm);
            }
        }
    }

    private void guardarOpcionPaquete(OpcionesPaqueteXY op, long idPaquete, Realm realm) {

        if (realm != null) {
            if (!realm.isInTransaction()) {
                realm.beginTransaction();
            }
            OpcionesPaquete p = realm.createObject(OpcionesPaquete.class, op.getId());
            p.setCantidad(op.getCantidad());
            p.setIdPaquete(idPaquete);
            p.setMostrar(op.isMostrar());
            p.setPrecio(op.getPrecioPaquete());
            p.setIdProducto(op.getIdProducto().getId());
            p.setDescripcion(op.getIdProducto().getDescripcionCorta());
            p.setIva(op.getIdProducto().isIva());
            p.setIvaCant(op.getIdProducto().getIvaCant());


            realm.insert(p);
            realm.commitTransaction();
        }
    }


    public List<PaquetesProducto> obtenerPaquetesProductos(int idProducto, Realm realm) {

        List<PaquetesProducto> pacs = new ArrayList<>();
        if (realm != null) {
            pacs = realm.where(PaquetesProducto.class)
                    .equalTo("idProducto", idProducto)
                    .sort("id").findAll();
        }
        return pacs;
    }

    public List<OpcionesPaquete> obtenerOpcionesPaquete(int idPaquete, Realm realm) {

        List<OpcionesPaquete> opciones = new ArrayList<>();
        if (realm != null) {
            opciones = realm.where(OpcionesPaquete.class)
                    .equalTo("idPaquete", idPaquete)
                    .sort("id").findAll();
        }
        return opciones;
    }

    public OpcionesPaquete obtenerOpcionPaquete(long idOpcion, Realm realm) {

        OpcionesPaquete opcion = new OpcionesPaquete();
        if (realm != null) {
            opcion = realm.where(OpcionesPaquete.class)
                    .equalTo("id", idOpcion)
                    .findFirst();
        }
        return opcion;
    }
}
