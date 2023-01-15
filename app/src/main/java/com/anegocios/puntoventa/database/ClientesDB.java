package com.anegocios.puntoventa.database;

import com.anegocios.puntoventa.bdlocal.ClienteXYDTOLocal;
import com.anegocios.puntoventa.dtosauxiliares.ClienteXYDTOAux;
import com.anegocios.puntoventa.jsons.ClienteXYDTO;
import com.anegocios.puntoventa.jsons.CorreosXYDTO;
import com.anegocios.puntoventa.jsons.TelefonoXYDTO;
import com.anegocios.puntoventa.utils.Utilerias;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmList;

public class ClientesDB {

    public void actualizarBDClientes(List<ClienteXYDTO> clientes,int idTienda, Realm realm ) {

        if(realm!=null) {
            for (ClienteXYDTO ps : clientes) {
                if (ps.isActivo()) {
                    if (!realm.isInTransaction()) {
                        realm.beginTransaction();
                    }
                    ClienteXYDTO clientesGuardado = realm.where(ClienteXYDTO.class).
                            equalTo("id", ps.getId()).findFirst();
                    if (clientesGuardado != null && clientesGuardado.getId() > 0) {
                        clientesGuardado.deleteFromRealm();
                        ClienteXYDTO p = realm.createObject(ClienteXYDTO.class, ps.getId());
                        p.setNombre(ps.getNombre());
                        p.setApellidoM(ps.getApellidoM());
                        p.setApellidoP(ps.getApellidoP());
                        p.setRuta(ps.getRuta());
                        p.setIdTienda(idTienda);
                        RealmList<CorreosXYDTO> correos = new RealmList<CorreosXYDTO>();
                        if (ps.getCorreosxy() != null && ps.getCorreosxy().size() > 0) {
                            CorreosXYDTO corr = realm.createObject(CorreosXYDTO.class);
                            for (CorreosXYDTO c : ps.getCorreosxy()) {
                                corr.setCorreo(c.getCorreo());
                                correos.add(corr);
                            }
                            p.setCorreo(ps.getCorreosxy().get(0).getCorreo());
                        } else {
                            p.setCorreo("");
                        }
                        p.setCorreosxy(correos);

                        if (ps.getDireccionesxy() != null && ps.getDireccionesxy().size() > 0) {
                            p.setEstado(ps.getDireccionesxy().get(0).getEstado());
                            p.setCalle(ps.getDireccionesxy().get(0).getCalle());
                            p.setNumeroExt(ps.getDireccionesxy().get(0).getNumeroExt());
                            p.setNumeroInt(ps.getDireccionesxy().get(0).getNumeroInt());
                            p.setColonia(ps.getDireccionesxy().get(0).getColonia());
                            p.setComentario(ps.getDireccionesxy().get(0).getComentario());
                            p.setMunicipio(ps.getDireccionesxy().get(0).getMunicipio());
                            p.setCp(ps.getDireccionesxy().get(0).getCp());


                        } else {
                            p.setCp("");
                            p.setMunicipio("");
                            p.setComentario("");
                            p.setColonia("");
                            p.setNumeroInt("");
                            p.setNumeroExt("");
                            p.setCalle("");
                            p.setEstado("");
                        }

                        RealmList<TelefonoXYDTO> telefonos = new RealmList<TelefonoXYDTO>();
                        if (ps.getTelefonosxy() != null && ps.getTelefonosxy().size() > 0) {
                            TelefonoXYDTO tel = realm.createObject(TelefonoXYDTO.class);
                            for (TelefonoXYDTO t : ps.getTelefonosxy()) {
                                tel.setNumero(t.getNumero());
                                tel.setTipo(t.getTipo());
                                telefonos.add(tel);
                            }
                            p.setTelefono(ps.getTelefonosxy().get(0).getNumero());
                        } else {
                            p.setTelefono("");
                        }
                        p.setTelefonosxy(telefonos);

                        realm.insert(p);
                        realm.commitTransaction();
                    } else {
                        ClienteXYDTO p = realm.createObject(ClienteXYDTO.class, ps.getId());
                        p.setNombre(ps.getNombre());
                        p.setApellidoM(ps.getApellidoM());
                        p.setApellidoP(ps.getApellidoP());
                        p.setIdTienda(idTienda);
                        p.setRuta(ps.getRuta());
                        RealmList<CorreosXYDTO> correos = new RealmList<CorreosXYDTO>();
                        if (ps.getCorreosxy() != null && ps.getCorreosxy().size() > 0) {
                            CorreosXYDTO corr = realm.createObject(CorreosXYDTO.class);
                            for (CorreosXYDTO c : ps.getCorreosxy()) {
                                corr.setCorreo(c.getCorreo());
                                correos.add(corr);
                            }
                            p.setCorreo(ps.getCorreosxy().get(0).getCorreo());
                        } else {
                            p.setCorreo("");
                        }
                        p.setCorreosxy(correos);


                        RealmList<TelefonoXYDTO> telefonos = new RealmList<TelefonoXYDTO>();
                        if (ps.getTelefonosxy() != null && ps.getTelefonosxy().size() > 0) {
                            TelefonoXYDTO tel = realm.createObject(TelefonoXYDTO.class);
                            for (TelefonoXYDTO t : ps.getTelefonosxy()) {
                                tel.setNumero(t.getNumero());
                                tel.setTipo(t.getTipo());
                                telefonos.add(tel);
                            }
                            p.setTelefono(ps.getTelefonosxy().get(0).getNumero());
                        } else {
                            p.setTelefono("");
                        }
                        p.setTelefonosxy(telefonos);

                        if (ps.getDireccionesxy() != null && ps.getDireccionesxy().size() > 0) {
                            p.setEstado(ps.getDireccionesxy().get(0).getEstado());
                            p.setCalle(ps.getDireccionesxy().get(0).getCalle());
                            p.setNumeroExt(ps.getDireccionesxy().get(0).getNumeroExt());
                            p.setNumeroInt(ps.getDireccionesxy().get(0).getNumeroInt());
                            p.setColonia(ps.getDireccionesxy().get(0).getColonia());
                            p.setComentario(ps.getDireccionesxy().get(0).getComentario());
                            p.setMunicipio(ps.getDireccionesxy().get(0).getMunicipio());
                            p.setCp(ps.getDireccionesxy().get(0).getCp());


                        } else {
                            p.setCp("");
                            p.setMunicipio("");
                            p.setComentario("");
                            p.setColonia("");
                            p.setNumeroInt("");
                            p.setNumeroExt("");
                            p.setCalle("");
                            p.setEstado("");
                        }


                        realm.insert(p);
                        realm.commitTransaction();
                    }

                }
                else
                {
                    if (!realm.isInTransaction()) {
                        realm.beginTransaction();
                    }
                    ClienteXYDTO cliente = realm.where(ClienteXYDTO.class).
                            equalTo("id", ps.getId()).findFirst();
                    if (cliente != null) {
                        cliente.deleteFromRealm();
                    }
                    realm.commitTransaction();
                }
            }
        }


    }


