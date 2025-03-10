FROM maven:3.9.9-amazoncorretto

WORKDIR /app

COPY . .

RUN mvn clean package

FROM openjdk:24-slim-bullseye

WORKDIR /app

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} /app/akkorHotel.jar

EXPOSE 8080


ENTRYPOINT ["java", "-jar", "/app/akkordBack.jar"]