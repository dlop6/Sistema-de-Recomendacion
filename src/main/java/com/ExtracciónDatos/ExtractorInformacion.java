package com.ExtracciónDatos;

import org.neo4j.driver.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.neo4j.driver.Record;

public class ExtractorInformacion {
    // Método para extraer la información de las películas
    public static Map<String, String[]> extraerInformacionPeliculas() {
        // Consulta para obtener el nombre de todas las películas
        String query = "MATCH (p:Pelicula) RETURN p.name AS pelicula";
        // Mapa para almacenar el nombre de la película y su información (director, actor, género)
        Map<String, String[]> peliculasConInformacion = new HashMap<>();

        try (
            // Establecer la conexión con la base de datos Neo4j
            Driver driver = GraphDatabase.driver("neo4j+s://9c5d5c00.databases.neo4j.io",
                                                  AuthTokens.basic("neo4j", "xgq5hyZRycODNp5U7cQuTg2GwIWNePhoKn4ZOLZoE30"));
            // Crear una sesión para ejecutar consultas
            Session session = driver.session()) {

            // Ejecutar la consulta para obtener el nombre de todas las películas
            Result result = session.run(query);

            // Iterar sobre los resultados
            while (result.hasNext()) {
                Record record = result.next();
                // Obtener el nombre de la película
                String pelicula = record.get("pelicula").asString();
                // Obtener la información de la película y almacenarla en el mapa
                String[] informacion = obtenerInformacionPelicula(session, pelicula);
                peliculasConInformacion.put(pelicula, informacion);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return peliculasConInformacion;
    }

    // Método para obtener la información de una película específica
    public static String[] obtenerInformacionPelicula(Session session, String pelicula) {
        // Arreglo para almacenar la información (director, actor, género) de la película
        String[] informacion = new String[3];
        // Consulta para obtener la información de la película y sus relaciones
        String query = "MATCH (p:Pelicula {name: $pelicula}) " +
                       "OPTIONAL MATCH (p)-[:DIRECTED_BY]->(d:Director) " +
                       "OPTIONAL MATCH (p)-[:ACTED_BY]->(a:Actor) " +
                       "OPTIONAL MATCH (p)-[:BELONGS_TO]->(g:Genero) " +
                       "RETURN d.name AS director, a.name AS actor, g.name AS genero";
        
        try {
            // Parámetros para la consulta
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("pelicula", pelicula);
            // Ejecutar la consulta y obtener el resultado
            Result result = session.run(query, parameters);
            // Verificar si hay resultados
            if (result.hasNext()) {
                Record record = result.next();
                // Obtener el nombre del director, actor y género de la película
                informacion[0] = record.get("director", "");
                informacion[1] = record.get("actor", "");
                informacion[2] = record.get("genero", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return informacion;
    }
}
