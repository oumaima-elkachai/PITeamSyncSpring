package com.teamsync.recruitment.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dm2gufdmn",
                "api_key", "745773944685639",
                "api_secret", "vAx35vcc91rIfxkHl8LcT9G3uBI",
                "secure", true
        ));
    }
}
