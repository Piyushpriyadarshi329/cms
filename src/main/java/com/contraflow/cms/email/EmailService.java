package com.contraflow.cms.email;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Sends HTML emails via SMTP (Spring Mail / JavaMailSender).
 * Configured in application.properties (spring.mail.*).
 */
@Service
@RequiredArgsConstructor
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;
    private final ITemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String from;

    /**
     * Render a Thymeleaf template (from templates/) with the given variables and send it as HTML.
     * @param templateName e.g. "email/tenant-created"
     */
    public void sendTemplate(String to, String subject, String templateName, Map<String, Object> model) {
        Context context = new Context();
        if (model != null) {
            context.setVariables(model);
        }
        String html = templateEngine.process(templateName, context);
        send(to, subject, html, true);
    }

    /**
     * @param html when true, {@code body} is sent as HTML; otherwise plain text.
     */
    public void send(String to, String subject, String body, boolean html) {
        if (from == null || from.isBlank()) {
            log.warn("Mail not configured (set MAIL_USERNAME / MAIL_PASSWORD) - skipping email '{}'", subject);
            return;
        }
        if (to == null || to.isBlank()) {
            log.warn("Skipping email '{}' - no recipient", subject);
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, StandardCharsets.UTF_8.name());
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, html);   // html=true -> content type text/html

            mailSender.send(message);
            log.info("Email sent to {} (subject: {})", to, subject);
        } catch (Exception e) {
            // wrap so the caller/consumer can decide what to do
            throw new RuntimeException("Failed to send email to " + to + ": " + e.getMessage(), e);
        }
    }

    /** Convenience overload — sends HTML. */
    public void sendHtml(String to, String subject, String htmlBody) {
        send(to, subject, htmlBody, true);
    }
}
