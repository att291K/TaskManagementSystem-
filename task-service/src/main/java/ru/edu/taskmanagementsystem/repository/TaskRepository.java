package ru.edu.taskmanagementsystem.repository;

import org.springframework.stereotype.Repository;
import ru.edu.taskmanagementsystem.model.TaskM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class TaskRepository {
    private final Map<Long, TaskM> tasks = new HashMap<>();
    private final AtomicLong id = new AtomicLong(0);

    public TaskM save(TaskM task) {
        task.setId(id.incrementAndGet());
        tasks.put(task.getId(),task);
        return  task;
    }

    public TaskM getById(long id){
        return tasks.get(id);
    }

    public List<TaskM> getAll(){
        return new ArrayList<>(tasks.values());
    }

    public void deleteById (long id){
        tasks.remove(id);
    }

    public void deleteAll (){
        tasks.clear();
    }

    public boolean existsById( long id){
        return tasks.containsKey(id);
    }
}
