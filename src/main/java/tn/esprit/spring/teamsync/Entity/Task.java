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
@Document(collection = "tasks")
public class Task {
    @Id
    private String id;            // Lombok will generate setId(String id)
    private String employeeId;
    private String projectId;
    private String title;
    private String description;
    private String deadline;      // ISO string format (e.g., "2024-03-15T14:30:00Z")
    private String priority;      // Could be an enum (e.g., "HIGH", "MEDIUM", "LOW")
    private String status;        // e.g., "TODO", "IN_PROGRESS", "DONE"
    private String attachment;    // File path or URL
}