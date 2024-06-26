package com;

import org.neo4j.driver.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class BaseInicialDatos {
    
    private static Driver driver;
    private static String csvFile;


    public BaseInicialDatos(String fileName) {
        csvFile = "src\\data\\" + fileName;
        
        driver = GraphDatabase.driver(
                "neo4j+s://9c5d5c00.databases.neo4j.io", //"bolt://localhost:7687",
                AuthTokens.basic(
                        "neo4j",
                        "xgq5hyZRycODNp5U7cQuTg2GwIWNePhoKn4ZOLZoE30"

                ));
    }

    public static void createNodesandConnections() {
       String line;
       BaseInicialDatos baseInicialDatos = new BaseInicialDatos("movies.csv");

        // Creación de Nodos
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            line = br.readLine(); // Se salta la línea de encabezados
            // Manejo para no repetir nodos
            ArrayList<String> nodosAgregados = new ArrayList<>();
            // Leer el archivo completo
            while ((line = br.readLine()) != null) {
                // Validación de nodos creados
                boolean peliculaEncontrada = false;
                boolean actorEncontrado = false;
                boolean generoEncontrado = false;
                boolean directorEncontrado = false;

                // Dividir todo en secciones
                String[] data = line.split(",");

                // Obtener nombre de la película, y validar si se agrega
                String pelicula = data[0];
                for (String nodo : nodosAgregados) {
                    if (pelicula.equals(nodo)) {
                        peliculaEncontrada = true;
                    }
                }
                if (!peliculaEncontrada) {
                    baseInicialDatos.crearNodo("Pelicula", pelicula);
                    nodosAgregados.add(pelicula);
                }

                // Obtener nombre Director, y validar si se agrega
                String director = "";
                if (data.length > 1) {
                    director = data[1];
                }
                for (String nodo : nodosAgregados) {
                    if (director.equals(nodo)) {
                        directorEncontrado = true;
                    }
                }
                if (!directorEncontrado && !director.isEmpty()) {
                    baseInicialDatos.crearNodo("Director", director);
                    nodosAgregados.add(director);
                }

                // Obtener actor principal, y validar si se agrega
                String actor = "";
                if (data.length > 2) {
                    actor = data[2];
                }
                for (String nodo : nodosAgregados) {
                    if (actor.equals(nodo)) {
                        actorEncontrado = true;
                    }
                }
                if (!actorEncontrado && !actor.isEmpty()) {
                    baseInicialDatos.crearNodo("Actor", actor);
                    nodosAgregados.add(actor);
                }

                // Obtener Género, y validar si se agrega
                String genero = "";
                if (data.length > 3) {
                    genero = data[3];
                }
                for (String nodo : nodosAgregados) {
                    if (genero.equals(nodo)) {
                        generoEncontrado = true;
                    }
                }
                if (!generoEncontrado && !genero.isEmpty()) {
                    baseInicialDatos.crearNodo("Genero", genero);
                    nodosAgregados.add(genero);
                }

                // Establecer todas las relaciones
                baseInicialDatos.relacionesNodos(pelicula, director, actor, genero);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Se cierra el driver
        driver.close();
    }

    public  void crearNodo( String label, String name) {
        String query = String.format("CREATE (n:%s {name: $name})", label);
        try (Session session = driver.session()) {
            session.writeTransaction(tx -> {
                tx.run(query, Map.of("name", name));
                return null;
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  void relacionesNodos(String pelicula, String director, String actor, String genero) {
        try (Session session = driver.session()) {
            // Relacion pelicula-director
            String query = "MATCH (p:Pelicula {name: $pelicula}), (d:Director {name: $director}) " +
                    "MERGE (p)-[:DIRECTED_BY]->(d)";
            session.run(query, Map.of("pelicula", pelicula, "director", director));

            // Relacion pelicula-actor
            query = "MATCH (p:Pelicula {name: $pelicula}), (a:Actor {name: $actor}) " +
                    "MERGE (p)-[:ACTED_BY]->(a)";
            session.run(query, Map.of("pelicula", pelicula, "actor", actor));

            // Relacion pelicula-genero
            query = "MATCH (p:Pelicula {name: $pelicula}), (g:Genero {name: $genero}) " +
                    "MERGE (p)-[:BELONGS_TO]->(g)";
            session.run(query, Map.of("pelicula", pelicula, "genero", genero));
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteAll() {
            try (Session session = driver.session()) {
                session.writeTransaction(tx -> {
                    tx.run("MATCH (n) DETACH DELETE n");
                    return null;
                });
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                driver.close();
            }   
    }

    public static void main(String[] args) {
        System.out.println("Elegir opción:");
        System.out.println("1. Crear nodos y relaciones");
        System.out.println("2. Borrar nodos y relaciones");
        Scanner scanner = new Scanner(System.in);
        int opcion = scanner.nextInt();
        if (opcion == 1) {
            createNodesandConnections();
        } else if (opcion == 2) {
            deleteAll();;
        }
    }
}
