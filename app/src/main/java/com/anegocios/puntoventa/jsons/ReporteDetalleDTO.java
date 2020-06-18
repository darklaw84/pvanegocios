package com.anegocios.puntoventa.jsons;

public class ReporteDetalleDTO {

    private int id;
    private String folio;
    private String folioApp;
    private double subTotal;
    private double total;
    private double abonado;
    private double saldo;
    private String cliente;
    private String idCliente;
    private boolean prodEntregado;

    public boolean isProdEntregado() {
        return prodEntregado;
    }

    public void setProdEntregado(boolean prodEntregado) {
        this.prodEntregado = prodEntregado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getAbonado() {
        return abonado;
    }

    public void setAbonado(double abonado) {
        this.abonado = abonado;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getFolioApp() {
        return folioApp;
    }

    public void setFolioApp(String folioApp) {
        this.folioApp = folioApp;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }
}
