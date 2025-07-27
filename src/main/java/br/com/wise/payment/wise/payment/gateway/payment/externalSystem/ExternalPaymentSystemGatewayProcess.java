package br.com.wise.payment.wise.payment.gateway.payment.externalSystem;

import br.com.wise.payment.wise.payment.domain.Payment;
import br.com.wise.payment.wise.payment.domain.Status;
import br.com.wise.payment.wise.payment.gateway.payment.ExternalPaymentSystemGateway;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class ExternalPaymentSystemGatewayProcess implements ExternalPaymentSystemGateway {
    Logger log = LogManager.getLogger(ExternalPaymentSystemGatewayProcess.class);

    public boolean createPaymentRequest(Payment payment) {
        return processPayment(payment);
    }

    private boolean processPayment(Payment payment){
        return payment.getTotalValue() != null
                && payment.getPaymentId() != null
                && payment.getCreditCardNumber() != null
                && payment.getStatus().equals(Status.PROCESSING)
                && payment.getMessage() == null;
    }
}
