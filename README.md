# Stock Market Simulator

A highly available REST API simulating a stock market.

## Key Features & Tech Stack
* **Java 21 & Spring Boot 3** (REST, Data JPA, Validation)
* **PostgreSQL & H2** (Production vs. Test environments)
* **Docker & Nginx** (Containerization & Round-Robin Load Balancing)
* **Concurrency Safe:** Uses Database Pessimistic Locking to prevent race conditions during trades.
* **High Availability:** Multi-instance backend with a `/chaos` endpoint to demonstrate failover and auto-healing.
* **Interactive UI:** Lightweight Vanilla JS & Bootstrap dashboard included.

## Architecture

As a system handling virtual assets (stocks), maintaining data integrity during concurrent requests is critical. The architecture ensures that no stock is lost or duplicated, and the system remains available even if a backend instance crashes.

```mermaid
graph TD
    Client([Client / UI]) -->|HTTP POST /wallets/...| Nginx[Nginx Load Balancer]
    Nginx -->|Round Robin| App1[Spring Boot - Instance 1]
    Nginx -->|Failover / Round Robin| App2[Spring Boot - Instance 2]
    
    subgraph Data Layer
        App1 -->|@Transactional + Pessimistic Lock| DB[(PostgreSQL)]
        App2 -->|@Transactional + Pessimistic Lock| DB
    end
```

### Why like that?

* Concurrency Control: Both instances share the same database. By applying `@Lock(LockModeType.PESSIMISTIC_WRITE)`, the database handles row-level locking. If Instance 1 and Instance 2 try to buy the exact same stock simultaneously, the database forces sequential execution, preventing Race Conditions and ensuring strict ACID compliance.


* Fault Tolerance: If App-1 fails, Nginx immediately routes all subsequent traffic to App-2. Docker's restart policies simultaneously boot up a replacement for App-1, providing seamless High Availability.


## How to Run

*Requirement: Docker and Docker Compose installed.*

**Linux / macOS:**
```bash
chmod +x start.sh
./start.sh 8080
```

**Windows:**

```DOS
./start.bat 8080
```

The argument is the port number, you may change it if you wish.

Access the Dashboard & API: http://localhost:8080

*How to Stop*
To gracefully stop the application and release the port (works on both Bash and DOS):

```
docker-compose down
```
## Core API Endpoints
* `GET /stocks` - Get bank state

* `POST /stocks` - Set bank state

* `GET /wallets/{id}` - Check wallet

* `POST /wallets/{id}/stocks/{stock}` - Execute trade ({"type": "buy"} or "sell")

* `GET /log` - View audit log

* `POST /chaos` - Kill backend instance (Test HA)


## Author

* [Patryk Blacha](https://github.com/PatrykBlacha)