package ru.edu.notification.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.KafkaListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;

@Slf4j
@Configuration
public class KafkaErrorHandlerConfig {

    @Bean
    public KafkaListenerErrorHandler kafkaErrorHandler() {
        return (message, exception) -> {
            log.error("❌ Ошибка обработки сообщения Kafka: {}", exception.getMessage());
            log.error("Сообщение: {}", message.getPayload());
            
            // Логируем стек ошибки
            if (exception.getCause() != null) {
                log.error("Причина: {}", exception.getCause().getMessage());
            }
            
            // Можно вернуть какое-то значение или null
            return null;
        };
    }
}