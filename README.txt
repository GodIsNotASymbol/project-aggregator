---Objetivo---
Hacer una aplicacion que implemente diferentes conceptos y tecnologias de java en el mismo ecosistema, 
preferiblemente construyendo desde las bases de java.
Acercarse lo mayormente posible a https://github.com/FoxtrotPotato/JavaRetooling/tree/main/Chicken%20Test

-- Instalacion --
Realizar la instalacion del docker mysql siguiendo las instrucciones del repositorio


---Arquitectura/Tecnologias---
Programar en orden de aparicion...

---------------------Ejecutable aparte--------------------------
- Ejecutable al que se le pasa una string, y la guarda en la base de datos (Mysql)
- Mysql tiene que estar dockerizada
- Tiene que tener un readme que explique como usar el ejecutable. O tenga un "usage"
- Tiene que tener en el readme como hacer la installation
TODO:
- Hacer que el docker de mysql sea publicado y que se pueda correr directamente con docker run. [NO HACE FALTA]
DONE:
-----------------------------------------------------------------

Aplicacion web:
- Una aplicacion web, Spring Boot MVC, que sea un RSS feed aggregator. TODO agregar otras capabilidades
- Maven para manejar las dependencias
- GIT para manejar el versionado
- Mysql para guardar los datos (fijarse si se puede usar docker)
- Tiene que tener en el readme como hacer la installation
- Desde el spring initializr pude elegir Spring Boot MVC que usa apache Tomcat como server

-----TODO(Pagina aggregator):
- Hacer Modelo entidad relacion, revisar como hacer para guardar fotos en una columna de una tabla
- Hacer la pagina principal, sin paginado
- Hacer paginado para la pagina principal
- Definir cantidad de items por pagina
- Hacer modal cuando se aprieta el item, sin difuminacion gris
- Hacer la difuminacion gris con el modal, y que se pueda cerrar
- Implementar Login, autenticacion y autorizacion
- Implementar pagina del edit, solo se puede acceder con Rol admin
- Chequear que funcione localmente, Explorar los limites de algunas cosas
- Chequear que funcione localmente y a ver que le parece a ma
- Como hago para publicar y abrir a la web la IP y el puerto? Puedo hacerlo con un google cloud gratis??? O amazon gratis???
-----DONE(Pagina aggregator):
- Arregladas las dependencias y maven instalo las JPA annotations como para hacer el modelo entidad relacion
- Hacer el css como para que main page este por debajo y al costado del header y de la sidebar. En cada pagina que voy a usar el template, tambien voy a usar el css
- Hacer que el template sea inheritable
- Entender como funciona Thymeleaf e inyectar algunas variables desde el controller
- Hacer un template con header y barra al costado.
- Poner todo en un mismo controller
- Correr un controller de test, Hello World
- Bajar Spring Boot y configurar la ide con JetBrains IDEA



-----------------------------------------------------------------
-----TODO (Web Dev):
https://github.com/FoxtrotPotato/JavaRetooling/tree/main/Chicken%20Test
- Buscar cursos en udemy sobre Java, pagarlos si es necesario para las certificaciones
- Hacer google codelabs https://codelabs.developers.google.com/?text=java
- Udemy: Java, java spring boot
- Me sirve esto?: https://java-programming.mooc.fi/
- 
-----DONE:
