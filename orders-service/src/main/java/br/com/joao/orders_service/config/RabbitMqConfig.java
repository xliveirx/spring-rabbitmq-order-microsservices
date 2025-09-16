package br.com.joao.orders_service.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMqConfig {

    public static final String ORDER_CREATED_QUEUE = "order-created-queue";
    public static final String PAYMENT_APPROVED_QUEUE = "payment-approved-queue";
    public static final String PAYMENT_FAILED_QUEUE = "payment-failed-queue";

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter){
        var rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

    @Bean
    public Queue orderCreatedQueue(){
        return new Queue(ORDER_CREATED_QUEUE);
    }


    @Bean
    public Queue paymentSuccessQueue(){
        return new Queue(PAYMENT_APPROVED_QUEUE);
    }


    @Bean
    public Queue paymentFailedQueue(){
        return new Queue(PAYMENT_FAILED_QUEUE);
    }
}
