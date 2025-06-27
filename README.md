# Transaction Management App

## Tech Stack
- Java 21
- Spring Boot 3.2+
- Maven
- H2 Database (in-memory)
- Docker
- JUnit 5
- Spring Cache (for caching GET requests)

## Run Locally
```bash
mvn clean install
java -jar target/transaction-0.0.1-SNAPSHOT.jar
```

## Docker
```bash
docker build -t transaction-app .
docker run -p 8080:8080 transaction-app
```

## Endpoints
- POST /api/transactions
- PUT /api/transactions/{id}
- DELETE /api/transactions/{id}
- GET /api/transactions?page=0&size=10

## Stress Test
Use [Apache JMeter](https://jmeter.apache.org/) or `wrk` for HTTP benchmarking.


---

## Transaction Management API
A simple banking system to manage transactions using Java 21 and Spring Boot.

### Features
- Create, update, delete, list transactions
- Pagination support
- In-memory H2 database
- Caching with Spring Cache
- RESTful API
- Docker containerization
- Unit and stress testing

### Tech Stack
- Java 21
- Spring Boot 3.2+
- H2 (In-memory)
- Maven
- Docker
- JUnit 5
- Spring Cache
- Lombok

### Running the App
#### Locally:
```bash
mvn clean install
java -jar target/transaction-0.0.1-SNAPSHOT.jar
```

#### Using Docker:
```bash
docker build -t transaction-app .
docker run -p 8080:8080 transaction-app
```

#### Using Docker Compose:
```bash
docker-compose up --build
```

### Endpoints
- `POST /api/transactions`
- `PUT /api/transactions/{id}`
- `DELETE /api/transactions/{id}`
- `GET /api/transactions?page=0&size=10`

### Stress Testing
Use [Apache JMeter](https://jmeter.apache.org/) or [`wrk`](https://github.com/wg/wrk):
```bash
wrk -t4 -c100 -d30s http://localhost:8080/api/transactions
```

### Testing
```bash
mvn test
```

### Notes
- No user authentication is implemented.
- All data is lost on server restart due to in-memory DB.