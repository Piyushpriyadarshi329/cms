package com.contraflow.cms.exception;

/**
 * Thrown when a proposal can no longer be modified because it has been converted
 * to a contract. Maps to HTTP 409 Conflict.
 */
public class ProposalLockedException extends RuntimeException {
    public ProposalLockedException(String message) {
        super(message);
    }
}
