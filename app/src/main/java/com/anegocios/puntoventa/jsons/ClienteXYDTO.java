package com.anegocios.puntoventa.jsons;

import com.anegocios.puntoventa.bdlocal.ClienteXYDTOLocal;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ClienteXYDTO extends RealmObject {

    public ClienteXYDTO() {

    }


    public ClienteXYDTO(ClienteXYDTOLocal c) {
        this.id = c.getId();
        this.apellidoP = c.getApellidoP();
        this.apellidoM = c.getApellidoM();
        this.telefono = c.getTelefono();
        this.nombre = c.getNombre();
        this.correo = c.getCorreo();
        this.calle=c.getCalle();
        this.numeroExt=c.getNumeroExt();
        this.numeroInt=c.getNumeroInt();
        this.colonia=c.getColonia();
        this.municipio=c.getMunicipio();
        this.estado=c.getEstado();
        this.cp=c.getCp();
        this.comentario=c.getComentario();
        this.idTienda=c.getIdTienda();
    }

    public ClienteXYDTO(ClienteXYDTO c) {
        this.id = c.getId();
        this.apellidoP = c.getApellidoP();
        this.apellidoM = c.getApellidoM();
        this.telefono = c.getTelefono();
        this.nombre = c.getNombre();
        this.correo = c.getCorreo();
        this.calle=c.getCalle();
        this.numeroExt=c.getNumeroExt();
        this.numeroInt=c.getNumeroInt();
        this.colonia=c.getColonia();
        this.municipio=c.getMunicipio();
        this.estado=c.getEstado();
        this.cp=c.getCp();
        this.comentario=c.getComentario();
        this.idTienda=c.getIdTienda();
        this.activo=c.isActivo();
    }


    @PrimaryKey
    private int id;
    private String apellidoP;
    private String apellidoM;
    private String nombre;
    private String zona;
    private String ruta;
    private String correo;
    private String telefono;
    private RealmList<TelefonoXYDTO> telefonosxy;
    private RealmList<CorreosXYDTO> correosxy;
    private RealmList<DireccionesXYDTO> direccionesxy;
    private boolean editado;
    private String estado;
    private String municipio;
    private String colonia;
    private String calle;
    private String numeroInt;
    private String numeroExt;
    private String cp;
    private String comentario;
    private int idTienda;
    private boolean activo;

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public int getIdTienda() {
        return idTienda;
    }

    public void setIdTienda(int idTienda) {
        this.idTienda = idTienda;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNumeroInt() {
        return numeroInt;
    }

    public void setNumeroInt(String numeroInt) {
        this.numeroInt = numeroInt;
    }

    public String getNumeroExt() {
        return numeroExt;
    }

    public void setNumeroExt(String numeroExt) {
        this.numeroExt = numeroExt;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public RealmList<DireccionesXYDTO> getDireccionesxy() {
        return direccionesxy;
    }

    public void setDireccionesxy(RealmList<DireccionesXYDTO> direccionesxy) {
        this.direccionesxy = direccionesxy;
    }

    public boolean isEditado() {
        return editado;
    }

    public void setEditado(boolean editado) {
        this.editado = editado;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getApellidoP() {
        return apellidoP;
    }

    public void setApellidoP(String apellidoP) {
        this.apellidoP = apellidoP;
    }

    public String getApellidoM() {
        return apellidoM;
    }

    public void setApellidoM(String apellidoM) {
        this.apellidoM = apellidoM;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public RealmList<TelefonoXYDTO> getTelefonosxy() {
        return telefonosxy;
    }

    public void setTelefonosxy(RealmList<TelefonoXYDTO> telefonosxy) {
        this.telefonosxy = telefonosxy;
    }

    public RealmList<CorreosXYDTO> getCorreosxy() {
        return correosxy;
    }

    public void setCorreosxy(RealmList<CorreosXYDTO> correosxy) {
        this.correosxy = correosxy;
    }
}
