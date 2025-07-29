package br.com.wise.payment.wise.payment.usecase;

import br.com.wise.payment.wise.payment.converter.PaymentConverter;
import br.com.wise.payment.wise.payment.domain.Payment;
import br.com.wise.payment.wise.payment.gateway.database.PaymentGateway;
import br.com.wise.payment.wise.payment.strategies.payment.PaymentCommunicationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProceedPaymentProcessingUseCase {
    private final PaymentGateway paymentGateway;
    private final PaymentConverter converter;

    private final PaymentCommunicationStrategy paymentCommunicationStrategy;
    private final FindPaymentIdUseCase findPaymentIdUseCase;

    public Payment execute(UUID uuid) {
        Payment payment = findPaymentIdUseCase.execute(uuid);
        Payment paymentProcessed = paymentCommunicationStrategy.processPayment(payment);

        return paymentGateway.updatePaymentAfterProcessing(uuid, converter.toEntity(paymentProcessed));
    }
}
