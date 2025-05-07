package com.example.events;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
<<<<<<< Updated upstream

@SpringBootApplication
public class EventsApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventsApplication.class, args);
	}

=======
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootApplication
public class EventsApplication {
    
    private static final Logger logger = LoggerFactory.getLogger(EventsApplication.class);
    
    @Autowired
    private MongoTemplate mongoTemplate;

    public static void main(String[] args) {
        SpringApplication.run(EventsApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void checkMongoConnection() {
        try {
            mongoTemplate.getDb().getName();
            logger.info("Successfully connected to MongoDB");
        } catch (Exception e) {
            logger.error("Failed to connect to MongoDB: {}", e.getMessage());
            throw e;
        }
    }
>>>>>>> Stashed changes
}
