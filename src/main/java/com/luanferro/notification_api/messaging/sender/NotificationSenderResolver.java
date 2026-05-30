package com.luanferro.notification_api.messaging.sender;

import com.luanferro.notification_api.entity.enums.NotificationChannel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class NotificationSenderResolver {

    private final Map<NotificationChannel, NotificationSender> senders;

    public NotificationSenderResolver(List<NotificationSender> senderList) {
        senders = senderList.stream()
                .collect(Collectors.toMap(
                        NotificationSender::getChannel,
                        sender -> sender
                ));
    }

    public NotificationSender resolve(NotificationChannel channel) {
        NotificationSender sender = senders.get(channel);
        if(sender == null) {
            throw new IllegalArgumentException("No sender found for channel: " + channel);
        }
        return sender;
    }
}
