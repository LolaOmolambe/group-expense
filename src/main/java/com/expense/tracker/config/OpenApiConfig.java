package com.expense.tracker.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
//import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@OpenAPIDefinition(info = @Info(title = "Standup",
        version = "v1",
        description =  "This is a sample Spring Boot RESTful service using springdoc-openapi and OpenAPI 3."))
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer"
)
public class OpenApiConfig {


//    @Bean
//    public OpenAPI customOpenAPI() {
//        return new OpenAPI()
//                .components(new Components())
//                .info(new Info().title("Standup Application API")
//                        .version("v1")
//                        .description(
//                                "This is a sample Spring Boot RESTful service using springdoc-openapi and OpenAPI 3."));
//    }


}
