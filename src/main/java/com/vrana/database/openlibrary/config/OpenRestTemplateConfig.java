package com.vrana.database.openlibrary.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OpenRestTemplateConfig {

    @Bean
    public RestTemplate openLibraryRestTemplate() {
        return new RestTemplate();
    }
}
