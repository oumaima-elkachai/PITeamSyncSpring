// AttachmentRepository.java
package tn.esprit.spring.teamsync.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.spring.teamsync.Entity.Attachment;
import java.util.List;

public interface AttachmentRepository extends MongoRepository<Attachment, String> {
    List<Attachment> findByTaskId(String taskId);
}