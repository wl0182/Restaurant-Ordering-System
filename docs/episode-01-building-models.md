---
layout: default
title: "Episode 1: Building the Domain Model"
---
# Spring Boot Tutorial Series — Episode 1: Building the Domain Model

A practical, step-by-step tutorial to build the domain model. Each step presents the code followed by concise explanations of the annotations and concepts used.

---

## Step-by-step: Build entities and explain as they are created

### Step 0) Prerequisites (Maven deps and minimal config)
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
Why: JPA requires a datasource; validation auto-config wires Bean Validation; Lombok reduces boilerplate; Security integrates with UserDetails.

---

### Step 1) Create the model package
Create a package:
```
com.wassimlagnaoui.RestaurantOrder.model
```
Purpose: centralize entities and enums for discoverability and consistent imports.

---

### Step 2) MenuItem — a flat entity (start simple)
```java
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "menu_item")
public class MenuItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Double price;
    private String imageUrl;
    private String category;
    private boolean available;
}
```
Explanation:
- @Entity registers the class with JPA so it maps to a table.
- @Table(name = "menu_item") sets a consistent snake_case table name.
- @Id + @GeneratedValue(IDENTITY) uses database auto-increment semantics.
- Lombok @Data/@Builder/@NoArgsConstructor/@AllArgsConstructor removes boilerplate while keeping JPA’s no-args requirement.
- Simple fields demonstrate basic column mapping (String, Double, boolean).

---

### Step 3) Order — parent aggregate with relationships
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
Explanation:
- LocalDateTime maps to a timestamp column.
- status as String keeps schema simple; using @Enumerated(EnumType.STRING) with OrderStatus adds type safety.
- @OneToMany(mappedBy = "order") indicates a bidirectional association; mappedBy matches the child field name.
- cascade = CascadeType.ALL persists/removes child items with the parent.
- @ManyToOne + @JoinColumn(name = "table_session_id") defines the foreign key to TableSession.

---

### Step 4) OrderItem — owning side of the Order relationship
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
Explanation:
- Owning side resides here; JPA creates the order_id FK column on OrderItem.
- @Column(name = "served") customizes column name; Boolean default false is a Java-side default.
- @ManyToOne associations are LAZY by default.

Optional helper to keep both sides in sync:
```java
public class Order {
  // ...existing fields...
  public void addItem(OrderItem item) {
    items.add(item);
    item.setOrder(this);
  }
}
```

---

### Step 5) TableSession — grouping orders by table/time
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
Explanation:
- Represents a dining session for reporting and grouping.
- @OneToMany(mappedBy = "tableSession") mirrors the @ManyToOne in Order; no FK here.

---

### Step 6) OrderStatus — domain enum for lifecycle (optional but recommended)
```java
public enum OrderStatus { PLACED, PREPARING, READY, SERVED, CANCELLED }
```
Explanation:
- Use with @Enumerated(EnumType.STRING) to persist human-readable values and avoid ORDINAL pitfalls.

---

### Step 7) User — integrate with Spring Security
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
Explanation:
- @Email adds a Bean Validation rule on email.
- @Enumerated(EnumType.STRING) for role stores the enum name.
- getAuthorities returns role; assumes Role implements GrantedAuthority or is adapted elsewhere.
- UserDetails exposes identity and account flags used by Spring Security.

---

### Step 8) Staff — reference data for access checks and metadata
```java
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Staff {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Long employeeId;
    private String role; // e.g., waiter, chef, manager
}
```
Explanation:
- Simple entity without relations; useful for validating registrations and staff lists.

---

### Step 9) Naming, validation, and migrations (hygiene)
- Prefer EnumType.STRING for enums; avoid ORDINAL.
- Add @NotBlank/@Positive where business rules require it.
- Keep table/column names consistent (snake_case in DB).
- Use Flyway/Liquibase for schema and seed data in production instead of ddl-auto.

---

## Cheat sheet (annotations used — quick definitions)
- @Entity: Marks a class as a JPA entity managed by the persistence context and mapped to a database table.
- @Table: Customizes table metadata (name, schema, indexes, unique constraints). Defaults to the entity name if omitted.
- @Id: Declares the primary key field of the entity (mandatory for persistence).
- @GeneratedValue: Configures primary key generation strategy (IDENTITY, SEQUENCE, TABLE, AUTO). IDENTITY uses DB auto-increment.
- @OneToMany: Declares a one-to-many relationship; placed on the non-owning side with mappedBy referencing the owning field.
- @ManyToOne: Declares the many-to-one association; usually the owning side that holds the foreign key column.
- @JoinColumn: Specifies the foreign key column name and nullability on the owning side of a relationship.
- @Enumerated(EnumType.STRING): Persists enum values as their names (stable across enum reordering).
- @Column: Customizes column details (name, length, nullable, unique, precision/scale).
- @Email: Bean Validation constraint ensuring a syntactically valid email address.
- @NotBlank: Bean Validation constraint ensuring non-null, non-whitespace content for strings.
- @Positive: Bean Validation constraint ensuring numeric values are > 0.
- Lombok @Data: Generates getters, setters, equals, hashCode, toString.
- Lombok @Builder: Adds a fluent builder for constructing instances.
- Lombok @NoArgsConstructor / @AllArgsConstructor: Generate constructors; JPA requires a no-args constructor (at least protected).
- UserDetails: Spring Security contract exposing username, password, authorities, and account status flags.

---
copyright: © 2025 Wassim Lagnaoui
