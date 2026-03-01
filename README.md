# EXC API

A Spring Boot REST API for managing product packages with multi-currency support.

## Tech Stack

- **Java 17**
- **Spring Boot 3.1.0**
- **Spring Data MongoDB**
- **MongoDB 6.0** (via Docker)
- **Maven** for build management

## Prerequisites

- Java 17+
- Docker & Docker Compose
- Maven (or use the included wrapper)

## Getting Started

### 1. Start with Docker (Recommended)

This starts the API, MongoDB, and Mongo Express (web UI):

```bash
docker compose up -d
```

Services will be available at:

- **API**: http://localhost:8080
- **Mongo Express** (DB viewer): http://localhost:8081

To rebuild after code changes:

```bash
docker compose down && docker compose build && docker compose up -d
```

### 2. Run Locally (Development)

Start MongoDB separately:

```bash
docker compose up -d mongo
```

Then run the Spring Boot app:

```bash
mvn spring-boot:run
```

Or using the wrapper:

```bash
./mvnw spring-boot:run
```

## Database

### MongoDB Configuration

- **Docker internal port**: 27017
- **Host-mapped port**: 27018 (access from host machine)
- **Database name**: `codingexercise`
- **Connection string** (app): `mongodb://mongo:27017/codingexercise`
- **Connection string** (local): `mongodb://localhost:27018/codingexercise`

### Seed Data

The `/mongo-init` folder contains seed scripts that run on first container start:

| File                  | Description                               |
| --------------------- | ----------------------------------------- |
| `01-seed-user.js`     | Creates demo users (`demo-user`, `tuser`) |
| `02-seed-products.js` | Creates sample products with USD prices   |
| `03-seed-packages.js` | Creates sample packages for `tuser`       |

**Default Users:**
| Username | Password |
|----------|----------|
| `demo-user` | `password123` |
| `tuser` | `tpass` |

To reset seed data:

```bash
docker compose down -v && docker compose up -d
```

## API Endpoints

### Authentication

| Method | Endpoint      | Description       |
| ------ | ------------- | ----------------- |
| POST   | `/auth/login` | Authenticate user |

**Request:**

```json
{
  "username": "demo-user",
  "password": "password123"
}
```

**Response:**

```json
{
  "authenticated": true,
  "token": "ZGVtby11c2VyOjE3MDk...",
  "username": "demo-user",
  "message": "Login successful",
  "locale": "en-US"
}
```

### Users

| Method | Endpoint                   | Description                   |
| ------ | -------------------------- | ----------------------------- |
| PUT    | `/users/{username}/locale` | Update user locale preference |

**Request:**

```json
{
  "locale": "en-GB"
}
```

### Products

| Method | Endpoint                                | Description                                |
| ------ | --------------------------------------- | ------------------------------------------ |
| GET    | `/products`                             | List all products (with locale conversion) |
| GET    | `/products?locale=en-GB`                | List products in GBP                       |
| POST   | `/products?name=X&price=Y&currency=USD` | Create a product                           |

**Supported Locales & Currencies:**
| Locale | Currency |
|--------|----------|
| `en-US` | USD |
| `en-GB` | GBP |
| `fr-FR` | EUR |
| `ja-JP` | JPY |

**Product Response:**

```json
{
  "id": "abc123",
  "name": "Starter Pack",
  "price": 9.99,
  "currency": "USD",
  "locale": "en-US"
}
```

> **Note:** Prices are stored internally as USD cents. When retrieved, they're converted to the requested locale's currency using live exchange rates from the Frankfurter API.

### Packages

| Method | Endpoint                            | Description                                |
| ------ | ----------------------------------- | ------------------------------------------ |
| GET    | `/packages`                         | List packages (optionally filter by owner) |
| GET    | `/packages?ownerUsername=demo-user` | List packages for a specific user          |
| GET    | `/packages/all`                     | List all packages from all users           |
| GET    | `/packages/{id}`                    | Get a specific package                     |
| POST   | `/packages`                         | Create a new package                       |
| PUT    | `/packages/{id}`                    | Update a package                           |
| DELETE | `/packages/{id}`                    | Delete a package                           |

**Create/Update Package Request:**

```json
{
  "name": "My Bundle",
  "description": "A collection of items",
  "productQuantities": {
    "product-id-1": 2,
    "product-id-2": 1
  },
  "ownerUsername": "demo-user"
}
```

## Project Structure

```
src/main/java/com/example/codingexercise/
├── CodingExerciseApplication.java    # Application entry point
├── config/                           # Configuration classes
│   └── GatewayConfig.java            # RestTemplate bean
├── controller/                       # REST controllers
│   ├── AuthController.java           # Login endpoint
│   ├── PackageController.java        # Package CRUD
│   ├── ProductController.java        # Product operations
│   ├── UserController.java           # User settings
│   └── dto/                          # Request/Response DTOs
├── exception/                        # Custom exceptions
├── gateway/                          # External API clients
│   └── ProductServiceGateway.java
├── model/                            # Domain entities
│   ├── Product.java
│   ├── ProductPackage.java
│   └── User.java
├── repository/                       # Data access layer
│   ├── PackageRepository.java
│   ├── ProductRepository.java
│   └── UserRepository.java
└── service/                          # Business logic
    └── CurrencyRateService.java      # Exchange rate conversion
```

## Testing

Run all tests:

```bash
mvn test
```

Run a specific test class:

```bash
mvn test -Dtest=PackageControllerTests
```

**Test Coverage:**

- `AuthControllerTests` - Login success/failure scenarios
- `PackageControllerTests` - Package CRUD operations
- `ProductControllerTests` - Product listing with locales
- `UserControllerTests` - Locale update
- `CurrencyRateServiceTests` - Currency conversion logic

## Currency Conversion

The API uses the [Frankfurter API](https://api.frankfurter.app/) for live exchange rates:

- Products are stored with `usdPrice` in cents (e.g., `$19.99` = `1999`)
- On retrieval, prices are converted to the requested locale's currency
- JPY prices have 0 decimal places; others have 2

## Environment Variables

| Variable                  | Default                                    | Description        |
| ------------------------- | ------------------------------------------ | ------------------ |
| `SPRING_DATA_MONGODB_URI` | `mongodb://localhost:27017/codingexercise` | MongoDB connection |

## Docker Services

| Service         | Port                            | Description      |
| --------------- | ------------------------------- | ---------------- |
| `app`           | 8080                            | Spring Boot API  |
| `mongo`         | 27018 (host) / 27017 (internal) | MongoDB database |
| `mongo-express` | 8081                            | MongoDB web UI   |

## Troubleshooting

**MongoDB connection issues:**

```bash
# Check if MongoDB is running
docker compose ps

# View MongoDB logs
docker compose logs mongo
```

**Reset everything:**

```bash
docker compose down -v
docker compose up -d
```

**Port conflicts:**

- API (8080): Change in `docker-compose.yml` under `app.ports`
- MongoDB (27018): Change in `docker-compose.yml` under `mongo.ports`
- Mongo Express (8081): Change in `docker-compose.yml` under `mongo-express.ports`
