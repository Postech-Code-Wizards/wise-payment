package br.com.wise.payment.wise.payment.strategies;

import br.com.wise.payment.wise.payment.domain.Payment;

public interface PaymentStrategy {
    Payment processPayment(Payment payment);
}
