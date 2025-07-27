package br.com.wise.payment.wise.payment.application.controller;

import br.com.wise.payment.wise.payment.application.dtos.request.PaymentRequest;
import br.com.wise.payment.wise.payment.application.dtos.response.PaymentResponse;
import br.com.wise.payment.wise.payment.converter.PaymentConverter;
import br.com.wise.payment.wise.payment.domain.Payment;
import br.com.wise.payment.wise.payment.domain.Status;
import br.com.wise.payment.wise.payment.usecase.ProceedPaymentProcessingUseCase;
import br.com.wise.payment.wise.payment.usecase.PutOnLineProcessingStartUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
@Import(PaymentControllerTest.TestConfig.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PaymentConverter paymentConverter;

    @Autowired
    private PutOnLineProcessingStartUseCase putOnLineProcessingStartUseCase;

    @Autowired
    private ProceedPaymentProcessingUseCase proceedPaymentProcessingUseCase;

    @Test
    void createPaymentAndReturnUUID_deveRetornarResponseCorreta() throws Exception {
        var request = new PaymentRequest("4111111111111111", 1000.0);
        var domainBefore = new Payment(null, BigDecimal.valueOf(1000.0), Status.PROCESSING, "4111111111111111", null);
        var domainAfter = new Payment(UUID.randomUUID(), BigDecimal.valueOf(1000.0), Status.SUCCESS, "4111111111111111", "OK");
        var response = new PaymentResponse(domainAfter.getPaymentId(), Status.SUCCESS, "OK");

        when(paymentConverter.toDomain(request)).thenReturn(domainBefore);
        when(putOnLineProcessingStartUseCase.execute(domainBefore)).thenReturn(domainAfter);
        when(paymentConverter.toResponse(domainAfter)).thenReturn(response);

        mockMvc.perform(post("/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

        verify(paymentConverter).toDomain(request);
        verify(putOnLineProcessingStartUseCase).execute(domainBefore);
        verify(paymentConverter).toResponse(domainAfter);
    }

    @Test
    void proceedPaymentProcessing_deveRetornarResponseCorreta() throws Exception {
        var uuid = UUID.randomUUID();
        var domainAfter = new Payment(uuid, BigDecimal.valueOf(500.0), Status.SUCCESS, "1234123412341234", "Done");
        var response = new PaymentResponse(uuid, Status.SUCCESS, "Done");

        when(proceedPaymentProcessingUseCase.execute(uuid)).thenReturn(domainAfter);
        when(paymentConverter.toResponse(domainAfter)).thenReturn(response);

        mockMvc.perform(get("/payment/proceed/{uuid}", uuid))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

        verify(proceedPaymentProcessingUseCase).execute(uuid);
        verify(paymentConverter).toResponse(domainAfter);
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public PaymentConverter paymentConverter() {
            return mock(PaymentConverter.class);
        }

        @Bean
        public PutOnLineProcessingStartUseCase putOnLineProcessingStartUseCase() {
            return mock(PutOnLineProcessingStartUseCase.class);
        }

        @Bean
        public ProceedPaymentProcessingUseCase proceedPaymentProcessingUseCase() {
            return mock(ProceedPaymentProcessingUseCase.class);
        }
    }
}