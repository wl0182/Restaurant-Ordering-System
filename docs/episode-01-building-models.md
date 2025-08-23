---
layout: default
title: "Tutorial 1: Building the Domain Model"

---
# Spring Boot Tutorial Series — Tutorial 1: Building the Domain Model

A practical, step-by-step tutorial to build the domain model. Each step shows a small code snippet and a short, English explanation.

---

## Step-by-step: Build entities and explain as they are created

### 0. Prerequisites
Required dependencies include:
- spring-boot-starter-data-jpa
- spring-boot-starter-validation
- spring-boot-starter-security (User implements UserDetails)
- lombok
- JDBC driver (e.g., postgresql)

Dev config (application.yml), example:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/restaurant
    username: postgres
    password: postgres
  jpa:
    hibernate:
      ddl-auto: update  # dev only; prefer Flyway/Liquibase in production
    show-sql: true
```
#### Explanation:
- `datasource`: tells Spring Boot how to connect to your database (URL, `username`, `password`). JPA uses this connection to read and write entities.
- `jpa.hibernate.ddl-auto=update`: during development, Hibernate updates the schema to match your entities at startup. In production, switch to managed migrations (Flyway/Liquibase) so every change is versioned and reviewed.
- `show-sql=true`: logs the SQL Hibernate runs. Great for learning and debugging, but turn it off in noisy environments.
- Validation starter: activates Bean Validation so annotations like `@NotBlank` and `@Email` are enforced automatically in controllers and on persistence.
- Security starter: required because our `User` implements `UserDetails` and integrates with Spring Security.

---

### 1. Create the model package
Create a package:
```text
com.wassimlagnaoui.RestaurantOrder.model
```
Purpose:
Keep all entities and enums together so component scanning and repository wiring stay predictable. Grouping the domain model under one package also makes it easy to apply consistent conventions and to find related code (entities, enums, and later their DTOs and repositories in neighboring packages).

---

### 2. MenuItem — a flat entity
```java
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "menu_item")
public class MenuItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name; // name shown in the menu

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @Column(name = "category", length = 50)
    private String category;

    @Column(name = "available", nullable = false)
    private boolean available;
}
```
#### Explanation:
- Purpose of the class: `MenuItem` models one dish or drink on the menu with its display details and price. It’s a standalone entity without child relationships.
- `id`: primary key of the table; `@Id` marks the identifier; `@GeneratedValue(IDENTITY)` lets the DB auto-increment; `@Column(name="id", nullable=false, updatable=false)` documents the column mapping and prevents accidental updates.
- `name`: human-friendly name shown in the menu; `@Column(name="name", nullable=false, length=100)` enforces presence and caps length to keep UI/DB consistent.
- `description`: short text describing the item; `@Column(name="description", length=500)` allows generous text while keeping storage bounded.
- `price`: numeric cost; `@Column(name="price", nullable=false)` ensures a value is always provided. In production prefer `BigDecimal` with `precision/scale` for currency.
- `imageUrl`: optional link used by the UI; `@Column(name="image_url", length=255)` maps camelCase to snake_case and bounds URL length.
- `category`: grouping like “Starters”, “Mains”; `@Column(name="category", length=50)` constrains values and simplifies indexing.
- `available`: stock/visibility toggle; `@Column(name="available", nullable=false)` guarantees a definite boolean in the DB.
- Class-level: `@Entity` + `@Table(name="menu_item")` map the class; Lombok `@Data`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor` generate boilerplate.

---

