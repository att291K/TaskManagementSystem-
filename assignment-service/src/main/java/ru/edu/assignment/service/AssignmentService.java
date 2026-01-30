package ru.edu.assignment.service;

import ru.edu.assignment.dto.AssignmentEvent;
import ru.edu.assignment.repository.AssignmentRepository;
import ru.edu.assignment.entity.Assignment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class AssignmentService {

    private final AssignmentRepository repository;
    private final KafkaTemplate<String, AssignmentEvent> kafkaTemplate;

    public AssignmentService(AssignmentRepository repository, KafkaTemplate<String, AssignmentEvent> kafkaTemplate) {
        this.repository = repository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public Assignment createAssignment(Assignment assignment) {
        Assignment savedAssignment = repository.save(assignment);

        AssignmentEvent event = new AssignmentEvent(
                savedAssignment.getTaskId(),
                savedAssignment.getEmployeeId()
        );

        kafkaTemplate.send("task.events", event);

        return savedAssignment;
    }
}