package br.com.wise.payment.wise.payment.gateway;

import br.com.wise.payment.wise.payment.domain.Payment;

public interface PaymentGateway {
    Payment processPayment(Payment payment);
}
