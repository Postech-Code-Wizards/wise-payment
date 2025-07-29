package br.com.wise.payment.wise.payment.usecase;

import br.com.wise.payment.wise.payment.converter.PaymentConverter;
import br.com.wise.payment.wise.payment.domain.Payment;
import br.com.wise.payment.wise.payment.gateway.database.PaymentGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PutOnLineProcessingStartUseCase {

    private final PaymentGateway paymentGateway;
    private final PaymentConverter converter;

    public Payment execute(Payment payment) {
        return paymentGateway.createProcessAndReturnUUID(converter.toEntity(payment));
    }
}
