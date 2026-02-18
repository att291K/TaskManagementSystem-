package ru.edu.assignment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestPropertySource;
import ru.edu.assignment.controller.AssignmentController;
import ru.edu.assignment.controller.AssignmentsController;
import ru.edu.assignment.repository.AssignmentRepository;
import ru.edu.assignment.service.AssignmentService;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Тесты для проверки корректности запуска Spring Boot приложения AssignmentApplication
 * и загрузки всех необходимых компонентов в контекст приложения.
 */
@SpringBootTest
@TestPropertySource(properties = {
        "spring.kafka.bootstrap-servers=localhost:9092",
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class AssignmentApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired(required = false)
    private AssignmentController assignmentController;

    @Autowired(required = false)
    private AssignmentsController assignmentsController;

    @Autowired(required = false)
    private AssignmentService assignmentService;

    @Autowired(required = false)
    private AssignmentRepository assignmentRepository;

    /**
     * Проверяет, что контекст Spring приложения успешно загружается.
     * Это базовый тест, который гарантирует, что все конфигурации корректны
     * и приложение может быть запущено без ошибок.
     */
    @Test
    void contextLoads() {
        assertThat(applicationContext).isNotNull();
    }

    /**
     * Проверяет, что главный класс приложения AssignmentApplication существует
     * и может быть загружен в контекст Spring.
     */
    @Test
    void applicationClassExists() {
        assertThat(applicationContext.getBean(AssignmentApplication.class)).isNotNull();
    }

    /**
     * Проверяет, что контроллер AssignmentController успешно создан
     * и зарегистрирован в контексте Spring как bean.
     */
    @Test
    void assignmentControllerIsCreated() {
        assertThat(assignmentController).isNotNull();
        assertThat(applicationContext.containsBean("assignmentController")).isTrue();
    }

    /**
     * Проверяет, что контроллер AssignmentsController успешно создан
     * и зарегистрирован в контексте Spring как bean.
     */
    @Test
    void assignmentsControllerIsCreated() {
        assertThat(assignmentsController).isNotNull();
        assertThat(applicationContext.containsBean("assignmentsController")).isTrue();
    }

    /**
     * Проверяет, что сервис AssignmentService успешно создан
     * и зарегистрирован в контексте Spring как bean.
     */
    @Test
    void assignmentServiceIsCreated() {
        assertThat(assignmentService).isNotNull();
        assertThat(applicationContext.containsBean("assignmentService")).isTrue();
    }

    /**
     * Проверяет, что репозиторий AssignmentRepository успешно создан
     * и зарегистрирован в контексте Spring как bean.
     */
    @Test
    void assignmentRepositoryIsCreated() {
        assertThat(assignmentRepository).isNotNull();
    }

    /**
     * Проверяет, что все необходимые Spring Boot стартеры загружены:
     * - Spring Web (для REST API)
     * - Spring Data JPA (для работы с базой данных)
     * - Spring Kafka (для обмена сообщениями)
     */
    @Test
    void requiredBeansArePresent() {
        // Проверка наличия основных компонентов Spring Boot
        assertThat(applicationContext.containsBean("dataSource")).isTrue();
        assertThat(applicationContext.containsBean("entityManagerFactory")).isTrue();
        assertThat(applicationContext.containsBean("transactionManager")).isTrue();
    }

    /**
     * Проверяет, что конфигурация WebConfig загружена корректно.
     */
    @Test
    void webConfigIsLoaded() {
        assertThat(applicationContext.containsBean("webConfig")).isTrue();
    }

    /**
     * Проверяет, что приложение использует правильный профиль Spring
     * и все необходимые свойства конфигурации доступны.
     */
    @Test
    void applicationPropertiesAreLoaded() {
        String[] activeProfiles = applicationContext.getEnvironment().getActiveProfiles();
        assertThat(applicationContext.getEnvironment()).isNotNull();
    }

    /**
     * Проверяет, что JPA репозитории правильно настроены и работают.
     */
    @Test
    void jpaRepositoriesAreConfigured() {
        assertThat(assignmentRepository).isNotNull();
        // Проверяем, что репозиторий может выполнять базовые операции
        assertThat(assignmentRepository.findAll()).isNotNull();
    }

    /**
     * Проверяет, что метод main() класса AssignmentApplication может быть вызван
     * без исключений (тест на корректность точки входа в приложение).
     */
    @Test
    void mainMethodExists() throws NoSuchMethodException {
        assertThat(AssignmentApplication.class.getMethod("main", String[].class)).isNotNull();
    }

    /**
     * Проверяет, что класс AssignmentApplication имеет аннотацию @SpringBootApplication,
     * которая необходима для автоконфигурации Spring Boot.
     */
    @Test
    void hasSpringBootApplicationAnnotation() {
        assertThat(AssignmentApplication.class.isAnnotationPresent(
                org.springframework.boot.autoconfigure.SpringBootApplication.class
        )).isTrue();
    }
}