### 3. Order — parent aggregate with relationships
```java
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orders")
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double total;
    private LocalDateTime orderDate;

    // Option A: store as String; Option B: use enum with @Enumerated
    private String status;
    // @Enumerated(EnumType.STRING)
    // private OrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;

    @ManyToOne
    @JoinColumn(name = "table_session_id")
    private TableSession tableSession;
}
```
#### Explanation:
- Purpose of the class: `Order` represents a customer’s order and acts as the parent aggregate for its `OrderItem` lines. It connects to a `TableSession` so you know which visit it belongs to.
- `id`: primary key for the order; `@Id` designates the identifier; `@GeneratedValue(strategy = GenerationType.IDENTITY)` relies on the database to assign it automatically.
- `total`: monetary sum of the items; compute it from `items` to avoid drift, or store it for faster reads and keep it consistent in a service layer.
- `orderDate`: timestamp set when the order is created; use it for reporting and sorting; no special annotation needed unless you customize column details via `@Column`.
- `status`: lifecycle state as text for simplicity; for safer code switch to an enum and add `@Enumerated(EnumType.STRING)` so the DB stores readable names rather than ordinals.
- `items`: lines belonging to this order; `@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)` means the foreign key lives on `OrderItem` and cascading lets you persist/delete the whole graph in one operation.
- `tableSession`: which visit/table this order belongs to; `@ManyToOne` establishes the relationship and `@JoinColumn(name = "table_session_id")` controls the foreign key column name and nullability.
- Class-level: `@Entity`, `@Table(name = "orders")` map persistence; Lombok reduces boilerplate with `@Data`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor`.

---

### 4. OrderItem — owning side of the Order relationship
```java
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantity;

    @Column(name = "served")
    private Boolean served = false;

    @ManyToOne
    private MenuItem menuItem;

    @ManyToOne
    private Order order; // owning side: holds the FK used by mappedBy in Order
}
```
#### Explanation:
- Purpose of the class: `OrderItem` captures one line of an order: which `MenuItem` was chosen, how many, and whether it has been served.
- `id`: primary key for the line item; `@Id` marks it as the identifier; `@GeneratedValue(strategy = GenerationType.IDENTITY)` lets the database assign it.
- `quantity`: how many units were ordered; add `@Positive` so it’s strictly greater than zero and consider a max limit for sanity.
- `served`: workflow flag that starts `false` and flips to `true` when delivered; `@Column(name = "served")` shows how to customize the column name (useful when matching an existing schema).
- `menuItem`: which product this line refers to; `@ManyToOne` creates the foreign key (by default column `menu_item_id`), customize with `@JoinColumn` if needed.
- `order`: reference to the parent `Order`; `@ManyToOne` makes this the owning side that holds the foreign key used by `mappedBy` in `Order`.
- Class-level: `@Entity` enables persistence; Lombok annotations generate boilerplate.

---

### 5. TableSession — grouping orders by table/time
```java
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TableSession {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime sessionStart;
    private LocalDateTime sessionEnd;
    private String tableNumber;

    @OneToMany(mappedBy = "tableSession")
    private List<Order> orders;
}
```
#### Explanation:
- Purpose of the class: `TableSession` models a single seating at a table so multiple `Order`s can be tied to one visit and closed together.
- `id`: primary key of the session; `@Id` identifies it; `@GeneratedValue(strategy = GenerationType.IDENTITY)` delegates ID creation to the database.
- `sessionStart`: when the guests were seated; set at open; consider indexing if you filter sessions by time often.
- `sessionEnd`: when the session is closed; remains `null` while active; can be used to compute duration and turnover.
- `tableNumber`: human-friendly identifier like "7" or "A3"; add an index or uniqueness if your business rules require it.
- `orders`: all orders placed during this session; `@OneToMany(mappedBy = "tableSession")` indicates the FK is on `Order` and this side is read-only for the association key.
- Class-level: `@Entity` for persistence and Lombok for boilerplate (`@Data`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor`).

---

### 6. OrderStatus — domain enum for lifecycle
```java
public enum OrderStatus { PLACED, PREPARING, READY, SERVED, CANCELLED }
```
#### Explanation:
- Purpose of the enum: defines the allowed lifecycle states for an order, making the domain explicit and preventing invalid values.
- Values and persistence:
  - States: `PLACED` → `PREPARING` → `READY` → `SERVED` (or `CANCELLED`).
  - Persist with `@Enumerated(EnumType.STRING)` in your entity to store readable names that won’t break if constants are reordered.

---

