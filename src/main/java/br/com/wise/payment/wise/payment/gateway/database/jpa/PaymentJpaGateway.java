package br.com.wise.payment.wise.payment.gateway.database.jpa;

import br.com.wise.payment.wise.payment.converter.PaymentConverter;
import br.com.wise.payment.wise.payment.domain.Payment;
import br.com.wise.payment.wise.payment.exceptions.PaymentNotFoundException;
import br.com.wise.payment.wise.payment.gateway.database.PaymentGateway;
import br.com.wise.payment.wise.payment.gateway.database.jpa.repositories.PaymentRepository;
import br.com.wise.payment.wise.payment.gateway.database.jpa.repositories.entities.PaymentEntity;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PaymentJpaGateway implements PaymentGateway {
    private static final Logger logger = LogManager.getLogger(PaymentJpaGateway.class);

    private static final String PAYMENT_UUID_SAVED = "[PaymentJpaGateway] Payment saved with UUID: {}";
    private static final String PAYMENT_NOT_FOUND = "[PaymentJpaGateway] Payment not found for UUID: ";
    private static final String NULL_UUID = "[PaymentJpaGateway] UUID is null";
    private static final String PAYMENT_ENTITY_NULL = "[PaymentJpaGateway] Payment entity is null";

    private final PaymentRepository repository;
    private final PaymentConverter converter;

    public Payment createProcessAndReturnUUID(PaymentEntity paymentEntity) {
        validatePaymentEntity(paymentEntity);

        PaymentEntity payment = repository.save(paymentEntity);
        logger.info(PAYMENT_UUID_SAVED, payment.getId());
        return converter.toDomain(payment);
    }

    public Payment findPaymentById(UUID uuid) {
        validatePaymentUUID(uuid);
        PaymentEntity paymentEntity = repository.findById(uuid)
                .orElseThrow(() -> new PaymentNotFoundException(PAYMENT_NOT_FOUND + uuid));
        return converter.toDomain(paymentEntity);
    }

    public Payment updatePaymentAfterProcessing(UUID paymentId, PaymentEntity paymentEntity) {
        validatePaymentEntity(paymentEntity);
        validatePaymentUUID(paymentId);

        PaymentEntity paymentToUpdate = repository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(PAYMENT_NOT_FOUND + paymentId));

        paymentToUpdate.setStatus(paymentEntity.getStatus());
        paymentToUpdate.setMessage(paymentEntity.getMessage());

        repository.save(paymentToUpdate);
        return converter.toDomain(paymentToUpdate);
    }

    private void validatePaymentEntity(PaymentEntity paymentEntity) {
        if (!validateEntity(paymentEntity)) {
            logger.error(PAYMENT_ENTITY_NULL);
            throw new PaymentNotFoundException(PAYMENT_ENTITY_NULL);
        }
    }

    private boolean validateEntity(PaymentEntity paymentEntity) {
        return paymentEntity.getStatus() != null
                && paymentEntity.getCreditCardNumber() != null
                && paymentEntity.getTotalValue() != null;
    }

    private void validatePaymentUUID(UUID uuid) {
        if (uuid == null) {
            logger.error(NULL_UUID);
            throw new PaymentNotFoundException(NULL_UUID);
        }
    }
}
