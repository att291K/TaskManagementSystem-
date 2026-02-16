
import org.junit.jupiter.api.Test;
import ru.edu.notification.dto.ApiResponse;

import static org.assertj.core.api.Assertions.assertThat;

class ApiResponseTest {

    @Test
    void success_ShouldCreateSuccessResponse() {
        // When
        ApiResponse<String> response = ApiResponse.success("Test message", "data");

        // Then
        assertThat(response.getStatus()).isEqualTo("success");
        assertThat(response.getMessage()).isEqualTo("Test message");
        assertThat(response.getData()).isEqualTo("data");
        assertThat(response.getCode()).isEqualTo(200);
        assertThat(response.getTimestamp()).isNotNull();
    }

    @Test
    void success_WithoutMessage_ShouldUseDefaultMessage() {
        // When
        ApiResponse<String> response = ApiResponse.success("data");

        // Then
        assertThat(response.getStatus()).isEqualTo("success");
        assertThat(response.getMessage()).isEqualTo("Operation completed successfully");
        assertThat(response.getData()).isEqualTo("data");
        assertThat(response.getCode()).isEqualTo(200);
    }

    @Test
    void error_ShouldCreateErrorResponse() {
        // When
        ApiResponse<String> response = ApiResponse.error("Error occurred", 500);

        // Then
        assertThat(response.getStatus()).isEqualTo("error");
        assertThat(response.getMessage()).isEqualTo("Error occurred");
        assertThat(response.getData()).isNull();
        assertThat(response.getCode()).isEqualTo(500);
    }

    @Test
    void error_WithData_ShouldCreateErrorResponseWithData() {
        // When
        ApiResponse<String> response = ApiResponse.error("Validation failed", "error details", 400);

        // Then
        assertThat(response.getStatus()).isEqualTo("error");
        assertThat(response.getMessage()).isEqualTo("Validation failed");
        assertThat(response.getData()).isEqualTo("error details");
        assertThat(response.getCode()).isEqualTo(400);
    }

    @Test
    void allArgsConstructor_ShouldCreateResponseWithAllFields() {
        // When
        ApiResponse<String> response = new ApiResponse<>(
                "partial",
                "Partial success",
                "partial data",
                java.time.LocalDateTime.now(),
                206
        );

        // Then
        assertThat(response.getStatus()).isEqualTo("partial");
        assertThat(response.getMessage()).isEqualTo("Partial success");
        assertThat(response.getData()).isEqualTo("partial data");
        assertThat(response.getCode()).isEqualTo(206);
    }

    @Test
    void gettersAndSetters_ShouldWorkCorrectly() {
        // Given
        ApiResponse<String> response = new ApiResponse<>();

        // When
        response.setStatus("custom");
        response.setMessage("Custom message");
        response.setData("custom data");
        response.setCode(201);
        response.setTimestamp(java.time.LocalDateTime.now());

        // Then
        assertThat(response.getStatus()).isEqualTo("custom");
        assertThat(response.getMessage()).isEqualTo("Custom message");
        assertThat(response.getData()).isEqualTo("custom data");
        assertThat(response.getCode()).isEqualTo(201);
        assertThat(response.getTimestamp()).isNotNull();
    }
}