package ru.edu.taskmanagementsystem.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.edu.taskmanagementsystem.model.TaskM;

@Repository
public interface TaskRepository extends JpaRepository<@NonNull TaskM, @NonNull Long>,
                                         PagingAndSortingRepository<@NonNull TaskM, @NonNull Long> {

}
