# Stock Market
Stock market backend project.

## FEATURES
    • Registering a new user
    • Login with a user
    • Creating a new stock
    • Deleting a stock
    • Updating a stock price
    • Creating a new stock exchange
    • Adding a stock to stock exchange
    • Deleting a stock from stock exchange
    • Getting information about a stock exchange

## TOOLS
- Java 17
- Spring Boot 3.3.2
    - Spring Web
    - Spring Security
    - Spring Data JPA
    - Validation
- JUnit5
- H2 Database
- Maven
- Json Web Token (JWT)
- Lombok
- MapStruct
- Docker
- Swagger 

## COMPILE AND RUN

Requires **Docker**

You just need to run below command to build docker image and run:

- docker-compose up --build

To stop:

- Ctrl + C. After that you can remove container/app by using **docker-compose down** command.

## USAGE

After running the application successfully it can be tested via Postman or Swagger documentation.

- Firstly you need to call **/api/v1/auth/signup** endpoint to register a new user to system.

- Then you need to call **/api/v1/auth/login** endpoint to get authenticated for all request calls.

- After that **/api/v1/auth/login** call successfully, you get **JWT Token** in response body.

- After that you can call other endpoints by using the JWT token. You should add **Authorization** header into all other request headers with the **Bearer**.

- **Swagger** documentation can be accessed from: http://localhost:8080/swagger-ui/index.html#/


**NOTE:** Postman collection is sent via email.

