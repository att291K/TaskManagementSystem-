package ru.edu.taskmanagementsystem.service;

import ru.edu.taskmanagementsystem.model.TaskM;


public interface TaskService {
    TaskM createTask(String title, String date);

    TaskM findById(Long id);

    void deleteById(Long id);

    void deleteAll();

    Iterable<TaskM> findAll();

    boolean existsById(Long id);

    TaskM createTask(TaskM task);
}
