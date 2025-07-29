package br.com.wise.payment.wise.payment.application.configuration.rabbit;


import br.com.wise.payment.wise.payment.application.dtos.rabbit.PaymentCallback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitConsumer {
    private static final Logger logger = LogManager.getLogger(RabbitConsumer.class);
    private static final String PRODUCT_MESSAGE = "Message produced: ";

    @RabbitListener(queues = RabbitConfig.QUEUE_NAME)
    public void receive(PaymentCallback paymentCallback) {
        logger.info(PRODUCT_MESSAGE + "{}", paymentCallback);
    }
}
