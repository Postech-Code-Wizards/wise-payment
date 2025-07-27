package br.com.wise.payment.wise.payment.gateway.database.jpa;

import br.com.wise.payment.wise.payment.converter.PaymentConverter;
import br.com.wise.payment.wise.payment.domain.Payment;
import br.com.wise.payment.wise.payment.domain.Status;
import br.com.wise.payment.wise.payment.exceptions.PaymentNotFoundException;
import br.com.wise.payment.wise.payment.gateway.database.jpa.repositories.PaymentRepository;
import br.com.wise.payment.wise.payment.gateway.database.jpa.repositories.entities.PaymentEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentJpaGatewayTest {

    private final PaymentRepository paymentRepository = mock(PaymentRepository.class);
    private final PaymentConverter paymentConverter = mock(PaymentConverter.class);
    private final PaymentJpaGateway paymentJpaGateway = new PaymentJpaGateway(paymentRepository, paymentConverter);

    @Test
    void createProcessAndReturnUUID_success() {
        var input = new PaymentEntity(null,
                new BigDecimal("50"), null, "4111111111111111", "");
        var saved = new PaymentEntity(UUID.randomUUID(),
                new BigDecimal("50"), Status.PROCESSING, "4111111111111111", "");
        var domain = new Payment(saved.getId(), saved.getTotalValue(), saved.getStatus(), saved.getCreditCardNumber(), "");

        when(paymentRepository.save(input)).thenReturn(saved);
        when(paymentConverter.toDomain(saved)).thenReturn(domain);

        var result = paymentJpaGateway.createProcessAndReturnUUID(input);
        assertThat(result).isEqualTo(domain);

        verify(paymentRepository).save(input);
        verify(paymentConverter).toDomain(saved);
    }

    @Test
    void createProcessAndReturnUUID_invalidEntity_throws() {
        var bad = new PaymentEntity(null,
                new BigDecimal("50"), Status.PROCESSING, "4111111111111111", "");
        assertThatThrownBy(() -> paymentJpaGateway.createProcessAndReturnUUID(bad))
                .isInstanceOf(PaymentNotFoundException.class)
                .hasMessageContaining("Payment entity is null");
    }

    @Test
    void findPaymentById_success() {
        var id = UUID.randomUUID();
        var ent = new PaymentEntity(id, new BigDecimal("20"), Status.PROCESSING, "1234", "");
        var domain = new Payment(id, ent.getTotalValue(), ent.getStatus(), ent.getCreditCardNumber(), "");

        when(paymentRepository.findById(id)).thenReturn(Optional.of(ent));
        when(paymentConverter.toDomain(ent)).thenReturn(domain);

        var result = paymentJpaGateway.findPaymentById(id);
        assertThat(result).isEqualTo(domain);

        verify(paymentRepository).findById(id);
        verify(paymentConverter).toDomain(ent);
    }

    @Test
    void findPaymentById_nullUuid_throws() {
        assertThatThrownBy(() -> paymentJpaGateway.findPaymentById(null))
                .isInstanceOf(PaymentNotFoundException.class)
                .hasMessageContaining("UUID is null");
    }

    @Test
    void findPaymentById_notFound_throws() {
        var id = UUID.randomUUID();
        when(paymentRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> paymentJpaGateway.findPaymentById(id))
                .isInstanceOf(PaymentNotFoundException.class)
                .hasMessageContaining("Payment not found for UUID");
    }

    @Test
    void updatePaymentAfterProcessing_success() {
        var id = UUID.randomUUID();
        var updateEnt = new PaymentEntity(null,
                new BigDecimal("30"), null, "2222", "msg");
        var existing = new PaymentEntity(id,
                new BigDecimal("10"), Status.PROCESSING, "2222", "");
        var domain = new Payment(id, existing.getTotalValue(), existing.getStatus(), existing.getCreditCardNumber(), updateEnt.getMessage());

        when(paymentRepository.findById(id)).thenReturn(Optional.of(existing));
        when(paymentConverter.toDomain(existing)).thenReturn(domain);

        var result = paymentJpaGateway.updatePaymentAfterProcessing(id, updateEnt);
        assertThat(result).isEqualTo(domain);

        assertThat(existing.getStatus()).isEqualTo(updateEnt.getStatus());
        assertThat(existing.getMessage()).isEqualTo(updateEnt.getMessage());
        verify(paymentRepository).save(existing);
        verify(paymentConverter).toDomain(existing);
    }

    @Test
    void updatePaymentAfterProcessing_nullUuid_throws() {
        var updateEnt = new PaymentEntity(null, BigDecimal.ONE, null, "x", "");
        assertThatThrownBy(() -> paymentJpaGateway.updatePaymentAfterProcessing(null, updateEnt))
                .isInstanceOf(PaymentNotFoundException.class)
                .hasMessageContaining("UUID is null");
    }

    @Test
    void updatePaymentAfterProcessing_invalidEntity_throws() {
        var id = UUID.randomUUID();
        var bad = new PaymentEntity(null, BigDecimal.ONE, Status.PROCESSING, "x", "");
        assertThatThrownBy(() -> paymentJpaGateway.updatePaymentAfterProcessing(id, bad))
                .isInstanceOf(PaymentNotFoundException.class)
                .hasMessageContaining("Payment entity is null");
    }

    @Test
    void updatePaymentAfterProcessing_notFound_throws() {
        var id = UUID.randomUUID();
        var updateEnt = new PaymentEntity(null, BigDecimal.ONE, null, "x", "");
        when(paymentRepository.findById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> paymentJpaGateway.updatePaymentAfterProcessing(id, updateEnt))
                .isInstanceOf(PaymentNotFoundException.class)
                .hasMessageContaining("Payment not found for UUID");
    }
}
