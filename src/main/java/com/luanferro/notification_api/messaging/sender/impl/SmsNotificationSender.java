package com.luanferro.notification_api.messaging.sender.impl;

import com.luanferro.notification_api.entity.Notification;
import com.luanferro.notification_api.entity.enums.NotificationChannel;
import com.luanferro.notification_api.messaging.sender.NotificationSender;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SmsNotificationSender implements NotificationSender {

    @Value("${twilio.account.sid}")
    private String accountSid;
    @Value("${twilio.auth.token}")
    private String authToken;
    @Value("${twilio.phone.number}")
    private String phoneNumber;

    @PostConstruct
    public void init() {
        Twilio.init(accountSid, authToken);
    }

    @Override
    public void send(Notification notification) {

        try {
            Message message = Message.creator(
                    new PhoneNumber(notification.getRecipient()),
                    new PhoneNumber(phoneNumber),
                    notification.getMessage()
            ).create();

            log.info("Enviando SMS para: {}, SID: {}", notification.getRecipient(), message.getSid());

        } catch (Exception e) {
            log.error("Erro ao enviar SMS para: {}", notification.getRecipient(), e);
            throw new RuntimeException("Erro ao enviar SMS para: " + notification.getRecipient(), e);
        }

    }

    @Override
    public NotificationChannel getChannel() {
        return NotificationChannel.SMS;
    }
}