    public List<ClienteXYDTOAux> obtenerClientesCompletos(int idTienda, Realm realm ) {
        List<ClienteXYDTOAux> completos = new ArrayList<ClienteXYDTOAux>();
        List<ClienteXYDTO> clientesServer = obtenerListaClientes(idTienda,realm);

        for (ClienteXYDTO p : clientesServer
        ) {
            completos.add(new ClienteXYDTOAux(p, "S"));
        }

        List<ClienteXYDTOLocal> clientesLocal = obtenerListaClientesLocales(idTienda,realm);

        for (ClienteXYDTOLocal p : clientesLocal
        ) {
            completos.add(new ClienteXYDTOAux(p, "L"));
        }
        if(completos!=null && completos.size()>0) {
            Collections.sort(completos, new Comparator<ClienteXYDTOAux>() {
                public int compare(ClienteXYDTOAux obj1, ClienteXYDTOAux obj2) {
                    // ## Ascending order
                    return obj1.getNombre().compareToIgnoreCase(obj2.getNombre()); // To compare string values
                    // return Integer.valueOf(obj1.empId).compareTo(Integer.valueOf(obj2.empId)); // To compare integer values

                    // ## Descending order
                    // return obj2.firstName.compareToIgnoreCase(obj1.firstName); // To compare string values
                    // return Integer.valueOf(obj2.empId).compareTo(Integer.valueOf(obj1.empId)); // To compare integer values
                }
            });
        }

        return completos;
    }


