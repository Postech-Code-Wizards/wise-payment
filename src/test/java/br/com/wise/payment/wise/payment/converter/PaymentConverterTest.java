package br.com.wise.payment.wise.payment.converter;

import br.com.wise.payment.wise.payment.application.dtos.request.PaymentRequest;
import br.com.wise.payment.wise.payment.application.dtos.response.PaymentResponse;
import br.com.wise.payment.wise.payment.domain.Payment;
import br.com.wise.payment.wise.payment.domain.Status;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PaymentConverterTest {

    private final PaymentConverter converter = new PaymentConverter();

    @Test
    void shouldConvertRequestToDomain() {
        PaymentRequest request = new PaymentRequest("12345678901234", 1500.0);

        Payment payment = converter.toDomain(request);

        assertNull(payment.getPaymentId());
        assertEquals(BigDecimal.valueOf(1500.0), payment.getTotalValue());
        assertEquals("12345678901234", payment.getCreditCardNumber());
        assertEquals(Status.PROCESSING, payment.getStatus());
        assertNull(payment.getMessage());
    }

    @Test
    void shouldConvertDomainToResponse() {
        UUID id = UUID.randomUUID();
        Payment payment = new Payment(
                id,
                BigDecimal.valueOf(1500.0),
                Status.SUCCESS,
                "12345678901234",
                "Pagamento aprovado"
        );

        PaymentResponse response = converter.toResponse(payment);

        assertEquals(id, response.paymentId());
        assertEquals(Status.SUCCESS, response.status());
        assertEquals("Pagamento aprovado", response.message());
    }
}