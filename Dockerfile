# Этап сборки (Builder)
FROM maven:3.8.7-eclipse-temurin-17 AS builder

WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

# Этап выполнения (Runtime)
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app
COPY --from=builder /app/target/app.jar .

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]