    public List<ClienteXYDTOLocal> obtenerListaClientesLocales(int idTienda, Realm realm ) {

        List<ClienteXYDTOLocal> clientes = new ArrayList<ClienteXYDTOLocal>();
        if(realm!=null) {

            clientes = realm.where(ClienteXYDTOLocal.class)
                    .equalTo("idTienda", idTienda)
                    .and()
                    .equalTo("enviado", false).findAll();

        }
        return clientes;
    }


    public List<ClienteXYDTO> obtenerListaClientes(int idTienda, Realm realm ) {

        List<ClienteXYDTO> clientes = new ArrayList<ClienteXYDTO>();
        if(realm!=null) {
            clientes = realm.where(ClienteXYDTO.class)
                    .equalTo("idTienda", idTienda)
                    .sort("nombre").findAll();
        }
        return clientes;
    }


    public List<ClienteXYDTO> obtenerListaClientesServer(String patron,int idTienda, Realm realm ) {

        List<ClienteXYDTO> regs = new ArrayList<ClienteXYDTO>();
        if(realm!=null) {
            regs = realm.where(ClienteXYDTO.class)
                    .equalTo("idTienda", idTienda)
                    .and()
                    .beginGroup()
                    .contains("nombre", patron, Case.INSENSITIVE).or()
                    .contains("apellidoP", patron, Case.INSENSITIVE).or()
                    .contains("calle", patron, Case.INSENSITIVE).or()
                    .contains("colonia", patron, Case.INSENSITIVE).or()
                    .contains("municipio", patron, Case.INSENSITIVE).or()
                    .contains("estado", patron, Case.INSENSITIVE).or()
                    .contains("telefono", patron, Case.INSENSITIVE).or()
                    .contains("apellidoM", patron, Case.INSENSITIVE)
                    .endGroup()
                    .sort("nombre").findAll();
        }
        return regs;
    }


    public List<ClienteXYDTOLocal> obtenerListaClientesLocal(String patron,int idTienda, Realm realm ) {

        List<ClienteXYDTOLocal> regs = new ArrayList<ClienteXYDTOLocal>();
        if(realm!=null) {
            regs = realm.where(ClienteXYDTOLocal.class).beginGroup()
                    .contains("nombre", patron, Case.INSENSITIVE).or()
                    .contains("apellidoP", patron, Case.INSENSITIVE).or()
                    .contains("calle", patron, Case.INSENSITIVE).or()
                    .contains("colonia", patron, Case.INSENSITIVE).or()
                    .contains("municipio", patron, Case.INSENSITIVE).or()
                    .contains("estado", patron, Case.INSENSITIVE).or()
                    .contains("telefono", patron, Case.INSENSITIVE).or()
                    .contains("apellidoM", patron, Case.INSENSITIVE).endGroup()
                    .and().equalTo("enviado", false)
                    .and()
                    .equalTo("idTienda", idTienda).findAll();
        }
        return regs;
    }

