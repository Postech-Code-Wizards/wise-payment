package br.com.wise.payment.wise.payment.gateway.payment.externalsystem;

import br.com.wise.payment.wise.payment.domain.Payment;
import br.com.wise.payment.wise.payment.domain.Status;
import br.com.wise.payment.wise.payment.gateway.payment.ExternalPaymentSystemGateway;
import org.springframework.stereotype.Component;

@Component
public class ExternalPaymentSystemGatewayProcess implements ExternalPaymentSystemGateway {

    public boolean createPaymentRequest(Payment payment) {
        return processPayment(payment);
    }

    private boolean processPayment(Payment payment) {
        return payment.getTotalValue() != null
                && payment.getPaymentId() != null
                && payment.getCreditCardNumber() != null
                && payment.getStatus().equals(Status.PROCESSING)
                && payment.getMessage() == null;
    }
}
