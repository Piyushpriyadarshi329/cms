package com.contraflow.cms.kafka;

import com.contraflow.cms.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Single consumer for the "emails" topic. Routes by the message KEY (EmailType)
 * so one listener handles every email kind (tenant/admin/proposal created, ...).
 *
 * concurrency=3 (in application.properties) -> one listener thread per partition.
 * ack-mode=manual -> we commit the offset only AFTER the email is handled.
 */
@Component
@RequiredArgsConstructor
public class EmailConsumer {

    private static final Logger log = LoggerFactory.getLogger(EmailConsumer.class);

    private final EmailService emailService;

    @KafkaListener(topics = KafkaProducerService.EMAILS_TOPIC, groupId = "email-service",
            autoStartup = "${app.kafka.enabled:true}")
    public void consume(ConsumerRecord<String, Object> record, Acknowledgment ack) {

        String key = record.key();
        Object payload = record.value();

        try {
            if (key == null) {
                log.warn("Email message with no key (partition={}, offset={}) - skipping",
                        record.partition(), record.offset());
                ack.acknowledge();
                return;
            }

            EmailType type = EmailType.valueOf(key);

            switch (type) {
                case TENANT_CREATED   -> handleTenantCreated(payload);
                case ADMIN_CREATED    -> handleAdminCreated(payload);
                case PROPOSAL_CREATED -> handleProposalCreated(payload);
            }

            ack.acknowledge();   // commit only after successful handling

        } catch (IllegalArgumentException e) {
            // unknown key value -> don't block the partition, just skip it
            log.warn("Unknown email type key '{}' - skipping. {}", key, e.getMessage());
            ack.acknowledge();
        } catch (Exception e) {
            // processing failed -> do NOT ack, so it can be reprocessed
            log.error("Failed to process email event key={} : {}", key, e.getMessage(), e);
        }
    }

    private void handleTenantCreated(Object payload) {
        Map<String, Object> data = asMap(payload);
        emailService.sendTemplate(
                str(data.get("email")),
                "Welcome to ContraFlow CMS",
                "email/tenant-created",
                Map.of("name", str(data.get("name")) != null ? str(data.get("name")) : "there"));
    }

    private void handleAdminCreated(Object payload) {
        Map<String, Object> data = asMap(payload);
        emailService.sendTemplate(
                str(data.get("email")),
                "Your admin account is ready",
                "email/admin-created",
                Map.of("name", str(data.get("firstName")) != null ? str(data.get("firstName")) : "Admin"));
    }

    private void handleProposalCreated(Object payload) {
        Map<String, Object> data = asMap(payload);
        String title = str(data.get("title"));
        emailService.sendTemplate(
                str(data.get("email")),
                "New proposal created",
                "email/proposal-created",
                Map.of("title", title != null ? title : ""));
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> asMap(Object payload) {
        return (payload instanceof Map) ? (Map<String, Object>) payload : Map.of();
    }

    private String str(Object v) {
        return v == null ? null : v.toString();
    }
}
