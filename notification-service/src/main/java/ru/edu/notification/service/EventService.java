package ru.edu.notification.service;

import ru.edu.notification.model.EventLog;
import ru.edu.notification.model.TaskEvent;
import ru.edu.notification.repository.EventLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventLogRepository eventLogRepository;

    public Page<EventLog> getAllEvents(Pageable pageable) {
        return eventLogRepository.findAll(pageable);
    }

    public List<EventLog> getEventsByTaskId(Long taskId) {
        return eventLogRepository.findByTaskId(taskId);
    }

    public List<EventLog> getEventsByEmployeeId(Long employeeId) {
        return eventLogRepository.findByEmployeeId(employeeId);
    }

    public List<EventLog> getRecentEvents(int limit) {
        return eventLogRepository.findAll(
                org.springframework.data.domain.PageRequest.of(0, limit)
        ).getContent();
    }

    public long getTotalEvents() {
        return eventLogRepository.count();
    }

    public List<EventLog> getEventsBetween(LocalDateTime start, LocalDateTime end) {
        return eventLogRepository.findByCreatedAtBetween(start, end);
    }

    // Новый метод: сохранение события из Kafka
    public void saveEventFromTaskEvent(TaskEvent event) {
        EventLog eventLog = event.toEventLog();
        eventLogRepository.save(eventLog);
    }
}