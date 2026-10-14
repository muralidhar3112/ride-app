# Ride-Hailing Backend System

A ride-hailing backend application built using **Java, Spring Boot, PostgreSQL, Redis, and Apache Kafka**. The application supports rider and driver workflows, geospatial driver discovery, ride lifecycle management, simulated payments, and event-driven notification processing.

## Features

- **Authentication and Authorization:** JWT-based authentication, BCrypt password hashing, and role-based access control.
- **User Management:** User registration and login APIs.
- **Driver Management:** Driver online/offline functionality, verification checks, and vehicle management.
- **Location Tracking:** Redis GEO for storing driver coordinates and searching for nearby drivers.
- **Ride Management:** Ride requests, driver assignment and acceptance, ride start and completion, and cancellation workflows.
- **Fare Calculation:** Base fare of ₹40 plus ₹12 per kilometre.
- **Payment Processing:** Simulated payments with PENDING, PAID, FAILED, and REFUNDED statuses.
- **Event-Driven Processing:** Apache Kafka producers and consumers for ride and payment events.
- **Notifications:** Backend notification persistence and event-driven notification processing.

## Technology Stack

| Technology | Purpose |
|---|---|
| Java 21 | Programming language |
| Spring Boot | Backend framework and REST APIs |
| Spring Security | Authentication and authorization |
| JWT | Token-based authentication |
| Spring Data JPA / Hibernate | ORM and database access |
| PostgreSQL | Persistent data storage |
| Redis | Geospatial driver location queries |
| Apache Kafka | Asynchronous event processing |
| Docker | Running supporting infrastructure |
| Maven | Build and dependency management |

## Architecture Overview

The application currently follows a **monolithic backend architecture**.

```text
             Rider / Driver
                   |
                   v
            REST API Requests
                   |
                   v
          Spring Boot Application
          /          |           \
         v           v            v
   PostgreSQL      Redis         Kafka
   Persistent      Driver       Producers
   Data            Locations        |
                                      v
                               Kafka Consumers
                                      |
                                      v
                              Notification Workflow
```

- **PostgreSQL** stores application data, including users, drivers, vehicles, rides, payments, and notifications.
- **Redis GEO** supports driver location storage and proximity-based searches.
- **Apache Kafka** enables asynchronous processing of ride and payment events.

## Ride Lifecycle

The application supports the following ride states:

```text
RIDE_REQUESTED
       |
       v
DRIVER_ASSIGNED
       |
       v
DRIVER_ACCEPTED
       |
       v
STARTED
       |
       v
COMPLETED
```

Ride cancellation is also supported, subject to the application's cancellation rules.

## Fare Calculation

The current simplified fare calculation is:

**Fare = Base Fare + (Distance × Rate per Kilometre)**

- Base fare: ₹40
- Rate: ₹12 per kilometre

For example, for a ride covering 10 km:

```text
Fare = 40 + (10 × 12)
     = ₹160
```

This is a simplified fare model and does not represent a production pricing engine.

## Prerequisites

Install the following before running the application:

- Java 21
- Maven, or use the included Maven Wrapper
- PostgreSQL
- Docker
- Redis
- Apache Kafka

## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/muralidhar3112/ride-app.git
cd ride-app
```

### 2. Configure PostgreSQL

Create a PostgreSQL database named `ride_app`.

Configure your database credentials through environment variables rather than committing passwords to GitHub.

### 3. Configure environment variables

Set the following environment variables in your terminal or IDE run configuration:

```text
DB_PASSWORD=your_database_password
JWT_SECRET=your_base64_encoded_jwt_secret
```

Use a strong, randomly generated JWT secret in Base64 format that satisfies the key-length requirements of your JWT implementation.

**Security:** Never commit real passwords, JWT secrets, access tokens, or other credentials to the repository.

### 4. Start Redis and Kafka

Start Redis and Kafka using your local Docker setup or existing installations.

The current application configuration expects:

```text
Redis:      localhost:6379
Kafka:      localhost:9092
PostgreSQL: localhost:5432
```

Ensure these services are running and accessible before starting the backend.

### 5. Run the application

On Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

Alternatively, open the project in IntelliJ IDEA, configure the environment variables in the run configuration, and run the Spring Boot application.

The application is configured to start on port `8080` by default.

## API Examples

The following examples illustrate the application's main workflows. Protected endpoints require a valid JWT where applicable.

### User Registration

```http
POST /api/users
Content-Type: application/json
```

Use the request fields defined by the application's user registration DTO or controller.

### User Login

```http
POST /api/users/login
Content-Type: application/json
```

Log in with valid credentials and use the returned JWT for subsequent authenticated requests.

### Update Driver Location

```http
PUT /api/drivers/location
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json
```

Example request body:

```json
{
  "latitude": 17.3855,
  "longitude": 78.4870
}
```

### Find Nearby Drivers

```http
GET /api/drivers/nearby?latitude=17.3855&longitude=78.4870
Authorization: Bearer <JWT_TOKEN>
```

The endpoint searches for nearby driver IDs using Redis GEO. Use the authentication role permitted by your current security configuration.

### Request a Ride

```http
POST /api/rides
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json
```

Example request body:

```json
{
  "pickupLatitude": 17.3855,
  "pickupLongitude": 78.4870,
  "destinationLatitude": 17.4500,
  "destinationLongitude": 78.3800
}
```

The API processes the ride request and returns a response based on the implemented ride workflow.

> **Note:** Request fields, response structures, and authorization requirements should be checked against the current controller and DTO implementations.

## Testing

The application has been tested locally for key workflows, including:

- User login and JWT-based requests.
- Ride request creation.
- Redis driver location storage and nearby-driver searches.
- Ride lifecycle transitions.
- Payment and Kafka event-processing workflows.

Testing and verification of all authorization rules, failure scenarios, and production-level reliability remain ongoing.

## Future Improvements

- Deploy the backend to a publicly accessible environment.
- Complete security verification and expand automated integration tests.
- Improve Kafka reliability with idempotent processing, dead-letter handling, and more comprehensive retry policies.
- Extract selected components into microservices.
- Introduce an API Gateway and explore distributed system design.
- Integrate a real payment gateway and external notification providers.

## Project Status

The current implementation is a locally developed Spring Boot monolith. Core backend workflows and supporting integrations have been implemented, with deployment, additional reliability improvements, and the separate microservices version planned as subsequent steps.

## Author

**Muralidhar Palli**

GitHub: [muralidhar3112](https://github.com/muralidhar3112)

---

*Built as a backend engineering project to explore REST API development, authentication, geospatial queries, relational persistence, and event-driven architecture.*