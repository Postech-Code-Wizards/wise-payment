package br.com.wise.payment.wise.payment.application.dtos.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PaymentRequest(
        @NotBlank String creditCard,
        @NotNull Double totalValue
) {
}
