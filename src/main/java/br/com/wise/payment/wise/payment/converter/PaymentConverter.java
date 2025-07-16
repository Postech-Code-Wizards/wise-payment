package br.com.wise.payment.wise.payment.converter;

import br.com.wise.payment.wise.payment.application.dtos.request.PaymentRequest;
import br.com.wise.payment.wise.payment.application.dtos.response.PaymentResponse;
import br.com.wise.payment.wise.payment.domain.Payment;
import br.com.wise.payment.wise.payment.domain.Status;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PaymentConverter {

    public Payment toDomain(PaymentRequest paymentRequest) {
        return new Payment(
                null,
                BigDecimal.valueOf(paymentRequest.totalValue()),
                Status.PROCESSING,
                paymentRequest.creditCard(),
                null
        );
    }

    public PaymentResponse toResponse(Payment payment) {
        return new PaymentResponse(
                payment.getPaymentId(),
                payment.getStatus(),
                payment.getMessage()
        );
    }
}
