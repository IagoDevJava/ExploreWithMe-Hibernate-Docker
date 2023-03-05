package ru.practicum.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.stat.StatClient;

@Configuration
public class ClientConfig {
    @Value("${stats-server.url}")
    private String url;

    @Bean
    StatClient hitClient() {
        RestTemplateBuilder builder = new RestTemplateBuilder();
        return new StatClient(url, builder);
    }
}
