package com.ExtracciónDatos;

import org.neo4j.driver.*;
import org.neo4j.driver.Record;
import java.util.*;

public class MovieExtractor {

    public static Map<String, String[]> extractMovieInfo() {
        // Mapa para almacenar la información de las películas
        Map<String, String[]> movieMap = new HashMap<>();

        // Conexión a la base de datos de Neo4j
        try (Driver driver = GraphDatabase.driver("neo4j+s://9c5d5c00.databases.neo4j.io", AuthTokens.basic("neo4j", "xgq5hyZRycODNp5U7cQuTg2GwIWNePhoKn4ZOLZoE30"))) {
            // Consulta Cypher para recuperar las películas con sus relaciones
            try (Session session = driver.session()) {
                // Consulta para obtener las películas con sus directores, actores y géneros
                String query = "MATCH (p:Pelicula)-[:DIRECTED]->(d:Director), (p)-[:ACTED]->(a:Actor), (p)-[:GENRE]->(g:Genero) RETURN p.name AS movie, d.name AS director, a.name AS actor, g.name AS genre";

                // Ejecutar la consulta
                Result result = session.run(query);

                // Procesar los resultados
                while (result.hasNext()) {
                    Record record = result.next();
                    String movie = record.get("movie").asString();
                    String director = record.get("director").asString();
                    String actor = record.get("actor").asString();
                    String genre = record.get("genre").asString();

                    // Agregar la información de la película al mapa
                    if (!movieMap.containsKey(movie)) {
                        String[] movieInfo = new String[3];
                        movieInfo[0] = director;
                        movieInfo[1] = actor;
                        movieInfo[2] = genre;
                        movieMap.put(movie, movieInfo);
                    }
                }
            }
        }

        return movieMap;
    }
}
