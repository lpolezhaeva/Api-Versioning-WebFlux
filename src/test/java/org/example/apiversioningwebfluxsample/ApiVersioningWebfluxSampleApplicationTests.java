package org.example.apiversioningwebfluxsample;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.example.apiversioningwebfluxsample.router.CustomerHandler;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class ApiVersioningWebfluxSampleApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private CustomerHandler customerHandler;

    // Integration tests for requests from requests.http (header-based versioning)
    @Test
    void getCustomers_v1Header_shouldReturnVersion10() {
        webTestClient.get()
                .uri("/api/customers")
                .header("X-API-Version", "1.0")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.version").isEqualTo("1.0")
                .jsonPath("$.data").isArray()
                .jsonPath("$.data.length()").isEqualTo(0);
    }

    @Test
    void getCustomers_v2Header_shouldReturnVersion20() {
        webTestClient.get()
                .uri("/api/customers")
                .header("X-API-Version", "2.0")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.version").isEqualTo("2.0")
                .jsonPath("$.data").isArray()
                .jsonPath("$.data.length()").isEqualTo(0);
    }

    // Direct tests for handler methods (controller methods)
    @Test
    void handler_listV10_returnsExpectedJson() {
        RouterFunction<ServerResponse> route = RouterFunctions.route(
                RequestPredicates.GET("/test/customers"), customerHandler::listV10);

        WebTestClient.bindToRouterFunction(route)
                .build()
                .get()
                .uri("/test/customers")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.version").isEqualTo("1.0")
                .jsonPath("$.data").isArray()
                .jsonPath("$.data.length()").isEqualTo(0);
    }

    @Test
    void handler_listV20_returnsExpectedJson() {
        RouterFunction<ServerResponse> route = RouterFunctions.route(
                RequestPredicates.GET("/test/customers"), customerHandler::listV20);

        WebTestClient.bindToRouterFunction(route)
                .build()
                .get()
                .uri("/test/customers")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.version").isEqualTo("2.0")
                .jsonPath("$.data").isArray()
                .jsonPath("$.data.length()").isEqualTo(0);
    }
}
