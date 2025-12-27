# ----------- Build Stage -----------
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy pom.xml first for dependency caching
COPY pom.xml .

# Download dependencies only
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build the JAR
RUN mvn clean package -DskipTests

# ----------- Runtime Stage -----------
FROM eclipse-temurin:21-jre
WORKDIR /app

# Optional env vars
ENV SPRING_PROFILES_ACTIVE=prod
ENV SERVER_PORT=8080

# Copy the Spring Boot JAR (repackaged executable)
COPY --from=build /app/target/*-SNAPSHOT.jar app.jar

# Expose the Spring Boot port
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
