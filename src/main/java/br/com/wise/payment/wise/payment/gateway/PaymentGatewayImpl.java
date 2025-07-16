package br.com.wise.payment.wise.payment.gateway;

import br.com.wise.payment.wise.payment.domain.Payment;
import br.com.wise.payment.wise.payment.domain.Status;
import br.com.wise.payment.wise.payment.usecase.VerifyWithBank;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentGatewayImpl implements PaymentGateway {
    Logger log = LogManager.getLogger(PaymentGatewayImpl.class);
    private static final String PAYMENT_SUCCESS = "Success with payment";
    private static final String PAYMENT_FAILURE = "Payment failure in class: ";

    private final VerifyWithBank verifyWithBank;

    @Override
    public Payment processPayment(Payment payment) {
        if (validatePaymentDataWithBank(payment)) {
            payment.generateId();
            payment.newStatus(Status.SUCCESS);
            log.info(PAYMENT_SUCCESS);
            return payment;
        }
        log.error(PAYMENT_FAILURE, getClass());
        payment.newStatus(Status.FAILURE_IN_PAYMENT_INFORMATION);
        return payment;
    }

    private boolean validatePaymentDataWithBank(Payment payment) {
        return verifyWithBank.execute(payment);
    }

}
