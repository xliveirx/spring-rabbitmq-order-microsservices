package br.com.joao.payment_service.service;

import br.com.joao.payment_service.dto.PaymentApprovedEvent;
import br.com.joao.payment_service.dto.PaymentFailedEvent;
import br.com.joao.payment_service.listener.dto.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static br.com.joao.payment_service.config.RabbitMqConfig.PAYMENT_APPROVED_QUEUE;
import static br.com.joao.payment_service.config.RabbitMqConfig.PAYMENT_FAILED_QUEUE;

@Service
public class PaymentService {

    private final RabbitTemplate rabbitTemplate;
    private final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    public PaymentService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void handleOrderCreated(OrderCreatedEvent event){

        boolean paymentSuccess = Math.random() > 0.2;

        if(paymentSuccess){

            var dto = new PaymentApprovedEvent(event.id(), event.userEmail(), event.total());

            rabbitTemplate.convertAndSend(PAYMENT_APPROVED_QUEUE, dto);

            logger.info("Payment approved, message sent to the queue {}", dto);
        }

        else {
            var dto = new PaymentFailedEvent(event.id(), event.userEmail(), "There was an error processing your payment, try again.");

            rabbitTemplate.convertAndSend(PAYMENT_FAILED_QUEUE, dto);

            logger.info("Payment failed, message sent to the queue {}", dto);
        }
    }
}
