package com.Driver;

import com.ExtracciónDatos.ExtractorInformacion;
import com.ValidacionDatos.JSONManager;
import com.ValidacionDatos.APIMovies;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Map;

import org.json.JSONObject;

public class Main {
    private static JSONObject user = new JSONObject();
    private static ExtractorInformacion extractorData = new ExtractorInformacion();

    public static void main(String[] args) {
        //Primer paso es cargar la versión más actualizada de la base de datos
        Map<String, String[]> dataPeliculas = extractorData.extraerInformacionPeliculas();

        boolean systemON = true;
        Scanner scanner = new Scanner(System.in);

        //Primer menú (manejo de usuarios)
        while(systemON){
            boolean systemON2 = true; //Variable a utilizar para el segundo menú.
            System.out.println("----BIENVENIDO A PELIFLIX, TU SISTEMA DE RECOMENDACIÓN DE PELÍCULAS FAVORITO----");
            System.out.println("1. Iniciar sesión");
            System.out.println("2. Registrarse");
            System.out.println("3. Salir del Programa");
            // Leer el nombre de usuario
            String opcion = scanner.nextLine();   
            JSONManager newUser = new JSONManager("pruebasneo4j\\src\\data\\users.json");
            // Verificar si el usuario ya existe en el sistema
            switch (opcion) {
                case "1":
                    System.out.println("-----------------------------------------");
                    System.out.println("Ingrese su nombre de usuario:");
                    JSONObject user = newUser.findUser(scanner.nextLine());
                    if (user == null) {
                        System.out.println("El usuario no existe en el sistema.\n");
                        break;
                    } 
                    //A partir de acá, si el usuario fue encontrado se trabajará con el menú de opciones principales
                    else {
                        System.out.println("Bienvenido, " + user.getString("name"));
                        System.out.println("¿Listo para una nueva aventura?");
                        ArrayList<String> userMovies = newUser.getMoviesfromUser(user);
                        ArrayList<String> movieData = APIMovies.getMovieData(userMovies.get(0));
                        // El array se verá de la siguiente manera:
                        // [Título, Director, Actor Principal, Género]
                        String[] infoPeliculaUsuario = new String[4];
                        for(int i = 0; i < movieData.size(); i++){
                            infoPeliculaUsuario[i] = movieData.get(i);
                        }
                        
                        //Segundo Menú a Desplegar
                        while(systemON2){
                            //Impresión de menú principal
                            System.out.println("1. Recomendación de Películas Personalizado");
                            System.out.println("(se mostrarán películas clasificadas acorde a coincidencias)");
                            System.out.println("2. Recomendación únicamente por género");
                            System.out.println("3. Recomendación únicamente por directo");
                            System.out.println("4. Recomendación únicamente por actor principal");
                            System.out.println("5. Regresar al Menú Principal");

                            switch (opcion = scanner.nextLine()) {
                                //Recomendación de Películas Personalizado
                                case "1":
                                    //Colocar método para recomendación completa
                                    getRecomendacionCompleta(dataPeliculas, infoPeliculaUsuario);
                                    break;
                            
                                //Recomendación de Películas por género
                                case "2":
                                    getRecomendacionCriterioUnico(dataPeliculas, infoPeliculaUsuario, "Genero");
                                    break;

                                //Recomendación de Películas por director
                                case "3":
                                    getRecomendacionCriterioUnico(dataPeliculas, infoPeliculaUsuario, "Director");
                                    break;

                                //Recomendación de Películas por actor
                                case "4":
                                getRecomendacionCriterioUnico(dataPeliculas, infoPeliculaUsuario, "Actor");
                                break;

                                //Regresar al menú principal
                                case "5":
                                    System.out.println("Esperamos haberte ayudado!!!\n");
                                    System.out.println("----- regresando al menú principal -----");
                                    systemON2 = false;
                                    break;

                                //En caso de input erróneo
                                default:
                                    System.out.println("No has colocado una opción válida...");
                                    System.out.println("Por favor, vuelve a intentarlo.");
                                    break;
                            }
                        }

                        break;
                    }
                    
                case "2":
                    //EN ESTÁ PARTE SE TIENE QUE VALIDAR QUE LA PELÍCULA A USAR SEA AGREGADA A NEO4J
                    System.out.println("-----------------------------------------");
                    newUser.addNewUser();
                    System.out.println("Usuario creado exitosamente.");
                    System.out.println("A continuación, tendrás que iniciar sesión.");
                    System.out.println("Regresando al Menú Principal...\n\n");
                    System.out.println("-----------------------------------------");
                    break;

                case "3":
                    System.out.println("-----------------------------------------");
                    System.out.println("Qué tenga un muy buen día!!\n" + "Ve a disfrutar de tus películas!!");
                    System.out.println("-----------------------------------------");
                    systemON = false;
                    break;

                default:
                    System.out.println("Oh no, has colocado una opción inválida...\n" + "Vuelve a intentarlo!\n");
                    break;
            }
        }
    }

