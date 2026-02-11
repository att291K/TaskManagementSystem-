
import ru.edu.notification.model.TaskEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.edu.notification.service.EventService;
import ru.edu.notification.service.KafkaConsumerService;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaConsumerServiceTest {

    @Mock
    private EventService eventService;

    @InjectMocks
    private KafkaConsumerService kafkaConsumerService;

    private TaskEvent testTaskEvent;

    @BeforeEach
    void setUp() {
        testTaskEvent = new TaskEvent(
                "TASK_ASSIGNED",
                123L,
                456L,
                "Test Task",
                "Test Employee",
                Instant.now()
        );
    }

    @Test
    void consumeTaskEvent_ShouldProcessAndSaveEvent() {
        // Given
        doNothing().when(eventService).saveEventFromTaskEvent(any(TaskEvent.class));

        // When
        kafkaConsumerService.consumeTaskEvent(testTaskEvent);

        // Then
        verify(eventService, times(1)).saveEventFromTaskEvent(testTaskEvent);
    }

    @Test
    void consumeTaskEvent_ShouldHandleDifferentEventTypes() {
        // Given
        TaskEvent taskCreatedEvent = new TaskEvent(
                "TASK_CREATED",
                124L,
                457L,
                "New Task",
                "Creator",
                Instant.now()
        );

        TaskEvent statusChangedEvent = new TaskEvent(
                "STATUS_CHANGED",
                125L,
                458L,
                "Updated Task",
                "IN_PROGRESS",
                Instant.now()
        );

        doNothing().when(eventService).saveEventFromTaskEvent(any(TaskEvent.class));

        // When
        kafkaConsumerService.consumeTaskEvent(taskCreatedEvent);
        kafkaConsumerService.consumeTaskEvent(statusChangedEvent);

        // Then
        verify(eventService, times(2)).saveEventFromTaskEvent(any(TaskEvent.class));
    }

    @Test
    void consumeTaskEvent_WhenEventServiceThrowsException_ShouldLogError() {
        // Given
        doThrow(new RuntimeException("Database error"))
                .when(eventService).saveEventFromTaskEvent(any(TaskEvent.class));

        // When
        kafkaConsumerService.consumeTaskEvent(testTaskEvent);

        // Then
        verify(eventService, times(1)).saveEventFromTaskEvent(testTaskEvent);
    }

    @Test
    void consumeTaskEvent_WithNullFields_ShouldHandleGracefully() {
        // Given
        TaskEvent eventWithNulls = new TaskEvent(
                "TASK_CREATED",
                null,
                null,
                null,
                null,
                null
        );

        doNothing().when(eventService).saveEventFromTaskEvent(any(TaskEvent.class));

        // When
        kafkaConsumerService.consumeTaskEvent(eventWithNulls);

        // Then
        verify(eventService, times(1)).saveEventFromTaskEvent(eventWithNulls);
    }
}