package com.ValidacionDatos;

import org.json.JSONArray;
import org.json.JSONObject;

import com.BaseInicialDatos;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class JSONManager {

    private JSONObject jsonFile;
    File file;

    public JSONManager(String fileName) {
        file = new File(fileName);
        jsonFile = leerJSONDesdeArchivo(file);
    }

    public JSONObject leerJSONDesdeArchivo(File file) {
        JSONObject jsonObject;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }
            jsonObject = new JSONObject(jsonContent.toString());
        } catch (IOException e) {
            // If the file doesn't exist or is empty, create a new JSONObject
            jsonObject = new JSONObject();
            jsonObject.put("users", new JSONArray());
        }
        return jsonObject;
    }

    public void guardarJSONEnArchivo(JSONObject jsonObject, File file) {
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(jsonObject.toString(4)); // Indented with 4 spaces for better readability
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addNewUser() {
        JSONObject newData = new JSONObject();
        BaseInicialDatos baseInicialDatos = new BaseInicialDatos("movies.csv");
        CSVManager csvManager = new CSVManager("movies.csv");

        System.out.println("Ingrese su nombre de usuario:");
        Scanner scanner = new Scanner(System.in);
        String userName = scanner.nextLine();
        System.out.println("Ingresar tus 3 películas favoritas: ");
        JSONArray favoriteMovies = new JSONArray();
        for (int i = 0; i < 3; i++) {
            System.out.print("Ingrese la película favorita #" + (i + 1) + ": ");
            String movie = scanner.nextLine();

            String[] movieData = APIMovies.getMovieData(movie);

            if (movieData != null) {
                favoriteMovies.put(movie);
                csvManager.turnArraytoData(movieData);
                createNodesfromMovies(baseInicialDatos, movieData, movie);

            } else {
                System.out.println("No se encontró información para la película " + movie);
                System.out.println(
                        "Por favor, verifique que el título sea el correcto y  esté en ingles e intente nuevamente.");

                i--;
                continue;
            }

        }
        newData.put("name", userName);
        newData.put("movies", favoriteMovies);
        newData.put("saved", new JSONArray());
        if (findUser(newData.getString("name")) == null) {
            JSONArray users = jsonFile.getJSONArray("users");
            users.put(newData);
            System.out.println("Usuario guardado exitosamente: " + newData.getString("name"));
        } else {
            System.out.println("El usuario " + newData.getString("name") + " ya existe en el sistema.");
        }
        guardarJSONEnArchivo(jsonFile, file);
    }

    public JSONObject findUser(String username) {
        JSONArray users = jsonFile.getJSONArray("users");
        for (int i = 0; i < users.length(); i++) {
            JSONObject user = users.getJSONObject(i);
            if (user.has("name") && user.getString("name").equals(username)) {
                return user;
            }
        }
        return null; // User not found
    }

    public ArrayList<String> getMoviesfromUser(JSONObject user) {
        ArrayList<String> movies = new ArrayList<String>();
        JSONArray moviesArray = user.getJSONArray("movies");
        for (int i = 0; i < moviesArray.length(); i++) {
            movies.add(moviesArray.getString(i));
        }
        return movies;
    }

    public ArrayList<String> getFavoritesfromUser(JSONObject user) {
        ArrayList<String> favorites = new ArrayList<String>();
        JSONArray favoritesArray = user.getJSONArray("saved");
        for (int i = 0; i < favoritesArray.length(); i++) {
            favorites.add(favoritesArray.getString(i));
        }
        return favorites;
    }

    public JSONObject getJSONFile() {
        return jsonFile;
    }

    public  static void createNodesfromMovies(BaseInicialDatos baseInicialDatos, String[] movies, String movie) {
        
        String [] movieData = APIMovies.getMovieData(movie);
        if (movieData != null) {
            String movieTitle = movieData[0];
            String director = movieData[1];
            String actor = movieData[2];
            String genre = movieData[3];
            baseInicialDatos.crearNodo("Pelicula", movieTitle);
            baseInicialDatos.crearNodo("Director", director);
            baseInicialDatos.crearNodo("Actor", actor);
            baseInicialDatos.crearNodo("Genero",genre);
            baseInicialDatos.relacionesNodos(movieTitle, director, actor, genre);
        }
    }

    public static void main(String[] args) {
        System.out.println("Ingresar tus películas favoritas: ");
        BaseInicialDatos baseInicialDatos = new BaseInicialDatos("movies.csv");
        CSVManager csvManager = new CSVManager("movies.csv");
        JSONArray favoriteMovies = new JSONArray();
        
        Scanner scanner = new Scanner(System.in);
        int numMovies = 10; // Change this value to the desired number of movies
        
        for (int i = 0; i < numMovies; i++) {
            System.out.print("Ingrese la película favorita #" + (i + 1) + ": ");
            String movie = scanner.nextLine();

            String[] movieData = APIMovies.getMovieData(movie);

            if (movieData != null) {
            favoriteMovies.put(movie);
            csvManager.turnArraytoData(movieData);
            createNodesfromMovies(baseInicialDatos, movieData, movie);

            } else {
            System.out.println("No se encontró información para la película " + movie);
            System.out.println(
                "Por favor, verifique que el título sea el correcto y esté en inglés e intente nuevamente.");

            i--;
            continue;
            }
        }
        scanner.close();

    }
}
