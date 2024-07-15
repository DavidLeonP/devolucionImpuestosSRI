package com.d3v.controlador;

import com.d3v.modelo.DatosBeneficios;
import com.d3v.proceso.ConfiguracionURL;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class ConsultaDocumentos {

    public void registrar(String token, DatosBeneficios beneficiario) {

        int maxRetries = 4;
        int retries = 0;

        while (retries < maxRetries) {
            try {
                String urlParameters = "token=" + URLEncoder.encode(token, "UTF-8");

                String url = ConfiguracionURL.URL_SOLICITUD_DEVOLUCION;
                URL apiUrl = new URL(url + "?" + urlParameters);

                System.err.println(apiUrl);

                // Abrir conexión HTTP
                HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");

                // Habilitar el envío de datos
                connection.setDoOutput(true);

                // Convertir el objeto movimiento a JSON
                Gson gson = new Gson();
                String jsonPayload = gson.toJson(beneficiario);
                
                System.out.println(jsonPayload);

                // Escribir el JSON en el cuerpo de la solicitud
                try (OutputStream outputStream = connection.getOutputStream()) {
                    byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
                    outputStream.write(input, 0, input.length);
                }

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

    }


    
}
