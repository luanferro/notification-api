package com.luanferro.notification_api.messaging;

import com.luanferro.notification_api.config.RabbitMQConfig;
import com.luanferro.notification_api.entity.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationProducer {

    private final RabbitTemplate rabbitTemplate;

    public void send(Notification notification) {
        int priority = switch (notification.getPriority()) {
            case HIGH -> 10;
            case MEDIUM -> 5;
            case LOW -> 1;
        };

        MessagePostProcessor messagePostProcessor = message -> {
            message.getMessageProperties().setPriority(priority);
            return message;
        };

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                notification.getId().toString(),
                messagePostProcessor
        );

        System.out.println("Publicado na fila com prioridade: " + priority);
    }
}
