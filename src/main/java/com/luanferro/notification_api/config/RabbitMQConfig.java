package com.luanferro.notification_api.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE = "notification.queue";
    public static final String EXCHANGE = "notification.exchange";
    public static final String ROUTING_KEY = "notification.routingKey";

    public static final String DLQ = "notification.dlq";
    public static final String DLQ_EXCHANGE = "notification.dlq.exchange";
    public static final String DLQ_ROUTING_KEY = "notification.dlq.routingKey";

    @Bean
    public Queue queue(){
        return  QueueBuilder
                    .durable(QUEUE)
                    .withArgument("x-max-priority", 10)
                    .withArgument("x-dead-letter-exchange", DLQ_EXCHANGE)
                    .withArgument("x-dead-letter-routing-key", DLQ_ROUTING_KEY)
                    .build();
    }

    @Bean
    public Queue queueDLQ(){
        return QueueBuilder
                    .durable(DLQ)
                    .build();
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public DirectExchange exchangeDLQ() {
        return new DirectExchange(DLQ_EXCHANGE);
    }

    @Bean
    public Binding binding(
            @Qualifier("queue") Queue queue,
            @Qualifier("exchange") DirectExchange exchange
    ){
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(ROUTING_KEY);
    }

    @Bean
    public Binding bindingDLQ(
            @Qualifier("queueDLQ") Queue queueDLQ,
            @Qualifier("exchangeDLQ") DirectExchange exchangeDLQ
    ) {
        return BindingBuilder
                .bind(queueDLQ)
                .to(exchangeDLQ)
                .with(DLQ_ROUTING_KEY);
    }
}
