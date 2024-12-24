package com.miniblog.account.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI postServiceAPI() {
        return new OpenAPI()
                .info(new Info().title("Account Service API")
                        .description("This is the REST API for Account Service")
                        .version("v0.0.1")
                        .license(new License().name("Apache 2.0")))
                .externalDocs(new ExternalDocumentation()
                        .description("You can refer to the Account Service Wiki Documentation")
                        .url("https://account-service-dummy-url.com/docs"));
    }
}

