package com.contraflow.cms.ai.controller;


import com.contraflow.cms.ai.dto.ChatRequest;
import com.contraflow.cms.ai.dto.ChatResponse;
import com.contraflow.cms.ai.dto.ContractPdfRequest;
import com.contraflow.cms.ai.dto.ContractPdfResponse;
import com.contraflow.cms.ai.services.ChatServices;
import com.contraflow.cms.ai.services.ContractPdfService;
import com.contraflow.cms.common.dto.ApiResponse;
import com.contraflow.cms.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AIController {

    private final ChatServices chatServices;
    private final ContractPdfService contractPdfService;

    @PostMapping("/chat")
    public ResponseEntity<ApiResponse<ChatResponse>> chat(@RequestBody ChatRequest chatRequest) {
        try {
            ChatResponse chatResponse = new ChatResponse();
            chatResponse.setResult(chatServices.OpenAIChat(chatRequest));
            return ResponseEntity.ok(ApiResponse.success("Chat response generated", chatResponse));
        } catch (Exception e) {
            // Missing key / upstream OpenAI failure -> 503 with a clean message, not a 500 stack.
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(ApiResponse.error("AI service unavailable: " + e.getMessage()));
        }
    }

    @PostMapping("/contract/pdf")
    public ResponseEntity<ApiResponse<ContractPdfResponse>> generateContractPdf(
            @RequestBody ContractPdfRequest request,
            @AuthenticationPrincipal AuthUser authUser) {
        try {
            ContractPdfResponse result =
                    contractPdfService.generate(authUser.getTenantId(), request.getContractId());
            return ResponseEntity.ok(ApiResponse.success("Contract PDF generated", result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(ApiResponse.error("Contract PDF generation failed: " + e.getMessage()));
        }
    }

}
