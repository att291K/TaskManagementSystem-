package ru.edu.assignment.controller;

import ru.edu.assignment.entity.Assignment;
import ru.edu.assignment.dto.AssignmentRequestDto;
import ru.edu.assignment.service.AssignmentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/assignments")
public class AssignmentController {
    private final AssignmentService service;

    public AssignmentController(AssignmentService service) {
        this.service = service;
    }

    @PostMapping
    public Assignment assign(@RequestBody AssignmentRequestDto assignmentRequest) {
        Assignment assignment = new Assignment();

        assignment.setAssignedAt(java.time.LocalDate.now());
        assignment.setEmployeeId(assignmentRequest.getEmployeeId());
        assignment.setTaskId(assignmentRequest.getTaskId());

        return service.createAssignment(assignment);
    }
}