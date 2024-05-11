package com.ValidacionDatos;


import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class APIMovies {

    static String apiKey = "b02b9a4c";


    public static ArrayList<String> getMovie(String movieTitle) {
       

        try {
            // Construir la URL para hacer la solicitud al servicio OMDB API
            String urlString = "http://www.omdbapi.com/?apikey=" + apiKey + "&t=" + movieTitle;
            URL url = new URL(urlString);

            // Realizar la solicitud GET al servicio web
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Leer la respuesta del servicio web
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Convertir la respuesta JSON a un objeto JSONObject
            JSONObject jsonResponse = new JSONObject(response.toString());

            String responseStatus = jsonResponse.getString("Response");

            if (responseStatus.equals("False")) {
                String errorMessage = jsonResponse.getString("Error");
                System.out.println("Error: " + errorMessage);
                return null;
            }

            // Extraer los datos del título, director, actores y género de la película
            String title = jsonResponse.getString("Title");
            String director = jsonResponse.getString("Director");
            String actors = jsonResponse.getString("Actors");
            String genre = jsonResponse.getString("Genre");

            return new ArrayList<String>() {{
                add(title);
                add(director);
                add(actors);
                add(genre);
            }};

        } catch (IOException e) {
            e.printStackTrace();
            return null;

        }
    }


}
