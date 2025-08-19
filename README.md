# RestaurantOrder Management System

A comprehensive full-stack restaurant management application designed for **servers**, **kitchen staff**, and **administrators** to streamline restaurant operations. Built with **Spring Boot** (Java) for the backend and **React** (Vite) for the frontend.

## Application Overview

This restaurant management system digitizes and optimizes the entire restaurant workflow from table management to kitchen operations and administrative oversight. The application serves three primary user roles:

### 🍽️ **For Servers**
- **Table Management**: Start and manage table sessions, assign customers to tables
- **Order Taking**: Create and modify customer orders with real-time menu access
- **Order Tracking**: Monitor order status and manage customer requests
- **Session Management**: Handle table checkouts and session summaries

### 👨‍🍳 **For Kitchen Staff**
- **Kitchen Queue Dashboard**: Real-time view of all pending orders
- **Order Preparation Tracking**: Mark items as prepared and ready for serving
- **Priority Management**: Organize orders by preparation time and table requirements
- **Status Updates**: Communicate order completion to serving staff

### 👨‍💼 **For Administrators**
- **Menu Management**: Add, edit, remove menu items and manage pricing
- **Staff Management**: Create and manage staff accounts with role-based access
- **Analytics & Statistics**: View revenue, popular items, and operational insights
- **Session Oversight**: Monitor all table sessions and transaction history
- **System Configuration**: Manage restaurant settings and operational parameters

## Key Features

### 🔐 **Authentication & Security**
- JWT-based authentication with role-based access control
- Secure user registration tied to employee ID verification
- Protected admin routes and sensitive operations

### 📊 **Real-time Operations**
- Live kitchen queue updates
- Real-time table session management
- Dynamic order status tracking
- Instant menu updates across all interfaces

### 📱 **Responsive Design**
- Mobile-friendly interface for on-the-go restaurant operations
- Optimized for tablets and handheld devices used by servers
- Intuitive dashboard layouts for different user roles

### 📈 **Business Intelligence**
- Revenue analytics and reporting
- Popular menu item tracking
- Table turnover statistics
- Staff performance insights

## Tech Stack

