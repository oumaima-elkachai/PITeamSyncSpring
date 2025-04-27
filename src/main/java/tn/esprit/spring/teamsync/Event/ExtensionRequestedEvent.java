package tn.esprit.spring.teamsync.Event;


import org.springframework.context.ApplicationEvent;

public class ExtensionRequestedEvent extends ApplicationEvent {
    private final String taskId;

    public ExtensionRequestedEvent(Object source, String taskId) {
        super(source);
        this.taskId = taskId;
    }

    public String getTaskId() { return taskId; }
}