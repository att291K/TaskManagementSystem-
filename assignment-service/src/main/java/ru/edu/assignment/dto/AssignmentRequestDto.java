package ru.edu.assignment.dto;

public class AssignmentRequestDto {
    private Long taskId;
    private Long employeeId;


    public Long getTaskId() { return taskId; }
    public void setTaskId(Long taskId) { this.taskId = taskId; }
    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
}