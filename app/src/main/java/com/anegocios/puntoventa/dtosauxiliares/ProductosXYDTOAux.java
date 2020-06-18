package com.anegocios.puntoventa.dtosauxiliares;


import com.anegocios.puntoventa.bdlocal.ProductosXYDTOLocal;
import com.anegocios.puntoventa.jsons.ProductosXYDTO;

import java.util.List;

public class ProductosXYDTOAux  {

    public ProductosXYDTOAux(ProductosXYDTO p,String tipoBDL)
    {
        this.id=p.getId();
        this.activo=p.isActivo();
        this.cantidadMayoreo=p.getCantidadMayoreo();
        this.codigoBarras=p.getCodigoBarras();
        this.colorGrupo=p.getColorGrupo();
        this.comision=p.getComision();
        this.descripcionCorta=p.getDescripcionCorta();
        this.idVR=p.getIdVR();
        this.imgProdURL=p.getImgProdURL();
        this.iva=p.isIva();
        this.ivaCant=p.getIvaCant();
        this.nombreGrupo=p.getNombreGrupo();
        this.precioCompra=p.getPrecioCompra();
        this.precioMayoreo=p.getPrecioMayoreo();
        this.precioVenta=p.getPrecioVenta();
        this.tipoBDL=tipoBDL;
        this.existencias=p.getExistencias();
        this.paquete=p.isPaquete();
        this.imgString=p.getImgString();
        this.cantidad=p.getCantidad();
        this.imagenGuardada=p.getImagenGuardada();
    }

    public ProductosXYDTOAux(ProductosXYDTOAux p,String tipoBDL)
    {
        this.id=p.getId();
        this.activo=p.isActivo();
        this.cantidadMayoreo=p.getCantidadMayoreo();
        this.codigoBarras=p.getCodigoBarras();
        this.colorGrupo=p.getColorGrupo();
        this.comision=p.getComision();
        this.descripcionCorta=p.getDescripcionCorta();
        this.idVR=p.getIdVR();
        this.imgProdURL=p.getImgProdURL();
        this.iva=p.isIva();
        this.ivaCant=p.getIvaCant();
        this.nombreGrupo=p.getNombreGrupo();
        this.precioCompra=p.getPrecioCompra();
        this.precioMayoreo=p.getPrecioMayoreo();
        this.precioVenta=p.getPrecioVenta();
        this.tipoBDL=tipoBDL;
        this.existencias=p.getExistencias();
        this.paquete=p.isPaquete();
        this.imgString=p.getImgString();
        this.cantidad=p.getCantidad();
        this.imagenGuardada=p.getImagenGuardada();
    }

    public ProductosXYDTOAux(ProductosXYDTOLocal p,String tipoBDL)
    {
        this.id=p.getId();
        this.activo=p.isActivo();
        this.cantidadMayoreo=p.getCantidadMayoreo();
        this.codigoBarras=p.getCodigoBarras();
        this.colorGrupo=p.getColorGrupo();
        this.comision=p.getComision();
        this.descripcionCorta=p.getDescripcionCorta();
        this.idVR=p.getIdVR();
        this.imgProdURL=p.getImgProdURL();
        this.iva=p.isIva();
        this.ivaCant=p.getIvaCant();
        this.nombreGrupo=p.getNombreGrupo();
        this.precioCompra=p.getPrecioCompra();
        this.precioMayoreo=p.getPrecioMayoreo();
        this.precioVenta=p.getPrecioVenta();
        this.tipoBDL=tipoBDL;
        this.existencias=p.getExistencias();
        this.imgString=p.getImgString();
        this.cantidad=p.getCantidad();
        this.imagenGuardada=p.getImagenGuardada();


    }

    private int id;
    private String codigoBarras;
    private double precioCompra;
    private double precioVenta;
    private double precioMayoreo;
    private double cantidadMayoreo;
    private int ivaCant;
    private boolean iva;
    private boolean accesoAgil;
    private boolean activo;
    private String descripcionCorta;
    private double comision;
    private String nombreGrupo;
    private String colorGrupo;
    private String imgProdURL;
    private String imgString;
    private boolean paquete;
    private double cantidad;
    private int idVR;
    private String tipoBDL;
    private double ivaTotal;
    private double existencias;
    private List<PaqueteOpcionAux> opciones;
    private String imagenGuardada;

    public String getImagenGuardada() {
        return imagenGuardada;
    }

    public void setImagenGuardada(String imagenGuardada) {
        this.imagenGuardada = imagenGuardada;
    }

    public String getImgString() {
        return imgString;
    }

    public void setImgString(String imgString) {
        this.imgString = imgString;
    }

    public List<PaqueteOpcionAux> getOpciones() {
        return opciones;
    }

    public void setOpciones(List<PaqueteOpcionAux> opciones) {
        this.opciones = opciones;
    }

    public double getExistencias() {
        return existencias;
    }

    public void setExistencias(double existencias) {
        this.existencias = existencias;
    }

    public double getIvaTotal() {
        return ivaTotal;
    }

    public void setIvaTotal(double ivaTotal) {
        this.ivaTotal = ivaTotal;
    }

    public String getTipoBDL() {
        return tipoBDL;
    }

    public void setTipoBDL(String tipoBDL) {
        this.tipoBDL = tipoBDL;
    }

    public int getIdVR() {
        return idVR;
    }

    public void setIdVR(int idVR) {
        this.idVR = idVR;
    }

    public String getNombreGrupo() {
        return nombreGrupo;
    }

    public void setNombreGrupo(String nombreGrupo) {
        this.nombreGrupo = nombreGrupo;
    }

    public String getColorGrupo() {
        return colorGrupo;
    }

    public void setColorGrupo(String colorGrupo) {
        this.colorGrupo = colorGrupo;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isPaquete() {
        return paquete;
    }

    public void setPaquete(boolean paquete) {
        this.paquete = paquete;
    }

    public String getImgProdURL() {
        return imgProdURL;
    }

    public void setImgProdURL(String imgProdURL) {
        this.imgProdURL = imgProdURL;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public boolean isIva() {
        return iva;
    }

    public void setIva(boolean iva) {
        this.iva = iva;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public double getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(double precioCompra) {
        this.precioCompra = precioCompra;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public double getPrecioMayoreo() {
        return precioMayoreo;
    }

    public void setPrecioMayoreo(double precioMayoreo) {
        this.precioMayoreo = precioMayoreo;
    }

    public double getCantidadMayoreo() {
        return cantidadMayoreo;
    }

    public void setCantidadMayoreo(double cantidadMayoreo) {
        this.cantidadMayoreo = cantidadMayoreo;
    }

    public int getIvaCant() {
        return ivaCant;
    }

    public void setIvaCant(int ivaCant) {
        this.ivaCant = ivaCant;
    }


    public boolean isAccesoAgil() {
        return accesoAgil;
    }

    public void setAccesoAgil(boolean accesoAgil) {
        this.accesoAgil = accesoAgil;
    }

    public String getDescripcionCorta() {
        return descripcionCorta;
    }

    public void setDescripcionCorta(String descripcionCorta) {
        this.descripcionCorta = descripcionCorta;
    }

    public double getComision() {
        return comision;
    }

    public void setComision(double comision) {
        this.comision = comision;
    }


}
