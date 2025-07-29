package br.com.wise.payment.wise.payment.gateway.rabbitmq.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CallbackPaymentDTO {
    String paymentId;
    boolean success;
}