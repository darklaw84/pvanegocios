package com.anegocios.puntoventa.jsons;

public class CorteDTO {

    private int idUserCorte;
    private double efectivoCont;
    private double efectivoCal;
    private double tarjetaCont;
    private double tarjetaCal;

    public int getIdUserCorte() {
        return idUserCorte;
    }

    public void setIdUserCorte(int idUserCorte) {
        this.idUserCorte = idUserCorte;
    }

    public double getEfectivoCont() {
        return efectivoCont;
    }

    public void setEfectivoCont(double efectivoCont) {
        this.efectivoCont = efectivoCont;
    }

    public double getEfectivoCal() {
        return efectivoCal;
    }

    public void setEfectivoCal(double efectivoCal) {
        this.efectivoCal = efectivoCal;
    }

    public double getTarjetaCont() {
        return tarjetaCont;
    }

    public void setTarjetaCont(double tarjetaCont) {
        this.tarjetaCont = tarjetaCont;
    }

    public double getTarjetaCal() {
        return tarjetaCal;
    }

    public void setTarjetaCal(double tarjetaCal) {
        this.tarjetaCal = tarjetaCal;
    }
}
