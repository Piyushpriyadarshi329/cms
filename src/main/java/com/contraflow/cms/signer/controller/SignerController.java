package com.contraflow.cms.signer.controller;

import com.contraflow.cms.common.dto.ApiResponse;
import com.contraflow.cms.security.AuthUser;
import com.contraflow.cms.signer.dto.SignerFetchResponse;
import com.contraflow.cms.signer.dto.SignerOtpRequest;
import com.contraflow.cms.signer.dto.SignerUpdateRequest;
import com.contraflow.cms.signer.service.SignerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tenant/signer")
@RequiredArgsConstructor
public class SignerController {

    private final SignerService signerService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<SignerFetchResponse>>> getAll(
            @AuthenticationPrincipal AuthUser authUser) {
        List<SignerFetchResponse> signers = signerService.getAllContractsSigner(authUser.getTenantId());
        return ResponseEntity.ok(ApiResponse.success("Signers fetched successfully", signers));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SignerFetchResponse>> getById(
            @PathVariable Long id,
            @AuthenticationPrincipal AuthUser authUser) {
        SignerFetchResponse signer = signerService.getById(authUser.getTenantId(), id);
        return ResponseEntity.ok(ApiResponse.success("Signer fetched successfully", signer));
    }

    @PostMapping("/{id}/otp")
    public ResponseEntity<ApiResponse<String>> sendOtp(
            @PathVariable Long id,
            @AuthenticationPrincipal AuthUser authUser) {
        return ResponseEntity.ok(ApiResponse.success(signerService.sendOtp(authUser.getTenantId(), id)));
    }

    // Returns the otpToken in `data` — caller must carry it into PUT /{id}.
    @PostMapping("/{id}/validate")
    public ResponseEntity<ApiResponse<String>> validateOtp(
            @PathVariable Long id,
            @RequestBody SignerOtpRequest request,
            @AuthenticationPrincipal AuthUser authUser) {
        String otpToken = signerService.validateOtp(authUser.getTenantId(), id, request.getOtp());
        return ResponseEntity.ok(ApiResponse.success("OTP verified", otpToken));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SignerFetchResponse>> update(
            @PathVariable Long id,
            @RequestBody SignerUpdateRequest request,
            @AuthenticationPrincipal AuthUser authUser) {
        SignerFetchResponse updated = signerService.updateRequest(authUser.getTenantId(), id, request);
        return ResponseEntity.ok(ApiResponse.success("Signer updated successfully", updated));
    }
}
