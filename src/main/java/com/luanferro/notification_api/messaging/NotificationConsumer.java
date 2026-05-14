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
import java.util.logging.Logger;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationService notificationService;
    Logger logger = Logger.getLogger(NotificationConsumer.class.getName());

    @Transactional
    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void consume(String notificationId) {

        UUID id = UUID.fromString(notificationId);

        System.out.println("Mensagem recebida da fila: " + notificationId);

        Notification notification = notificationService.findById(id);
        logger.info("Mensagem enviada da fila: " + notification);
        notificationService.updateStatus(notification, NotificationStatus.SENT);

    }
}
