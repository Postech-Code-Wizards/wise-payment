package br.com.wise.payment.wise.payment.application.controller;

import br.com.wise.payment.wise.payment.application.dtos.request.PaymentRequest;
import br.com.wise.payment.wise.payment.application.dtos.response.PaymentResponse;
import br.com.wise.payment.wise.payment.converter.PaymentConverter;
import br.com.wise.payment.wise.payment.domain.Payment;
import br.com.wise.payment.wise.payment.domain.Status;
import br.com.wise.payment.wise.payment.gateway.PaymentGateway;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PaymentGateway paymentGateway;

    @Autowired
    private PaymentConverter paymentConverter;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public PaymentGateway paymentGateway() {
            return mock(PaymentGateway.class);
        }

        @Bean
        public PaymentConverter paymentConverter() {
            return mock(PaymentConverter.class);
        }
    }

    @Test
    void shouldCreatePaymentSuccessfully() throws Exception {
        PaymentRequest request = new PaymentRequest("12345678901234", 1000.0);

        Payment domain = new Payment(
                null,
                BigDecimal.valueOf(1000.0),
                null,
                "12345678901234",
                null
        );

        Payment processed = new Payment(
                UUID.randomUUID(),
                BigDecimal.valueOf(1000.0),
                Status.SUCCESS,
                "12345678901234",
                "Pagamento aprovado"
        );

        PaymentResponse response = new PaymentResponse(
                processed.getPaymentId(),
                processed.getStatus(),
                processed.getMessage()
        );

        when(paymentConverter.toDomain(request)).thenReturn(domain);
        when(paymentGateway.processPayment(domain)).thenReturn(processed);
        when(paymentConverter.toResponse(processed)).thenReturn(response);

        mockMvc.perform(post("/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

        verify(paymentConverter).toDomain(request);
        verify(paymentGateway).processPayment(domain);
        verify(paymentConverter).toResponse(processed);
    }
}
