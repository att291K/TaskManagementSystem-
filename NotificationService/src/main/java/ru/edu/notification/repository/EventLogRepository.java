package ru.edu.notification.repository;

import ru.edu.notification.model.EventLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventLogRepository extends JpaRepository<EventLog, Long> {

    // Найти все события для задачи
    List<EventLog> findByTaskId(Long taskId);

    // Найти все события для сотрудника
    List<EventLog> findByEmployeeId(Long employeeId);

    // Найти события по типу
    List<EventLog> findByEventType(String eventType);

    // Найти события по диапазону дат создания
    List<EventLog> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    // Пагинация
    Page<EventLog> findAll(Pageable pageable);
}