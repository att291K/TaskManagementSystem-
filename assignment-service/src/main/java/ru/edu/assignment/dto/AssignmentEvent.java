package ru.edu.assignment.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AssignmentEvent {
    private String eventType;
    private Long taskId;
    private Long employeeId;
    private Object timestamp;

    public AssignmentEvent() {}

    public AssignmentEvent(Long taskId, Long employeeId) {
        this.eventType = "TASK_ASSIGNED";
        this.taskId = taskId;
        this.employeeId = employeeId;
        this.timestamp = java.time.LocalDateTime.now().toString();
    }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public Long getTaskId() { return taskId; }
    public void setTaskId(Long taskId) { this.taskId = taskId; }
    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
    public Object getTimestamp() { return timestamp; }
    public void setTimestamp(Object timestamp) { this.timestamp = timestamp; }
}