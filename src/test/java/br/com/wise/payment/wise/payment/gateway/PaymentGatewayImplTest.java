package br.com.wise.payment.wise.payment.gateway;


import br.com.wise.payment.wise.payment.domain.Payment;
import br.com.wise.payment.wise.payment.domain.Status;
import br.com.wise.payment.wise.payment.usecase.VerifyWithBank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PaymentGatewayImplTest {

    private VerifyWithBank verifyWithBank;
    private PaymentGatewayImpl paymentGateway;

    @BeforeEach
    void setUp() {
        verifyWithBank = mock(VerifyWithBank.class);
        paymentGateway = new PaymentGatewayImpl(verifyWithBank);
    }

    @Test
    void shouldReturnPaymentWithSuccessStatusWhenBankValidationPasses() {
        Payment payment = mock(Payment.class);
        when(verifyWithBank.execute(payment)).thenReturn(true);

        Payment result = paymentGateway.processPayment(payment);

        verify(payment).generateId();
        verify(payment).newStatus(Status.SUCCESS);
        assertEquals(payment, result);
    }

    @Test
    void shouldReturnPaymentWithFailureStatusWhenBankValidationFails() {
        Payment payment = mock(Payment.class);
        when(verifyWithBank.execute(payment)).thenReturn(false);

        Payment result = paymentGateway.processPayment(payment);

        verify(payment, never()).generateId();
        verify(payment).newStatus(Status.FAILURE_IN_PAYMENT_INFORMATION);
        assertEquals(payment, result);
    }
}