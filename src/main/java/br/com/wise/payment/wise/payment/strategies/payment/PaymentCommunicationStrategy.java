package br.com.wise.payment.wise.payment.strategies.payment;

import br.com.wise.payment.wise.payment.domain.Payment;
import br.com.wise.payment.wise.payment.domain.Status;
import br.com.wise.payment.wise.payment.gateway.payment.ExternalPaymentSystemGateway;
import br.com.wise.payment.wise.payment.gateway.rabbitmq.CallbackPaymentGateway;
import br.com.wise.payment.wise.payment.gateway.rabbitmq.dto.CallbackPaymentDTO;
import br.com.wise.payment.wise.payment.strategies.PaymentStrategy;
import br.com.wise.payment.wise.payment.usecase.VerifyWithBankUseCase;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentCommunicationStrategy implements PaymentStrategy {
    Logger log = LogManager.getLogger(PaymentCommunicationStrategy.class);
    private static final String PAYMENT_SUCCESS = "Success with payment";
    private static final String PAYMENT_FAILURE = "Payment failure in class: ";

    private final ExternalPaymentSystemGateway externalPaymentSystemGateway;
    private final VerifyWithBankUseCase verifyWithBankUseCase;
    private final CallbackPaymentGateway callbackPaymentGateway;

    @Override
    public Payment processPayment(Payment payment) {
        boolean externalPaymentProcess = externalPaymentSystemGateway.createPaymentRequest(payment);
        if (validatePaymentDataWithBank(payment) && externalPaymentProcess) {
            CallbackPaymentDTO callbackPaymentDTO = new CallbackPaymentDTO(payment.getPaymentId().toString(), true);
            callbackPaymentGateway.send(callbackPaymentDTO);
            payment.newStatus(Status.SUCCESS);
            log.info(PAYMENT_SUCCESS);
            return payment;
        }
        CallbackPaymentDTO callbackPaymentDTO = new CallbackPaymentDTO(payment.getPaymentId().toString(), false);
        callbackPaymentGateway.send(callbackPaymentDTO);
        log.error(PAYMENT_FAILURE, getClass());
        payment.newStatus(Status.FAILURE_IN_PAYMENT_INFORMATION);
        return payment;
    }

    private boolean validatePaymentDataWithBank(Payment payment) {
        return verifyWithBankUseCase.execute(payment);
    }
}