# RestaurantOrder

A full-stack restaurant order management system built with **Spring Boot** (Java) for the backend and **React** (Vite) for the frontend. It allows restaurant staff to manage tables, orders, and kitchen workflows efficiently.

## Features

- User authentication and session management (JWT)
- Table and order management
- Kitchen dashboard for order tracking
- Responsive UI with Tailwind CSS
- RESTful API with OpenAPI documentation

## Tech Stack

- **Backend:** Java 17, Spring Boot, Spring Security, Spring Data JPA, JWT, PostgreSQL
- **Frontend:** React, Vite, Tailwind CSS
- **API Docs:** OpenAPI (Swagger via springdoc-openapi)
- **Build Tools:** Maven, npm

## Getting Started

### Prerequisites

- Java 17+
- Maven
- Node.js & npm

### Backend Setup

1. Navigate to the project root:
    ```sh
    cd RestaurantOrder
    ```
2. Build and run the backend:
    ```sh
    ./mvnw spring-boot:run
    ```
3. API docs available at: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

### Frontend Setup

1. Go to the frontend directory:
    ```sh
    cd frontend
    ```
2. Install dependencies:
    ```sh
    npm install
    ```
3. Start the development server:
    ```sh
    npm run dev
    ```

The frontend will be available at [http://localhost:5173](http://localhost:5173) by default.

## Database

- **Production:** PostgreSQL (default)
- **Development/Testing:** H2 in-memory (optional, for local/dev use)

## API Overview

The backend exposes a RESTful API. Here are some key endpoints:

### Authentication

- `POST /api/auth/login` — User login (returns JWT token)

### Menu Items

- `GET /api/menu-items` — List all menu items
- `GET /api/menu-items/{id}` — Get menu item by ID
- `POST /api/menu-items/add` — Add a new menu item
- `PUT /api/menu-items/{id}` — Update menu item availability
- `GET /api/menu-items/category/{category}` — Get menu items by category
- `GET /api/menu-items/available` — List available menu items

### Table Sessions

- `POST /sessions/start` — Start a new table session
- `PUT /sessions/{tableNumber}/end` — End a table session
- `GET /sessions/{id}` — Get session by ID
- `GET /sessions/active` — List all active sessions
- `GET /sessions/active/{tableNumber}` — Get active session for a table
- `GET /sessions/tables` — List all tables
- `GET /sessions/{id}/item-summary` — Get item summary for a session
- `GET /sessions/{id}/checkout-summary` — Get checkout summary for a session

### Orders

- `POST /orders` — Place a new order
- `GET /orders/{id}` — Get order by ID
- `POST /orders/{id}/serve` — Mark an order as served
- `POST /orders/orderItem/{id}/serve` — Mark an order item as served
- `GET /orders/sessions/{id}` — Get orders by session
- `GET /orders/sessions/{id}/unserved` — Get unserved order items for a session
- `GET /orders/sessions/{id}/served` — Get served order items for a session
- `GET /orders/kitchen/queue` — Get kitchen order queue

### API Documentation

- OpenAPI/Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Backend Dependencies

Key dependencies from `pom.xml`:

- `spring-boot-starter-web` — REST API
- `spring-boot-starter-data-jpa` — Database access
- `spring-boot-starter-security` — Security and authentication
- `spring-boot-starter-validation` — Input validation
- `springdoc-openapi-starter-webmvc-ui` — API documentation (Swagger UI)
- `io.jsonwebtoken` (jjwt) — JWT authentication
- `org.postgresql:postgresql` — PostgreSQL driver (runtime)
- `com.h2database:h2` — H2 in-memory database (runtime)
- `lombok` — Boilerplate code reduction (optional)
- `spring-boot-starter-test` — Testing

## Project Structure

- `src/main/java/com/wassimlagnaoui/RestaurantOrder/` — Spring Boot backend
- `frontend/` — React frontend

## Customization

- Update backend configs in `src/main/resources/application.yml`
- Modify frontend styles in `frontend/src/pages/*.css` or use Tailwind utility classes


# 🐳 Running with Docker

This project is fully containerized using Docker. You can run the backend, frontend, and PostgreSQL database with a single command.

### Prerequisites

Docker installed

Docker Compose installed

Build & Run
# Clone the repository
git clone https://github.com/wl0182/Restaurant-Ordering-System.git
cd Restaurant-Ordering-System

# Build and start containers
docker-compose up --build


#### This will start:

Backend (Spring Boot) → available at http://localhost:8080

Frontend (React + Vite) → available at http://localhost:5173

PostgreSQL Database → running on port 5432

#### Stopping Containers
docker-compose down

### Notes

The backend connects automatically to the PostgreSQL container.

You can update DB credentials in docker-compose.yml if needed by setting your environment variables. 

For persistence, a Docker volume is configured for PostgreSQL data.

