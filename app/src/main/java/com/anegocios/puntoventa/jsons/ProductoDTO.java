package com.anegocios.puntoventa.jsons;

import java.util.List;



public class ProductoDTO  {

    private String idAndroid;
    private int idUT;
    private List<ProductosXYDTO> productosxy;
    private boolean all;
    private boolean exito;
    private String msg;
    private String idTienda;

    public String getIdTienda() {
        return idTienda;
    }

    public void setIdTienda(String idTienda) {
        this.idTienda = idTienda;
    }

    public String getIdAndroid() {
        return idAndroid;
    }

    public void setIdAndroid(String idAndroid) {
        this.idAndroid = idAndroid;
    }

    public int getIdUT() {
        return idUT;
    }

    public void setIdUT(int idUT) {
        this.idUT = idUT;
    }

    public List<ProductosXYDTO> getProductosxy() {
        return productosxy;
    }

    public void setProductosxy(List<ProductosXYDTO> productosxy) {
        this.productosxy = productosxy;
    }

    public boolean isAll() {
        return all;
    }

    public void setAll(boolean all) {
        this.all = all;
    }

    public boolean isExito() {
        return exito;
    }

    public void setExito(boolean exito) {
        this.exito = exito;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
