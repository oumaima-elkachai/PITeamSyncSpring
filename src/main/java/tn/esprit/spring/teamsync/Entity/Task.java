package tn.esprit.spring.teamsync.Entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    private List<String> requiredSkills = new ArrayList<>();

    @Field
    private LocalDate requestedExtensionDate;

    @Field
    private String extensionStatus; // PENDING, APPROVED, REJECTED


    // Relationships
    private String employeeId; // Optional: assigned employee

    public enum Priority { HIGH, MEDIUM, LOW }
    public enum Status { TODO, IN_PROGRESS, DONE }

    @Field
    private List<Map<String, String>> links = new ArrayList<>();
}