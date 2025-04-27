// AttachmentServiceImpl.java
package tn.esprit.spring.teamsync.Services.MPL;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.spring.teamsync.Entity.Attachment;
import tn.esprit.spring.teamsync.Repository.AttachmentRepository;
import tn.esprit.spring.teamsync.Services.Interfaces.AttachmentService;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import tn.esprit.spring.teamsync.Repository.TaskRepository;


@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {

    private final GridFsTemplate gridFsTemplate;
    private final AttachmentRepository attachmentRepository;
    private final ApplicationEventPublisher eventPublisher;

    private final EmailService emailService;
    private final TaskRepository taskRepository; // Add this dependency
    private final Cloudinary cloudinary;
    @Override
    public List<Attachment> getAttachmentsByTask(String taskId) {
        return attachmentRepository.findByTaskId(taskId);
    }

    @Override
    public Attachment storeFile(MultipartFile file, String taskId, String userId) throws IOException {
        Map<String, Object> uploadResult = cloudinary.uploader()
                .upload(file.getBytes(),
                        ObjectUtils.asMap(
                                "resource_type", "auto",
                                "folder", "task_attachments"
                        ));

        Attachment attachment = new Attachment();
        attachment.setTaskId(taskId);
        attachment.setFileName(file.getOriginalFilename());
        attachment.setFileType(file.getContentType());
        attachment.setFileSize(file.getSize());
        attachment.setUploadedAt(LocalDateTime.now());
        attachment.setUploadedBy(userId);
        attachment.setCloudinaryUrl(uploadResult.get("secure_url").toString());
        attachment.setPublicId(uploadResult.get("public_id").toString());

        return attachmentRepository.save(attachment);
    }

    @Override
    public ResponseEntity<Object> downloadFile(String id) {
        return attachmentRepository.findById(id)
                .map(attachment -> {
                    try {
                        // Get file URL from Cloudinary
                        String downloadUrl = cloudinary.url()
                                .generate(attachment.getPublicId());

                        return ResponseEntity.status(HttpStatus.FOUND)
                                .header(HttpHeaders.LOCATION, downloadUrl)
                                .build();
                    } catch (Exception e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public void deleteAttachment(String id) {
        attachmentRepository.findById(id).ifPresent(attachment -> {
            try {
                // Delete from Cloudinary
                cloudinary.uploader().destroy(attachment.getPublicId(), ObjectUtils.emptyMap());
                // Delete metadata
                attachmentRepository.deleteById(id);
            } catch (IOException e) {
                throw new RuntimeException("Failed to delete file from Cloudinary", e);
            }
        });
    }

}