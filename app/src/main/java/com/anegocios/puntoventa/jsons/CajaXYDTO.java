package com.anegocios.puntoventa.jsons;

public class CajaXYDTO {

    private int id;
    private int idUT;
    private double montoInicial;
    private String fechaInicio;
    private String fechaFin;
    private CorteDTO corte;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUT() {
        return idUT;
    }

    public void setIdUT(int idUT) {
        this.idUT = idUT;
    }

    public double getMontoInicial() {
        return montoInicial;
    }

    public void setMontoInicial(double montoInicial) {
        this.montoInicial = montoInicial;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public CorteDTO getCorte() {
        return corte;
    }

    public void setCorte(CorteDTO corte) {
        this.corte = corte;
    }
}
