package com.anegocios.puntoventa.jsons;

import com.anegocios.puntoventa.bdlocal.ProductosXYDTOLocal;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ProductosXYDTO extends RealmObject {

    public ProductosXYDTO()
    {

    }


    public ProductosXYDTO(ProductosXYDTOLocal l)
    {
        this.codigoBarras=l.getCodigoBarras();
        this.precioCompra=l.getPrecioCompra();
        this.precioVenta=l.getPrecioVenta();
        this.precioMayoreo=l.getPrecioMayoreo();
        this.cantidadMayoreo=l.getCantidadMayoreo();
        this.ivaCant=l.getIvaCant();
        this.iva=l.isIva();
        this.descripcionCorta=l.getDescripcionCorta();
        this.comision=l.getComision();
        this.idVR=l.getIdVR();
        this.existencias=l.getExistencias();
        this.idTienda=l.getIdTienda();
        this.imgString=l.getImgString();
    }


    public ProductosXYDTO(ProductosXYDTO l)
    {
        this.id=l.getId();
        this.codigoBarras=l.getCodigoBarras();
        this.precioCompra=l.getPrecioCompra();
        this.precioVenta=l.getPrecioVenta();
        this.precioMayoreo=l.getPrecioMayoreo();
        this.cantidadMayoreo=l.getCantidadMayoreo();
        this.ivaCant=l.getIvaCant();
        this.iva=l.isIva();
        this.descripcionCorta=l.getDescripcionCorta();
        this.comision=l.getComision();
        this.idVR=l.getIdVR();
        this.existencias=l.getExistencias();
        this.idTienda=l.getIdTienda();
        this.imgString=l.getImgString();
    }


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
    private double cantidad;
    private int idVR;
    private GrupoVRDTO idGrupoVR;
    private boolean editado;
    private int idAPP;
    private double existencias;
    private int idTienda;
    private RealmList<PaqueteXYDTO>  paquetesxy;
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

    public int getIdAPP() {
        return idAPP;
    }

    public void setIdAPP(int idAPP) {
        this.idAPP = idAPP;
    }

    public double getExistencias() {
        return existencias;
    }

    public void setExistencias(double existencias) {
        this.existencias = existencias;
    }

    public boolean isEditado() {
        return editado;
    }

    public void setEditado(boolean editado) {
        this.editado = editado;
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

    public GrupoVRDTO getIdGrupoVR() {
        return idGrupoVR;
    }

    public void setIdGrupoVR(GrupoVRDTO idGrupoVR) {
        this.idGrupoVR = idGrupoVR;
    }

    public List<PaqueteXYDTO> getPaquetesxy() {
        return paquetesxy;
    }

    public void setPaquetesxy(RealmList<PaqueteXYDTO> paquetesxy) {
        this.paquetesxy = paquetesxy;
    }
}
