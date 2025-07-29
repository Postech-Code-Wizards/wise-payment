package br.com.wise.payment.wise.payment.converter;

import br.com.wise.payment.wise.payment.application.dtos.request.PaymentRequest;
import br.com.wise.payment.wise.payment.domain.Payment;
import br.com.wise.payment.wise.payment.domain.Status;
import br.com.wise.payment.wise.payment.gateway.database.jpa.repositories.entities.PaymentEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class PaymentConverterTest {

    private final PaymentConverter converter = new PaymentConverter();

    @Test
    void converteRequestParaDomainCorreto() {
        var req = new PaymentRequest("4111111111111111", 123.45);
        var dom = converter.toDomain(req);

        assertThat(dom.getPaymentId()).isNull();
        assertThat(dom.getTotalValue()).isEqualByComparingTo("123.45");
        assertThat(dom.getStatus()).isEqualTo(Status.PROCESSING);
        assertThat(dom.getCreditCardNumber()).isEqualTo("4111111111111111");
        assertThat(dom.getMessage()).isNull();
    }

    @Test
    void converteEntityParaDomainCorreto() {
        var uuid = UUID.randomUUID();
        var ent = new PaymentEntity(uuid, new BigDecimal("987.65"), Status.PROCESSING, "5555666677778888", "OK");
        var dom = converter.toDomain(ent);

        assertThat(dom.getPaymentId()).isEqualTo(uuid);
        assertThat(dom.getTotalValue()).isEqualByComparingTo("987.65");
        assertThat(dom.getStatus()).isEqualTo(Status.PROCESSING);
        assertThat(dom.getCreditCardNumber()).isEqualTo("5555666677778888");
        assertThat(dom.getMessage()).isEqualTo(ent.getMessage());
    }

    @Test
    void converteDomainParaEntityCorreto() {
        var id = UUID.randomUUID();
        var dom = new Payment(id, new BigDecimal("100.00"), Status.PROCESSING, "1234567890123456", "Authorized");
        var ent = converter.toEntity(dom);

        assertThat(ent.getId()).isEqualTo(id);
        assertThat(ent.getTotalValue()).isEqualByComparingTo(dom.getTotalValue());
        assertThat(ent.getStatus()).isEqualTo(Status.PROCESSING);
        assertThat(ent.getCreditCardNumber()).isEqualTo("1234567890123456");
        assertThat(ent.getMessage()).isEqualTo("Authorized");
    }

    @Test
    void converteDomainParaResponseCorreto() {
        var id = UUID.randomUUID();
        var dom = new Payment(id, new BigDecimal("50.00"), Status.SUCCESS, "1111222233334444", "Payment successful");
        var resp = converter.toResponse(dom);

        assertThat(resp.paymentId()).isEqualTo(id);
        assertThat(resp.status()).isEqualTo(Status.SUCCESS);
        assertThat(resp.message()).isEqualTo("Payment successful");
    }
}