    public List<ClienteXYDTOAux> obtenerClientesCompletosPatron(String patron,int idTienda, Realm realm ) {
        List<ClienteXYDTOAux> completos = new ArrayList<ClienteXYDTOAux>();
        List<ClienteXYDTO> clientesServer = obtenerListaClientesServer(patron,idTienda,realm);

        for (ClienteXYDTO p : clientesServer
        ) {
            completos.add(new ClienteXYDTOAux(p, "S"));
        }

        List<ClienteXYDTOLocal> productosLocal = obtenerListaClientesLocal(patron,idTienda,realm);

        for (ClienteXYDTOLocal p : productosLocal
        ) {
            completos.add(new ClienteXYDTOAux(p, "L"));
        }
        if(completos!=null && completos.size()>0) {
            Collections.sort(completos, new Comparator<ClienteXYDTOAux>() {
                public int compare(ClienteXYDTOAux obj1, ClienteXYDTOAux obj2) {
                    // ## Ascending order
                    return obj1.getNombre().compareToIgnoreCase(obj2.getNombre()); // To compare string values
                    // return Integer.valueOf(obj1.empId).compareTo(Integer.valueOf(obj2.empId)); // To compare integer values

                    // ## Descending order
                    // return obj2.firstName.compareToIgnoreCase(obj1.firstName); // To compare string values
                    // return Integer.valueOf(obj2.empId).compareTo(Integer.valueOf(obj1.empId)); // To compare integer values
                }
            });
        }
        return completos;
    }


    public String actualizarClienteLocal(ClienteXYDTOAux p,int idTienda, Realm realm ) {

        String error = "";

        if(realm!=null) {
            if (!realm.isInTransaction()) {
                realm.beginTransaction();
            }

            boolean exito = true;

            ClienteXYDTOLocal pe = realm.where(ClienteXYDTOLocal.class).equalTo("id", p.getId()).findFirst();
            if (pe == null) {
                error = "Ya no existe el cliente";
            } else {
                pe.setTelefono(p.getTelefono());
                pe.setEnviado(false);
                pe.setCorreo(p.getCorreo());
                pe.setNombre(p.getNombre());
                pe.setApellidoM(p.getApellidoM());
                pe.setApellidoP(p.getApellidoP());
                pe.setRuta(p.getRuta());
                pe.setZona(p.getZona());
                pe.setCalle(p.getCalle());
                pe.setNumeroExt(p.getNumeroExt());
                pe.setNumeroInt(p.getNumeroInt());
                pe.setColonia(p.getColonia());
                pe.setMunicipio(p.getMunicipio());
                pe.setEstado(p.getEstado());
                pe.setCp(p.getCp());
                pe.setComentario(p.getComentario());
                pe.setIdTienda(idTienda);
                realm.insertOrUpdate(pe);
                realm.commitTransaction();
            }
        }
        else
        {
            error="No se pudo obtener la instancia de la BD";
        }

        return error;
    }


    public String actualizarClienteServer(ClienteXYDTOAux p,int idTienda, Realm realm ) {

        String error = "";

        if(realm!=null) {
            if (!realm.isInTransaction()) {
                realm.beginTransaction();
            }

            boolean exito = true;

            ClienteXYDTO pe = realm.where(ClienteXYDTO.class).equalTo("id", p.getId()).findFirst();
            if (pe == null) {
                error = "Ya no existe el cliente";
            } else {
                pe.setTelefono(p.getTelefono());
                pe.setEditado(true);
                pe.setCorreo(p.getCorreo());
                pe.setNombre(p.getNombre());
                pe.setApellidoM(p.getApellidoM());
                pe.setApellidoP(p.getApellidoP());
                pe.setRuta(p.getRuta());
                pe.setZona(p.getZona());
                pe.setCalle(p.getCalle());
                pe.setNumeroExt(p.getNumeroExt());
                pe.setNumeroInt(p.getNumeroInt());
                pe.setColonia(p.getColonia());
                pe.setMunicipio(p.getMunicipio());
                pe.setEstado(p.getEstado());
                pe.setCp(p.getCp());
                pe.setComentario(p.getComentario());
                pe.setIdTienda(idTienda);
                realm.insertOrUpdate(pe);
                realm.commitTransaction();
            }
        }
        else
        {
            error="No se pudo obtener la instancia de la BD";
        }

        return error;
    }


