package com.contraflow.cms.kafka;

/**
 * Used as the Kafka message KEY on the "emails" topic so a single consumer
 * can switch on the key to decide which email to send.
 */
public enum EmailType {
    TENANT_CREATED,
    ADMIN_CREATED,
    PROPOSAL_CREATED
}
