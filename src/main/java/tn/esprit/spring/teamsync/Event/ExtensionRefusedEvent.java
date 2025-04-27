package tn.esprit.spring.teamsync.Event;


import org.springframework.context.ApplicationEvent;

public class ExtensionRefusedEvent extends ApplicationEvent {
    private final String taskId;

    public ExtensionRefusedEvent(Object source, String taskId) {
        super(source);
        this.taskId = taskId;
    }

    public String getTaskId() { return taskId; }
}