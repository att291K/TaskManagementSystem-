package ru.edu.assignment.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.jspecify.annotations.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.edu.assignment.dto.AssignmentRequestDto;
import ru.edu.assignment.dto.AssignmentsRequestDto;
import ru.edu.assignment.entity.Assignment;
import ru.edu.assignment.service.AssignmentService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@RestController
@RequestMapping("/assignments")
public class AssignmentsController {
    private final AssignmentService service;

    public AssignmentsController(AssignmentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<List<Assignment>> assignments(@RequestBody AssignmentsRequestDto AssignmentsRequest) {

        List<Assignment> totalAssignment = service.getAssignments(AssignmentsRequest.getTaskIds());

        return ResponseEntity.ok().body(totalAssignment);
    }
}