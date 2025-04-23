package tn.esprit.spring.teamsync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class TeamsyncApplication {

    public static void main(String[] args) {
        SpringApplication.run(TeamsyncApplication.class, args);
    }

}
