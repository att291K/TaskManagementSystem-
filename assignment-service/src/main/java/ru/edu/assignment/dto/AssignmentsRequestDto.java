package ru.edu.assignment.dto;

import java.util.List;

public class AssignmentsRequestDto {
    private List<String> taskIds;

    public List<String> getTaskIds() { return taskIds; }
    public void setTaskIds(List<String> taskIds) { this.taskIds = taskIds; }
}