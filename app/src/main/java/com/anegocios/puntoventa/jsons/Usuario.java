package com.anegocios.puntoventa.jsons;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Usuario extends RealmObject {

    @PrimaryKey
    private Integer id;

    private String username;

    private String pass;

    private String salt;

    private Boolean clientes;

    private Boolean productos;
    private Boolean vendedores;

    private Boolean proveedores;

    private Boolean reportes;

    private Boolean usarCaja;

    private Boolean cortarCaja;

    private Boolean cotizar;

    private Boolean elevarCotizacion;

    private Boolean venderCredito;

    private Boolean pedido;

    private Boolean pagosCxC;


    private int utInt;



    private RealmList<UT> UT = null;

    private Boolean clt_C;
    private Boolean clt_R;
    private Boolean clt_U;
    private Boolean clt_D;
    private Boolean prod_C;
    private Boolean prod_R;
    private Boolean prod_U;
    private Boolean prod_D;
    private Boolean cajaDesc;
    private Boolean cajaPropina;
    private Boolean editPrecio;
    private Boolean mostrarImagenes;
    private String vigencia;
    private String tipo;

    public String getVigencia() {
        return vigencia;
    }

    public void setVigencia(String vigencia) {
        this.vigencia = vigencia;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Boolean getMostrarImagenes() {
        return mostrarImagenes;
    }

    public void setMostrarImagenes(Boolean mostrarImagenes) {
        this.mostrarImagenes = mostrarImagenes;
    }

    public Boolean getClt_C() {
        return clt_C;
    }

    public void setClt_C(Boolean clt_C) {
        this.clt_C = clt_C;
    }

    public Boolean getClt_R() {
        return clt_R;
    }

    public void setClt_R(Boolean clt_R) {
        this.clt_R = clt_R;
    }

    public Boolean getClt_U() {
        return clt_U;
    }

    public void setClt_U(Boolean clt_U) {
        this.clt_U = clt_U;
    }

    public Boolean getClt_D() {
        return clt_D;
    }

    public void setClt_D(Boolean clt_D) {
        this.clt_D = clt_D;
    }

    public Boolean getProd_C() {
        return prod_C;
    }

    public void setProd_C(Boolean prod_C) {
        this.prod_C = prod_C;
    }

    public Boolean getProd_R() {
        return prod_R;
    }

    public void setProd_R(Boolean prod_R) {
        this.prod_R = prod_R;
    }

    public Boolean getProd_U() {
        return prod_U;
    }

    public void setProd_U(Boolean prod_U) {
        this.prod_U = prod_U;
    }

    public Boolean getProd_D() {
        return prod_D;
    }

    public void setProd_D(Boolean prod_D) {
        this.prod_D = prod_D;
    }

    public Boolean getCajaDesc() {
        return cajaDesc;
    }

    public void setCajaDesc(Boolean cajaDesc) {
        this.cajaDesc = cajaDesc;
    }

    public Boolean getCajaPropina() {
        return cajaPropina;
    }

    public void setCajaPropina(Boolean cajaPropina) {
        this.cajaPropina = cajaPropina;
    }

    public Boolean getEditPrecio() {
        return editPrecio;
    }

    public void setEditPrecio(Boolean editPrecio) {
        this.editPrecio = editPrecio;
    }

    public int getUtInt() {
        return utInt;
    }

    public void setUtInt(int utInt) {
        this.utInt = utInt;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Boolean getClientes() {
        return clientes;
    }

    public void setClientes(Boolean clientes) {
        this.clientes = clientes;
    }

    public Boolean getProductos() {
        return productos;
    }

    public void setProductos(Boolean productos) {
        this.productos = productos;
    }

    public Boolean getVendedores() {
        return vendedores;
    }

    public void setVendedores(Boolean vendedores) {
        this.vendedores = vendedores;
    }

    public Boolean getProveedores() {
        return proveedores;
    }

    public void setProveedores(Boolean proveedores) {
        this.proveedores = proveedores;
    }

    public Boolean getReportes() {
        return reportes;
    }

    public void setReportes(Boolean reportes) {
        this.reportes = reportes;
    }

    public Boolean getUsarCaja() {
        return usarCaja;
    }

    public void setUsarCaja(Boolean usarCaja) {
        this.usarCaja = usarCaja;
    }

    public Boolean getCortarCaja() {
        return cortarCaja;
    }

    public void setCortarCaja(Boolean cortarCaja) {
        this.cortarCaja = cortarCaja;
    }

    public Boolean getCotizar() {
        return cotizar;
    }

    public void setCotizar(Boolean cotizar) {
        this.cotizar = cotizar;
    }

    public Boolean getElevarCotizacion() {
        return elevarCotizacion;
    }

    public void setElevarCotizacion(Boolean elevarCotizacion) {
        this.elevarCotizacion = elevarCotizacion;
    }

    public Boolean getVenderCredito() {
        return venderCredito;
    }

    public void setVenderCredito(Boolean venderCredito) {
        this.venderCredito = venderCredito;
    }

    public Boolean getPedido() {
        return pedido;
    }

    public void setPedido(Boolean pedido) {
        this.pedido = pedido;
    }

    public Boolean getPagosCxC() {
        return pagosCxC;
    }

    public void setPagosCxC(Boolean pagosCxC) {
        this.pagosCxC = pagosCxC;
    }


    public RealmList<com.anegocios.puntoventa.jsons.UT> getUT() {
        return UT;
    }

    public void setUT(RealmList<com.anegocios.puntoventa.jsons.UT> UT) {
        this.UT = UT;
    }
}
