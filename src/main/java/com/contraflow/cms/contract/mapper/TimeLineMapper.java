package com.contraflow.cms.contract.mapper;

import com.contraflow.cms.contract.dto.TimeLineResponse;
import com.contraflow.cms.contract.entity.ApprovalAction;
import com.contraflow.cms.contract.entity.ContractApprovalHistory;
import com.contraflow.cms.tenant.entity.TenantUser;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;


@Component
public class TimeLineMapper {

    private static final ZoneId ZONE = ZoneId.systemDefault();

    public TimeLineResponse mapToResponse(ContractApprovalHistory h) {

        // Accessing the lazy actionBy here is safe: the mapper runs inside
        // ContractService.getContractDetail's @Transactional, so the session is open.
        TenantUser actor = h.getActionBy();
        String actorName = actor != null ? (actor.getFirstName() + " " + actor.getLastName()) : null;
        ApprovalAction action = h.getAction();

        return TimeLineResponse.builder()
                .id(h.getId() != null ? String.valueOf(h.getId()) : null)
                .email(label(action))
                .type(type(action))
                .status("COMPLETED")
                .name(action == ApprovalAction.CREATED ? ("Submitted by " + actorName) : actorName)
                .time(toEpochMilli(h.getCreatedAt()))
                .actionTime(toEpochMilli(h.getActionAt()))
                .owner(actor != null
                        ? TimeLineResponse.Owner.builder()
                        .id(actor.getId())
                        .ownerType("EMPLOYEE")
                        .ownerId(String.valueOf(actor.getId()))
                        .ownerName(actorName)
                        .build()
                        : null)
                .data(List.of())
                .build();
    }

    private Long toEpochMilli(LocalDateTime dt) {
        return dt != null ? dt.atZone(ZONE).toInstant().toEpochMilli() : null;
    }

    /** Node heading shown in the timeline. */
    private String label(ApprovalAction action) {
        if (action == null) return null;
        return switch (action) {
            case CREATED        -> "App Started";
            case APPROVED       -> "Approver";
            case SENT_FOR_ESIGN -> "E-Sign";
            case CLOSED         -> "App Completed";
            case REVERTED       -> "Reverted";
            case DECLINED       -> "Declined";
            case WITHDRAW       -> "Withdrawn";
        };
    }

    /** Workflow type: "start" for the first node, "Completed" for the last, else the approval workflow. */
    private String type(ApprovalAction action) {
        if (action == null) return null;
        return switch (action) {
            case CREATED -> "start";
            case CLOSED  -> "Completed";
            default      -> "VENDOR-CONTRACT";
        };
    }
}
