package com.contraflow.cms.contract.mapper;

import com.contraflow.cms.contract.dto.TimeLineResponse;
import com.contraflow.cms.contract.entity.ContractApprovalHistory;
import com.contraflow.cms.tenant.entity.TenantUser;
import org.springframework.stereotype.Component;


@Component
public class TimeLineMapper {

    public TimeLineResponse mapToResponse(ContractApprovalHistory contractApprovalHistory) {

        // Accessing the lazy actionBy here is safe: the mapper is called inside
        // ContractService.getContract's @Transactional, so the session is open.
        TenantUser actor = contractApprovalHistory.getActionBy();

        return TimeLineResponse.builder()
                .action(contractApprovalHistory.getAction())
                .actionById(actor != null ? actor.getId() : null)
                .actionByName(actor != null ? (actor.getFirstName() + " " + actor.getLastName()) : null)
                .actionByEmail(actor != null ? actor.getEmail() : null)
                .actionAt(contractApprovalHistory.getActionAt())
                .comment(contractApprovalHistory.getComment())
                .build();
    }
}
