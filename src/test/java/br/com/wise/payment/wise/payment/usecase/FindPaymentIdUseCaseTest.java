package br.com.wise.payment.wise.payment.usecase;

import br.com.wise.payment.wise.payment.domain.Payment;
import br.com.wise.payment.wise.payment.gateway.database.PaymentGateway;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FindPaymentIdUseCaseTest {

    private final PaymentGateway paymentGateway = mock(PaymentGateway.class);
    private final FindPaymentIdUseCase useCase = new FindPaymentIdUseCase(null, paymentGateway);

    @Test
    void execute_shouldReturnPayment_whenFound() {
        UUID id = UUID.randomUUID();
        Payment payment = new Payment(id, BigDecimal.ZERO, null, "0000", "");
        when(paymentGateway.findPaymentById(id)).thenReturn(payment);

        Payment result = useCase.execute(id);

        assertSame(payment, result);
        verify(paymentGateway).findPaymentById(id);
    }

    @Test
    void execute_shouldThrow_whenNotFound() {
        UUID id = UUID.randomUUID();
        when(paymentGateway.findPaymentById(id)).thenThrow(new RuntimeException("not found"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> useCase.execute(id));
        assertEquals("not found", exception.getMessage());
        verify(paymentGateway).findPaymentById(id);
    }
}