    public String guardarBDClienteLocal(ClienteXYDTOLocal ps,int idTienda, Realm realm ) {

        String error = "";

        if(realm!=null) {
            if (!realm.isInTransaction()) {
                realm.beginTransaction();
            }

            Number maxId = realm.where(ClienteXYDTOLocal.class).max("id");
            int nextId = (maxId == null) ? 1 : maxId.intValue() + 1;
            ClienteXYDTOLocal p = realm.createObject(ClienteXYDTOLocal.class, nextId);

            p.setApellidoM(ps.getApellidoM());
            p.setApellidoP(ps.getApellidoP());
            p.setCorreo(ps.getCorreo());
            p.setNombre(ps.getNombre());
            p.setRuta(ps.getRuta());
            p.setTelefono(ps.getTelefono());
            p.setZona(ps.getZona());
            p.setCalle(ps.getCalle());
            p.setNumeroExt(ps.getNumeroExt());
            p.setNumeroInt(ps.getNumeroInt());
            p.setColonia(ps.getColonia());
            p.setMunicipio(ps.getMunicipio());
            p.setEstado(ps.getEstado());
            p.setCp(ps.getCp());
            p.setComentario(ps.getComentario());
            p.setIdTienda(idTienda);
            p.setEnviado(false);
            realm.insert(p);
            realm.commitTransaction();
        }
        else
        {
            error="No se pudo obtener la instancia de la BD";
        }
        return error;
    }


    public List<ClienteXYDTO> obtenerListaClientesServerEditados(int idTienda, Realm realm ) {

        List<ClienteXYDTO> regs = new ArrayList<ClienteXYDTO>();
        if(realm!=null) {
            regs = realm.where(ClienteXYDTO.class)
                    .equalTo("idTienda", idTienda).and()
                    .equalTo("editado", true).findAll();
        }
        return regs;
    }


    public ClienteXYDTO obtenerClienteServer(int idCliente, Realm realm ) {

        ClienteXYDTO cliente= new ClienteXYDTO();
        if(realm!=null) {
            cliente= realm.where(ClienteXYDTO.class)
                    .equalTo("id", idCliente)
                    .findFirst();
        }
        return cliente;

    }

    public ClienteXYDTOLocal obtenerClienteLocal(int idCliente, Realm realm ) {

        ClienteXYDTOLocal cliente= new ClienteXYDTOLocal();
        if(realm!=null) {
            cliente= realm.where(ClienteXYDTOLocal.class)
                    .equalTo("id", idCliente)
                    .findFirst();
        }
        return cliente;

    }

    public List<ClienteXYDTOLocal> obtenerListaClientesLocalesEnviar(int idTienda, Realm realm ) {

        List<ClienteXYDTOLocal> regs = new ArrayList<ClienteXYDTOLocal>();
        if(realm!=null) {
            regs = realm.where(ClienteXYDTOLocal.class)
                    .equalTo("idTienda", idTienda)
                    .equalTo("enviado", false).findAll();
        }
        return regs;
    }



    public void actualizarClienteLocalEnviado( int idLocal, Realm realm ) {

        if(realm!=null) {
            if (!realm.isInTransaction()) {
                realm.beginTransaction();
            }
            ClienteXYDTOLocal p = realm.where(ClienteXYDTOLocal.class).equalTo("id", idLocal).findFirst();
            p.setEnviado(true);
            realm.insertOrUpdate(p);
            realm.commitTransaction();
        }

    }


    public void actualizarClienteServerEditadoFalse(int idServer, Realm realm ) {

        if(realm!=null) {
            if (!realm.isInTransaction()) {
                realm.beginTransaction();
            }
            ClienteXYDTO p = realm.where(ClienteXYDTO.class).equalTo("id", idServer).findFirst();
            p.setEditado(false);
            realm.insertOrUpdate(p);
            realm.commitTransaction();
        }

    }
}
