# ----------- Build Stage -----------
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy pom.xml first for dependency caching
COPY pom.xml .

# Download dependencies only
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build fat JAR with exact name
RUN mvn clean package -DskipTests -DfinalName=fintrack

# ----------- Runtime Stage -----------
FROM eclipse-temurin:21-jre
WORKDIR /app

# Set environment variables (optional defaults)
ENV SPRING_PROFILES_ACTIVE=prod
ENV SERVER_PORT=8080

# Copy built JAR from build stage
COPY --from=build /app/target/fintrack.jar app.jar

# Expose the Spring Boot port
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
