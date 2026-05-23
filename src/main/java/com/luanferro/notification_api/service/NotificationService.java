package com.luanferro.notification_api.service;

import com.luanferro.notification_api.entity.enums.NotificationChannel;
import com.luanferro.notification_api.entity.enums.NotificationStatus;
import com.luanferro.notification_api.messaging.NotificationProducer;
import com.luanferro.notification_api.dto.NotificationRequest;
import com.luanferro.notification_api.entity.Notification;
import com.luanferro.notification_api.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationProducer notificationProducer;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[1-9]\\d{1,14}$");


    public Notification save(NotificationRequest notificationRequest) {

        validateRecipient(notificationRequest);

        Notification newNotification = new Notification();

        newNotification.setChannel(notificationRequest.channel());
        newNotification.setMessage(notificationRequest.message());
        newNotification.setRecipient(notificationRequest.recipient());
        newNotification.setPriority(notificationRequest.priority());

        notificationRepository.save(newNotification);

        notificationProducer.send(newNotification);

        return newNotification;
    }

    public Notification findById(UUID id) {
        return notificationRepository.findById(id).orElseThrow(
                () -> new RuntimeException("notificacao nao encontrada"));
    }

    @Transactional
    public Notification updateStatus(Notification notification, NotificationStatus status) {
        notification.setStatus(status);
        return notificationRepository.save(notification);
    }

    private void validateRecipient(NotificationRequest req) {
        String recipient = req.recipient();

        switch (req.channel()) {
            case EMAIL -> {
                if(!EMAIL_PATTERN.matcher(recipient).matches()){
                    throw new IllegalArgumentException("Endereço de email inválido: " + recipient);
                }
            }
            case SMS -> {
                if(!PHONE_PATTERN.matcher(recipient).matches()){
                    throw new IllegalArgumentException("Número de telefone inválido: " + recipient);
                }
            }
            default -> {

            }
        }
    }
}
