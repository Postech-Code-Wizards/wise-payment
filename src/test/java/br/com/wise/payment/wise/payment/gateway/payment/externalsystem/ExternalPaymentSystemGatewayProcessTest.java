package br.com.wise.payment.wise.payment.gateway.payment.externalsystem;

import br.com.wise.payment.wise.payment.domain.Payment;
import br.com.wise.payment.wise.payment.domain.Status;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ExternalPaymentSystemGatewayProcessTest {

    private final ExternalPaymentSystemGatewayProcess gateway = new ExternalPaymentSystemGatewayProcess();

    @Test
    void shouldReturnTrueWhenAllFieldsValid() {
        var payment = new Payment(
                UUID.randomUUID(),
                BigDecimal.valueOf(100.00),
                Status.PROCESSING,
                "4111111111111111",
                null
        );

        assertTrue(gateway.createPaymentRequest(payment));
    }

    @Test
    void shouldReturnFalseWhenTotalValueIsNull() {
        var payment = new Payment(
                UUID.randomUUID(),
                null,
                Status.PROCESSING,
                "4111111111111111",
                ""
        );

        assertFalse(gateway.createPaymentRequest(payment));
    }

    @Test
    void shouldReturnFalseWhenPaymentIdIsNull() {
        var payment = new Payment(
                null,
                BigDecimal.valueOf(100.00),
                Status.PROCESSING,
                "4111111111111111",
                ""
        );

        assertFalse(gateway.createPaymentRequest(payment));
    }

    @Test
    void shouldReturnFalseWhenCreditCardNumberIsNull() {
        var payment = new Payment(
                UUID.randomUUID(),
                BigDecimal.valueOf(100.00),
                Status.PROCESSING,
                null,
                ""
        );

        assertFalse(gateway.createPaymentRequest(payment));
    }

    @Test
    void shouldReturnFalseWhenStatusIsNotProcessing() {
        var payment = new Payment(
                UUID.randomUUID(),
                BigDecimal.valueOf(100.00),
                Status.SUCCESS,
                "4111111111111111",
                ""
        );

        assertFalse(gateway.createPaymentRequest(payment));
    }

    @Test
    void shouldReturnFalseWhenMessageIsNotEmpty() {
        var payment = new Payment(
                UUID.randomUUID(),
                BigDecimal.valueOf(100.00),
                Status.PROCESSING,
                "4111111111111111",
                "Error"
        );

        assertFalse(gateway.createPaymentRequest(payment));
    }
}
