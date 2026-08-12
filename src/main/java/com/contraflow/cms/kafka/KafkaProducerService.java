package com.contraflow.cms.kafka;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    public static final String EMAILS_TOPIC = "emails";

    private static final Logger log = LoggerFactory.getLogger(KafkaProducerService.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    // Turn Kafka off where there's no broker (e.g. Render) via KAFKA_ENABLED=false
    @Value("${app.kafka.enabled:true}")
    private boolean kafkaEnabled;

    /**
     * Publish an email event to the single "emails" topic.
     * Never breaks the caller: if Kafka is disabled or the broker is unreachable,
     * it logs and returns instead of throwing.
     */
    public void sendEmail(EmailType type, Object payload) {
        if (!kafkaEnabled) {
            log.debug("Kafka disabled - skipping {} event", type);
            return;
        }
        try {
            kafkaTemplate.send(EMAILS_TOPIC, type.name(), payload);
        } catch (Exception e) {
            log.warn("Kafka publish failed for {} - continuing without it: {}", type, e.getMessage());
        }
    }
}
