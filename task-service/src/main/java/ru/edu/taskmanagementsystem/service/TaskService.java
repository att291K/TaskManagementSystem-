package ru.edu.taskmanagementsystem.service;

import org.jspecify.annotations.Nullable;
import ru.edu.taskmanagementsystem.model.TaskM;


public interface TaskService {
    TaskM createTask(String title, String description, String status);

    default TaskM createTask () {
        return new TaskM();
    }

    TaskM findById(Long id);

    void deleteById(Long id);

    void deleteAll();

    Iterable<TaskM> findAll();

    boolean existsById(Long id);

    TaskM createTask(TaskM task);

    @Nullable TaskM updateTask(TaskM task);
}
