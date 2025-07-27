package br.com.wise.payment.wise.payment.gateway.payment;

import br.com.wise.payment.wise.payment.domain.Payment;

public interface ExternalPaymentSystemGateway {
    boolean createPaymentRequest(Payment payment);
}
