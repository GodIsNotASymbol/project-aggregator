FROM openjdk:17-jdk-slim
WORKDIR /project-aggregator
COPY target/project-aggregator-0.0.1-SNAPSHOT.jar app.jar
COPY application.properties application.properties
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.location=file:application.properties"]

