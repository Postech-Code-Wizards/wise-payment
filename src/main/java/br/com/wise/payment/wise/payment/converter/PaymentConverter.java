package br.com.wise.payment.wise.payment.converter;

import br.com.wise.payment.wise.payment.application.dtos.request.PaymentRequest;
import br.com.wise.payment.wise.payment.application.dtos.response.PaymentResponse;
import br.com.wise.payment.wise.payment.domain.Payment;
import br.com.wise.payment.wise.payment.domain.Status;
import br.com.wise.payment.wise.payment.gateway.database.jpa.repositories.entities.PaymentEntity;
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

    public Payment toDomain(PaymentEntity paymentEntity) {
        return new Payment(
                paymentEntity.getId(),
                paymentEntity.getTotalValue(),
                paymentEntity.getStatus(),
                paymentEntity.getCreditCardNumber(),
                paymentEntity.getMessage()
        );
    }

    public PaymentEntity toEntity(Payment payment) {
        return new PaymentEntity(
                payment.getPaymentId(),
                payment.getTotalValue(),
                payment.getStatus(),
                payment.getCreditCardNumber(),
                payment.getMessage()
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