### 7. User — integrate with Spring Security
```java
@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements UserDetails {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Email(message = "Valid Email Required")
    private String email;

    private String password;
    private String phone;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return List.of(role); }
    @Override public String getUsername() { return this.email; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
```
#### Explanation:
- Purpose of the class: `User` represents an application account and integrates with Spring Security by implementing `UserDetails` so it can be authenticated/authorized.
- `id`: primary key of the user; `@Id` marks it; `@GeneratedValue(strategy = GenerationType.IDENTITY)` lets the DB assign it.
- `name`: person’s display name; `@Column(nullable = false)` ensures the column can’t be `NULL` at the database level.
- `email`: login identifier and contact; `@Column(nullable = false)` enforces presence, and `@Email` validates the format; also add a unique index in the database to prevent duplicates.
- `password`: hashed credential (e.g., `BCrypt`); never store plain text; compare via a configured `PasswordEncoder`.
- `phone`: optional contact number; add formatting/validation as needed for your locale.
- `role`: business role for the account; `@Enumerated(EnumType.STRING)` stores the role’s name for readability and stability.
- Class-level and interfaces: `@Entity`, `@Table(name = "users")` control persistence mapping; Lombok reduces boilerplate; implementing `UserDetails` requires exposing authorities and account status flags used by Spring Security.

---

### 8. Staff — reference data for access checks and metadata
```java
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Staff {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true, length = 120)
    private String email;

    @Column(name = "employee_id", nullable = false, unique = true)
    private Long employeeId;

    @Column(name = "role", nullable = false, length = 40)
    private String role; // e.g., waiter, chef, manager
}
```
#### Explanation:
- Purpose of the class: `Staff` lists valid employees; useful for validating registrations, linking orders to staff, and reporting.
- `id`: primary key for the staff row; `@Id` marks it; `@GeneratedValue(IDENTITY)` uses DB auto-increment; `@Column(name="id", nullable=false, updatable=false)` documents the mapping and prevents updates.
- `firstName`: given name for display/search; `@Column(name="first_name", nullable=false, length=50)` enforces presence and caps length.
- `lastName`: family name; `@Column(name="last_name", nullable=false, length=50)` mirrors constraints for consistency.
- `email`: contact and potential login key; `@Column(name="email", nullable=false, unique=true, length=120)` prevents duplicates and controls size.
- `employeeId`: organization identifier; `@Column(name="employee_id", nullable=false, unique=true)` guarantees one-to-one mapping with an employee.
- `role`: operational role (waiter/chef/manager); `@Column(name="role", nullable=false, length=40)` constrains allowed text length for indexes/UI.
- Class-level: `@Entity` for persistence and Lombok for boilerplate.

---

### 9. Key Takeaways: Naming, validation, and migrations
Tips:
- Enums: prefer `EnumType.STRING` so names are stored, not fragile ordinal numbers.
- Validation: add `@NotBlank`, `@Positive`, and `@Email` to fail fast on bad input, both at the API layer and before persistence.
- Naming: keep table/column names consistent (snake_case in DB). It simplifies queries and migrations.
- Migrations: in production, manage schema with Flyway/Liquibase instead of `ddl-auto` so every change is traceable.
- Defaults: when a field needs a default, set it in your migration scripts and mirror it in code for clarity.

---

## Annotation Summary 

- **@Entity**: Marks a class as a JPA entity managed by the persistence context and mapped to a database table.
- **@Table**: Customizes table metadata (name, schema, indexes, unique constraints). Defaults to the entity name if omitted.
- **@Id**: Declares the primary key field of the entity (mandatory for persistence).
- **@GeneratedValue**: Configures primary key generation strategy (IDENTITY, SEQUENCE, TABLE, AUTO). IDENTITY uses DB auto-increment.
- **@OneToMany**: Declares a one-to-many relationship; placed on the non-owning side with mappedBy referencing the owning field.
- **@ManyToOne**: Declares the many-to-one association; usually the owning side that holds the foreign key column.
- **@JoinColumn**: Specifies the foreign key column name and nullability on the owning side of a relationship.
- **@Enumerated(EnumType.STRING)**: Persists enum values as their names (stable across enum reordering).
- **@Column**: Customizes column details (name, length, nullable, unique, precision/scale).
- **@Email**: Bean Validation constraint ensuring a syntactically valid email address.
- **@NotBlank**: Bean Validation constraint ensuring non-null, non-whitespace content for strings.
- **@Positive**: Bean Validation constraint ensuring numeric values are > 0.
- **Lombok @Data**: Generates getters, setters, equals, hashCode, toString.
- **Lombok @Builder**: Adds a fluent builder for constructing instances.
- **Lombok @NoArgsConstructor / @AllArgsConstructor**: Generate constructors; JPA requires a no-args constructor (at least protected).
- **UserDetails**: Spring Security contract exposing username, password, authorities, and account status flags.
