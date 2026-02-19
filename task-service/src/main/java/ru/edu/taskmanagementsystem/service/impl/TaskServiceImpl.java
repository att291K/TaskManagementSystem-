package ru.edu.taskmanagementsystem.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.edu.taskmanagementsystem.aop.annotations.Audited;
import ru.edu.taskmanagementsystem.model.enums.Status;
import ru.edu.taskmanagementsystem.model.TaskM;
import ru.edu.taskmanagementsystem.repository.TaskRepository;
import ru.edu.taskmanagementsystem.service.NotificationServiceClient;
import ru.edu.taskmanagementsystem.service.TaskService;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final NotificationServiceClient notificationClient;

    @Audited
    @Transactional
    public TaskM createTask(String title, String description, String status) {
        TaskM task = new TaskM();
        task.setTitle(title);
        task.setStatus(Status.valueOf(status));
        task.setDescription(description);
        task.setDateOfCreate(LocalDateTime.now());

       // СНАЧАЛА сохраняем
    TaskM savedTask = taskRepository.save(task);
    //log.info("Task saved with ID: {}", savedTask.getId());
    
    // ПОТОМ отправляем уведомление с реальным ID
    try {
        notificationClient.notifyTaskCreated(
            savedTask.getId(),  // теперь ID не 0!
            savedTask.getTitle(),
            5L  // TODO: взять из контекста безопасности
        );
    } catch (Exception e) {
        // Логируем, но не прерываем транзакцию
        //log.error("Failed to send notification but task was created", e);
    }
    
    return savedTask;
    }

    @Override
    public TaskM createTask(TaskM task) {


         // СНАЧАЛА сохраняем
    TaskM savedTask = taskRepository.save(task);
    //log.info("Task saved with ID: {}", savedTask.getId());
    
    // ПОТОМ отправляем уведомление с реальным ID
    try {
        notificationClient.notifyTaskCreated(
            savedTask.getId(),  // теперь ID не 0!
            savedTask.getTitle(),
            5L  // TODO: взять из контекста безопасности
        );
    } catch (Exception e) {
        // Логируем, но не прерываем транзакцию
        //log.error("Failed to send notification but task was created", e);
    }
    
    return savedTask;
    }

    @Override
    public @Nullable TaskM updateTask(TaskM task) {
        notificationClient.notifyTaskCreated(
            task.getId(),
            task.getTitle(),
            5L
        );
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
            task.setTitle("task description 2");
            task.setDescription("");
            task.setDateOfCreate(LocalDateTime.now());
            task.setStatus(Status.OPEN);
            taskRepository.save(task);
        }
        catch (Exception ignored) {}

    }
}
