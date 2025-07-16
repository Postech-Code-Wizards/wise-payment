package br.com.wise.payment.wise.payment.application.dtos.response;

import br.com.wise.payment.wise.payment.domain.Status;

import java.util.UUID;

public record PaymentResponse(
        UUID paymentId,
        Status status,
        String message
) {
}
