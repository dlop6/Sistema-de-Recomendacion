package com.Driver;

import com.ValidacionDatos.JSONManager;

import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONObject;

public class Main {

    JSONObject user = new JSONObject();

    public static void main(String[] args) {
        System.out.println("BIENVENIDO A PELIFLIX, TU SISTEMA DE RECOMENDACIÓN DE PELÍCULAS FAVORITO");
        System.out.println("1. Iniciar sesión");
        System.out.println("2. Registrarse");
        // Leer el nombre de usuario
        
        Scanner scanner = new Scanner(System.in);
        String opcion = scanner.nextLine();

   
        JSONManager newUser = new JSONManager("pruebasneo4j\\data\\users.json");

        // Verificar si el usuario ya existe en el sistema
        switch (opcion) {
            case "1":
                System.out.println("Ingrese su nombre de usuario:");
                JSONObject user = newUser.findUser(scanner.nextLine());
                if (user == null) {
                    System.out.println("El usuario no existe en el sistema.");
                    break;
                } else {
                    System.out.println("Bienvenido , " + user.getString("name"));
                    ArrayList<String> userMovies = newUser.getMoviesfromUser(user);
                    break;    
                }
                
            case "2":
                newUser.addNewUser();
                System.out.println("Usuario creado exitosamente, ya puedes iniciar sesión.");

                break;
        
            default:
                break;
        }


    }
    
}
