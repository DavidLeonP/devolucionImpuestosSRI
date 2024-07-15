package com.d3v.controlador;

import com.d3v.modelo.DatosBeneficios;
import com.d3v.proceso.ConfiguracionURL;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.json.JSONArray;
import org.json.JSONObject;

public class ConsultaPendientes {

    public List<DatosBeneficios> consultar() {
        
        List<DatosBeneficios> datosBeneficio = new ArrayList<DatosBeneficios>();
        int maxRetries = 4;
        int retries = 0;

        while (retries < maxRetries) {
            try {
                String urlParameters = "estado=" + URLEncoder.encode("P", "UTF-8");

                String url = ConfiguracionURL.URL_CONSULTA_DOCUMENTOS_PENDIENTES;
                URL apiUrl = new URL(url + "?" + urlParameters);

                System.err.println(apiUrl);

                // Abrir conexión HTTP
                HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");

                // Habilitar el envío de datos
                connection.setDoOutput(true);

                // Obtener el código de respuesta
                int responseCode = connection.getResponseCode();
                System.out.println("Código de respuesta: " + responseCode);

                // Si la respuesta es exitosa (código 200), leerla y procesarla
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    StringBuilder response = new StringBuilder();

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    // Imprimir la respuesta
                    System.out.println("Respuesta del servicio:");
                    System.out.println(response.toString());

                    datosBeneficio = analizarRespuesta(response.toString());

                    // Cerrar la conexión
                    connection.disconnect();
                    break; // Salir del bucle si la solicitud fue exitosa
                } else if (responseCode == HttpURLConnection.HTTP_INTERNAL_ERROR
                        || responseCode == HttpURLConnection.HTTP_BAD_GATEWAY
                        || responseCode == HttpURLConnection.HTTP_UNAVAILABLE) {
                    // Si hay un error del servidor, se reintentará la solicitud
                    retries++;
                    System.out.println("Reintentando la solicitud (" + retries + " de " + maxRetries + ")");
                    TimeUnit.SECONDS.sleep(5); // Esperar antes de reintentar
                } else {
                    // Si hay otro código de respuesta, manejarlo según sea necesario
                    System.out.println("Código de respuesta inesperado: " + responseCode);
                    // Cerrar la conexión
                    connection.disconnect();
                    break;
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }

        if (retries >= maxRetries) {
            System.out.println("Se ha excedido el número máximo de reintentos.");
        }

        return datosBeneficio;
    }
    
    
    public List<DatosBeneficios> analizarRespuesta(String json) {
        List<DatosBeneficios> datosBeneficio = new ArrayList<DatosBeneficios>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray dataArray = jsonObject.getJSONArray("payload");

            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject dataObject = dataArray.getJSONObject(i);

                DatosBeneficios datos = new DatosBeneficios();
                datos.setRucEmisor(dataObject.getString("rucEmisor"));
                datos.setClaveAccesoComprobante(dataObject.getString("claveAcceso"));
                datos.setIdBeneficiario(dataObject.getString("idBeneficiario"));
                datos.setCodigoBeneficio(dataObject.getString("codigoBeneficio"));
                datos.setBaseImponible(dataObject.getString("baseImponible"));
                datos.setPorcentajeIva(dataObject.getString("porcentajeIva"));
                datos.setMontoIva(dataObject.getString("montoIva"));
                datosBeneficio.add(datos);

            }

        } catch (Exception e) {
            System.err.println("Se generó una excepción al analizar la solicitud: " + e.toString());
        }
        return datosBeneficio;
    }



    
}
