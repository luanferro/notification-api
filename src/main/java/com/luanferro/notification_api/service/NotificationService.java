package com.luanferro.notification_api.service;

import com.luanferro.notification_api.entity.enums.NotificationStatus;
import com.luanferro.notification_api.messaging.NotificationProducer;
import com.luanferro.notification_api.dto.NotificationRequest;
import com.luanferro.notification_api.entity.Notification;
import com.luanferro.notification_api.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationProducer notificationProducer;

    public Notification save(NotificationRequest notificationRequest) {
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
    public void updateStatus(Notification notification, NotificationStatus status) {
        notification.setStatus(status);
        notificationRepository.save(notification);
    }
}
