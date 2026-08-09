package com.contraflow.cms.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    public static final String EMAILS_TOPIC = "emails";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * Publish an email event to the single "emails" topic.
     * KEY   = email type (TENANT_CREATED / ADMIN_CREATED / PROPOSAL_CREATED, ...)
     * VALUE = the payload (JSON) the consumer needs to build that email.
     * The consumer switches on the key to pick the right template/recipient.
     */
    public void sendEmail(EmailType type, Object payload) {
        kafkaTemplate.send(EMAILS_TOPIC, type.name(), payload);
    }
}
