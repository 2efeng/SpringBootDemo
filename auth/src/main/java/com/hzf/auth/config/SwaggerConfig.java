package com.hzf.auth.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class SwaggerConfig {


    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                .addSecurityItem(securityRequirement())
                .info(info())
                .components(components());
    }

    private Info info() {
        return new Info()
                .title("MyApplication")
                .description("Application API Document")
                .version("Application Version");
    }

    private Components components() {
        return new Components()
                .addSecuritySchemes("bearer-key",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                );
    }

    private SecurityRequirement securityRequirement() {
        return new SecurityRequirement()
                .addList("bearer-jwt", Arrays.asList("read", "write"))
                .addList("bearer-key", Collections.emptyList());
    }

}
