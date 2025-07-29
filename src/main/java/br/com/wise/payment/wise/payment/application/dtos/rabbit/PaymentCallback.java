package br.com.wise.payment.wise.payment.application.dtos.rabbit;

import br.com.wise.payment.wise.payment.domain.Status;

import java.util.UUID;

public record PaymentCallback(
        UUID paymentId,
        Status status,
        String message
) {
}
