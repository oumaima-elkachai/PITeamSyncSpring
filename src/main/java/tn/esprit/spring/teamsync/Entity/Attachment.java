// Attachment.java
package tn.esprit.spring.teamsync.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "attachments")
@Data
public class Attachment {
    @Id
    private String id;
    private String taskId;
    private String fileName;
    private String fileType;
    private long fileSize;
    private LocalDateTime uploadedAt;
    private String uploadedBy; // employee ID
    private String gridFsId;
}