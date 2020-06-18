package com.anegocios.puntoventa.jsons;

public class CajaTicketDTO {

    private int id;
    private String fechaInicio;
    private int idUT;
    private TicketCajaTicketDTO ticket;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public int getIdUT() {
        return idUT;
    }

    public void setIdUT(int idUT) {
        this.idUT = idUT;
    }

    public TicketCajaTicketDTO getTicket() {
        return ticket;
    }

    public void setTicket(TicketCajaTicketDTO ticket) {
        this.ticket = ticket;
    }
}
