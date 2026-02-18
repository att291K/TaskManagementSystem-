package ru.edu.assignment.repository;

import ru.edu.assignment.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    @Query(value = "SELECT DISTINCT ON (a.task_id) * FROM assignment a WHERE a.task_id IN :taskIds ORDER BY id DESC", nativeQuery = true)
    List<Assignment> findAllByTaskIdsDesc(@Param("taskIds") List<String> taskIds);
}