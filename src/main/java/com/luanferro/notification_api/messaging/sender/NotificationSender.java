package com.luanferro.notification_api.messaging.sender;

import com.luanferro.notification_api.entity.Notification;
import com.luanferro.notification_api.entity.enums.NotificationChannel;

public interface NotificationSender {
    void send(Notification notification);
    NotificationChannel getChannel();
}
