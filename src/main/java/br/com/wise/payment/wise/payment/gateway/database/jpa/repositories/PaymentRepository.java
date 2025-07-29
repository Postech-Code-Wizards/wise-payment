package br.com.wise.payment.wise.payment.gateway.database.jpa.repositories;

import br.com.wise.payment.wise.payment.gateway.database.jpa.repositories.entities.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, UUID> {
}
