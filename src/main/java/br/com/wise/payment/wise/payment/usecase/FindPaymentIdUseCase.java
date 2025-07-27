package br.com.wise.payment.wise.payment.usecase;

import br.com.wise.payment.wise.payment.converter.PaymentConverter;
import br.com.wise.payment.wise.payment.domain.Payment;
import br.com.wise.payment.wise.payment.gateway.database.PaymentGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FindPaymentIdUseCase {
    private final PaymentConverter converter;
    private final PaymentGateway paymentGateway;

    public Payment execute(UUID uuid) {
        return paymentGateway.findPaymentById(uuid);
    }
}
