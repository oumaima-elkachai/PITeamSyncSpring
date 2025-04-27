package tn.esprit.spring.teamsync.Event;


import org.springframework.context.ApplicationEvent;

public class ExtensionApprovedEvent extends ApplicationEvent {
    private final String taskId;

    public ExtensionApprovedEvent(Object source, String taskId) {
        super(source);
        this.taskId = taskId;
    }

    public String getTaskId() { return taskId; }
}