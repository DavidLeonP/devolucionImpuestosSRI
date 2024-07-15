
package com.d3v.controlador;

import com.d3v.proceso.ConfiguracionURL;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import org.json.JSONObject;

public class SolicitudToken {

    String token;
    
    public void procesarSolicitud(String rucContribuyente,String claveContribuyenteMD5){
        
        try {
   
            String url = ConfiguracionURL.URL_SOLICITUD_TOKEN;
            String urlBase = url + URLEncoder.encode(rucContribuyente, "UTF-8") + "/" + URLEncoder.encode(claveContribuyenteMD5, "UTF-8");

            URL apiUrl = new URL(urlBase);
             
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            int responseCode = connection.getResponseCode();
            System.out.println("Código de respuesta: " + responseCode);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            System.out.println("Respuesta del servicio:");
            System.out.println(response.toString());
              
            //Analizar respuesta del servicio
            try {
                JSONObject jsonResponse = new JSONObject(response.toString());

                String accessToken = jsonResponse.getString("access_token");
                System.out.println("Token: " + accessToken);

                setToken(accessToken);
                
            } catch (Exception e) {
                System.out.println("No se puedo analizar la respuesta: " + e.toString());
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
