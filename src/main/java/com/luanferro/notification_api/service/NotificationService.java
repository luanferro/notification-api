package com.luanferro.notification_api.service;

import com.luanferro.notification_api.messaging.NotificationProducer;
import com.luanferro.notification_api.dto.NotificationRequest;
import com.luanferro.notification_api.entity.Notification;
import com.luanferro.notification_api.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
