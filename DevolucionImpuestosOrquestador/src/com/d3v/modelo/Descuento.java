package com.d3v.modelo;

public class Descuento {

    private double montoIvaDevolver;
    private String codigo;
    private String mensaje;

    public Descuento() {
        super();
    }

    public Descuento(double montoIvaDevolver, String codigo, String mensaje) {
        this.montoIvaDevolver = montoIvaDevolver;
        this.codigo = codigo;
        this.mensaje = mensaje;
    }

    public double getMontoIvaDevolver() {
        return montoIvaDevolver;
    }

    public void setMontoIvaDevolver(double montoIvaDevolver) {
        this.montoIvaDevolver = montoIvaDevolver;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
