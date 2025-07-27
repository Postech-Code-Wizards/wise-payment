package br.com.wise.payment.wise.payment.application.controller;

import br.com.wise.payment.wise.payment.application.dtos.request.PaymentRequest;
import br.com.wise.payment.wise.payment.application.dtos.response.PaymentResponse;
import br.com.wise.payment.wise.payment.converter.PaymentConverter;
import br.com.wise.payment.wise.payment.domain.Payment;
import br.com.wise.payment.wise.payment.usecase.ProceedPaymentProcessingUseCase;
import br.com.wise.payment.wise.payment.usecase.PutOnLineProcessingStartUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentConverter converter;

    private final PutOnLineProcessingStartUseCase putOnLineProcessingStartUseCase;
    private final ProceedPaymentProcessingUseCase proceedPaymentProcessingUseCase;

    @PostMapping
    public PaymentResponse createPaymentAndReturnUUID(@Valid @RequestBody PaymentRequest paymentRequest) {
        Payment payment = converter.toDomain(paymentRequest);
        return converter.toResponse(putOnLineProcessingStartUseCase.execute(payment));
    }

    @GetMapping("/proceed/{uuid}")
    public PaymentResponse proceedPaymentProcessing(@Valid @PathVariable UUID uuid) {
        return converter.toResponse(proceedPaymentProcessingUseCase.execute(uuid));
    }
}
