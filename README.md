# Peliflix: Sistema de Recomendación de Películas

Peliflix es un proyecto de software desarrollado como parte de un proyecto universitario. Este sistema de recomendación de películas utiliza una base de datos de películas y usuarios para ofrecer recomendaciones personalizadas.

## Descripción del Proyecto

El objetivo principal de Peliflix es proporcionar a los usuarios recomendaciones de películas personalizadas basadas en sus preferencias. El sistema recopila información sobre las películas que ha visto el usuario y utiliza estos datos para sugerir nuevas películas que podrían ser de su interés.

## Explicación del algoritmo
El algoritmo de recomendación de películas implementado en el sistema utiliza un enfoque de filtrado colaborativo basado en memoria para proporcionar recomendaciones personalizadas. Este enfoque se basa en comparar las características de las películas vistas por el usuario con un conjunto de películas disponibles en la base de datos. El algoritmo consta de dos métodos principales: getRecomendacionCompleta y getRecomendacionCriterioUnico.

El método getRecomendacionCompleta analiza cada película en la base de datos comparándola con un conjunto de características proporcionadas por el usuario (director, actor, género) en tres grupos diferentes. Clasifica las películas según el número de coincidencias (1, 2 o 3) y selecciona al azar hasta cinco películas de cada grupo de coincidencias para mostrar al usuario. Esto asegura que las recomendaciones sean variadas y relevantes, maximizando la probabilidad de que el usuario encuentre algo de su interés.

El método getRecomendacionCriterioUnico se enfoca en un solo tipo de característica (director, actor o género) y compara cada película del conjunto con las características del usuario en el índice correspondiente. Las coincidencias se almacenan en una lista y se seleccionan hasta cinco películas al azar para presentar al usuario, proporcionando recomendaciones basadas en un único criterio específico.

Este enfoque de filtrado colaborativo basado en memoria permite que las recomendaciones se generen de manera eficiente y personalizada, aprovechando la similitud entre las películas para ofrecer opciones relevantes al usuario. La inclusión de un componente aleatorio en la selección de películas ayuda a garantizar que las recomendaciones sean diversas, incluso cuando muchas películas cumplen con los criterios de coincidencia.


## Características Principales

- **Inicio de Sesión y Registro:** Los usuarios pueden iniciar sesión con sus nombres de usuario existentes o registrarse como nuevos usuarios.
- **Recomendaciones Personalizadas:** El sistema ofrece recomendaciones de películas personalizadas basadas en las preferencias del usuario y en las películas que ha visto anteriormente.
- **Exploración por Género, Director y Actor:** Los usuarios pueden explorar películas por género, director o actor principal.
- **Integración con API Externa:** Utiliza la API de OMDB para obtener información detallada sobre las películas.

## Tecnologías Utilizadas

- **Java:** El proyecto está desarrollado principalmente en Java.
- **Neo4j:** Se utiliza una base de datos Neo4j para almacenar información sobre películas y usuarios.
- **API OMDB:** Se integra con la API de OMDB para obtener información detallada sobre películas.
- **JSON:** Se utiliza JSON para el almacenamiento de datos de usuarios y películas.
- **CSV:** Se utiliza para almacenar temporalmente los datos de las películas antes de cargarlos en la base de datos.

## Instalación y Uso

Para utilizar Peliflix, sigue estos pasos:

1. Clona este repositorio en tu máquina local.
2. Abre el proyecto en tu entorno de desarrollo preferido (Eclipse, IntelliJ, etc.).
3. Asegúrate de tener Neo4j instalado y configurado en tu máquina local.
4. Configura las credenciales de la base de datos Neo4j en el código si es necesario.
5. Ejecuta la clase `Main` para iniciar el programa.
6. Sigue las instrucciones en la consola para iniciar sesión, registrar un nuevo usuario y recibir recomendaciones de películas.

## Contribuyentes

- Diego López
- Bryan Martínez
- Adriana Palacios
