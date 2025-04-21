// AttachmentServiceImpl.java
package tn.esprit.spring.teamsync.Services.MPL;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.spring.teamsync.Entity.Attachment;
import tn.esprit.spring.teamsync.Repository.AttachmentRepository;
import tn.esprit.spring.teamsync.Services.Interfaces.AttachmentService;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentRepository attachmentRepository;

    @Override
    public Attachment storeFile(MultipartFile file, String taskId, String userId) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        Attachment attachment = new Attachment();
        attachment.setTaskId(taskId);
        attachment.setFileName(fileName);
        attachment.setFileType(file.getContentType());
        attachment.setFileSize(file.getSize());
        attachment.setUploadedAt(LocalDateTime.now());
        attachment.setUploadedBy(userId);

        return attachmentRepository.save(attachment);
    }

    @Override
    public List<Attachment> getAttachmentsByTask(String taskId) {
        return attachmentRepository.findByTaskId(taskId);
    }

    @Override
    public ResponseEntity<byte[]> downloadFile(String id) {
        return attachmentRepository.findById(id)
                .map(attachment -> {
                    // Implement actual file retrieval logic here
                    // This is just a placeholder response
                    return ResponseEntity.ok()
                            .header("Content-Disposition", "attachment; filename=\"" + attachment.getFileName() + "\"")
                            .body(new byte[0]);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}