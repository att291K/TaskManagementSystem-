package ru.edu.taskmanagementsystem.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.edu.taskmanagementsystem.aop.annotations.Audited;
import ru.edu.taskmanagementsystem.model.User;
import ru.edu.taskmanagementsystem.model.enums.Status;
import ru.edu.taskmanagementsystem.model.TaskM;
import ru.edu.taskmanagementsystem.repository.TaskRepository;
import ru.edu.taskmanagementsystem.service.TaskService;

import java.time.LocalDateTime;
import java.util.HashSet;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Audited
    @Transactional
    public TaskM createTask(String title, String description, String status) {
        TaskM task = new TaskM();
        task.setTitle(title);
        task.setStatus(Status.valueOf(status));
        task.setDescription(description);
        task.setDateOfCreate(LocalDateTime.now());
        return taskRepository.save(task);
    }

    @Override
    public TaskM createTask(TaskM task) {
        return taskRepository.save(task);
    }

    @Override
    public TaskM findById(Long id) {
        return taskRepository.getReferenceById(id);
    }

    @Override
    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        taskRepository.deleteAll();
    }

    @Override
    public Iterable<TaskM> findAll() {
        return taskRepository.findAll();
    }


    @Override
    public boolean existsById(Long id) {
        return taskRepository.existsById(id);
    }

    @PostConstruct
    public void fillDatabase() {
        try {
            TaskM task = new TaskM();
            task.setTitle("task description 1");
            task.setDescription("");
            task.setDateOfCreate(LocalDateTime.now());
            task.setStatus(Status.Created);
            taskRepository.save(task);
        }
        catch (Exception ignored) {}

    }
}
