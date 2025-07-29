package br.com.wise.payment.wise.payment.gateway.rabbitmq;

import br.com.wise.payment.wise.payment.gateway.rabbitmq.dto.CallbackPaymentDTO;

public interface CallbackPaymentGateway {

    void send(CallbackPaymentDTO callbackPaymentDTO);

}