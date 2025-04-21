// AttachmentService.java
package tn.esprit.spring.teamsync.Services.Interfaces;

import tn.esprit.spring.teamsync.Entity.Attachment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

public interface AttachmentService {
    Attachment storeFile(MultipartFile file, String taskId, String userId) throws IOException;
    List<Attachment> getAttachmentsByTask(String taskId);
    ResponseEntity<byte[]> downloadFile(String id);
}