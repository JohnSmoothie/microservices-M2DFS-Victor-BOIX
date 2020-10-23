package com.ecommerce.microcommerce.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
// activer swagger
public class SwaggerConfig {
    @Bean
    public Docket api() {

        // implementer cette methode pour le swagger
        return null;
    }
}