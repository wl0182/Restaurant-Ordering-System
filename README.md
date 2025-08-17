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
- **DevOps & Deployment:** Docker, Docker Compose, Jenkins (CI/CD)

## Getting Started

### Prerequisites

- Docker Desktop
- (Optional for local development) Java 17+, Maven, Node.js & npm

### Running with Docker Compose (Recommended)

1. **Clone the repository:**
    ```sh
    git clone https://github.com/wassim4592/RestaurantOrder.git
    cd RestaurantOrder
    ```
2. **Create a `.env` file in the project root** with the following variables:
    ```env
    POSTGRES_DB=your_db_name
    POSTGRES_USER=your_db_user
    POSTGRES_PASSWORD=your_db_password
    SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/your_db_name
    SPRING_DATASOURCE_USERNAME=your_db_user
    SPRING_DATASOURCE_PASSWORD=your_db_password
    JWT_SECRET=your_very_long_secure_jwt_secret
    ```
3. **Run the application:**
    ```sh
    docker-compose up
    ```
    This will pull the backend and frontend images from Docker Hub and start all services.

- Backend API: [http://localhost:8090](http://localhost:8090)
- Frontend: [http://localhost:3000](http://localhost:3000)
- Swagger UI: [http://localhost:8090/swagger-ui.html](http://localhost:8090/swagger-ui.html)

### Local Development (Optional)

You can still run backend and frontend locally for development:

#### Backend Setup

1. Build and run the backend:
    ```sh
    ./mvnw spring-boot:run
    ```
2. API docs available at: [http://localhost:8090/swagger-ui.html](http://localhost:8090/swagger-ui.html)

#### Frontend Setup

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
- `POST /api/auth/register` — User registration

### Menu Items

- `GET /api/menu-items` — List all menu items
- `GET /api/menu-items/{id}` — Get menu item by ID
- `POST /api/menu-items/add` — Add a new menu item
- `PUT /api/menu-items/{id}` — Update menu item availability
- `PUT /api/menu-items/{id}/price` — Update menu item price
- `PUT /api/menu-items/{id}/name` — Update menu item name
- `PUT /api/menu-items/{id}/category` — Update menu item category
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
- `GET /sessions/{id}/item-names` — Get all item names for a session
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
- `GET /orders/{id}/Items-status` — Check if all items in an order are served

### Statistics

- `GET /api/stats/total-revenue` — Get total revenue by date
- `GET /api/stats/total-revenue-by-menu-item` — Get total revenue by menu item
- `GET /api/stats/most-ordered-items` — Get most ordered items
- `GET /api/stats/average-session-revenue-by-date` — Get average session revenue by date

### API Documentation

- OpenAPI/Swagger UI: [http://localhost:8090/swagger-ui.html](http://localhost:8090/swagger-ui.html)

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

## Docker Images

- Backend: [`wassim4592/restaurant_backend:latest`](https://hub.docker.com/r/wassim4592/restaurant_backend)
- Frontend: [`wassim4592/restaurant_frontend:latest`](https://hub.docker.com/r/wassim4592/restaurant_frontend)

## CI/CD & Deployment

- You can automate builds and pushes to Docker Hub using Jenkins or GitHub Actions.
- For production, deploy using Docker Compose and provide a secure `.env` file.

---
