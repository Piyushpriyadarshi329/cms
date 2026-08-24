package com.contraflow.cms.signer.service;

import com.contraflow.cms.contract.entity.Contract;
import com.contraflow.cms.contract.repository.ContractRepository;
import com.contraflow.cms.exception.InvalidTokenException;
import com.contraflow.cms.exception.ResourceNotFoundException;
import com.contraflow.cms.signer.Enum.ESign_Status;
import com.contraflow.cms.signer.dto.SignerFetchResponse;
import com.contraflow.cms.signer.dto.SignerUpdateRequest;
import com.contraflow.cms.signer.entity.Signer;
import com.contraflow.cms.signer.repository.SignerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class SignerService {
    @Autowired
    private SignerRepository signerRepository;
    @Autowired
    private ContractRepository contractRepository;

    private static final SecureRandom secureRandom = new SecureRandom();

    public String generateOtp() {
        int otp = secureRandom.nextInt(1_000_000);
        return String.format("%06d", otp);
    }


  public List<SignerFetchResponse> getAllContractsSigner(Long tenantId){
      return signerRepository.findByTenantIdAndDeletedFalse(tenantId)
              .stream()
              .map(this::toResponse)
              .toList();
  }

  public SignerFetchResponse getById(Long tenantId, Long id){
      Signer signer = signerRepository.findByTenantIdAndIdAndDeletedFalse(tenantId, id)
              .orElseThrow(() -> new ResourceNotFoundException("Signer not found with id : " + id));
      return toResponse(signer);
  }

  public String createRequest(Long tenantId, UUID contractId){
    Contract contract = contractRepository.findByIdAndDeletedFalse(contractId)
            .orElseThrow(() -> new ResourceNotFoundException("Contract not found with id : " + contractId));

    Signer signer = Signer.builder()
            .tenantId(tenantId)
            .contract(contract)
            .status(ESign_Status.INTERNAL_SIGN_REQUEST)
            .build();

    signerRepository.save(signer);
    return "Request Sent for ESign Successfully";
  }

  public String sendOtp(Long tenantId, Long id){
    Signer signer = signerRepository.findByTenantIdAndIdAndDeletedFalse(tenantId, id)
            .orElseThrow(() -> new ResourceNotFoundException("Signer not found with id : " + id));

    signer.setOtp(generateOtp());
    signer.setOtpExpiresAt(LocalDateTime.now().plusMinutes(5));
    signerRepository.save(signer);
    return "OTP sent successfully";
  }

  // Verifies the OTP and, on success, issues a short-lived, single-use otpToken.
  // The caller must carry this token into updateRequest — without it, the sign
  // fields/status can't be changed, so no one can complete a sign step without OTP.
  public String validateOtp(Long tenantId, Long id, String otp){
    Signer signer = signerRepository.findByTenantIdAndIdAndDeletedFalse(tenantId, id)
            .orElseThrow(() -> new ResourceNotFoundException("Signer not found with id : " + id));

    if (signer.getOtp() == null
            || !signer.getOtp().equals(otp)
            || signer.getOtpExpiresAt() == null
            || signer.getOtpExpiresAt().isBefore(LocalDateTime.now())) {
      throw new InvalidTokenException("Invalid or expired OTP");
    }

    String otpToken = UUID.randomUUID().toString();
    signer.setOtpToken(otpToken);
    signer.setOtpTokenExpiresAt(LocalDateTime.now().plusMinutes(15));

    // OTP is single-use — clear it now so it can't be replayed.
    signer.setOtp(null);
    signer.setOtpExpiresAt(null);

    signerRepository.save(signer);
    return otpToken;
  }

  public SignerFetchResponse updateRequest(Long tenantId, Long id, SignerUpdateRequest request){
    Signer signer = signerRepository.findByTenantIdAndIdAndDeletedFalse(tenantId, id)
            .orElseThrow(() -> new ResourceNotFoundException("Signer not found with id : " + id));

    if (signer.getOtpToken() == null
            || !signer.getOtpToken().equals(request.getOtpToken())
            || signer.getOtpTokenExpiresAt() == null
            || signer.getOtpTokenExpiresAt().isBefore(LocalDateTime.now())) {
      throw new InvalidTokenException("Invalid or expired otpToken");
    }

    signer.setContractUrl(request.getContractUrl());
    signer.setInternalSignBy(request.getInternalSignBy());
    signer.setInternalSignAt(request.getInternalSignAt());
    signer.setInternalSignUrl(request.getInternalSignUrl());
    signer.setClientSignBy(request.getClientSignBy());
    signer.setClientSignAt(request.getClientSignAt());
    signer.setClientSignUrl(request.getClientSignUrl());
    signer.setESignUrl(request.getESignUrl());
    signer.setStatus(request.getESignStatus());

    signer.setOtpToken(null);
    signer.setOtpTokenExpiresAt(null);

    Signer saved = signerRepository.save(signer);
    return toResponse(saved);
  }

  private SignerFetchResponse toResponse(Signer signer){
    return SignerFetchResponse.builder()
            .id(signer.getId())
            .tenantId(signer.getTenantId())
            .contractId(signer.getContract() != null ? signer.getContract().getId() : null)
            .contractUrl(signer.getContractUrl())
            .internalSignBy(signer.getInternalSignBy())
            .internalSignAt(signer.getInternalSignAt())
            .internalSignUrl(signer.getInternalSignUrl())
            .clientSignBy(signer.getClientSignBy())
            .clientSignAt(signer.getClientSignAt())
            .clientSignUrl(signer.getClientSignUrl())
            .eSignUrl(signer.getESignUrl())
            .eSignStatus(signer.getStatus())
            .build();
  }

}
