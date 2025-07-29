package br.com.wise.payment.wise.payment.gateway.rabbitmq;

import br.com.wise.payment.wise.payment.application.configuration.rabbit.RabbitConfig;
import br.com.wise.payment.wise.payment.gateway.rabbitmq.dto.CallbackPaymentDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class CallbackPaymentAdapter implements CallbackPaymentGateway {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void send(CallbackPaymentDTO callbackPaymentDTO) {
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME,
                RabbitConfig.ROUTING_KEY,
                callbackPaymentDTO);
    }

}