
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.edu.notification.controller.EventController;
import ru.edu.notification.dto.ApiResponse;
import ru.edu.notification.dto.EventDTO;
import ru.edu.notification.model.EventLog;
import ru.edu.notification.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class EventControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private EventService eventService;

    @InjectMocks
    private EventController eventController;

    private EventLog testEvent;
    private EventDTO testEventDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(eventController).build();
        objectMapper = new ObjectMapper();

        testEvent = new EventLog(
                1L,
                "TASK_CREATED",
                123L,
                456L,
                "Создана задача 'Тестовая задача'",
                LocalDateTime.of(2024, 1, 15, 10, 30, 0)
        );

        testEventDTO = EventDTO.fromEntity(testEvent);
    }

    @Test
    void getAllEvents_ShouldReturnPaginatedEvents() throws Exception {
        // Given
        Page<EventLog> eventPage = new PageImpl<>(List.of(testEvent));
        when(eventService.getAllEvents(any(Pageable.class))).thenReturn(eventPage);

        // When & Then
        mockMvc.perform(get("/api/events")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Events retrieved successfully"))
                .andExpect(jsonPath("$.data.events[0].id").value(1))
                .andExpect(jsonPath("$.data.events[0].eventType").value("TASK_CREATED"))
                .andExpect(jsonPath("$.data.totalPages").value(1))
                .andExpect(jsonPath("$.data.totalItems").value(1));
    }

    @Test
    void getAllEvents_WithDefaultPagination_ShouldWork() throws Exception {
        // Given
        Page<EventLog> eventPage = new PageImpl<>(List.of(testEvent));
        when(eventService.getAllEvents(any(Pageable.class))).thenReturn(eventPage);

        // When & Then
        mockMvc.perform(get("/api/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"));
    }

    @Test
    void getEventsByTask_ShouldReturnEventsForTask() throws Exception {
        // Given
        when(eventService.getEventsByTaskId(123L)).thenReturn(List.of(testEvent));

        // When & Then
        mockMvc.perform(get("/api/events/task/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Task events retrieved"))
                .andExpect(jsonPath("$.data[0].taskId").value(123))
                .andExpect(jsonPath("$.data[0].eventType").value("TASK_CREATED"));
    }

    @Test
    void getEventsByTask_WhenNoEvents_ShouldReturnEmptyList() throws Exception {
        // Given
        when(eventService.getEventsByTaskId(999L)).thenReturn(List.of());

        // When & Then
        mockMvc.perform(get("/api/events/task/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(0));
    }

    @Test
    void getEventsByEmployee_ShouldReturnEventsForEmployee() throws Exception {
        // Given
        when(eventService.getEventsByEmployeeId(456L)).thenReturn(List.of(testEvent));

        // When & Then
        mockMvc.perform(get("/api/events/employee/456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].employeeId").value(456));
    }

    @Test
    void getRecentEvents_ShouldReturnLimitedEvents() throws Exception {
        // Given
        when(eventService.getRecentEvents(10)).thenReturn(List.of(testEvent));

        // When & Then
        mockMvc.perform(get("/api/events/recent")
                        .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Recent events retrieved"))
                .andExpect(jsonPath("$.data.length()").value(1));
    }

    @Test
    void getRecentEvents_WithDefaultLimit_ShouldWork() throws Exception {
        // Given
        when(eventService.getRecentEvents(10)).thenReturn(List.of(testEvent));

        // When & Then
        mockMvc.perform(get("/api/events/recent"))
                .andExpect(status().isOk());
    }

    @Test
    void getStats_ShouldReturnStatistics() throws Exception {
        // Given
        when(eventService.getTotalEvents()).thenReturn(5L);

        // When & Then
        mockMvc.perform(get("/api/events/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalEvents").value(5))
                .andExpect(jsonPath("$.data.service").value("Notification Service"))
                .andExpect(jsonPath("$.data.status").value("running"));
    }

    @Test
    void healthCheck_ShouldReturnHealthStatus() throws Exception {
        // Given
        when(eventService.getTotalEvents()).thenReturn(5L);

        // When & Then
        mockMvc.perform(get("/api/events/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("UP"))
                .andExpect(jsonPath("$.data.database").value("H2"))
                .andExpect(jsonPath("$.data.kafka").value("connected"));
    }

    @Test
    void createTestEvent_ShouldReturnTestEvent() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/events/test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Test event created"))
                .andExpect(jsonPath("$.data.eventType").value("TEST_EVENT"))
                .andExpect(jsonPath("$.data.taskId").value(999));
    }

    @Test
    void getEventsByTask_WithInvalidId_ShouldHandleGracefully() throws Exception {
        // Given
        when(eventService.getEventsByTaskId(-1L)).thenReturn(List.of());

        // When & Then
        mockMvc.perform(get("/api/events/task/-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void getEvents_WithInvalidPageParameters_ShouldHandleGracefully() throws Exception {
        // Given
        when(eventService.getAllEvents(any(Pageable.class))).thenReturn(Page.empty());

        // When & Then
        mockMvc.perform(get("/api/events")
                        .param("page", "-1")
                        .param("size", "-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.events").isArray());
    }
}