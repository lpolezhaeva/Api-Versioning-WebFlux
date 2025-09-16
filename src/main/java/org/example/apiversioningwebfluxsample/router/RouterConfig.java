package org.example.apiversioningwebfluxsample.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterConfig {

    private static RequestPredicate headerVersion(String wanted) {
        return headers(h -> wanted.equals(h.firstHeader("X-API-Version")));
    }

    private static RequestPredicate queryVersion(String wanted) {
        return queryParam("api-version", v -> v.equals(wanted));
    }

    @Bean
    public RouterFunction<ServerResponse> customersRoutesHeader(CustomerHandler h) {
        // Enable this bean if your project uses HEADER resolver
        return route(GET("/api/customers").and(headerVersion("1.0")), h::listV10)
                .andRoute(GET("/api/customers").and(headerVersion("2.0")), h::listV20);
    }

    // @Bean
    public RouterFunction<ServerResponse> customersRoutesQuery(CustomerHandler h) {
        // Enable this bean if your project uses QUERY resolver
        return route(GET("/api/customers").and(queryVersion("1.0")), h::listV10)
                .andRoute(GET("/api/customers").and(queryVersion("2.0")), h::listV20);
    }

    // @Bean
    public RouterFunction<ServerResponse> customersRoutesPath(CustomerHandler h) {
        // Enable this bean if your project uses a PATH resolver
        return RouterFunctions.nest(path("/api"),
                route(GET("/v1/customers"), h::listV10)
                        .andRoute(GET("/v2/customers"), h::listV20));
    }

    // Optional: MEDIA-TYPE strategy (Accept: application/json; v=2.0)
    // You can implement a simple predicate that parses the Accept header if you need it:
    // private static RequestPredicate mediaTypeParamV(String wanted) {
    //   return headers(h -> {
    //     var accept = h.accept();
    //     return accept.stream().anyMatch(mt -> {
    //       var param = mt.getParameters().get("v");
    //       return wanted.equals(param);
    //     });
    //   });
    // }
}