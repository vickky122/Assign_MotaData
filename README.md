# 🧠 Centralized Logging System — Microservices Project (Java + Spring Boot)

This project demonstrates a **Centralized Logging System** built using **Spring Boot** (Java 17) where multiple microservices send their logs to a **Log Collector Service** running locally. The system helps monitor log data and application metrics effectively.

---

## 🚀 Project Overview

### 🧩 Components:
1. **Log Collector Service** (`log-collector`)  
   - Listens for log messages on a custom TCP port (default: `9000`)  
   - Saves logs to a local file  
   - Exposes actuator metrics for monitoring  

2. **Log Storage Service**  
   - Handles saving and reading logs from a specified file path  
   - Allows testing log persistence  

3. **Monitoring & Metrics**  
   - Provides health and metrics endpoints via Spring Boot Actuator  

---

## 🏗️ Tech Stack

| Layer | Technology |
|-------|-------------|
| Language | **Java 17** |
| Framework | **Spring Boot 3.x** |
| Dependencies | Spring Boot Actuator, Web, Lombok |
| Build Tool | Maven |
| Log Protocol | TCP (port: 9000) |
| Log Format | Plain text stored locally |
| Monitoring | Actuator Endpoints |

---



---

## 🧠 Project Structure

```bash
Directory structure:
└── vickky122-centralized_logging_system/
    ├── docker-compose.yml
    ├── logs.jsonl
    ├── log-collector/
    │   ├── Dockerfile
    │   ├── mvnw
    │   ├── mvnw.cmd
    │   ├── pom.xml
    │   ├── src/
    │   │   ├── main/
    │   │   │   ├── java/
    │   │   │   │   └── com/
    │   │   │   │       └── example/
    │   │   │   │           └── log_collector/
    │   │   │   │               ├── LogCollectorApplication.java
    │   │   │   │               ├── blacklist/
    │   │   │   │               │   └── BlacklistChecker.java
    │   │   │   │               ├── config/
    │   │   │   │               │   └── CollectorConfig.java
    │   │   │   │               ├── controller/
    │   │   │   │               │   └── AdminController.java
    │   │   │   │               ├── metrics/
    │   │   │   │               │   └── MetricsService.java
    │   │   │   │               ├── model/
    │   │   │   │               │   └── ParsedLog.java
    │   │   │   │               └── service/
    │   │   │   │                   ├── ForwardingService.java
    │   │   │   │                   ├── LogParserService.java
    │   │   │   │                   ├── LogParserServiceTest.java
    │   │   │   │                   └── TcpUdpListenerService.java
    │   │   │   └── resources/
    │   │   │       └── application.properties
    │   │   └── test/
    │   │       └── java/
    │   │           └── com/
    │   │               └── example/
    │   │                   └── log_collector/
    │   │                       └── LogCollectorApplicationTests.java
    │   └── .mvn/
    │       └── wrapper/
    │           └── maven-wrapper.properties
    ├── log_server/
    │   ├── Dockerfile
    │   ├── mvnw
    │   ├── mvnw.cmd
    │   ├── pom.xml
    │   ├── src/
    │   │   ├── main/
    │   │   │   ├── java/
    │   │   │   │   └── com/
    │   │   │   │       └── log_server/
    │   │   │   │           └── log_server/
    │   │   │   │               ├── LogServerApplication.java
    │   │   │   │               ├── config/
    │   │   │   │               │   └── MongoConfig.java
    │   │   │   │               ├── controller/
    │   │   │   │               │   ├── IngestController.java
    │   │   │   │               │   ├── MetricsController.java
    │   │   │   │               │   └── QueryController.java
    │   │   │   │               ├── model/
    │   │   │   │               │   └── LogEntity.java
    │   │   │   │               ├── repository/
    │   │   │   │               │   └── LogRepository.java
    │   │   │   │               └── service/
    │   │   │   │                   ├── LogStorageService.java
    │   │   │   │                   ├── LogStorageServiceTest.java
    │   │   │   │                   ├── MetricsService.java
    │   │   │   │                   └── QueryService.java
    │   │   │   └── resources/
    │   │   │       └── application.properties
    │   │   └── test/
    │   │       └── java/
    │   │           └── com/
    │   │               └── log_server/
    │   │                   └── log_server/
    │   │                       └── LogServerApplicationTests.java
    │   └── .mvn/
    │       └── wrapper/
    │           └── maven-wrapper.properties
    └── windows-client/
        ├── Dockerfile
        ├── mvnw
        ├── mvnw.cmd
        ├── pom.xml
        ├── src/
        │   ├── main/
        │   │   ├── java/
        │   │   │   └── com/
        │   │   │       └── example/
        │   │   │           └── windows_client/
        │   │   │               ├── WindowsClientApplication.java
        │   │   │               ├── config/
        │   │   │               │   └── ClientConfig.java
        │   │   │               ├── controller/
        │   │   │               │   └── MetricsController.java
        │   │   │               ├── model/
        │   │   │               │   └── LogMessage.java
        │   │   │               ├── service/
        │   │   │               │   ├── LogGeneratorService.java
        │   │   │               │   ├── MetricsService.java
        │   │   │               │   └── TcpUdpSenderService.java
        │   │   │               └── util/
        │   │   │                   └── LogFormatter.java
        │   │   └── resources/
        │   │       └── application.properties
        │   └── test/
        │       └── java/
        │           └── com/
        │               └── example/
        │                   └── windows_client/
        │                       └── WindowsClientApplicationTests.java
        └── .mvn/
            └── wrapper/
                └── maven-wrapper.properties

```

