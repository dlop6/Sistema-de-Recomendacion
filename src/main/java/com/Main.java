package com;

import org.neo4j.driver.*;
import org.neo4j.driver.Record;

public class Main {
    public static void main(String[] args) {
        Driver driver = GraphDatabase.driver(
                "neo4j+s://9c5d5c00.databases.neo4j.io", //"bolt://localhost:7687",
                AuthTokens.basic(
                        "neo4j",
                        "xgq5hyZRycODNp5U7cQuTg2GwIWNePhoKn4ZOLZoE30"

                        ));

        try (Session session = driver.session()) {

            session.writeTransaction(tx -> {
                tx.run("CREATE (user:Person {name:'Adriana'})");
                tx.run("CREATE (user:Person {name:'Bryan'})");
                tx.run("CREATE (user:Person {name: 'Diego'})");
                tx.run("CREATE (user:Serie {name:'Suits'})");
                tx.run("CREATE (user:Serie {name:'Breaking Bad'})");
                tx.run("CREATE (user:Serie {name:'Vecinos'})");
                tx.run("CREATE (user:Genero {name:'Comedia'})");
                tx.run("CREATE (user:Genero {name:'Accion'})");
                tx.run("CREATE (user:Genero {name:'Drama'})");
                return null;
            });

            Result result = session.run("MATCH (n) RETURN n.name AS name");
            while (result.hasNext()) {
                Record record = result.next();
                String name = record.get("name").asString();
                System.out.println("Name: " + name);
            }

            result = session.run("""
                    MATCH (p:Person {name:"Adriana"}), (a:Serie {name:"Suits"})
                    MERGE (p)-[e:WATCH]-(a)
                    RETURN p.name, e, a.name""");

            result = session.run("""
                    MATCH (a:Serie {name: "Suits"}), (g:Genero {name: "Drama"})
                    MERGE (a)-[e2:GENERO]-(g)
                    RETURN a.name, e2, g.name""");
            
            result = session.run("""
                    MATCH (p:Person {name:"Bryan"}), (a:Serie {name:"Breaking Bad"})
                    MERGE (p)-[e3:WATCH]-(a)
                    RETURN p.name, e3, a.name""");

            result = session.run("""
                    MATCH (a:Serie {name: "Breaking Bad"}), (g:Genero {name: "Accion"})
                    MERGE (a)-[e4:GENERO]-(g)
                    RETURN a.name, e4, g.name""");
                    
            result = session.run("""
                    MATCH (p:Person {name:"Diego"}), (a:Serie {name:"Vecinos"})
                    MERGE (p)-[e5:WATCH]-(a)
                    RETURN p.name, e5, a.name""");

            result = session.run("""
                MATCH (a:Serie {name: "Vecinos"}), (g:Genero {name: "Comedia"})
                MERGE (a)-[e6:GENERO]-(g)
                RETURN a.name, e6, g.name""");

            while (result.hasNext()) {
                Record record = result.next();
                System.out.println(record);
            }

            /*


            // Remove nodes to re-start.

            session.writeTransaction(tx -> {
                tx.run("MATCH (p1:Person {name:'Juan'})-[r:WATCH]-(a2:Serie {name:'Pasion de Gavilanes'}) DELETE r");
                tx.run("MATCH (p:Person) DELETE p");
                tx.run("MATCH (p:Serie) DELETE p");
                tx.run("MATCH (p:Genero) DELETE p");
                return null;
            });

             */

            result = session.run("MATCH (n) RETURN n.name AS name");
            while (result.hasNext()) {
                Record record = result.next();
                String name = record.get("name").asString();
                System.out.println("Name: " + name);
            }

 

        } finally {
            driver.close();
        }
    }
}