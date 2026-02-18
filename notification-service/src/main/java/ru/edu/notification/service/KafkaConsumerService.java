package ru.edu.notification.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.edu.notification.adapter.KafkaMessageAdapter;
import ru.edu.notification.model.TaskEvent;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final EventService eventService;
    private final KafkaMessageAdapter messageAdapter;
    private final ObjectMapper objectMapper;

    @KafkaListener(
        topics = "task.events",
        groupId = "notification-group",
        containerFactory = "kafkaListenerContainerFactory",
        errorHandler = "kafkaErrorHandler"
    )
    public void consumeTaskEvent(String message) {
        log.info("📨 Получено сообщение из Kafka: {}", message);

        try {
            TaskEvent event = messageAdapter.adapt(message);
            
            log.info("📊 Адаптированное событие: {}", event);
            log.info("📊 Тип события: {}", event.eventType());
            log.info("📝 ID задачи: {}", event.taskId());
            log.info("👤 ID сотрудника: {}", event.employeeId());

            eventService.saveEventFromTaskEvent(event);
            log.info("✅ Событие сохранено в БД: {}", event.eventType());
            
        } catch (Exception e) {
            log.error("❌ Ошибка обработки сообщения: {}", e.getMessage(), e);
        }
    }
}