    //Método para Recomendación de películas completo
    public static void getRecomendacionCompleta(Map<String, String[]> peliculasCompletas, String[] infoPeliculaUsuario){
        for(int i = 3; i == 0; i--){
            System.out.println("Películas que coinciden con " + i + " características de tu película seleccionada:");
            System.out.println("----------------------------------------");
            for(Map.Entry<String, String[]> entry : peliculasCompletas.entrySet()){
                //Contador de las coincidencias encontradas
                int contador = 0;

                String peliculaEstudiada =  entry.getKey();
                String[] infoPeliculaEstudiada = entry.getValue();

                //Comparación de las pelícuals existentes
                if(infoPeliculaUsuario[0].equals(infoPeliculaEstudiada[0])){
                    contador++;
                } if(infoPeliculaUsuario[1].equals(infoPeliculaEstudiada[1])){
                    contador++;
                } if(infoPeliculaUsuario[2].equals(infoPeliculaEstudiada[2])){
                    contador++;
                }

                //Comparador de condición si la cantidad de coincidencias es igual a "i"
                if(contador == i){
                    System.out.println("--------------------------------------");
                    System.out.println("Nombre Película: " + peliculaEstudiada);
                    System.out.println("Director: " + infoPeliculaEstudiada[0]);
                    System.out.println("Actor: " + infoPeliculaEstudiada[1]);
                    System.out.println("Genero: " + infoPeliculaEstudiada[2]);
                    System.out.println("--------------------------------------");
                }
            }
        }
    }

     //Método para recomendación de películas por un solo criterio
    public static void getRecomendacionCriterioUnico (Map<String, String[]> peliculasCompletas, String[] infoPeliculaUsuario, String tipoComparacion){
        int indexArray = -1;
        
        if(tipoComparacion.equals("Director")){
            indexArray = 0;
        } else if(tipoComparacion.equals("Actor")){
            indexArray = 1;
        } else if(tipoComparacion.equals("Genero")){
            indexArray = 2;
        }

        System.out.println("Recomendaciones por: " + tipoComparacion);
        System.out.println("--------------------------------------\n");

        //Se recorren las películas completas
        for(Map.Entry<String, String[]> entry : peliculasCompletas.entrySet()){
            String peliculaEstudiada =  entry.getKey();
            String[] infoPeliculaEstudiada = entry.getValue();
            if(infoPeliculaEstudiada[indexArray].equals(infoPeliculaUsuario[indexArray])){
                System.out.println("--------------------------------------");
                    System.out.println("Nombre Película: " + peliculaEstudiada);
                    System.out.println("Director: " + infoPeliculaEstudiada[0]);
                    System.out.println("Actor: " + infoPeliculaEstudiada[1]);
                    System.out.println("Genero: " + infoPeliculaEstudiada[2]);
                    System.out.println("--------------------------------------");
            }
        }
    }
}
