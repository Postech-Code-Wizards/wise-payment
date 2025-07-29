package br.com.wise.payment.wise.payment.gateway.rabbitmq;

import br.com.wise.payment.wise.payment.application.configuration.rabbit.RabbitConfig;
import br.com.wise.payment.wise.payment.gateway.rabbitmq.dto.CallbackPaymentDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.UUID;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CallbackPaymentAdapterTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private CallbackPaymentAdapter callbackPaymentAdapter;

    private CallbackPaymentDTO callbackPaymentDTO;

    @BeforeEach
    void setUp() {

        callbackPaymentDTO = new CallbackPaymentDTO(
                UUID.randomUUID().toString(),
                true
        );
    }

    @Test
    @DisplayName("Should send callback payment DTO to RabbitMQ successfully")
    void shouldSendCallbackPaymentDTOToRabbitMQSuccessfully() {
        callbackPaymentAdapter.send(callbackPaymentDTO);

        verify(rabbitTemplate).convertAndSend(
                RabbitConfig.EXCHANGE_NAME,
                RabbitConfig.ROUTING_KEY,
                callbackPaymentDTO
        );
    }
}