package com.juliomesquita.admin.catalog.infrastructure.configuration.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.juliomesquita.admin.catalog.infrastructure.configuration.mapper.json.Json;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfig {
    @Bean
    public ObjectMapper objectMapper() {
        return Json.mapper();
    }
}
