package com.contraflow.cms.exception;

/**
 * Thrown when a contract approval action is attempted from a status that does not
 * allow it (e.g. finance-approving a contract still pending manager approval).
 * Maps to HTTP 409 Conflict.
 */
public class InvalidContractStateException extends RuntimeException {
    public InvalidContractStateException(String message) {
        super(message);
    }
}