- **Backend:** Java 17, Spring Boot, Spring Security, Spring Data JPA, JWT, PostgreSQL
- **Frontend:** React, Vite, Tailwind CSS
- **API Documentation:** OpenAPI (Swagger via springdoc-openapi)
- **Build Tools:** Maven, npm
- **Testing:** JUnit 5, Mockito (Unit Testing)
- **DevOps & Deployment:** Docker, Docker Compose, Jenkins (CI/CD)
- **Database:** PostgreSQL with Docker volumes for data persistence

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
    JWT_SECRET=your_very_long_secure_jwt_secret_256_bits_or_more
    ```
3. **Run the application:**
    ```sh
    docker-compose up
    ```
    This will pull the backend and frontend images from Docker Hub and start all services.

### Application URLs

- **Frontend Application:** [http://localhost:3000](http://localhost:3000)
- **Backend API:** [http://localhost:8080](http://localhost:8080)
- **API Documentation:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## User Access & Testing

### 🔑 **Admin Access Required**

**Important:** To test the application, you need admin credentials since user registration requires a valid staff Employee ID from the database.

**Default Admin Login:**
- **Email:** `admin@example.com`
- **Password:** `Password`

### 👥 **User Registration Process**

1. **Staff Verification:** New users can only register if their Employee ID exists in the Staff database
2. **Role Assignment:** All new registrations receive `ROLE_USER` by default
3. **Admin Privileges:** Only `ROLE_ADMIN` users can access administrative features

### 🎭 **Role-Based Features**

#### Regular Staff (`ROLE_USER`)
- ✅ Kitchen Dashboard - View and manage cooking queue
- ✅ Tables Dashboard - Manage table sessions and orders
- ✅ Order Management - Create and track customer orders
- ❌ Admin Dashboard - Restricted access

#### Administrators (`ROLE_ADMIN`)
- ✅ All staff features listed above
- ✅ Admin Dashboard - Complete system oversight
- ✅ Menu Management - Add/edit/remove menu items
- ✅ Staff Management - Create and manage employee accounts
- ✅ Analytics & Statistics - Revenue and operational insights
- ✅ Session Summaries - Complete transaction history

### 🧪 **Setting Up Test Data**

1. Login with admin credentials
2. Navigate to **Staff Management** in the Admin Dashboard
3. Add staff members with unique Employee IDs
4. New users can register using these Employee IDs
5. Test different user roles and permissions

## API Endpoints Overview

The application provides a comprehensive RESTful API documented with OpenAPI. Here are the available endpoints organized by functionality:

### 🔐 **Authentication** (`/api/auth`)
- `POST /api/auth/login` - User authentication with email/password
- `POST /api/auth/register` - New user registration (requires valid Employee ID)

### 🍽️ **Menu Management** (`/api/menu-items`)
- `GET /api/menu-items` - Get all menu items
- `GET /api/menu-items/{id}` - Get specific menu item by ID
- `GET /api/menu-items/available` - Get only available menu items
- `GET /api/menu-items/category/{category}` - Get menu items by category
- `POST /api/menu-items/add` - Create new menu item (Admin only)
- `PUT /api/menu-items/{id}` - Toggle menu item availability (Admin only)
- `PUT /api/menu-items/{id}/name` - Update menu item name (Admin only)
- `PUT /api/menu-items/{id}/price` - Update menu item price (Admin only)
- `PUT /api/menu-items/{id}/category` - Update menu item category (Admin only)

### 🪑 **Table & Session Management** (`/sessions`)
- `GET /sessions/tables` - Get all restaurant tables
- `GET /sessions/active` - Get all active table sessions
- `GET /sessions/active/{tableNumber}` - Get active session for specific table
- `GET /sessions/{id}` - Get session details by ID
- `GET /sessions/{id}/item-summary` - Get order item summary for session
- `GET /sessions/{id}/item-names` - Get all item names for session
- `GET /sessions/{id}/checkout-summary` - Get complete checkout summary
- `GET /sessions/{date}/sessions-by-date` - Get sessions by specific date
- `POST /sessions/start` - Start new table session
- `PUT /sessions/{tableNumber}/end` - End table session and generate receipt

### 📋 **Order Management** (`/orders`)
- `POST /orders` - Create new customer order
- `GET /orders/{id}` - Get order details by ID
- `GET /orders/{id}/Items-status` - Check if all items in order are served
- `GET /orders/sessions/{id}` - Get all orders for a session
- `GET /orders/sessions/{id}/served` - Get served items for session
- `GET /orders/sessions/{id}/unserved` - Get unserved items for session
- `GET /orders/kitchen/queue` - Get kitchen preparation queue
- `POST /orders/{id}/serve` - Mark entire order as served
- `POST /orders/orderItem/{id}/serve` - Mark individual order item as served

### 👥 **Staff Management** (`/api/staff`) - Admin Only
- `GET /api/staff/all` - Get all staff members
- `POST /api/staff/add` - Add new staff member
- Staff management endpoints for updating and removing staff

### 📊 **Analytics & Statistics** (`/api/stats`) - Admin Only
- `GET /api/stats/total-revenue` - Daily revenue statistics
- `GET /api/stats/total-revenue-by-menu-item` - Revenue breakdown by menu items
- `GET /api/stats/most-ordered-items` - Most popular menu items with order counts
- `GET /api/stats/average-session-revenue-by-date` - Average revenue per session by date

### 🔧 **Additional Features**
- **Real-time Kitchen Queue**: Live updates for kitchen staff
- **Session Tracking**: Complete table session lifecycle management
- **Order Status Tracking**: Individual item and order status management
- **Revenue Analytics**: Comprehensive business intelligence
- **Role-based Access Control**: Different endpoints accessible based on user roles

### 📱 **Frontend Integration**
The React frontend seamlessly integrates with all these endpoints through the ApiService class, providing:
- **Dashboard Views**: Kitchen, Tables, Admin dashboards
- **Real-time Updates**: Live order status and queue management
- **Form Handling**: Menu item creation, staff management
- **Data Visualization**: Statistics and analytics display
- **Responsive Design**: Mobile-optimized for restaurant operations

## Database Schema

The application uses PostgreSQL with the following main entities:
- **Users** - Authentication and role management
- **Staff** - Employee information and ID verification
- **MenuItems** - Restaurant menu with pricing and availability
- **Tables** - Physical restaurant tables
- **TableSessions** - Customer seating sessions
- **Orders** - Customer orders linked to table sessions
- **OrderItems** - Individual items within orders

## Development & Deployment

### 🧪 **Testing**
- Comprehensive unit tests for all service layers
- Integration tests for API endpoints
- Run tests: `./mvnw test`

### 🐳 **Docker Deployment**
- Backend and frontend containerized separately
- PostgreSQL database with persistent volumes
- Environment-based configuration
- Production-ready Docker Compose setup

### 🚀 **CI/CD Pipeline**
- Jenkins integration for automated builds
- Docker Hub image publishing
- Automated testing and deployment workflows

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

**Built with ❤️ for modern restaurant operations**
