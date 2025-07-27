package br.com.wise.payment.wise.payment.usecase;

import br.com.wise.payment.wise.payment.domain.Payment;
import br.com.wise.payment.wise.payment.gateway.database.PaymentGateway;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class FindPaymentIdUseCaseTest {

    private final PaymentGateway paymentGateway = mock(PaymentGateway.class);
    private final FindPaymentIdUseCase useCase = new FindPaymentIdUseCase(paymentGateway);

    @Test
    void execute_shouldReturnPaymentWhenIdIsValid() {
        UUID uuid = UUID.randomUUID();
        Payment payment = new Payment(uuid, BigDecimal.valueOf(100), null, "4111111111111111", null);

        when(paymentGateway.findPaymentById(uuid)).thenReturn(payment);

        Payment result = useCase.execute(uuid);

        assertThat(result).isSameAs(payment);
        verify(paymentGateway).findPaymentById(uuid);
    }

    @Test
    void execute_shouldReturnNullWhenPaymentNotFound() {
        UUID uuid = UUID.randomUUID();

        when(paymentGateway.findPaymentById(uuid)).thenReturn(null);

        Payment result = useCase.execute(uuid);

        assertThat(result).isNull();
        verify(paymentGateway).findPaymentById(uuid);
    }
}
