package com.luanferro.notification_api.messaging;

import com.luanferro.notification_api.config.RabbitMQConfig;
import com.luanferro.notification_api.entity.Notification;
import com.luanferro.notification_api.entity.enums.NotificationStatus;
import com.luanferro.notification_api.service.NotificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationService notificationService;

    @Transactional
    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void consume(String notificationId) {

        UUID id = UUID.fromString(notificationId);

        log.info("Mensagem recebida da fila: {}", notificationId);
        Notification notification = notificationService.findById(id);
        
        log.info("Mensagem enviada da fila: {}", notification);
        notificationService.updateStatus(notification, NotificationStatus.SENT);

    }

    @RabbitListener(queues = RabbitMQConfig.DLQ)
    public void consumeDLQ(String notificationId) {
        UUID id = UUID.fromString(notificationId);
        Notification notification = notificationService.findById(id);
        notificationService.updateStatus(notification, NotificationStatus.FAILED);
        log.info("Notificacao movida para DLQ: {}", notification.getId());
    }
}
