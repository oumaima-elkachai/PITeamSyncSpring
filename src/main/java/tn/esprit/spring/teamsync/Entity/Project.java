package tn.esprit.spring.teamsync.Entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "projects")
public class Project {
    @Id
    private String id; // MongoDB uses String for @Id by default
    private String title;
    private String description;
    private List<String> owners;         // List of employee IDs
    private List<String> teamMembers;    // List of employee IDs
    private List<String> taskIds;        // List of task IDs
}