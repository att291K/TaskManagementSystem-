
import ru.edu.notification.dto.EventDTO;
import ru.edu.notification.model.EventLog;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class EventDTOTest {

    @Test
    void fromEntity_ShouldConvertEventLogToDTO() {
        // Given
        EventLog eventLog = new EventLog(
                1L,
                "TASK_CREATED",
                123L,
                456L,
                "Test message",
                LocalDateTime.of(2024, 1, 15, 10, 30, 0)
        );

        // When
        EventDTO dto = EventDTO.fromEntity(eventLog);

        // Then
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getEventType()).isEqualTo("TASK_CREATED");
        assertThat(dto.getTaskId()).isEqualTo(123L);
        assertThat(dto.getEmployeeId()).isEqualTo(456L);
        assertThat(dto.getMessage()).isEqualTo("Test message");
        assertThat(dto.getCreatedAt()).isEqualTo(eventLog.getCreatedAt());
    }

    @Test
    void fromEntity_WithNullFields_ShouldHandleGracefully() {
        // Given
        EventLog eventLog = new EventLog(
                1L,
                "TEST_EVENT",
                null,
                null,
                "Test message",
                LocalDateTime.now()
        );

        // When
        EventDTO dto = EventDTO.fromEntity(eventLog);

        // Then
        assertThat(dto.getTaskId()).isNull();
        assertThat(dto.getEmployeeId()).isNull();
        assertThat(dto.getMessage()).isEqualTo("Test message");
    }

    @Test
    void allArgsConstructor_ShouldCreateDTOWithAllFields() {
        // When
        EventDTO dto = new EventDTO(
                1L,
                "TASK_CREATED",
                123L,
                456L,
                "Test message",
                LocalDateTime.now()
        );

        // Then
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getEventType()).isEqualTo("TASK_CREATED");
        assertThat(dto.getTaskId()).isEqualTo(123L);
        assertThat(dto.getEmployeeId()).isEqualTo(456L);
        assertThat(dto.getMessage()).isEqualTo("Test message");
        assertThat(dto.getCreatedAt()).isNotNull();
    }

    @Test
    void gettersAndSetters_ShouldWorkCorrectly() {
        // Given
        EventDTO dto = new EventDTO();
        LocalDateTime now = LocalDateTime.now();

        // When
        dto.setId(1L);
        dto.setEventType("TASK_ASSIGNED");
        dto.setTaskId(123L);
        dto.setEmployeeId(456L);
        dto.setMessage("Task assigned");
        dto.setCreatedAt(now);

        // Then
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getEventType()).isEqualTo("TASK_ASSIGNED");
        assertThat(dto.getTaskId()).isEqualTo(123L);
        assertThat(dto.getEmployeeId()).isEqualTo(456L);
        assertThat(dto.getMessage()).isEqualTo("Task assigned");
        assertThat(dto.getCreatedAt()).isEqualTo(now);
    }

    @Test
    void toString_ShouldReturnStringRepresentation() {
        // Given
        EventDTO dto = new EventDTO(
                1L,
                "TASK_CREATED",
                123L,
                456L,
                "Test message",
                LocalDateTime.of(2024, 1, 15, 10, 30, 0)
        );

        // When
        String string = dto.toString();

        // Then
        assertThat(string).contains("EventDTO");
        assertThat(string).contains("id=1");
        assertThat(string).contains("eventType=TASK_CREATED");
    }
}