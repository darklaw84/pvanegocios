package com.anegocios.puntoventa.jsons;

import java.util.List;

public class ClienteDTO  {

    private String idAndroid;
    private long idUT;
    private String msg;
    private List<ClienteXYDTO> clientesxy;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
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

    public List<ClienteXYDTO> getClientesxy() {
        return clientesxy;
    }

    public void setClientesxy(List<ClienteXYDTO> clientesxy) {
        this.clientesxy = clientesxy;
    }
}
