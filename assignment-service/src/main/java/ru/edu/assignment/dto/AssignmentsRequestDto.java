package ru.edu.assignment.dto;

import java.util.List;

public class AssignmentsRequestDto {
    private List<Long> taskIds;
    public List<Long> getTaskIds() { return taskIds; }
    public void setTaskIds(List<Long> taskIds) { this.taskIds = taskIds; }
}