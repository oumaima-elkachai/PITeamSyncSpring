package tn.esprit.spring.teamsync.Entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "projects")
public class Project {
    @Id
    private String id;

    private String title;
    private String description;
    private String owner; // Static name (e.g., "John Doe")
    private LocalDate dueDate;
    private String status; // Active, On Hold, Completed
    private String type;   // Engineering, Design, etc.

    // Relationships (IDs only)
    private List<String> taskIds = new ArrayList<>();      // Task IDs
    private List<String> teamMemberIds = new ArrayList<>(); // Employee IDs
}