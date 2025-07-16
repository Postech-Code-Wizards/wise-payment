package br.com.wise.payment.wise.payment.application.dtos.rabbit;

public record PaymentMessageDTO(
        String creditCard,
        double totalValue
) {
}
