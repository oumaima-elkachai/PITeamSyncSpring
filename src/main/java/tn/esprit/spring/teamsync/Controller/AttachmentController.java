// AttachmentController.java
package tn.esprit.spring.teamsync.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.spring.teamsync.Entity.Attachment;
import tn.esprit.spring.teamsync.Services.Interfaces.AttachmentService;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/attachments")
@CrossOrigin(origins = "http://localhost:4200")
public class AttachmentController {

    private final AttachmentService attachmentService;

    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Attachment> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("taskId") String taskId,
            @RequestParam("userId") String userId) throws IOException {

        Attachment attachment = attachmentService.storeFile(file, taskId, userId);
        return new ResponseEntity<>(attachment, HttpStatus.CREATED);
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<Attachment>> getAttachmentsByTask(@PathVariable String taskId) {
        List<Attachment> attachments = attachmentService.getAttachmentsByTask(taskId);
        return ResponseEntity.ok(attachments);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String id) {
        return attachmentService.downloadFile(id);
    }

    // AttachmentController.java



}