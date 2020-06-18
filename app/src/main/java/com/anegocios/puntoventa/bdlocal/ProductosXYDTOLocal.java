package com.anegocios.puntoventa.bdlocal;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ProductosXYDTOLocal extends RealmObject {

    @PrimaryKey
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
    private int cantidad;
    private int idVR;
    private boolean enviado;
    private int idServer;
    private double existencias;
    private int idTienda;
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

    public int getIdTienda() {
        return idTienda;
    }

    public void setIdTienda(int idTienda) {
        this.idTienda = idTienda;
    }

    public double getExistencias() {
        return existencias;
    }

    public void setExistencias(double existencias) {
        this.existencias = existencias;
    }

    public int getIdServer() {
        return idServer;
    }

    public void setIdServer(int idServer) {
        this.idServer = idServer;
    }

    public boolean isEnviado() {
        return enviado;
    }

    public void setEnviado(boolean enviado) {
        this.enviado = enviado;
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

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
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
