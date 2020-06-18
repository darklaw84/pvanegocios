package com.anegocios.puntoventa.jsons;

import java.util.ArrayList;

public class ReporteDTO {

    private String idAndroid;
    private long idUT;
    private String reporte;
    private String FI;
    private String FF;
    private boolean exito;
    private ArrayList<ReporteDetalleDTO> pedidos;
    private ArrayList<ReporteDetalleDTO> ventas;
    private ArrayList<ReporteDetalleDTO> cotizaciones;

    public boolean isExito() {
        return exito;
    }

    public void setExito(boolean exito) {
        this.exito = exito;
    }

    public ArrayList<ReporteDetalleDTO> getPedidos() {
        return pedidos;
    }

    public void setPedidos(ArrayList<ReporteDetalleDTO> pedidos) {
        this.pedidos = pedidos;
    }

    public ArrayList<ReporteDetalleDTO> getVentas() {
        return ventas;
    }

    public void setVentas(ArrayList<ReporteDetalleDTO> ventas) {
        this.ventas = ventas;
    }

    public ArrayList<ReporteDetalleDTO> getCotizaciones() {
        return cotizaciones;
    }

    public void setCotizaciones(ArrayList<ReporteDetalleDTO> cotizaciones) {
        this.cotizaciones = cotizaciones;
    }

    public String getIdAndroid() {
        return idAndroid;
    }

    public void setIdAndroid(String idAndroid) {
        this.idAndroid = idAndroid;
    }

    public long getIdUT() {
        return idUT;
    }

    public void setIdUT(long idUT) {
        this.idUT = idUT;
    }

    public String getReporte() {
        return reporte;
    }

    public void setReporte(String reporte) {
        this.reporte = reporte;
    }

    public String getFI() {
        return FI;
    }

    public void setFI(String FI) {
        this.FI = FI;
    }

    public String getFF() {
        return FF;
    }

    public void setFF(String FF) {
        this.FF = FF;
    }
}
