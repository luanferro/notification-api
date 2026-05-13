package com.luanferro.notification_api.service;

import com.luanferro.notification_api.dto.NotificationRequest;
import com.luanferro.notification_api.entity.Notification;
import com.luanferro.notification_api.repository.NotificationRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public Notification save(NotificationRequest notificationRequest) {
        Notification notification = new Notification();

        notification.setChannel(notificationRequest.channel());
        notification.setMessage(notificationRequest.message());
        notification.setRecipient(notificationRequest.recipient());
        notification.setPriority(notificationRequest.priority());

        return notificationRepository.save(notification);
    }
}
