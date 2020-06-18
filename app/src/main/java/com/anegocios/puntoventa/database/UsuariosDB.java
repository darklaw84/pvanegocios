package com.anegocios.puntoventa.database;

import com.anegocios.puntoventa.jsons.Usuario;
import com.anegocios.puntoventa.jsons.UsuariosUts;
import com.anegocios.puntoventa.utils.Utilerias;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class UsuariosDB {

    public void actualizarBDUsuarios(List<Usuario> usuarios,Realm realm) {


        if (realm != null) {
            for (Usuario ps : usuarios) {
                if (!realm.isInTransaction()) {
                    realm.beginTransaction();
                }
                Usuario usuarioGuardado = realm.where(Usuario.class).
                        equalTo("id", ps.getId()).findFirst();
                if (usuarioGuardado != null && usuarioGuardado.getId() > 0) {
                    usuarioGuardado.setClientes(ps.getClientes());
                    usuarioGuardado.setCortarCaja(ps.getCortarCaja());
                    usuarioGuardado.setCotizar(ps.getCotizar());
                    usuarioGuardado.setElevarCotizacion(ps.getElevarCotizacion());
                    usuarioGuardado.setPagosCxC(ps.getPagosCxC());
                    usuarioGuardado.setPass(ps.getPass());
                    usuarioGuardado.setPedido(ps.getPedido());
                    usuarioGuardado.setProductos(ps.getProductos());
                    usuarioGuardado.setProveedores(ps.getProveedores());
                    usuarioGuardado.setReportes(ps.getReportes());
                    usuarioGuardado.setSalt(ps.getSalt());
                    usuarioGuardado.setUsarCaja(ps.getUsarCaja());
                    usuarioGuardado.setUsername(ps.getUsername());
                    usuarioGuardado.setUtInt(ps.getUT().get(0).getId());
                    usuarioGuardado.setVendedores(ps.getVendedores());
                    usuarioGuardado.setVenderCredito(ps.getVenderCredito());
                    usuarioGuardado.setClt_C(ps.getClt_C());
                    usuarioGuardado.setClt_R(ps.getClt_R());
                    usuarioGuardado.setClt_U(ps.getClt_U());
                    usuarioGuardado.setProd_C(ps.getProd_C());
                    usuarioGuardado.setProd_R(ps.getProd_R());
                    usuarioGuardado.setProd_U(ps.getProd_U());
                    if (ps.getCajaPropina() == null) {
                        usuarioGuardado.setCajaPropina(false);
                    } else {
                        usuarioGuardado.setCajaPropina(ps.getCajaPropina());
                    }
                    if (ps.getCajaDesc() == null) {
                        usuarioGuardado.setCajaDesc(false);
                    } else {
                        usuarioGuardado.setCajaDesc(ps.getCajaDesc());
                    }
                    usuarioGuardado.setEditPrecio(ps.getEditPrecio());


                    realm.insertOrUpdate(usuarioGuardado);
                    realm.commitTransaction();
                } else {
                    Usuario p = realm.createObject(Usuario.class, ps.getId());
                    p.setClientes(ps.getClientes());
                    p.setCortarCaja(ps.getCortarCaja());
                    p.setCotizar(ps.getCotizar());
                    p.setElevarCotizacion(ps.getElevarCotizacion());
                    p.setPagosCxC(ps.getPagosCxC());
                    p.setPass(ps.getPass());
                    p.setPedido(ps.getPedido());
                    p.setProductos(ps.getProductos());
                    p.setProveedores(ps.getProveedores());
                    p.setReportes(ps.getReportes());
                    p.setSalt(ps.getSalt());
                    p.setUsarCaja(ps.getUsarCaja());
                    p.setUsername(ps.getUsername());
                    p.setUtInt(ps.getUT().get(0).getId());
                    p.setVendedores(ps.getVendedores());
                    p.setVenderCredito(ps.getVenderCredito());
                    p.setClt_C(ps.getClt_C());
                    p.setClt_R(ps.getClt_R());
                    p.setClt_U(ps.getClt_U());
                    p.setProd_C(ps.getProd_C());
                    p.setProd_R(ps.getProd_R());
                    p.setProd_U(ps.getProd_U());
                    if (ps.getCajaDesc() == null) {
                        p.setCajaDesc(false);
                    } else {
                        p.setCajaDesc(ps.getCajaDesc());
                    }

                    if (ps.getCajaPropina() == null) {
                        p.setCajaPropina(false);
                    } else {
                        p.setCajaPropina(ps.getCajaPropina());
                    }

                    p.setEditPrecio(ps.getEditPrecio());
                    realm.insert(p);
                    realm.commitTransaction();
                }
            }
        }


    }


    public void guardarUTUsuario(int idUsuario, int idTienda, int idUT,Realm realm,String vigencia,String tipoLic) {

        if (realm != null) {
            if (!realm.isInTransaction()) {
                realm.beginTransaction();
            }
            Number maxId = realm.where(UsuariosUts.class).max("id");

            int nextId = (maxId == null) ? 1 : maxId.intValue() + 1;
            UsuariosUts p = realm.createObject(UsuariosUts.class, nextId);
            p.setIdTienda(idTienda);
            p.setIdUsuario(idUsuario);
            p.setIdUT(idUT);
            p.setVigencia(vigencia);
            p.setTipoLic(tipoLic);
            realm.insert(p);
            realm.commitTransaction();
        }

    }




    public UsuariosUts verificarExistenciaUT(int idUsuario, int idTienda, int idUT,Realm realm) {


        UsuariosUts us = new UsuariosUts();
        if (realm != null) {
            us = realm.where(UsuariosUts.class)
                    .equalTo("idUsuario", idUsuario)
                    .and()
                    .equalTo("idTienda", idTienda)
                    .and()
                    .equalTo("idUT", idUT).findFirst();
        } else {
            us = null;
        }
        return us;
    }

    public UsuariosUts obtenerIdUTUsuario(int idUsuario, int idTienda,Realm realm) {



        UsuariosUts us = new UsuariosUts();
        if (realm != null) {
            us = realm.where(UsuariosUts.class)
                    .equalTo("idUsuario", idUsuario)
                    .and()
                    .equalTo("idTienda", idTienda)
                    .findFirst();
        }
        return us;
    }

    public List<Usuario> obtenerListaUsuarios(Realm realm) {


        List<Usuario> registros = new ArrayList<Usuario>();
        if (realm != null) {
            registros = realm.where(Usuario.class).findAll();
        }
        return registros;
    }


    public List<UsuariosUts> obtenerTiendasUT(int idUsuario,Realm realm) {


        List<UsuariosUts> registros = new ArrayList<UsuariosUts>();
        try {
            if (realm != null) {
                registros = realm.where(UsuariosUts.class)
                        .equalTo("idUsuario", idUsuario).findAll();
            }
        }
        catch(Exception ex)
        {
            System.out.println("Error al obtener TiendasUT: "+ex.getMessage());
        }
        return registros;
    }


}
