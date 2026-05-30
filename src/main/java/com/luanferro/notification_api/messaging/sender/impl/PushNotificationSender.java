package com.luanferro.notification_api.messaging.sender.impl;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.luanferro.notification_api.entity.Notification;
import com.luanferro.notification_api.entity.enums.NotificationChannel;
import com.luanferro.notification_api.messaging.sender.NotificationSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PushNotificationSender implements NotificationSender {

    @Override
    public void send(Notification sendNotification) {

        String deviceToken = sendNotification.getRecipient();

        try {
            Message message = Message.builder()
                    .setToken(deviceToken)
                    .setNotification(com.google.firebase.messaging.Notification.builder()
                            .setTitle("Notificacao")
                            .setBody(sendNotification.getMessage())
                            .build())
                    .build();

            FirebaseMessaging.getInstance().send(message);

            log.info("Enviando push notification");
        } catch (FirebaseMessagingException e) {
            throw new RuntimeException("Erro ao enviar push notification para: " + deviceToken, e);
        }

    }

    @Override
    public NotificationChannel getChannel() {
        return NotificationChannel.PUSH;
    }
}