---

## ▶️ How to Run the Project Locally

### Clone the Repository
```bash
git clone https://github.com/vickky122/Centralized_Logging_System.git
```

### Build the Project
```bash
mvn clean install
```

### Run the Application
```bash
mvn spring-boot:run
```

The **Log Collector Service** will start on port `9000`.

---

## 🔗 API Endpoints & Verification Steps

| Endpoint | Method | Description | Expected Response |
|-----------|--------|-------------|------------------|
| `http://localhost:8082/actuator/health` | GET | Check if service is healthy | `{ "status": "UP" }` |
| `http://localhost:8082/actuator/metrics` | GET | List all available metrics | JSON list of metrics |
| `http://localhost:8082/actuator/metrics/jvm.memory.used` | GET | Shows JVM memory usage | Metric data in JSON |
| `http://localhost:8082/actuator/logfile` | GET | Retrieve current log file | Log content in plain text |
| `tcp://localhost:9000` | POST (via Socket) | Send logs manually using socket client | Logs appended to file |

---

## 🧪 Example Log Sending (Test)

### 🧱 Using Netcat:
```bash
echo "INFO: Log message from service A" | nc localhost 9000
```

### 🧱 Using Telnet:
```bash
telnet localhost 9000
```
Then type:
```bash
INFO: This is a sample log from Microservice A
```
Press Enter to send the log.

---

## 📜 Example Log Output
Logs are stored in a local file, typically under:
```bash
/logs/log-collector.log
```

Sample content:
```bash
2025-10-27 12:32:21 INFO  Received: Log message from service A
2025-10-27 12:32:23 WARN  Received: High memory usage detected
2025-10-27 12:32:25 ERROR Received: NullPointerException in ServiceB
```

---

## 🧩 Screenshots Placeholder

🖼️ **1️⃣ Application Running** — Screenshot showing application running successfully on localhost. 
<img width="1908" height="1064" alt="image" src="https://github.com/user-attachments/assets/1e46fd6e-6ae4-4d21-b668-b121268df4c0" />

🖼️ **2️⃣ Actuator Health Check** — Screenshot of `/actuator/health` returning `{ "status": "UP" }`.  
<img width="1390" height="880" alt="image" src="https://github.com/user-attachments/assets/f1d5d713-4696-4415-89e7-bdc6de75e381" />

🖼️ **3️⃣ Metrics Endpoint** — Screenshot of `/actuator/metrics` JSON response.  
<img width="1368" height="820" alt="image" src="https://github.com/user-attachments/assets/a7031118-3ba6-4eb1-b982-67786da03533" />

🖼️ **4️⃣ JVM Memory Metrics** — Screenshot of `/actuator/metrics/jvm.memory.used`.  
<img width="1381" height="885" alt="image" src="https://github.com/user-attachments/assets/531e3f3b-2840-40f5-b636-c1eb44a82664" />

🖼️ ** Other endpoints used in the project screenshot
<img width="1382" height="768" alt="image" src="https://github.com/user-attachments/assets/6be93b14-8dd2-46ea-88f2-4ad43451cd78" />
<img width="1367" height="676" alt="image" src="https://github.com/user-attachments/assets/70a66ff6-8818-4538-80f0-99f1e156d876" />
<img width="1382" height="846" alt="image" src="https://github.com/user-attachments/assets/decf6378-4863-40cb-b643-380c205ebd21" />
<img width="732" height="315" alt="image" src="https://github.com/user-attachments/assets/93a6151a-d209-49d8-9f4c-44478bc59d0b" />
<img width="606" height="305" alt="image" src="https://github.com/user-attachments/assets/92bd9daf-7ef7-4ffb-8feb-c109efb2ed97" />




---

## ✅ Verification Checklist

| Step | Task | Status |
|------|------|--------|
| 1 | application.properties configured | ☑️ |
| 2 | Actuator enabled & endpoints accessible | ☑️ |
| 3 | Logs received on port 9000 | ☑️ |
| 4 | Log file created and updated | ☑️ |
| 5 | Metrics and health check verified | ☑️ |

---


## 📚 Expected Response Summary

✅ **Health Check**
```json
{
  "status": "UP"
}
```

✅ **Metrics List (Partial Example)**
```json
{
  "names": [
    "jvm.memory.used",
    "jvm.gc.pause",
    "system.cpu.usage",
    "process.uptime"
  ]
}
```

✅ **JVM Memory Metric**
```json
{
  "name": "jvm.memory.used",
  "measurements": [
    { "statistic": "VALUE", "value": 21817264.0 }
  ]
}
```

✅ **Log File Endpoint**
```
INFO: Log message from service A
WARN: High memory usage detected
ERROR: NullPointerException in ServiceB
```

