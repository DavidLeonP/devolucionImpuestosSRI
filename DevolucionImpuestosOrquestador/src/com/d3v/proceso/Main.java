
package com.d3v.proceso;

import com.d3v.controlador.*;
import com.d3v.modelo.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.codec.digest.DigestUtils;

public class Main {

    public static void main(String[] args) {

        //Solicitud de token para habilitar autorizacion de devolucion de impuestos
        //Tiempo maximo de uso de token 35 minutos
        String md5Hash = DigestUtils.md5Hex(ConfiguracionURL.SRI_CREDENCIAL);
        System.err.println(md5Hash);
        SolicitudToken solicitudToken = new SolicitudToken();
        solicitudToken.procesarSolicitud(ConfiguracionURL.SRI_USUARIO, md5Hash);

        // Consulta individual de informacion de solicitud
        List<DatosBeneficios> listaDatosBenficiario = new ArrayList<DatosBeneficios>();
        ConsultaPendientes servicioConsultaPendientes = new ConsultaPendientes();
        listaDatosBenficiario = servicioConsultaPendientes.consultar();
        for (DatosBeneficios datosBeneficio  : listaDatosBenficiario) {
            System.out.println("Procesando...");

            //Registrar solicitud de autorizacion ambiente de pruebas
            SolicitudDevolucion solicitudDevolucion = new SolicitudDevolucion();
            DatosBeneficios datos = new DatosBeneficios(
                    datosBeneficio.getRucEmisor(), //Ruc de vendedor
                    datosBeneficio.getClaveAccesoComprobante(), // Clave de acceso
                    datosBeneficio.getIdBeneficiario(), //Cedula del cliente
                    datosBeneficio.getCodigoBeneficio(), // Codigo del cliente
                    datosBeneficio.getBaseImponible(), // Total con Impuestos
                    datosBeneficio.getPorcentajeIva(), // Tarifa con Impuestos
                    datosBeneficio.getMontoIva()); // Impuesto
            solicitudDevolucion.registrar(solicitudToken.getToken(), datos);
            
            
            ActualizacionEstado servicioActualizarEstado = new ActualizacionEstado();
            DatosActualizar datosActualizar = new DatosActualizar();
            datosActualizar.setClaveAccesoComprobante(datosBeneficio.getClaveAccesoComprobante());
            datosActualizar.setMensajeSolicitud(solicitudDevolucion.getRespuestaSolicitud());
            datosActualizar.setEstado("X");
            servicioActualizarEstado.actualizar(datosActualizar);
            
            System.out.println("Termina solicitud");
        }

    }
    
}
