# Ebike Rebalancer System

A backend automation platform built with **Spring Boot and Java** for optimizing the allocation, distribution, and tracking of electric bicycles across a network of stations.

The system combines relational entity management with operational analytics, fleet redistribution logic, and an AI-powered support interface using the **Groq API**.

---

## Overview

The **Ebike Rebalancer System** is designed to help manage an electric bike-sharing network by providing APIs and services for:

* Managing electric and manual bikes
* Managing stations and vehicles
* Tracking users and authentication
* Monitoring fleet distribution
* Analyzing station and vehicle operations
* Executing bike rebalancing operations
* Maintaining rebalancing logs
* Providing AI-assisted support through Groq

The application follows a layered architecture that separates API handling, business logic, and data persistence.

---

## Tech Stack

| Technology      | Purpose                         |
| --------------- | ------------------------------- |
| Java            | Core programming language       |
| Spring Boot     | Backend framework               |
| Spring Data JPA | Data access and persistence     |
| Hibernate       | ORM implementation              |
| H2 Database     | In-memory relational database   |
| Maven           | Build and dependency management |
| Groq API        | AI-powered chat/support         |
| REST API        | Client-server communication     |

---

## Architecture

The project follows a clean layered architecture:

```text
Client
  |
  v
Controllers
  |
  v
Services
  |
  v
Repositories
  |
  v
JPA / Hibernate
  |
  v
H2 Database
```

### Controllers

Controllers expose REST endpoints and handle incoming API requests.

The application includes controllers for areas such as:

* Authentication
* Analytics
* Stations
* Vehicles
* Rebalancing
* AI/Groq support

### Services

Services contain the application's business logic, including:

* User operations
* Fleet management
* File handling
* Station operations
* Rebalancing algorithms
* Operational analytics
* AI support integration

### Repositories

Repositories provide the persistence layer using **Spring Data JPA**, allowing the application to perform standard CRUD operations without requiring boilerplate database-access code.

### Models

The domain model includes entities such as:

* `EBike`
* `ManualBike`
* `Station`
* `User`
* `Vehicle`
* `RebalanceLog`

---

## Configuration

The application uses `src/main/resources/application.properties` for its local development configuration.

Example configuration:

```properties
# Application Info
spring.application.name=ebike-system
server.port=8080

# Database Settings
spring.datasource.url=jdbc:h2:mem:ebikedb;DB_CLOSE_DELAY=-1
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=root
spring.datasource.password=fascinated4
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

# JPA & Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Groq / AI Configuration
groq.api.key=${GROQ_API_KEY:gsk_mock_api_key_for_testing}

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
spring.h2.console.settings.web-allow-others=true
```

### Groq API Key

For AI functionality, provide your Groq API key through the `GROQ_API_KEY` environment variable.

Linux/macOS:

```bash
export GROQ_API_KEY=your_groq_api_key
```

Windows PowerShell:

```powershell
$env:GROQ_API_KEY="your_groq_api_key"
```

The application falls back to a mock key when `GROQ_API_KEY` is not configured. AI functionality that requires a valid API key will not work with the mock value.

---

## Prerequisites

Before running the project, make sure the following are installed:

* **Java 17 or later**
* **Maven 3.8+**
* **Git**

Verify your installations:

```bash
java -version
mvn -version
git --version
```

---

## Getting Started

### 1. Clone the Repository

```bash
git clone <repository-url>
cd Ebike-Rebalancer-System/springboot-backend
```

Replace `<repository-url>` with the actual GitHub repository URL.

### 2. Configure Environment Variables

Set the Groq API key if you want to use the AI functionality:

```bash
export GROQ_API_KEY=your_groq_api_key
```

For Windows PowerShell:

```powershell
$env:GROQ_API_KEY="your_groq_api_key"
```

### 3. Build the Application

Run:

```bash
mvn clean install
```

This will compile the project, run the available tests, and package the application.

### 4. Run the Application

Start the Spring Boot application using:

```bash
mvn spring-boot:run
```

The backend will start on:

```text
http://localhost:8080
```

---

## H2 Database Console

The project uses an **H2 in-memory database** for local development.

The H2 console is available at:

```text
http://localhost:8080/h2-console
```

Use the following connection details:

```text
JDBC URL: jdbc:h2:mem:ebikedb
User Name: root
Password: fascinated4
```

> Note: Because the database is configured as an in-memory database, its data is lost when the application stops.

---

## API

The backend exposes REST APIs for managing the application's main resources.

The API is organized around areas such as:

```text
/auth
/analytics
/stations
/vehicles
/rebalancing
```

The exact endpoints and request/response formats depend on the controller implementations in the project.

The backend runs on:

```text
http://localhost:8080
```

---

## Core Functionality

### Fleet Management

The system supports management of different types of bikes and fleet-related entities, including:

* Electric bikes
* Manual bikes
* Vehicles used for transportation
* Stations
* Users

### Station Management

Stations represent locations where bikes can be distributed, stored, picked up, or returned.

The system can use station-level information to determine where bikes are needed and where excess bikes are available.

### Rebalancing

The rebalancing functionality is responsible for redistributing bikes across stations based on operational requirements.

A typical rebalancing workflow can be represented as:

```text
Station Data
     |
     v
Analyze Bike Distribution
     |
     v
Identify Imbalanced Stations
     |
     v
Calculate Redistribution
     |
     v
Assign Vehicle / Route
     |
     v
Execute Rebalancing
     |
     v
Create Rebalance Log
```

### Analytics

The analytics layer provides operational information that can be used to understand:

* Bike distribution
* Station demand
* Fleet utilization
* Rebalancing requirements
* Vehicle operations

### AI Support

The system integrates with **Groq** to provide an AI-powered support interface.

The AI integration can be used as an additional interface for interacting with operational information and assisting users with system-related queries.

---

## Project Structure

A simplified representation of the backend structure is:

```text
springboot-backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── ...
│   │   │       ├── controller/
│   │   │       ├── service/
│   │   │       ├── repository/
│   │   │       └── model/
│   │   │
│   │   └── resources/
│   │       └── application.properties
│   │
│   └── test/
│       └── ...
│
├── pom.xml
└── README.md
```

The exact package structure may vary depending on the current implementation.

---

## Development

To run the application during development:

```bash
mvn spring-boot:run
```

To create a production-style packaged JAR:

```bash
mvn clean package
```

The generated JAR will typically be available under:

```text
target/
```

It can then be started with:

```bash
java -jar target/<application-name>.jar
```

---

## Database Notes

The application currently uses H2 for local development:

```text
Database: H2
Mode: In-memory
Database Name: ebikedb
```

For a production deployment, the database configuration should be replaced with a persistent database such as PostgreSQL or MySQL.

Sensitive configuration values such as database passwords and API keys should also be provided through environment variables or a secure configuration system rather than committed to source control.

---

## Future Improvements

Potential improvements to the system include:

* Persistent PostgreSQL/MySQL database support
* Docker and Docker Compose support
* API documentation with OpenAPI/Swagger
* Authentication using JWT or OAuth2
* Improved route optimization algorithms
* Real-time station availability updates
* WebSocket-based operational monitoring
* Automated scheduled rebalancing
* Advanced fleet-demand forecasting
* Comprehensive unit and integration test coverage
* Production-ready monitoring and logging

---

## Author

Developed as a backend system for automated electric-bike fleet management, station analytics, and intelligent rebalancing.
