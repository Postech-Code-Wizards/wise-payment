package br.com.wise.payment.wise.payment.usecase;

import br.com.wise.payment.wise.payment.converter.PaymentConverter;
import br.com.wise.payment.wise.payment.domain.Payment;
import br.com.wise.payment.wise.payment.gateway.database.PaymentGateway;
import br.com.wise.payment.wise.payment.gateway.database.jpa.repositories.entities.PaymentEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PutOnLineProcessingStartUseCaseTest {

    private final PaymentGateway paymentGateway = mock(PaymentGateway.class);
    private final PaymentConverter converter = mock(PaymentConverter.class);
    private final PutOnLineProcessingStartUseCase useCase =
            new PutOnLineProcessingStartUseCase(paymentGateway, converter);

    @Test
    void execute_shouldReturnCreatedPayment() {
        Payment input = new Payment(null, BigDecimal.valueOf(100), null, "4111111111111111", null);
        PaymentEntity entity = new PaymentEntity(null, BigDecimal.valueOf(100), null, "4111111111111111", null);
        Payment created = new Payment(UUID.randomUUID(), BigDecimal.valueOf(100), null, "4111111111111111", null);

        when(converter.toEntity(input)).thenReturn(entity);
        when(paymentGateway.createProcessAndReturnUUID(entity)).thenReturn(created);

        Payment result = useCase.execute(input);

        assertThat(result).isSameAs(created);
        verify(converter).toEntity(input);
        verify(paymentGateway).createProcessAndReturnUUID(entity);
    }

    @Test
    void execute_shouldPropagateException() {
        Payment input = new Payment(null, BigDecimal.ZERO, null, "0000", null);
        PaymentEntity entity = new PaymentEntity(null, BigDecimal.ZERO, null, "0000", null);

        when(converter.toEntity(input)).thenReturn(entity);
        when(paymentGateway.createProcessAndReturnUUID(entity))
                .thenThrow(new RuntimeException("error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> useCase.execute(input));
        assertThat(ex).hasMessage("error");
    }
}
