package com.luanferro.notification_api.messaging.sender.impl;

import com.luanferro.notification_api.entity.Notification;
import com.luanferro.notification_api.entity.enums.NotificationChannel;
import com.luanferro.notification_api.messaging.sender.NotificationSender;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.List;

@Slf4j
@Component
public class EmailNotificationSender implements NotificationSender {

    @Value("${resend.api.key}")
    private String resendApiKey;
    private Resend resend;

    @PostConstruct
    public void init() {
        resend = new Resend(resendApiKey);
    }

    @Override
    public void send(Notification notification) {

        try {
            CreateEmailOptions emailOptions = CreateEmailOptions.builder()
                    .from("onboarding@resend.dev")
                    .to(List.of(notification.getRecipient()))
                    .subject("Notificacao")
                    .html("<p>" + notification.getMessage() + "</p>")
                    .build();

            resend.emails().send(emailOptions);

            log.info("Enviando email para: {}", notification.getRecipient());
        } catch (ResendException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public NotificationChannel getChannel() {
        return NotificationChannel.EMAIL;
    }
}
