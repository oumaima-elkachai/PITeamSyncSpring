package tn.esprit.spring.teamsync.Event;

import org.springframework.context.ApplicationEvent;

public class TaskAssignedEvent extends ApplicationEvent {
    private final String taskId;
    private final String assigneeId;

    public TaskAssignedEvent(Object source, String taskId, String assigneeId) {
        super(source);
        this.taskId = taskId;
        this.assigneeId = assigneeId;
    }

    public String getTaskId() { return taskId; }
    public String getAssigneeId() { return assigneeId; }
}