package ru.edu.taskmanagementsystem.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.edu.taskmanagementsystem.dto.NotificationEventDto;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationServiceClient {
    
    private final RestTemplate restTemplate;
    private final String notificationServiceUrl = "http://notification-service:8083/api/events";
    
    public void notifyTaskCreated(Long taskId, String taskTitle, Long createdBy) {
        // Добавьте ЭТОТ лог
        log.info("🔔 NOTIFICATION CLIENT CALLED with taskId={}, title={}, createdBy={}", 
                 taskId, taskTitle, createdBy);
        
        NotificationEventDto event = NotificationEventDto.builder()
                .eventType("TASK_CREATED")
                .taskId(taskId)
                .employeeId(createdBy)
                .taskTitle(taskTitle)
                .build();
        
        try {
            String url = notificationServiceUrl + "/task/created";
            log.info("📤 Sending to URL: {}", url);
            log.info("📦 Event: {}", event);
            
            restTemplate.postForEntity(url, event, String.class);
            log.info("✅ Notification sent successfully");
        } catch (Exception e) {
            log.error("❌ Failed to send notification: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Отправляет уведомление о назначении задачи
     */
    public void notifyTaskAssigned(Long taskId, String taskTitle, 
                                   Long assignedTo, String employeeName) {
        NotificationEventDto event = NotificationEventDto.builder()
                .eventType("TASK_ASSIGNED")
                .taskId(taskId)
                .employeeId(assignedTo)
                .employeeName(employeeName)
                .taskTitle(taskTitle)
                .build();
        
        try {
            String url = notificationServiceUrl + "/task/assigned";
            restTemplate.postForEntity(url, event, String.class);
            log.info("Notification sent: task assigned - {} to {}", taskId, assignedTo);
        } catch (Exception e) {
            log.error("Failed to send notification: {}", e.getMessage());
        }
    }
}
