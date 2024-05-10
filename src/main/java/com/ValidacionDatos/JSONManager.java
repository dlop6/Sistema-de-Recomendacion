package com.ValidacionDatos;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;
import java.util.List;

public class JSONManager {
    public static void main(String[] args) {
        // Create or load the existing JSON file
        File file = new File("pruebasneo4j\\data\\users.json");

        // Read the existing JSON data from the file
        JSONObject jsonObject = leerJSONDesdeArchivo(file);

        // Append new data
        JSONObject newData = new JSONObject();
        newData.put("name", "Maria");
        newData.put("movies", new JSONArray(List.of("Movie1", "Movie2")));
        newData.put("favorites", new JSONArray(List.of("Favorite1", "Favorite2")));

        appendData(jsonObject, newData);

        // Save the updated JSON to the file
        guardarJSONEnArchivo(jsonObject, file);
    }

    public static JSONObject leerJSONDesdeArchivo(File file) {
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

    public static void appendData(JSONObject jsonObject, JSONObject newData) {
        JSONArray users = jsonObject.getJSONArray("users");
        users.put(newData);
    }

    public static void guardarJSONEnArchivo(JSONObject jsonObject, File file) {
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(jsonObject.toString(4)); // Indented with 4 spaces for better readability
            System.out.println("JSON data appended to file: " + file.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
