package util;

import dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

// utility class for creating standarized REST API responses used in rest controllers

public class ResponseUtils {

    private ResponseUtils() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    // ===== SUCCESS RESPONSES =====

    public static <T> ResponseEntity<ApiResponse<T>> ok(String message, T data) {
        return ResponseEntity.ok(new ApiResponse<>(true, message, data));
    }
    
    public static <T> ResponseEntity<ApiResponse<T>> ok(String message) {
        return ResponseEntity.ok(new ApiResponse<>(true, message));
    }
    
    public static <T> ResponseEntity<ApiResponse<T>> created(String message, T data) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new ApiResponse<>(true, message, data));
    }
    
    public static <T> ResponseEntity<ApiResponse<T>> created(String message) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new ApiResponse<>(true, message));
    }

    // ===== ERROR RESPONSES =====

    public static <T> ResponseEntity<ApiResponse<T>> badRequest(String message) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ApiResponse<>(false, message, null));
    }

    public static <T> ResponseEntity<ApiResponse<T>> unauthorized(String message) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(new ApiResponse<>(false, message, null));
    }
    
    public static <T> ResponseEntity<ApiResponse<T>> forbidden(String message) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(new ApiResponse<>(false, message, null));
    }
    
    public static <T> ResponseEntity<ApiResponse<T>> notFound(String message) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiResponse<>(false, message, null));
    }
    
    public static <T> ResponseEntity<ApiResponse<T>> conflict(String message) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(new ApiResponse<>(false, message, null));
    }
    
    public static <T> ResponseEntity<ApiResponse<T>> internalError(String message) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ApiResponse<>(false, message, null));
    }
}