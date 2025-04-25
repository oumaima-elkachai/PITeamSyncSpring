package com.example.events.config;
import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary(){
        Map<String, String> config = new HashMap<>();
      
        config.put("cloud_name", "dhsburvlt");
        config.put("api_key", "839199513191141");
        config.put("api_secret", "9ED8N2GUKFqvvz37UslxKkcmXHc");

        return new Cloudinary(config);
    }
}