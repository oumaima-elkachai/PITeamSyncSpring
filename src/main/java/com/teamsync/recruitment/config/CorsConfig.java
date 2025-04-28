package com.teamsync.recruitment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // Allow all paths and allow methods like POST, OPTIONS, etc.
                registry.addMapping("/**")  // Allow all paths
                        .allowedOrigins("http://localhost:4200")  // Allow Angular frontend origin
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Allow all relevant methods, including OPTIONS for preflight
                        .allowedHeaders("*")  // Allow all headers
                        .allowCredentials(true);  // Allow cookies if needed
            }
        };
    }
}
