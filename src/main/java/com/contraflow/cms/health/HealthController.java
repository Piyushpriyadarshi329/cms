package com.contraflow.cms.health;

import com.contraflow.cms.common.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Lightweight public liveness endpoint for load balancers / Render healthcheck.
 * Full path: /api/v1/health (servlet path is /api/v1).
 */
@RestController
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<Map<String, Object>>> health() {
        return ResponseEntity.ok(
                ApiResponse.success("Service is healthy", Map.of("status", "UP")));
    }
}
