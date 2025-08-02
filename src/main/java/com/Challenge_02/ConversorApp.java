package com.Challenge_02;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashSet;
import java.util.Set;

public class ConversorApp {

    public static void main(String[] args) throws IOException, InterruptedException {
        Conversor.eleccionUserMenu();
    }

    public static double obtenerTasa(String urlFinal, String monedaObjetivo) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(urlFinal)).build();
        HttpResponse<String> respuesta = client.send(request, HttpResponse.BodyHandlers.ofString());
        String jsonResponse = respuesta.body();
        JsonElement elemento = JsonParser.parseString(jsonResponse);
        JsonObject objectRoot = elemento.getAsJsonObject();
        if (objectRoot.has("result") && !objectRoot.get("result").getAsString().equalsIgnoreCase("success")) {
            if (objectRoot.has("error-type")) {
                System.out.println("Error de API: " + objectRoot.get("error-type").getAsString() + ". Por favor, verifique los códigos de moneda.");
            } else {
                System.out.println("Error de API: Respuesta no exitosa.");
            }
            return 0.0;
        }
        JsonObject conversionRates = objectRoot.getAsJsonObject("conversion_rates");
        if (conversionRates != null && conversionRates.has(monedaObjetivo)) {
            return conversionRates.get(monedaObjetivo).getAsDouble();
        } else {
            System.out.println("Error: No se encontró la tasa para la moneda objetivo en la respuesta de la API.");
            return 0.0;
        }
    }


    public static Set<String> obtenerCodigosMoneda(String claveAPI) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        String url = "https://v6.exchangerate-api.com/v6/" + claveAPI + "/codes";
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        HttpResponse<String> respuesta = client.send(request, HttpResponse.BodyHandlers.ofString());
        String jsonResponse = respuesta.body();
        JsonElement elemento = JsonParser.parseString(jsonResponse);
        JsonObject objectRoot = elemento.getAsJsonObject();
        Set<String> codigosValidos = new HashSet<>();

        if (objectRoot.has("supported_codes")) {
            JsonArray supportedCodes = objectRoot.getAsJsonArray("supported_codes");
            for (JsonElement codigoElemento : supportedCodes) {
                JsonArray codigoArray = codigoElemento.getAsJsonArray();
                codigosValidos.add(codigoArray.get(0).getAsString());
            }
        }
        return codigosValidos;
    }
}