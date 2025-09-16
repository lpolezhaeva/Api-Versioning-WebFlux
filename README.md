# WebFlux RouterFunction — API Versioning Demo

Minimal **Spring Boot WebFlux** app to verify **Endpoints discovery** for **RouterFunction** routes with version predicates.

## Test case in scope

**TC43. Endpoints discovery (WebFlux RouterFunction)**

**Steps to reproduce:**

1. Start the app.
2. Open **View → Tool Windows → Endpoints** in IntelliJ IDEA.
   
**Expected:**

* `/api/customers@1.0 [GET]`
* `/api/customers@2.0 [GET]`

## Requirements

* **JDK 21+**
* **Maven 3.9+**
* **IntelliJ IDEA Ultimate** (for Endpoints/HTTP Client)

## How to run

You can start the application in two ways:

### 1. Run via Maven
```bash
mvn spring-boot:run
```
The server starts at [http://localhost:8080](http://localhost:8080).

### 2. Run directly in IntelliJ IDEA
Open `app/ApiVersioningWebfluxSampleApplication.java` and run its `main` method.  
This launches the Spring Boot app with the same configuration as `mvn spring-boot:run`.

## How to run tests
You can run the automated tests via Maven or your IDE.

- All tests:
  ```bash
  mvn test
  ```
- Specific test class:
  ```bash
  mvn -Dtest=ApiVersioningWebfluxSampleApplicationTests test
  ```
- Single test method (example):
  ```bash
  mvn -Dtest=ApiVersioningWebfluxSampleApplicationTests#getCustomers_v1Header_shouldReturnVersion10 test
  ```
- From IntelliJ IDEA: open the test class or method and click the gutter run icon, or use Maven Tool Window → Lifecycle → test.

---

## Project structure

```
src/main/java/
  app/
    DemoApplication.java            # @SpringBootApplication (parent package)
  router/
    CustomerHandler.java            # @Component with v1.0 / v2.0 handlers
    RouterConfig.java               # @Configuration with RouterFunction beans
requests.http                       # Sample HTTP Client requests
pom.xml                             # Maven build (WebFlux starter)
```

## How versioning is expressed (this demo)

* **Header strategy** using `X-API-Version`.
* `RouterConfig` wires two routes:

    * `GET /api/customers` + header `X-API-Version: 1.0` → `CustomerHandler#listV10`
    * `GET /api/customers` + header `X-API-Version: 2.0` → `CustomerHandler#listV20`

> You can adapt to query/path/media-type by changing the predicates in `RouterConfig`.

## IntelliJ steps (TC43)

1. Run the app (`mvn spring-boot:run`).
2. Open **View → Tool Windows → Endpoints**.
3. Expand the module → you should see `/api/customers@1.0` and `/api/customers@2.0`.
4. Right-click an entry → **Generate request** to append to `requests.http` and run.

## Sample requests (`requests.http`)
The following HTTP requests are available in the `requests.http` file (at the project root) and can be executed with IntelliJ IDEA’s HTTP Client.

## pom.xml (essentials)

Make sure you have at least:

```xml
<dependencies>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
  </dependency>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
  </dependency>
</dependencies>

<build>
  <plugins>
    <plugin>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-maven-plugin</artifactId>
    </plugin>
  </plugins>
</build>
```