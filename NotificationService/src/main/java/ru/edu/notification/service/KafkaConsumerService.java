package ru.edu.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.edu.notification.model.TaskEvent;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final EventService eventService;

    // Укажите containerFactory, если используете кастомную конфигурацию
    @KafkaListener(
            topics = "task.events",
            groupId = "notification-group",
            containerFactory = "kafkaListenerContainerFactory"  // ссылка на бин из конфигурации
    )
    public void consumeTaskEvent(TaskEvent event) {
        log.info("📨 Получено событие: {}", event);
        log.info("📊 Тип события: {}", event.eventType());
        log.info("📝 ID задачи: {}", event.taskId());
        log.info("👤 ID сотрудника: {}", event.employeeId());

        try {
            eventService.saveEventFromTaskEvent(event);
            log.info("✅ Событие сохранено в БД: {}", event.eventType());
        } catch (Exception e) {
            log.error("❌ Ошибка сохранения события: {}", e.getMessage(), e);
        }
    }
}