package br.com.wise.payment.wise.payment.gateway.database;

import br.com.wise.payment.wise.payment.domain.Payment;
import br.com.wise.payment.wise.payment.gateway.database.jpa.repositories.entities.PaymentEntity;

import java.util.UUID;

public interface PaymentGateway {
    Payment createProcessAndReturnUUID(PaymentEntity paymentEntity);

    Payment findPaymentById(UUID uuid);

    Payment updatePaymentAfterProcessing(UUID savedPaymentId, PaymentEntity paymentEntity);
}