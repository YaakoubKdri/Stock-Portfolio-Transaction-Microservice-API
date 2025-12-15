# Stock Portfolio & Transaction Microservice

This project is a **Spring Boot–based microservice** responsible for managing
a user's **stock holdings** and **transaction history** (BUY / SELL operations).

It is designed as an **enterprise-style backend service**, aligned with
Java / Spring Boot job requirements (REST APIs, JPA, testing, Swagger).

---

## 🧰 Tech Stack

- **Java 21**
- **Spring Boot**
- **Spring Web (REST API)**
- **Spring Data JPA (Hibernate)**
- **H2 (in-memory DB for local dev)**
- **Springdoc OpenAPI (Swagger)**
- **JUnit 5 + Mockito**
- **Docker**

---

## 📘 API Documentation (Swagger / OpenAPI)

Swagger UI is enabled using springdoc-openapi.
- Swagger UI: http://localhost:8080/swagger-ui/index.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs 
---
## 📌 REST API Endpoints
- Execute a Transaction
```
POST /api/v1/portfolio/transaction
```
-Example request:
````bash
{
"userId": 1,
"stockSymbol": "Apple",
"type": "BUY",
"quantity": 10,
"price": 150.0
}
````
- Get User Holdings
````
GET /api/v1/portfolio/{userId}/holdings
````
- Get Transaction History
````
GET /api/v1/portfolio/{userId}/transactions
````
- Get Portfolio Metrics 
````
GET /api/v1/portfolio/{userId}/metrics
````