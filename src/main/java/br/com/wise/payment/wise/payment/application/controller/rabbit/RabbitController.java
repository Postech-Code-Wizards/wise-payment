package br.com.wise.payment.wise.payment.application.controller.rabbit;

import br.com.wise.payment.wise.payment.application.configuration.rabbit.RabbitProducer;
import br.com.wise.payment.wise.payment.application.dtos.rabbit.PaymentCallback;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rabbit")
@RequiredArgsConstructor
public class RabbitController {
    private final RabbitProducer producer;

    @PostMapping
    public ResponseEntity<Void> send(@RequestBody PaymentCallback paymentMessage) {
        producer.send(paymentMessage);
        return ResponseEntity.ok().build();
    }
}
