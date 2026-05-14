package com.luanferro.notification_api.messaging;

import com.luanferro.notification_api.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationConsumer {

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void consume(String notificationId) {
        System.out.println("Mensagem recebida da fila: " + notificationId);
    }
}
