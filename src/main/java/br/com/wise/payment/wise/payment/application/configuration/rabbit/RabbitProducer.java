package br.com.wise.payment.wise.payment.application.configuration.rabbit;

import br.com.wise.payment.wise.payment.application.dtos.rabbit.PaymentCallback;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitProducer {

    private final RabbitTemplate rabbitTemplate;

    public void send(PaymentCallback paymentMessage) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE_NAME,
                RabbitConfig.ROUTING_KEY,
                paymentMessage
        );
    }

}
