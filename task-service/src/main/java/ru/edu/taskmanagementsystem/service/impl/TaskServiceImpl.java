package ru.edu.taskmanagementsystem.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.edu.taskmanagementsystem.model.TaskM;
import ru.edu.taskmanagementsystem.repository.TaskRepository;
import ru.edu.taskmanagementsystem.service.TaskService;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    //@Audited
    @Transactional
    public TaskM createTask(String title, String date) {
        TaskM task = new TaskM();
        task.setTitle(title);
        task.setDateOfCreate(LocalDateTime.parse(date));
        return taskRepository.save(task);
    }

    @Override
    public TaskM createTask(TaskM task) {
        return taskRepository.save(task);
    }

    @Override
    public TaskM findById(Long id) {
        return taskRepository.getById(id);
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public Iterable<TaskM> findAll() {
        return null;
    }

    @Override
    public boolean existsById(Long id) {
        return false;
    }



    @PostConstruct
    public void fillDatabase() {
        TaskM task = new TaskM();
        task.setTitle("event description 1");
        task.setDateOfCreate(LocalDateTime.now());
        taskRepository.save(task);
    }
}
