# Multi-stage Dockerfile for building and running the Spring Boot app

# Build stage: use Maven to build the project
FROM maven:3.8.8-openjdk-17 AS build
WORKDIR /workspace/app

# Copy only what we need for an efficient build
COPY pom.xml ./
COPY src ./src

# Run a package build (skip tests for faster iterations; change as needed)
RUN mvn -B -DskipTests package

# Run stage: use a smaller JRE image
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the built jar from the build stage. Adjust the jar name if your artifactId/version differs.
COPY --from=build /workspace/app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
