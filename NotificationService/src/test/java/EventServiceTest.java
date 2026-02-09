
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.edu.notification.model.EventLog;
import ru.edu.notification.repository.EventLogRepository;
import ru.edu.notification.service.EventService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventLogRepository eventLogRepository;

    @InjectMocks
    private EventService eventService;

    private EventLog testEvent1;
    private EventLog testEvent2;

    @BeforeEach
    void setUp() {
        testEvent1 = new EventLog(
                1L,
                "TASK_CREATED",
                123L,
                456L,
                "Создана задача 'Тестовая задача'",
                LocalDateTime.of(2024, 1, 15, 10, 30, 0)
        );

        testEvent2 = new EventLog(
                2L,
                "TASK_ASSIGNED",
                123L,
                789L,
                "Задача назначена на сотрудника",
                LocalDateTime.of(2024, 1, 15, 11, 0, 0)
        );
    }

    @Test
    void getAllEvents_ShouldReturnPageOfEvents() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<EventLog> expectedPage = new PageImpl<>(List.of(testEvent1, testEvent2));
        when(eventLogRepository.findAll(pageable)).thenReturn(expectedPage);

        // When
        Page<EventLog> result = eventService.getAllEvents(pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).getEventType()).isEqualTo("TASK_CREATED");
        assertThat(result.getContent().get(1).getEventType()).isEqualTo("TASK_ASSIGNED");

        verify(eventLogRepository).findAll(pageable);
    }

    @Test
    void getEventsByTaskId_ShouldReturnEventsForTask() {
        // Given
        when(eventLogRepository.findByTaskId(123L)).thenReturn(List.of(testEvent1, testEvent2));

        // When
        List<EventLog> result = eventService.getEventsByTaskId(123L);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).allMatch(event -> event.getTaskId().equals(123L));

        verify(eventLogRepository).findByTaskId(123L);
    }

    @Test
    void getEventsByTaskId_WhenNoEvents_ShouldReturnEmptyList() {
        // Given
        when(eventLogRepository.findByTaskId(999L)).thenReturn(List.of());

        // When
        List<EventLog> result = eventService.getEventsByTaskId(999L);

        // Then
        assertThat(result).isEmpty();
        verify(eventLogRepository).findByTaskId(999L);
    }

    @Test
    void getEventsByEmployeeId_ShouldReturnEventsForEmployee() {
        // Given
        when(eventLogRepository.findByEmployeeId(456L)).thenReturn(List.of(testEvent1));

        // When
        List<EventLog> result = eventService.getEventsByEmployeeId(456L);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEmployeeId()).isEqualTo(456L);

        verify(eventLogRepository).findByEmployeeId(456L);
    }



    @Test
    void getRecentEvents_ShouldReturnLimitedNumberOfEvents() {
        // Given
        Pageable pageable = PageRequest.of(0, 5);
        when(eventLogRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(testEvent1, testEvent2)));

        // When
        List<EventLog> result = eventService.getRecentEvents(5);

        // Then
        assertThat(result).hasSize(2);
        verify(eventLogRepository).findAll(pageable);
    }

    @Test
    void getTotalEvents_ShouldReturnCount() {
        // Given
        when(eventLogRepository.count()).thenReturn(10L);

        // When
        long result = eventService.getTotalEvents();

        // Then
        assertThat(result).isEqualTo(10L);
        verify(eventLogRepository).count();
    }

   }