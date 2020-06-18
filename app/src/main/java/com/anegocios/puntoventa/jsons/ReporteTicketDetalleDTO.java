package com.anegocios.puntoventa.jsons;

import java.util.List;

public class ReporteTicketDetalleDTO {

    private String idAndroid;
    private String idUT;
    private String reporte;
    private String folio;

    private boolean exito;
    private List<VentasDetalleTicket> ventas;

    public String getIdAndroid() {
        return idAndroid;
    }

    public void setIdAndroid(String idAndroid) {
        this.idAndroid = idAndroid;
    }

    public String getIdUT() {
        return idUT;
    }

    public void setIdUT(String idUT) {
        this.idUT = idUT;
    }

    public String getReporte() {
        return reporte;
    }

    public void setReporte(String reporte) {
        this.reporte = reporte;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public boolean isExito() {
        return exito;
    }

    public void setExito(boolean exito) {
        this.exito = exito;
    }

    public List<VentasDetalleTicket> getVentas() {
        return ventas;
    }

    public void setVentas(List<VentasDetalleTicket> ventas) {
        this.ventas = ventas;
    }
}
