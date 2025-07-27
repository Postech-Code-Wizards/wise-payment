package br.com.wise.payment.wise.payment.usecase;

import br.com.wise.payment.wise.payment.converter.PaymentConverter;
import br.com.wise.payment.wise.payment.domain.Payment;
import br.com.wise.payment.wise.payment.gateway.database.PaymentGateway;
import br.com.wise.payment.wise.payment.gateway.database.jpa.repositories.entities.PaymentEntity;
import br.com.wise.payment.wise.payment.strategies.payment.PaymentCommunicationStrategy;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ProceedPaymentProcessingUseCaseTest {

    private final PaymentGateway paymentGateway = mock(PaymentGateway.class);
    private final PaymentConverter converter = mock(PaymentConverter.class);
    private final PaymentCommunicationStrategy strategy = mock(PaymentCommunicationStrategy.class);
    private final FindPaymentIdUseCase findUseCase = mock(FindPaymentIdUseCase.class);
    private final ProceedPaymentProcessingUseCase useCase =
            new ProceedPaymentProcessingUseCase(paymentGateway, converter, strategy, findUseCase);

    @Test
    void execute_shouldReturnUpdatedPayment_whenProcessingSucceeds() {
        UUID id = UUID.randomUUID();
        Payment found = new Payment(id, BigDecimal.valueOf(50), null, "4111", "");
        Payment processed = new Payment(id, BigDecimal.valueOf(50), null, "4111", "");
        PaymentEntity entity = new PaymentEntity(id, BigDecimal.valueOf(50), null, "4111", "");
        Payment updated = new Payment(id, BigDecimal.valueOf(50), null, "4111", "done");

        when(findUseCase.execute(id)).thenReturn(found);
        when(strategy.processPayment(found)).thenReturn(processed);
        when(converter.toEntity(processed)).thenReturn(entity);
        when(paymentGateway.updatePaymentAfterProcessing(id, entity)).thenReturn(updated);

        Payment result = useCase.execute(id);

        assertThat(result).isSameAs(updated);
        verify(findUseCase).execute(id);
        verify(strategy).processPayment(found);
        verify(converter).toEntity(processed);
        verify(paymentGateway).updatePaymentAfterProcessing(id, entity);
    }

    @Test
    void execute_shouldPropagateException_whenGatewayThrows() {
        UUID id = UUID.randomUUID();
        Payment found = new Payment(id, BigDecimal.ONE, null, "0000", "");
        when(findUseCase.execute(id)).thenReturn(found);
        when(strategy.processPayment(found)).thenReturn(found);
        when(converter.toEntity(found)).thenReturn(new PaymentEntity(id, BigDecimal.ONE, null, "0000", ""));
        when(paymentGateway.updatePaymentAfterProcessing(eq(id), any()))
                .thenThrow(new RuntimeException("db error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> useCase.execute(id));
        assertThat(ex).hasMessage("db error");
    }
}
