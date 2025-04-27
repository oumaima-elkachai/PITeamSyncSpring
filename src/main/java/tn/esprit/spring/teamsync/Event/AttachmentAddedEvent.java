package tn.esprit.spring.teamsync.Event;


import org.springframework.context.ApplicationEvent;

public class AttachmentAddedEvent extends ApplicationEvent {
    private final String taskId;
    private final String attachmentId;
    private final String userId;

    public AttachmentAddedEvent(Object source, String taskId, String attachmentId, String userId) {
        super(source);
        this.taskId = taskId;
        this.attachmentId = attachmentId;
        this.userId = userId;
    }

    // Getters
    public String getTaskId() { return taskId; }
    public String getAttachmentId() { return attachmentId; }
    public String getUserId() { return userId; }
}