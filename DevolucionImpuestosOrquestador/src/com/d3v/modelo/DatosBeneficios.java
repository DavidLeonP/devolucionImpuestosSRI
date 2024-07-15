
package com.d3v.modelo;

public class DatosBeneficios {

    private String rucEmisor;
    private String claveAccesoComprobante;
    private String idBeneficiario;
    private String codigoBeneficio;
    private String baseImponible;
    private String porcentajeIva;
    private String montoIva;

    public DatosBeneficios() {
        super();
    }

    public DatosBeneficios(String rucEmisor, String claveAccesoComprobante, String idBeneficiario, String codigoBeneficio, String baseImponible, String porcentajeIva, String montoIva) {
        this.rucEmisor = rucEmisor;
        this.claveAccesoComprobante = claveAccesoComprobante;
        this.idBeneficiario = idBeneficiario;
        this.codigoBeneficio = codigoBeneficio;
        this.baseImponible = baseImponible;
        this.porcentajeIva = porcentajeIva;
        this.montoIva = montoIva;
    }

    public String getRucEmisor() {
        return rucEmisor;
    }

    public void setRucEmisor(String rucEmisor) {
        this.rucEmisor = rucEmisor;
    }

    public String getClaveAccesoComprobante() {
        return claveAccesoComprobante;
    }

    public void setClaveAccesoComprobante(String claveAccesoComprobante) {
        this.claveAccesoComprobante = claveAccesoComprobante;
    }

    public String getIdBeneficiario() {
        return idBeneficiario;
    }

    public void setIdBeneficiario(String idBeneficiario) {
        this.idBeneficiario = idBeneficiario;
    }

    public String getCodigoBeneficio() {
        return codigoBeneficio;
    }

    public void setCodigoBeneficio(String codigoBeneficio) {
        this.codigoBeneficio = codigoBeneficio;
    }

    public String getBaseImponible() {
        return baseImponible;
    }

    public void setBaseImponible(String baseImponible) {
        this.baseImponible = baseImponible;
    }

    public String getPorcentajeIva() {
        return porcentajeIva;
    }

    public void setPorcentajeIva(String porcentajeIva) {
        this.porcentajeIva = porcentajeIva;
    }

    public String getMontoIva() {
        return montoIva;
    }

    public void setMontoIva(String montoIva) {
        this.montoIva = montoIva;
    }

    
}
