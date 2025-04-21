package tn.esprit.spring.teamsync.Entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
 import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "tasks")
public class Task {
    @Id
    private String id;

    private String projectId; // MUST be linked to a project

    private String title;
    private String description;
    private LocalDate deadline;
    private Priority priority;
    private Status status;

    // Relationships
    private String employeeId; // Optional: assigned employee

    public enum Priority { HIGH, MEDIUM, LOW }
    public enum Status { TODO, IN_PROGRESS, DONE }
}