package com.anegocios.puntoventa.dtosauxiliares;

import com.anegocios.puntoventa.bdlocal.ClienteXYDTOLocal;
import com.anegocios.puntoventa.jsons.ClienteXYDTO;

public class ClienteXYDTOAux {

    public ClienteXYDTOAux() {

    }

    public ClienteXYDTOAux(ClienteXYDTO c , String tipoBDL)
    {
        this.id=c.getId();
        this.apellidoP=c.getApellidoP()==null?"":c.getApellidoP();
        this.apellidoM=c.getApellidoM()==null?"":c.getApellidoM();
        this.nombre=c.getNombre()==null?"":c.getNombre();
        this.zona=c.getZona();
        this.ruta=c.getRuta();
        this.correo=c.getCorreo();
        this.telefono=c.getTelefono();
        this.calle=c.getCalle();
        this.numeroExt=c.getNumeroExt();
        this.numeroInt=c.getNumeroInt();
        this.colonia=c.getColonia();
        this.municipio=c.getMunicipio();
        this.estado=c.getEstado();
        this.cp=c.getCp();
        this.comentario=c.getComentario();
        this.tipoBDL=tipoBDL;
    }

    public ClienteXYDTOAux(ClienteXYDTOAux c , String tipoBDL)
    {
        this.id=c.getId();
        this.apellidoP=c.getApellidoP()==null?"":c.getApellidoP();
        this.apellidoM=c.getApellidoM()==null?"":c.getApellidoM();
        this.nombre=c.getNombre()==null?"":c.getNombre();
        this.zona=c.getZona();
        this.ruta=c.getRuta();
        this.correo=c.getCorreo();
        this.telefono=c.getTelefono();
        this.calle=c.getCalle();
        this.numeroExt=c.getNumeroExt();
        this.numeroInt=c.getNumeroInt();
        this.colonia=c.getColonia();
        this.municipio=c.getMunicipio();
        this.estado=c.getEstado();
        this.cp=c.getCp();
        this.comentario=c.getComentario();
        this.tipoBDL=tipoBDL;
    }


    public ClienteXYDTOAux(ClienteXYDTOLocal c , String tipoBDL)
    {
        this.id=c.getId();
        this.apellidoP=c.getApellidoP()==null?"":c.getApellidoP();
        this.apellidoM=c.getApellidoM()==null?"":c.getApellidoM();
        this.nombre=c.getNombre()==null?"":c.getNombre();
        this.zona=c.getZona();
        this.ruta=c.getRuta();
        this.correo=c.getCorreo();
        this.telefono=c.getTelefono();
        this.calle=c.getCalle();
        this.numeroExt=c.getNumeroExt();
        this.numeroInt=c.getNumeroInt();
        this.colonia=c.getColonia();
        this.municipio=c.getMunicipio();
        this.estado=c.getEstado();
        this.cp=c.getCp();
        this.comentario=c.getComentario();
        this.tipoBDL=tipoBDL;
    }



    private int id;
    private String apellidoP;
    private String apellidoM;
    private String nombre;
    private String zona;
    private String ruta;
    private String correo;
    private String telefono;
    private String tipoBDL;
    private String estado;
    private String municipio;
    private String colonia;
    private String calle;
    private String numeroInt;
    private String numeroExt;
    private String cp;
    private String comentario;

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

    public String getTipoBDL() {
        return tipoBDL;
    }

    public void setTipoBDL(String tipoBDL) {
        this.tipoBDL = tipoBDL;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
}
