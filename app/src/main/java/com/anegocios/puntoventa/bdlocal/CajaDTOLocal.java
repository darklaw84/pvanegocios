package com.anegocios.puntoventa.bdlocal;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class CajaDTOLocal extends RealmObject {


    @PrimaryKey
    private int idCaja;
    private int idUsuario;
    private int idTienda;
    private String fechaInicio;
    private double montoInicial;
    private String fechaFin;
    private boolean tieneCorteLocal;
    private boolean creadaServer;
    private boolean creadoCorte;
    private int idCajaServer;
    private int idUsuarioCorte;
    private double efectivoContado;
    private double tarjetaContado;
    private double efectivoCalculado;
    private double tarjetaCalculado;

    public boolean isTieneCorteLocal() {
        return tieneCorteLocal;
    }

    public void setTieneCorteLocal(boolean tieneCorteLocal) {
        this.tieneCorteLocal = tieneCorteLocal;
    }

    public boolean isCreadaServer() {
        return creadaServer;
    }

    public void setCreadaServer(boolean creadaServer) {
        this.creadaServer = creadaServer;
    }

    public boolean isCreadoCorte() {
        return creadoCorte;
    }

    public void setCreadoCorte(boolean creadoCorte) {
        this.creadoCorte = creadoCorte;
    }

    public int getIdUsuarioCorte() {
        return idUsuarioCorte;
    }

    public void setIdUsuarioCorte(int idUsuarioCorte) {
        this.idUsuarioCorte = idUsuarioCorte;
    }

    public double getEfectivoContado() {
        return efectivoContado;
    }

    public void setEfectivoContado(double efectivoContado) {
        this.efectivoContado = efectivoContado;
    }

    public double getTarjetaContado() {
        return tarjetaContado;
    }

    public void setTarjetaContado(double tarjetaContado) {
        this.tarjetaContado = tarjetaContado;
    }

    public double getEfectivoCalculado() {
        return efectivoCalculado;
    }

    public void setEfectivoCalculado(double efectivoCalculado) {
        this.efectivoCalculado = efectivoCalculado;
    }

    public double getTarjetaCalculado() {
        return tarjetaCalculado;
    }

    public void setTarjetaCalculado(double tarjetaCalculado) {
        this.tarjetaCalculado = tarjetaCalculado;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdTienda() {
        return idTienda;
    }

    public void setIdTienda(int idTienda) {
        this.idTienda = idTienda;
    }

    public int getIdCajaServer() {
        return idCajaServer;
    }

    public void setIdCajaServer(int idCajaServer) {
        this.idCajaServer = idCajaServer;
    }



    public int getIdCaja() {
        return idCaja;
    }

    public void setIdCaja(int idCaja) {
        this.idCaja = idCaja;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public double getMontoInicial() {
        return montoInicial;
    }

    public void setMontoInicial(double montoInicial) {
        this.montoInicial = montoInicial;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }
}
