package com.contraflow.cms.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Standard envelope for every API response (success and error).
 * Null fields (data / errors) are omitted from the JSON so payloads stay clean.
 *
 * <pre>
 * { "success": true,  "message": "...", "data": {...},  "timestamp": "..." }
 * { "success": false, "message": "...", "errors": {...}, "timestamp": "..." }
 * </pre>
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;

    /** Optional detail for error responses (e.g. field -> validation message). */
    private Object errors;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> success(T data) {
        return success("Success", data);
    }

    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .build();
    }

    public static <T> ApiResponse<T> error(String message, Object errors) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .errors(errors)
                .build();
    }
}
