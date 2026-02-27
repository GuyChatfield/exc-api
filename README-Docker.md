# Docker + Mongo development setup

This project includes a simple Docker setup for development. It provides a local MongoDB instance (and mongo-express UI) and a Docker build for the Spring Boot application.

Files added:

- `Dockerfile` — multi-stage Dockerfile that uses Maven to build the project and a small JRE image to run the produced JAR.
- `docker-compose.yml` — brings up `mongo`, `mongo-express` (UI at :8081) and the `app` service (your Spring Boot app on :8080).
- `.dockerignore` — excludes local build artifacts and IDE files from the Docker build context.

Quick start (requires Docker and docker-compose):

1. Build and start Mongo only (fast):

```bash
docker-compose up -d mongo
```

2. Build the app image and start everything (first run will build the app image with Maven):

```bash
docker-compose up --build
```

3. Open the app at: http://localhost:8080
   Open mongo-express UI at: http://localhost:8081

Notes:

- The compose file sets `SPRING_DATA_MONGODB_URI=mongodb://mongo:27017/codingexercise` for the `app` service. If you later add Spring Data MongoDB to the project, Spring Boot will automatically pick this up.
- If you don't have Docker available and only want the Mongo instance for local dev, you can run the official Mongo image manually:

```bash
docker run -d --name mongo -p 27017:27017 -v mongo_data:/data/db mongo:6.0
```

- Building the app image runs `mvn -DskipTests package` inside the builder image. If you want to run tests during image build, remove `-DskipTests` from the `Dockerfile` build stage.
