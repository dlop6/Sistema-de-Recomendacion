package com.ValidacionDatos;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class APIMovies {

    static String apiKey = "b02b9a4c";

    public static String[] getMovieData(String movieTitle) {

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
            String director = jsonResponse.getString("Director");
            String actors = jsonResponse.getString("Actors");
            String genre = jsonResponse.getString("Genre");

            // Get only the first value of actors and genre
            String[] movieData = new String[3];
            movieData[0] = director.split(",")[0];
            movieData[1] = actors.split(",")[0];
            movieData[2] = genre.split(",")[0];

            return movieData;

        } catch (IOException e) {
            e.printStackTrace();
            return null;

        }
    }

    public static String [] getAllMovieData(JSONObject user){
        JSONArray userMovies = user.getJSONArray("movies");
        String[] movieData = new String[9];
        int count = 0;
        for (int i = 0; i < userMovies.length(); i++) {

            String movie = userMovies.getString(i);
            String[] data = getMovieData(movie);
            if (data != null) {
                movieData[count] = data[0];
                movieData[count + 1] = data[1];
                movieData[count + 2] = data[2];
            }
            count += 3;
        }
        return movieData;

    }

}
