package com;

import org.neo4j.driver.*;
import org.neo4j.driver.Record;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class BaseInicialDatos {
    public static void main(String[] args) {
        System.out.println("Elegir opción:");
        System.out.println("1. Crear nodos y relaciones");
        System.out.println("2. Borrar nodos y relaciones");
        Scanner scanner = new Scanner(System.in);
        int opcion = scanner.nextInt();
        if(opcion == 1){
            createNodesandConnections();
        } else if(opcion == 2){
            DeleteAllNodesAndRelationships.main(args);
        }

    }

    public static void createNodesandConnections(){
        String csvFile = "pruebasneo4j\\movies.csv";
        String line;
        Driver driver = GraphDatabase.driver(
                "neo4j+s://9c5d5c00.databases.neo4j.io", //"bolt://localhost:7687",
                AuthTokens.basic(
                        "neo4j",
                        "xgq5hyZRycODNp5U7cQuTg2GwIWNePhoKn4ZOLZoE30"

                        ));

        //Creación de Nodos
        try(BufferedReader br = new BufferedReader(new FileReader(csvFile))){
            line = br.readLine(); //Se salta la línea de encabezados
            //Manejo para no repetir nodos
            ArrayList<String> nodosAgregados = new ArrayList<>();
            //Leer el archivo completo
            while((line = br.readLine()) != null){
                //Validación de nodos creados
                boolean peliculaEncontrada = false;
                boolean actorEncontrado = false;
                boolean generoEncontrado = false;
                boolean directorEncontrado = false;

                //Dividir todo en secciones
                String[] data = line.split(",");

                //Obtener nombre de la película, y validar si se agrega
                String pelicula = data[0];
                for(String nodo:nodosAgregados){
                    if(pelicula.equals(nodo)){
                        peliculaEncontrada = true;
                    }
                } if(!peliculaEncontrada){
                    crearNodosPelicula(driver, pelicula);
                    nodosAgregados.add(pelicula);
                }

                //Obtener nombre Director, y validar si se agrega
                String director = "";
                if (data.length > 1) {
                    director = data[1];
                }
                for(String nodo:nodosAgregados){
                    if(director.equals(nodo)){
                        directorEncontrado = true;
                    }
                } if(!directorEncontrado && !director.isEmpty()){
                    crearNodosDirector(driver, director);
                    nodosAgregados.add(director);
                }

                //Obtener actor principal, y validar si se agrega
                String actor = "";
                if (data.length > 2) {
                    actor = data[2];
                }
                for(String nodo:nodosAgregados){
                    if(actor.equals(nodo)){
                        actorEncontrado = true;
                    }
                } if(!actorEncontrado && !actor.isEmpty()){
                    crearNodosActor(driver, actor);
                    nodosAgregados.add(actor);
                }

                //Obtener Género, y validar si se agrega
                String genero = "";
                if (data.length > 3) {
                    genero = data[3];
                }
                for(String nodo: nodosAgregados){
                    if(genero.equals(nodo)){
                        generoEncontrado = true;
                    }
                } if(!generoEncontrado && !genero.isEmpty()){
                    crearNodosGenero(driver, genero);
                    nodosAgregados.add(genero);
                }

                //Establecer todas las relaciones
                relacionesNodos(driver, pelicula, director, actor, genero);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Se cierra el driver
    }

    public static void crearNodosPelicula(Driver driver, String pelicula){
        String query = "CREATE (user:Pelicula {name: '" + pelicula.replaceAll("'", "\\\\'") +"'})";
        try (Session session = driver.session()){
            session.writeTransaction(tx -> {
                tx.run(query);
                return null;
            });
        }
    }

    public static void crearNodosDirector(Driver driver, String director){
        String query = "CREATE (user:Director {name: '" + director.replaceAll("'", "\\\\'") +"'})";
        try (Session session = driver.session()){
            session.writeTransaction(tx -> {
                tx.run(query);
                return null;
            });
        }
    }

    public static void crearNodosActor(Driver driver, String actor){
        String query = "CREATE (user:Actor {name: '" + actor.replaceAll("'", "\\\\'") +"'})";
        try (Session session = driver.session()){
            session.writeTransaction(tx -> {
                tx.run(query);
                return null;
            });
        }
    }

    public static void crearNodosGenero(Driver driver, String genero){
        String query = "CREATE (user:Genero {name: '" + genero.replaceAll("'", "\\\\'") +"'})";
        try (Session session = driver.session()){
            session.writeTransaction(tx -> {
                tx.run(query);
                return null;
            });
        } 
    }

    public static void relacionesNodos(Driver driver, String pelicula, String director, String actor, String genero){
        try(Session session =driver.session()){
            //Base para crear el match
            Result result = session.run("MATCH (n) RETURN n.name AS name");
            while (result.hasNext()) {
                Record record = result.next();
                String name = record.get("name").asString();
                System.out.println("Name: " + name);
            }
            //Relacion pelicula-director
            String query = "MATCH (p:Pelicula {name:\"" + pelicula +"\"}), (d:Director {name:\"" + director + "\"})" +
            "MERGE (p)-[e:DIRECTED]-(d)"+
            "RETURN p.name, e, d.name";
            result = session.run(query);

            //Relacion pelicula-actor
            String query1 = "MATCH (p:Pelicula {name:\"" + pelicula +"\"}), (a:Actor {name:\"" + actor + "\"})" +
            "MERGE (p)-[e2:ACTED]-(a)"+
            "RETURN p.name, e2, a.name";
            result = session.run(query1);

            //Relacion pelicula-genero
            String query2 = "MATCH (p:Pelicula {name:\"" + pelicula +"\"}), (g:Genre {name:\"" + genero + "\"})" +
            "MERGE (p)-[e3:GENRE]-(g)"+
            "RETURN p.name, e3, g.name";
            result = session.run(query2);

        }
    }

    public class DeleteAllNodesAndRelationships {
        public static void main(String[] args) {
            Driver driver = GraphDatabase.driver("neo4j+s://9c5d5c00.databases.neo4j.io", AuthTokens.basic("neo4j", "xgq5hyZRycODNp5U7cQuTg2GwIWNePhoKn4ZOLZoE30"));
    
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
    }
}