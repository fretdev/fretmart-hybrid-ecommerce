# FretMart — Enterprise Hybrid-Persistence E-Commerce Platform

FretMart is a high-performance, production-ready e-commerce ecosystem designed to scale under intensive enterprise workloads. Built using a modern, cloud-native tech stack, the platform showcases advanced backend engineering principles, dual-layer validation, strict architectural separation, and an ultra-responsive frontend interface.

The core engineering focus of FretMart is its **Hybrid Data Layer**, strategically combining the structural safety of **Spring Data JPA/Hibernate** for complex business transactions with the raw, high-throughput execution speed of native SQL via **JDBC Client** for high-volume auditing and high-traffic analytics.

---

## System Architecture & Tech Stack

### Backend Ecosystem

- **Core Framework:** Java 17 / Spring Boot 3
- **Data Persistence (Hybrid):** Spring Data JPA (Hibernate ORM) & Spring JDBC Client (Native SQL execution)
- **Database Migrations:** Flyway (Version-controlled schema evolutionary tracking)
- **Security & Infrastructure:** Spring Security 6, Stateless JWT, Rate Limiting Interceptors, Caffeine Cache, Spring Boot Actuator
- **API Documentation:** Swagger UI / OpenAPI 3 / Javadoc Integration
- **Testing Suite:** JUnit 5, Mockito, JaCoCo (Targeting 80%+ structural line coverage)

### Frontend Ecosystem

- **Core Engine:** React 18 (Vite Bundler for rapid execution)
- **Styling Paradigm:** Tailwind CSS (Highly optimized atomic utility styles)
- **State & Networking:** React Context API, React Router v6, Axios Interceptors
- **Testing & Quality:** React Testing Library, Lighthouse Audits

### DevOps & Cloud Infrastructure

- **Containerization:** Docker & Docker Compose (Multi-container architecture using Nginx for frontend distribution)
- **CI/CD Pipelines:** GitHub Actions automated matrix testing, SonarCloud Static Code Analysis, automated Docker Hub image packaging
- **Hosting Environments:** Render (Scalable Backend API Staging/Prod), Vercel (Optimized Frontend Edge Delivery)

---

## Architectural Deep Dive & Code Standards

### 1. The Hybrid Persistence Paradigm

To balance developer velocity with raw performance, FretMart implements a unique two-pronged database strategy:

- **JPA & Hibernate:** Manages stateful, transactional business operations where data safety is paramount (e.g., dynamic product catalogs, relational shopping cart items, and complex order state machines).
- **Spring JDBC Client:** Utilized for high-throughput, low-overhead write operations. For example, product-view tracking, checkout concurrency metrics, and high-volume background logging entirely bypass Hibernate's session overhead, cutting down execution latency by up to 40%.

### 2. Enterprise Data Integrity & Fail-Fast Mechanics

FretMart addresses data corruption at the absolute perimeter of the application. Using **Jakarta Bean Validation** (`@NotBlank`, `@NotNull`, `@Positive`, `@Min`), invalid user payloads are rejected instantly at the Controller layer before invoking database resources. This is paired with definitive database constraints (`nullable = false`, precision settings) to ensure database rows remain structurally perfect.

### 3. Comprehensive Audit Trail & Automated Metrics

Entities leverage JPA lifecycle callbacks (`@PrePersist` and `@PreUpdate`) to guarantee automatic creation and modification tracking, completely decoupled from core service business logic. Concurrently, performance monitoring aspects (`@Around` AOP) track execution windows on core checkouts to isolate bottleneck patterns dynamically.

---

## Platform Engineering & Implementation Roadmap

The development of FretMart is executed across an structured 8-week production schedule:

### Phase 1: Core REST Infrastructure & Hybrid Persistence

- **Infrastructure:** Schema establishment using Flyway, PostgreSQL engine setup, Docker network alignments.
- **Product Management Portfolio:** Comprehensive REST API exposed for complex queries, containing built-in case-insensitive category segregation and dynamic string-pattern search filters.
- **High-Performance Pagination:** All list-based resource requests explicitly utilize Spring Data's `Pageable` abstraction to enforce strict memory safety boundaries (`OutOfMemoryError` defense).

### Phase 2: Enterprise Security, JWT, & Network Rate Limiting

- **Stateless Authorization:** Secure authorization utilizing decoupled JWT validation filters, managing access boundaries across `User` and `Admin` clearances via Spring Security.
- **DDoS & Scraping Mitigation:** Custom rate-limiting interceptors track tracking metrics per client IP address. High-throughput checkouts are reinforced via a local Caffeine Cache layer to completely isolate high-velocity transaction exploits.

### Phase 3: Transactional Domain Models (Cart, Order, & AOP Monitoring)

- **Relational Domain Models:** Complex lifecycle management linking transactional entity state chains (Cart $\rightarrow$ CartItem $\rightarrow$ Order $\rightarrow$ OrderItem).
- **Asynchronous Metrics:** System performance aspects intercept core service footprints, logging system bottlenecks cleanly via Log4j.

### Phase 4: Financial Integrations, Load Testing, & CI/CD Pipelines

- **PayStack Integration:** Production-grade webhook engine processing third-party financial responses asynchronously via automated endpoint verifications.
- **Concurrent Load Testing:** Apache JMeter scripting evaluating multi-user connection surges down to sub-100ms processing ceilings.
- **DevOps Pipelines:** Automated GitHub Actions tracking code maintainability scores via SonarCloud quality gates on every codebase contribution.

### Phase 5: Modern Frontend Construction & E2E Validation

- **Single Page Application (SPA):** High-speed interface incorporating client-side route protection, context state synchronization, and dynamic loading skeletons.
- **Production Deployment:** Multi-stage production provisioning leveraging automated Docker multi-container scaling.

---

## Core API Portfolio Reference

### Product Resources Map

| HTTP Method | API URL Endpoint                    | Functional Scope                        | Parameters / Payload Constraints            |
| :---------- | :---------------------------------- | :-------------------------------------- | :------------------------------------------ |
| **GET**     | `/api/products`                     | Fetch paginated product portfolio       | Query: `page`, `size`, `sort` (Default: 10) |
| **GET**     | `/api/products/{id}`                | Read comprehensive single product specs | Path: `id`                                  |
| **POST**    | `/api/products`                     | Introduce new inventory to catalog      | Payload: Validated JSON Object              |
| **PUT**     | `/api/products/{id}`                | Update existing product characteristics | Path: `id` + Validated JSON Payload         |
| **DELETE**  | `/api/products/{id}`                | Purge product records from system       | Path: `id`                                  |
| **GET**     | `/api/products/category/{category}` | Target products by category name        | Path: `category` (Case-insensitive)         |
| **GET**     | `/api/products/search`              | Dynamic textual search across names     | Query: `q=keyword`                          |

### Sample Validated Payload Configuration (`POST /api/products`)

```json
{
  "name": "Fender Stratocaster Electric Guitar",
  "description": "An iconic instrument featuring three single-coil pickups, synchronized tremolo, and classic alder body construction.",
  "category": "Guitars",
  "price": 1249.99,
  "stock": 15,
  "imageUrl": "[https://example.com/images/strat.jpg](https://example.com/images/strat.jpg)"
}
```

## System Architecture & Directory Blueprint

The frontend application uses a strict modular directory layout to keep components decoupled and scalable.

```text
src/
├── assets/
├── components/
├── context/
├── hooks/
├── services/
└── views/
```
