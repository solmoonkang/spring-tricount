# Dockerfile

FROM openjdk:17-alpine

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} app.jar

COPY ./data/tricount /data/tricount

ENTRYPOINT ["java", "-Dspring.profiles.active=local", "-jar", "app.jar"]
