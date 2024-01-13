FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

COPY pom.xml .
COPY src src

RUN mvn clean package

FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app
COPY --from=builder /app/target/lobby-backend-0.0.1-SNAPSHOT.jar /app/lobby-backend.jar

CMD ["java", "-jar", "lobby-backend.jar"]
