// AttachmentServiceImpl.java
package tn.esprit.spring.teamsync.Services.MPL;

import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.spring.teamsync.Entity.Attachment;
import tn.esprit.spring.teamsync.Event.AttachmentAddedEvent;
import tn.esprit.spring.teamsync.Repository.AttachmentRepository;
import tn.esprit.spring.teamsync.Services.Interfaces.AttachmentService;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import tn.esprit.spring.teamsync.Entity.Task;
import tn.esprit.spring.teamsync.Repository.TaskRepository;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;



@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {

    private final GridFsTemplate gridFsTemplate;
    private final AttachmentRepository attachmentRepository;
    private final ApplicationEventPublisher eventPublisher;

    private final EmailService emailService;
    private final TaskRepository taskRepository; // Add this dependency



    @Override
    public void deleteAttachment(String id) {
        attachmentRepository.findById(id).ifPresent(attachment -> {
            // Delete from GridFS
            gridFsTemplate.delete(new Query(Criteria.where("_id").is(new ObjectId(attachment.getGridFsId()))));
            // Delete metadata
            attachmentRepository.deleteById(id);
        });
    }


    @Override
    public Attachment storeFile(MultipartFile file, String taskId, String userId) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        ObjectId gridFsId = gridFsTemplate.store(file.getInputStream(), fileName, file.getContentType());

        Attachment attachment = new Attachment();
        attachment.setGridFsId(gridFsId.toString());
        attachment.setTaskId(taskId);
        attachment.setFileName(fileName);
        attachment.setFileType(file.getContentType());
        attachment.setFileSize(file.getSize());
        attachment.setUploadedAt(LocalDateTime.now());
        attachment.setUploadedBy(userId);

        Attachment savedAttachment = attachmentRepository.save(attachment);
        eventPublisher.publishEvent(new AttachmentAddedEvent(this, taskId, savedAttachment.getId(), userId));

        return savedAttachment;
    }

    @Override
    public List<Attachment> getAttachmentsByTask(String taskId) {
        return attachmentRepository.findByTaskId(taskId);
    }

    @Override
    public ResponseEntity<byte[]> downloadFile(String id) {
        return attachmentRepository.findById(id)
                .map(attachment -> {
                    try {
                        Query query = new Query(Criteria.where("_id").is(attachment.getGridFsId()));
                        GridFSFile file = gridFsTemplate.findOne(query);

                        if (file == null) {
                            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new byte[0]);
                        }

                        GridFsResource resource = gridFsTemplate.getResource(file);
                        byte[] data = IOUtils.toByteArray(resource.getInputStream());

                        return ResponseEntity.ok()
                                .header(HttpHeaders.CONTENT_DISPOSITION,
                                        "attachment; filename=\"" + attachment.getFileName() + "\"")
                                .contentType(MediaType.parseMediaType(attachment.getFileType()))
                                .body(data);

                    } catch (IOException e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new byte[0]);
                    }
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new byte[0]));
    }
}