package br.com.joao.orders_service.listener;

import br.com.joao.orders_service.listener.dto.PaymentFailedEvent;
import br.com.joao.orders_service.listener.dto.PaymentApprovedEvent;
import br.com.joao.orders_service.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import static br.com.joao.orders_service.config.RabbitMqConfig.PAYMENT_APPROVED_QUEUE;
import static br.com.joao.orders_service.config.RabbitMqConfig.PAYMENT_FAILED_QUEUE;

@Component
public class PaymentEventListener {

    private final Logger logger = LoggerFactory.getLogger(PaymentEventListener.class);
    private final OrderService orderService;

    public PaymentEventListener(OrderService orderService) {
        this.orderService = orderService;
    }

    @RabbitListener(queues = PAYMENT_APPROVED_QUEUE)
    public void listenApprovedPayments(Message<PaymentApprovedEvent> message){
        logger.info("Payment approved consumed {}", message.getPayload());

        orderService.handleApprovedPayment(message.getPayload());

    }

    @RabbitListener(queues = PAYMENT_FAILED_QUEUE)
    public void listenFailedPayments(Message<PaymentFailedEvent> message){
        logger.info("Payment failed consumed {}", message.getPayload());

        orderService.handleFailedPayment(message.getPayload());

    }
}
