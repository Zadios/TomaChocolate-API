# TomaChocolate - API

> **Live Demo:** [tomachocolate.vercel.app](https://tomachocolate.vercel.app/)

*Note: The application interface is in Spanish as it is tailored for local users in Latin America to split everyday expenses.*

Welcome to the TomaChocolate backend repository! This is the REST API of an application designed to simplify and optimize the split of group expenses in gatherings, barbecues, or events. The main goal is to offer an extremely simple, fast, and robust user experience without leaving functionalities aside in the process.

This project provides the necessary services for the web interface: [TomaChocolate-Front](https://github.com/Zadios/TomaChocolate-Front).


## Main Features

- **Centralized Data Management:** Efficiently processes and persists information regarding gatherings, participants, and associated expenses through a relational model.
- **Smart Balance Calculation:** Incorporates an optimized algorithm on the server that automates the split of bills, minimizing the amount of cross-transfers needed.
- **REST Services Exposure:** Decoupled, clean endpoints configured with CORS policies for native integration with modern user interfaces.
- **Robust Architecture:** Global exception handling to guarantee structured error responses and strict data validation using DTOs.


## Technologies Used

The backend was developed under modern standards of the Java ecosystem:

* **Java 21** (Taking advantage of the language's latest stable features).
* **Spring Boot 3.x** (Main framework for REST API creation and dependency injection).
* **Spring Data JPA / Hibernate** (For entity mapping and database abstraction).
* **MySQL** (Relational database for the persistence of the status of gatherings and expenses).
* **Lombok** (For reducing boilerplate code).
* **Maven** (As a build automation tool and project lifecycle management).
* **Swagger / OpenAPI** (For documentation, testing, and interactive exposure of endpoints).


## Local Setup (clone project on another computer)

#### 1\. Clone the repository

```bash
git clone https://github.com/Zadios/TomaChocolate-API.git
cd TomaChocolate-API
```

#### 2\. Configure the database:
Make sure you have a MySQL instance running and create a database named tomachocolate_db. Then, verify your credentials in the src/main/resources/application.properties file:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/tomachocolate_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

#### 3\. Run the application:

You can compile and run the server using the Maven wrapper from the project root:

```text
./mvnw spring-boot:run
```
The API will be available locally at http://localhost:8080.

#### 4\. API Documentation (Swagger):
Once the server is running, you can access the interactive interface to test the endpoints at:
http://localhost:8080/swagger-ui/index.html


## Project Structure

The backend follows a standardized layered architecture for Spring Boot, promoting decoupling and easy maintainability:

```text
src/main/java/com/tomachocolate/api/
├── config/       # Global app configurations (CORS, security, etc.)
├── controller/   # REST API endpoints (Meetings, Expenses, Participants)
├── dto/          # Data Transfer Objects (Requests, Responses, and Payloads)
├── exception/    # Global exception handling and structured error responses
├── model/        # Domain entities for persistence with JPA / Hibernate
├── repository/   # Data access layer (Spring Data Repositories)
└── service/      # Pure business logic (Balance optimization algorithm)
```
#### Key Package Details:
* **config/WebConfig.java:** Centralizes CORS policies, allowing secure and native communication with the React frontend (http://localhost:5173).

* **dto/:** Protects the database model using DTOs (such as MeetingRequest or ParticipantUpdateDTO) to strictly type the data entering and leaving the API.

## Developer
- Ariel Viscovich - [LinkedIn](https://www.linkedin.com/in/arielviscovich)
