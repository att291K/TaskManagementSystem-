package ru.edu.assignment.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.jspecify.annotations.NonNull;
import org.springframework.http.ResponseEntity;
import ru.edu.assignment.entity.Assignment;
import ru.edu.assignment.dto.AssignmentRequestDto;
import ru.edu.assignment.service.AssignmentService;
import org.springframework.web.bind.annotation.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.IOException;

@RestController
@RequestMapping("/assignment")
public class AssignmentController {
    private final AssignmentService service;

    public AssignmentController(AssignmentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Assignment> assign(@NonNull HttpServletRequest request, @RequestBody AssignmentRequestDto assignmentRequest) throws IOException {
        String authorization = request.getHeader("Authorization");

        if (authorization != null && authorization.startsWith("Bearer ")) {
            URL url = new URL("http://auth-service:8080/getRoles"); // Target URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", authorization); // Set request headers

            int responseCode = connection.getResponseCode();
            System.out.println("GET Response Code :: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) { // Success
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                if (!response.toString().contains("MANAGER")) {
                    return ResponseEntity.status(401).body(null);
                }
            } else {
                return ResponseEntity.status(401).body(null);
            }
        } else {
            return ResponseEntity.status(401).body(null);
        }

        Assignment assignment = new Assignment();

        assignment.setAssignedAt(java.time.LocalDate.now());
        assignment.setEmployeeId(assignmentRequest.getEmployeeId());
        assignment.setTaskId(assignmentRequest.getTaskId());

        Assignment totalAssignment = service.createAssignment(assignment);

        return ResponseEntity.ok().body(totalAssignment);
    }
}