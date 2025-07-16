package br.com.wise.payment.wise.payment.application.controller.rabbit;

import br.com.wise.payment.wise.payment.application.configuration.rabbit.RabbitProducer;
import br.com.wise.payment.wise.payment.application.dtos.rabbit.PaymentMessageDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RabbitController.class)
class RabbitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RabbitProducer producer;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public RabbitProducer rabbitProducer() {
            return mock(RabbitProducer.class);
        }
    }

    @Test
    void shouldSendPaymentMessage() throws Exception {
        PaymentMessageDTO message = new PaymentMessageDTO("12345678901234", 1000.0);

        mockMvc.perform(post("/api/rabbit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(message)))
                .andExpect(status().isOk());

        verify(producer).send(message);
    }
}
