## Project Agregator
Project Aggregator is a small private e-commerce/vlog webpage developed using the Java Spring Boot framework ecosystem.
The technologies used to develop the webpage include: Spring Security, Spring Boot MVC, the Pebble template engine, Mysql, Docker, 
Bootstrap, Maven.
### Installation
- First read DOCKER_README.txt to set up the mysql database
- Run `$PWD/mvnw install`
- Run `$PWD/mvnw package`
- Now under the target directory there will be a file called `project-aggregator-0.0.1-SNAPSHOT.jar`
- Run `java -jar $PWD/target/project-aggregator-0.0.1-SNAPSHOT.jar`
- The app will receive connections on "localhost:8080/"