---

## ✅ Completion Checklist

| Requirement | Status |
|--------------|---------|
| Microservice Communication | ✅ |
| Event-Driven / Concurrent Architecture | ✅ |
| Resource-Managed Concurrency | ✅ |
| Health & Metrics Endpoints | ✅ |
| Log Collection, Parsing, Forwarding | ✅ |
| Log Querying APIs | ✅ |
| Unit Tests | ✅ |
| Dockerization Ready | ✅ |
| Documentation (README + Screenshots) | ✅ |

---

## 🏁 Conclusion
🎯 I have now successfully completed all the required parts of the **Centralized Logging System** project.  
The project:

- Collects logs via TCP  
- Stores logs locally  
- Exposes health and metrics endpoints for monitoring  
- Works locally without Docker  

All requirements are fulfilled and verifiable using the above endpoints and screenshots.

---

## 🧑‍💻 Author
**Vikrant Kumar Yadav**  
📅 Completed: **October 2025**

---

# 🎯 Assignment Objective

Design and implement a centralized logging system using Java or Golang microservices, containerized with Docker.

---

## 📋 The System Should

- Simulate multiple client microservices that generate logs.  
- Send logs to a log collector microservice.  
- Forward and store logs in a central logging microservice.  
- Provide a user-facing API to query the logs.

This assignment assesses understanding of:
- **Microservice communication**
- **Event-driven and concurrent programming (Java or Golang)**
- **Resource-managed concurrency**
- **API design**
- **Docker orchestration**

---

## 🏗️ Architecture Overview

### 1️⃣ Client Microservices
- Simulate system-level logs for Linux or Windows environments.
- Each runs independently and generates structured logs every 1–2 seconds.
- Sends logs to the Log Collector via **TCP/UDP** in JSON format.

**Linux Log Categories (choose any two):**
- Syslog (kernel, cron, auth)
- Login Audit (success/failure)
- Logout Audit (session terminations)

**Windows Log Categories (choose any two):**
- Login Audit (success/failure)
- Logout Audit
- Event Logs (application/system events)
- File Audit (file read/write/delete access)
- User Audit (user creation, privilege changes)

**Example Log (Linux):**
```json
{"message": "<86> aiops9242 sudo: pam_unix(sudo:session): session opened for user root(uid=0) by motadata(uid=1000)"}
```

**Example Log (Windows):**
```json
{"message": "<134> WIN-EQ5V3RA5F7H Microsoft-Windows-Security-Auditing: A user account was successfully logged on. Account Name: Motadata"}
```

---

### 2️⃣ Log Collector Microservice (`log-collector`)
**Responsibilities:**
- Accept incoming logs over TCP/UDP.  
- Validate and parse: Extract fields like timestamp, event.category, event.source.type, username, hostname, severity.  
- Enrich logs by checking against a blacklist.  
- Forward parsed logs to **Central Logging Service** via `POST /ingest`.

**Example Forwarded Payload:**
```json
{
  "timestamp": "2025-07-29T12:35:24Z",
  "event.category": "login.audit",
  "username": "root",
  "hostname": "aiops9242",
  "severity": "INFO",
  "raw.message": "<86> aiops9242 sudo: pam_unix(sudo:session): session opened for user root(uid=0)",
  "is.blacklisted": false
}
```

---

### 3️⃣ Central Logging Microservice (`log-server`)
**Responsibilities:**
- Ingest logs via `POST /ingest`
- Store logs in file-based or NoSQL storage  
- Provide `GET /logs` with filters and options:

**Supported Filters:**
- `service`, `level`, `username`, `is.blacklisted`

**Supported Options:**
- `limit`, `sort`

**Example Queries:**
```
GET /logs?service=windows_logout
GET /logs?level=error
GET /logs?username=root&is.blacklisted=true
```

---

### 4️⃣ Metrics Endpoint
Each service exposes `/metrics` showing:
- Total logs received  
- Logs grouped by category and severity  

---

### 🐳 Dockerization 
- Dockerize each microservice (client, log-collector, log-server)  
- Use `docker-compose.yml` for orchestration  
- Enable inter-service communication  

---

### ⚙️ Concurrency & Event-Driven Programming
The system must use **event-driven/concurrent** architecture to process logs in parallel without blocking.

- Java: Use **Spring WebFlux**, **Executors**, or **Reactive Streams**.  
- Resource-managed concurrency with bounded thread pools.  
- Implement **backpressure**, **queue limits**, and **graceful shutdown**.

---

## 📦 Submission Requirements

- Public GitHub repository  
- Include:
  - Source code with tests  
  - Event-driven or concurrent processing  
  - Dockerfile(s)  
  - `docker-compose.yml`  
  - README with setup and API usage  

---

## 🧮 Evaluation Criteria

| Criteria | Weight |
|-----------|---------|
| Event-driven / Concurrent Architecture | 30% |
| Code Readability & Maintainability | 20% |
| Fulfillment of Requirements & Tests | 20% |
| API Design & Modularity | 15% |
| Docker Orchestration | 15% |

---

✅ **All assignment expectations have been completed and verified successfully.**
