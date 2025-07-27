package br.com.wise.payment.wise.payment.gateway;

import br.com.wise.payment.wise.payment.domain.Payment;
import br.com.wise.payment.wise.payment.domain.Status;
import br.com.wise.payment.wise.payment.gateway.payment.ExternalPaymentSystemGateway;
import br.com.wise.payment.wise.payment.strategies.payment.PaymentCommunicationStrategy;
import br.com.wise.payment.wise.payment.usecase.VerifyWithBankUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PaymentCommunicationStrategyTest {

    private ExternalPaymentSystemGateway externalPaymentSystemGateway;
    private VerifyWithBankUseCase verifyWithBankUseCase;
    private PaymentCommunicationStrategy paymentStrategy;

    @BeforeEach
    void setUp() {
        externalPaymentSystemGateway = mock(ExternalPaymentSystemGateway.class);
        verifyWithBankUseCase = mock(VerifyWithBankUseCase.class);
        paymentStrategy = new PaymentCommunicationStrategy(externalPaymentSystemGateway, verifyWithBankUseCase);
    }

    @Test
    void deveRetornarStatusSuccess_quandoPagamentoExternoEValidacaoForemTrue() {
        Payment payment = new Payment(
                null,
                null,
                null,
                null,
                null
        );
        when(externalPaymentSystemGateway.createPaymentRequest(payment)).thenReturn(true);
        when(verifyWithBankUseCase.execute(payment)).thenReturn(true);

        Payment resultado = paymentStrategy.processPayment(payment);

        assertEquals(Status.SUCCESS, resultado.getStatus());
        verify(externalPaymentSystemGateway).createPaymentRequest(payment);
        verify(verifyWithBankUseCase).execute(payment);
    }

    @Test
    void deveRetornarStatusFailure_quandoPagamentoExternoForFalse() {
        Payment payment = new Payment(
                null,
                null,
                null,
                null,
                null
        );
        when(externalPaymentSystemGateway.createPaymentRequest(payment)).thenReturn(false);
        when(verifyWithBankUseCase.execute(payment)).thenReturn(true);

        Payment resultado = paymentStrategy.processPayment(payment);

        assertEquals(Status.FAILURE_IN_PAYMENT_INFORMATION, resultado.getStatus());
    }

    @Test
    void deveRetornarStatusFailure_quandoValidacaoComBancoForFalse() {
        Payment payment = new Payment(
                null,
                null,
                null,
                null,
                null
        );
        when(externalPaymentSystemGateway.createPaymentRequest(payment)).thenReturn(true);
        when(verifyWithBankUseCase.execute(payment)).thenReturn(false);

        Payment resultado = paymentStrategy.processPayment(payment);

        assertEquals(Status.FAILURE_IN_PAYMENT_INFORMATION, resultado.getStatus());
    }
}