package com.contraflow.cms.proposal.validation;

import com.contraflow.cms.proposal.dto.ProposalDiscussionRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ProposalDiscussionValidator
        implements ConstraintValidator<ValidProposalDiscussion, ProposalDiscussionRequest> {

    @Override
    public boolean isValid(
            ProposalDiscussionRequest request,
            ConstraintValidatorContext context) {

        if (request == null) {
            return true;
        }

        // Terms are not changed → these fields are optional
        if (!Boolean.TRUE.equals(request.getTermChanged())) {
            return true;
        }

        // Terms changed → all these fields are mandatory
        return request.getProposalStartDate() != null
                && request.getProposalAmount() != null
                && request.getBilling() != null
                && request.getStartDate() != null
                && request.getEndDate() != null;
    }
}